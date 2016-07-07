/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.xml.dsl.api.resolver;

import static org.mule.metadata.java.api.utils.JavaTypeUtils.getType;
import static org.mule.metadata.utils.MetadataTypeUtils.getSingleAnnotation;
import static org.mule.runtime.extension.api.introspection.parameter.ExpressionSupport.SUPPORTED;
import static org.mule.runtime.extension.api.util.NameUtils.getTopLevelTypeName;
import static org.mule.runtime.extension.api.util.NameUtils.hyphenize;
import static org.mule.runtime.extension.api.util.NameUtils.pluralize;
import static org.mule.runtime.extension.api.util.NameUtils.singularize;
import static org.mule.runtime.extension.xml.dsl.api.XmlModelUtils.createXmlModelProperty;
import org.mule.metadata.api.model.ArrayType;
import org.mule.metadata.api.model.DictionaryType;
import org.mule.metadata.api.model.MetadataType;
import org.mule.metadata.api.model.ObjectType;
import org.mule.metadata.api.visitor.MetadataTypeVisitor;
import org.mule.metadata.java.api.annotation.ClassInformationAnnotation;
import org.mule.runtime.extension.api.annotation.Extensible;
import org.mule.runtime.extension.api.annotation.Extension;
import org.mule.runtime.extension.api.annotation.capability.Xml;
import org.mule.runtime.extension.api.introspection.ExtensionModel;
import org.mule.runtime.extension.api.introspection.Named;
import org.mule.runtime.extension.api.introspection.parameter.ExpressionSupport;
import org.mule.runtime.extension.api.introspection.parameter.ParameterModel;
import org.mule.runtime.extension.api.introspection.property.ImportedTypesModelProperty;
import org.mule.runtime.extension.api.introspection.property.SubTypesModelProperty;
import org.mule.runtime.extension.xml.dsl.api.DslElementDeclaration;
import org.mule.runtime.extension.xml.dsl.api.property.XmlModelProperty;
import org.mule.runtime.extension.xml.dsl.internal.DslElementDeclarationBuilder;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Provides the {@link DslElementDeclaration} of any {@link Named Component}
 * based on the {@link ExtensionModel Extension model} in for which the Component was declared.
 *
 * @since 1.0
 */
public class DslElementResolver
{

    private Map<MetadataType, XmlModelProperty> importedTypes;
    private final Map<MetadataType, List<MetadataType>> subTypesMapping;
    private final XmlModelProperty extensionXml;


    /**
     * @param model the {@link ExtensionModel} that provides context for resolving the
     *              component's {@link DslElementDeclaration}
     * @throws IllegalArgumentException if the {@link ExtensionModel} doesn't have an {@link XmlModelProperty}
     */
    public DslElementResolver(ExtensionModel model)
    {
        this.extensionXml = loadXmlProperties(model);
        this.subTypesMapping = loadSubTypes(model);
        this.importedTypes = loadImportedTypes(model);
    }

    public DslElementDeclaration resolve(final Named model)
    {
        return DslElementDeclarationBuilder.create().withElementName(hyphenize(model.getName()))
                .withNamespace(extensionXml.getNamespace())
                .build();
    }

    public DslElementDeclaration resolve(final ParameterModel model)
    {
        final ExpressionSupport expressionSupport = model.getExpressionSupport();
        final DslElementDeclarationBuilder builder = DslElementDeclarationBuilder.create();
        final String namespace = importedTypes.getOrDefault(model.getType(), extensionXml).getNamespace();

        model.getType().accept(
                new MetadataTypeVisitor()
                {
                    @Override
                    protected void defaultVisit(MetadataType metadataType)
                    {
                        builder.withAttributeName(model.getName())
                                .withNamespace(namespace)
                                .withElementName(hyphenize(model.getName()));
                    }

                    @Override
                    public void visitArrayType(ArrayType arrayType)
                    {
                        defaultVisit(arrayType);
                        MetadataType genericType = arrayType.getType();
                        if (shouldGenerateChildElements(genericType, expressionSupport))
                        {
                            builder.supportsChildDeclaration(true);
                            genericType.accept(getArrayItemTypeVisitor(builder, model.getName(), namespace, false));
                        }
                    }

                    @Override
                    public void visitObject(ObjectType objectType)
                    {
                        builder.withAttributeName(model.getName())
                                .withNamespace(namespace)
                                .withElementName(hyphenize(model.getName()));

                        if (shouldGenerateChildElements(objectType, expressionSupport))
                        {
                            builder.supportsChildDeclaration(true);

                            if (typeRequiresWrapperElement(objectType))
                            {
                                builder.asWrappedElement(true)
                                        .withNamespace(namespace);
                            }
                            else
                            {
                                builder.withNamespace(extensionXml.getNamespace());
                            }
                        }
                    }

                    @Override
                    public void visitDictionary(DictionaryType dictionaryType)
                    {
                        builder.withAttributeName(model.getName())
                                .withNamespace(namespace)
                                .withElementName(hyphenize(pluralize(model.getName())));

                        MetadataType keyType = dictionaryType.getKeyType();
                        builder.supportsChildDeclaration(shouldGenerateChildElements(keyType, expressionSupport));

                        dictionaryType.getValueType().accept(getDictionaryValueTypeVisitor(builder, model.getName(), namespace));
                    }
                }
        );
        return builder.build();
    }

    private MetadataTypeVisitor getArrayItemTypeVisitor(final DslElementDeclarationBuilder listBuilder, final String parameterName, final String namespace, boolean itemize)
    {
        return new MetadataTypeVisitor()
        {
            @Override
            public void visitObject(ObjectType objectType)
            {
                listBuilder.withGeneric(objectType, resolve(objectType, namespace));
            }

            @Override
            public void visitArrayType(ArrayType arrayType)
            {
                DslElementDeclarationBuilder genericBuilder = DslElementDeclarationBuilder.create()
                        .withNamespace(namespace)
                        .withElementName(getItemName());

                MetadataType genericType = arrayType.getType();
                if (shouldGenerateChildElements(genericType, SUPPORTED))
                {
                    genericBuilder.supportsChildDeclaration(true);
                    genericType.accept(getArrayItemTypeVisitor(genericBuilder, parameterName, namespace, true));
                }

                listBuilder.withGeneric(arrayType, genericBuilder.build());
            }

            @Override
            protected void defaultVisit(MetadataType metadataType)
            {
                //TODO MULE-10029 review convention of singular/plural
                listBuilder.withGeneric(metadataType,
                                    DslElementDeclarationBuilder.create()
                                            .withNamespace(namespace)
                                            .withElementName(getItemName())
                                            .build());
            }

            private String getItemName()
            {
                //TODO MULE-10029 review "item" convention for List<List<?>>
                return itemize ? hyphenize(singularize(parameterName)).concat("-item")
                               : hyphenize(singularize(parameterName));
            }
        };
    }

    private MetadataTypeVisitor getDictionaryValueTypeVisitor(final DslElementDeclarationBuilder mapBuilder, final String parameterName, final String namespace)
    {
        return new MetadataTypeVisitor()
        {
            @Override
            public void visitObject(ObjectType objectType)
            {
                mapBuilder.withGeneric(objectType,
                                       DslElementDeclarationBuilder.create()
                                               //TODO broken namespace for extensible types
                                               .withNamespace(namespace)
                                               //TODO MULE-10029 handle subtypes for maps xml generation (not required for parsers)
                                               .withElementName(hyphenize(singularize(parameterName)))
                                               .supportsChildDeclaration(shouldGenerateChildElements(objectType, SUPPORTED))
                                               .build());
            }

            @Override
            public void visitArrayType(ArrayType arrayType)
            {
                DslElementDeclarationBuilder listBuilder = DslElementDeclarationBuilder.create()
                        .withNamespace(namespace)
                        .withElementName(hyphenize(singularize(parameterName)));

                MetadataType genericType = arrayType.getType();
                if (shouldGenerateChildElements(genericType, SUPPORTED))
                {
                    listBuilder.supportsChildDeclaration(true);
                    genericType.accept(getArrayItemTypeVisitor(listBuilder, parameterName, namespace, true));
                }

                mapBuilder.withGeneric(arrayType, listBuilder.build());
            }

            @Override
            protected void defaultVisit(MetadataType metadataType)
            {
                mapBuilder.withGeneric(metadataType,
                                       DslElementDeclarationBuilder.create()
                                               .withNamespace(namespace)
                                               .withElementName(hyphenize(singularize(parameterName)))
                                               .build());
            }
        };
    }

    public DslElementDeclaration resolve(MetadataType type)
    {
        String namespace = importedTypes.getOrDefault(type, extensionXml).getNamespace();
        return resolve(type, namespace);
    }

    public DslElementDeclaration resolve(MetadataType type, final String ownerNamespace)
    {
        //TODO Review for subtypes with imports
        XmlModelProperty importedXml = importedTypes.get(type);
        String namespace = importedXml != null ? importedXml.getNamespace() : ownerNamespace;

        final DslElementDeclarationBuilder builder = DslElementDeclarationBuilder.create();
        type.accept(new MetadataTypeVisitor()
        {
            @Override
            public void visitObject(ObjectType objectType)
            {
                builder.withNamespace(namespace)
                        .withElementName(getTopLevelTypeName(objectType))
                        .supportsChildDeclaration(shouldGenerateChildElements(objectType, ExpressionSupport.SUPPORTED));

                objectType.getFields().forEach(
                        field -> {
                            DslElementDeclarationBuilder fieldBuilder = DslElementDeclarationBuilder.create();
                            String childName = field.getKey().getName().getLocalPart();
                            field.getValue().accept(getObjectFieldVisitor(fieldBuilder, childName, namespace));
                            builder.withChild(childName, fieldBuilder.build());
                        });
            }

        });

        return builder.build();
    }

    private MetadataTypeVisitor getObjectFieldVisitor(final DslElementDeclarationBuilder fieldBuilder, final String fieldName, final String ownerNamespace)
    {
        return new MetadataTypeVisitor()
        {
            @Override
            public void visitObject(ObjectType objectType)
            {
                fieldBuilder.withNamespace(ownerNamespace)
                        .withElementName(hyphenize(fieldName))
                        .supportsChildDeclaration(shouldGenerateChildElements(objectType, ExpressionSupport.SUPPORTED));

                objectType.getFields().stream()
                        .filter(f -> !f.getValue().equals(objectType))
                        .forEach(
                                field -> {
                                    DslElementDeclarationBuilder fieldBuilder = DslElementDeclarationBuilder.create();
                                    String childName = field.getKey().getName().getLocalPart();
                                    field.getValue().accept(getObjectFieldVisitor(fieldBuilder, childName, ownerNamespace));
                                    fieldBuilder.withChild(childName, fieldBuilder.build());
                                });
            }

            @Override
            public void visitArrayType(ArrayType arrayType)
            {
                fieldBuilder.withNamespace(ownerNamespace)
                        .withElementName(hyphenize(pluralize(fieldName)));

                MetadataType genericType = arrayType.getType();
                if (shouldGenerateChildElements(genericType, ExpressionSupport.SUPPORTED))
                {
                    fieldBuilder.supportsChildDeclaration(true);
                    genericType.accept(getArrayItemTypeVisitor(fieldBuilder, fieldName, ownerNamespace, false));
                }
            }

            @Override
            public void visitDictionary(DictionaryType dictionaryType)
            {
                fieldBuilder.withNamespace(ownerNamespace)
                        .withElementName(hyphenize(pluralize(fieldName)));

                MetadataType keyType = dictionaryType.getKeyType();
                fieldBuilder.supportsChildDeclaration(shouldGenerateChildElements(keyType, ExpressionSupport.SUPPORTED));

                dictionaryType.getValueType().accept(getDictionaryValueTypeVisitor(fieldBuilder, fieldName, ownerNamespace));
            }
        };
    }


    private boolean shouldGenerateChildElements(MetadataType metadataType, ExpressionSupport expressionSupport)
    {
        boolean isExpressionRequired = ExpressionSupport.REQUIRED == expressionSupport;
        boolean isPojo = metadataType instanceof ObjectType;

        Optional<ClassInformationAnnotation> classInformation = getSingleAnnotation(metadataType, ClassInformationAnnotation.class);
        boolean isInstantiable = !isPojo || classInformation.map(ClassInformationAnnotation::isInstantiable).orElse(false);
        //TODO MULE-10031 replace class usage with type annotation
        boolean isExtensible = getType(metadataType).isAnnotationPresent(Extensible.class);

        return !isExpressionRequired &&
               (subTypesMapping.containsKey(metadataType) || isExtensible ||
                (isInstantiable && (!isPojo || !((ObjectType) metadataType).getFields().isEmpty())));
    }

    private boolean typeRequiresWrapperElement(MetadataType metadataType)
    {
        boolean isPojo = metadataType instanceof ObjectType;
        //TODO MULE-10031 replace class usage with type annotation
        boolean isExtensible = getType(metadataType).isAnnotationPresent(Extensible.class);
        boolean hasSubtypes = subTypesMapping.containsKey(metadataType);

        return isPojo && (isExtensible || hasSubtypes);
    }

    private Map<MetadataType, XmlModelProperty> loadImportedTypes(ExtensionModel extension)
    {
        final Map<MetadataType, XmlModelProperty> xmlByType = new HashMap<>();
        extension.getModelProperty(ImportedTypesModelProperty.class)
                .map(ImportedTypesModelProperty::getImportedTypes)
                .ifPresent(imports -> imports
                        .forEach((type, ownerExtension) -> {
                            //TODO MULE-10028 stop using `createXmlModelProperty` and `ownerClass`
                            Class<?> ownerClass = getType(ownerExtension);
                            xmlByType.put(type, createXmlModelProperty(ownerClass.getAnnotation(Xml.class),
                                                                       ownerClass.getAnnotation(Extension.class).name(), ""));
                        }));

        return xmlByType;
    }

    private Map<MetadataType, List<MetadataType>> loadSubTypes(ExtensionModel extension)
    {
        return extension.getModelProperty(SubTypesModelProperty.class)
                .map(SubTypesModelProperty::getSubTypesMapping)
                .orElse(Collections.emptyMap());
    }

    private XmlModelProperty loadXmlProperties(ExtensionModel extension)
    {
        return extension.getModelProperty(XmlModelProperty.class)
                .orElseThrow(() -> new IllegalArgumentException(
                        String.format("The extension [%s] does not have the [%s], required for its Xml Dsl Resolution",
                                      extension.getName(), XmlModelProperty.class.getSimpleName())));
    }

}
