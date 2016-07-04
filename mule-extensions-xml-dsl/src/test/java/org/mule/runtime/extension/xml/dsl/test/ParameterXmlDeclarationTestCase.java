/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.xml.dsl.test;

import static java.util.Collections.singletonList;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;
import static org.mule.runtime.extension.api.util.NameUtils.defaultNamespace;
import static org.mule.runtime.extension.api.util.NameUtils.hyphenize;
import static org.mule.runtime.extension.api.util.NameUtils.pluralize;
import org.mule.metadata.api.model.MetadataType;
import org.mule.runtime.extension.api.annotation.Extension;
import org.mule.runtime.extension.api.annotation.capability.Xml;
import org.mule.runtime.extension.api.introspection.property.ImportedTypesModelProperty;
import org.mule.runtime.extension.api.introspection.property.SubTypesModelProperty;
import org.mule.runtime.extension.xml.dsl.api.DslElementDeclaration;
import org.mule.runtime.extension.xml.dsl.api.resolver.DslElementResolver;
import org.mule.runtime.extension.xml.dsl.test.model.AbstractType;
import org.mule.runtime.extension.xml.dsl.test.model.ChildOfAbstractType;
import org.mule.runtime.extension.xml.dsl.test.model.ComplexFieldsType;
import org.mule.runtime.extension.xml.dsl.test.model.EmptyType;
import org.mule.runtime.extension.xml.dsl.test.model.ExtensibleType;
import org.mule.runtime.extension.xml.dsl.test.model.InterfaceDeclaration;
import org.mule.runtime.extension.xml.dsl.test.model.InterfaceDeclarationWithMapping;
import org.mule.runtime.extension.xml.dsl.test.model.InterfaceImplementation;
import org.mule.runtime.extension.xml.dsl.test.model.NonDefaultConstructor;
import org.mule.runtime.extension.xml.dsl.test.model.SimpleFieldsType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * //TODO
 */
@RunWith(MockitoJUnitRunner.class)
public class ParameterXmlDeclarationTestCase extends BaseXmlDeclarationTestCase
{

    @Test
    public void testSimpleTypeParameter()
    {
        when(parameterModel.getType()).thenReturn(TYPE_LOADER.load(String.class));
        DslElementDeclaration result = new DslElementResolver(extension).resolve(parameterModel);

        assertAttributeName(PARAMETER_NAME, result);
        assertElementName(hyphenize(PARAMETER_NAME), result);
        assertElementNamespace(NAMESPACE, result);
        assertChildElementDeclarationIs(false, result);
        assertIsWrappedElement(false, result);
    }

    @Test
    public void testSimplePojoParameter()
    {
        when(parameterModel.getType()).thenReturn(TYPE_LOADER.load(SimpleFieldsType.class));
        DslElementDeclaration result = new DslElementResolver(extension).resolve(parameterModel);

        assertChildElementDeclarationIs(true, result);
        assertIsWrappedElement(false, result);
    }

    @Test
    public void testComplexPojoParameter()
    {
        when(parameterModel.getType()).thenReturn(TYPE_LOADER.load(ComplexFieldsType.class));
        DslElementDeclaration result = new DslElementResolver(extension).resolve(parameterModel);

        assertChildElementDeclarationIs(true, result);
        assertIsWrappedElement(false, result);
    }

    @Test
    public void testEmptyTypeParameter()
    {
        when(parameterModel.getType()).thenReturn(TYPE_LOADER.load(EmptyType.class));
        DslElementDeclaration result = new DslElementResolver(extension).resolve(parameterModel);

        assertChildElementDeclarationIs(false, result);
        assertIsWrappedElement(false, result);
    }

    @Test
    public void testExtensibleTypeParameter()
    {
        when(parameterModel.getType()).thenReturn(TYPE_LOADER.load(ExtensibleType.class));
        DslElementDeclaration result = new DslElementResolver(extension).resolve(parameterModel);

        assertChildElementDeclarationIs(true, result);
        assertIsWrappedElement(true, result);
    }

    @Test
    public void testInterfaceTypeParameter()
    {
        when(parameterModel.getType()).thenReturn(TYPE_LOADER.load(InterfaceDeclaration.class));
        DslElementDeclaration result = new DslElementResolver(extension).resolve(parameterModel);

        assertChildElementDeclarationIs(false, result);
        assertIsWrappedElement(false, result);
    }

    @Test
    public void testAbstractClassParameter()
    {
        when(parameterModel.getType()).thenReturn(TYPE_LOADER.load(AbstractType.class));
        DslElementDeclaration result = new DslElementResolver(extension).resolve(parameterModel);

        assertChildElementDeclarationIs(false, result);
        assertIsWrappedElement(false, result);
    }

    @Test
    public void testNonDefaultConstructorParameter()
    {
        when(parameterModel.getType()).thenReturn(TYPE_LOADER.load(NonDefaultConstructor.class));
        DslElementDeclaration result = new DslElementResolver(extension).resolve(parameterModel);

        assertChildElementDeclarationIs(false, result);
        assertIsWrappedElement(false, result);
    }

    @Test
    public void testInterfaceWithMappingParameter()
    {
        Map<MetadataType, List<MetadataType>> mapping = new HashMap<>();
        mapping.put(TYPE_LOADER.load(InterfaceDeclarationWithMapping.class),
                    singletonList(TYPE_LOADER.load(InterfaceImplementation.class)));

        when(extension.getModelProperty(SubTypesModelProperty.class)).thenReturn(Optional.of(new SubTypesModelProperty(mapping)));

        when(parameterModel.getType()).thenReturn(TYPE_LOADER.load(InterfaceDeclarationWithMapping.class));
        DslElementDeclaration result = new DslElementResolver(extension).resolve(parameterModel);

        assertChildElementDeclarationIs(true, result);
        assertIsWrappedElement(true, result);
    }

    @Test
    public void testAbstractWithMappingParameter()
    {
        Map<MetadataType, List<MetadataType>> mapping = new HashMap<>();
        mapping.put(TYPE_LOADER.load(AbstractType.class),
                    singletonList(TYPE_LOADER.load(ChildOfAbstractType.class)));

        when(extension.getModelProperty(SubTypesModelProperty.class)).thenReturn(Optional.of(new SubTypesModelProperty(mapping)));

        when(parameterModel.getType()).thenReturn(TYPE_LOADER.load(AbstractType.class));
        DslElementDeclaration result = new DslElementResolver(extension).resolve(parameterModel);

        assertChildElementDeclarationIs(true, result);
        assertIsWrappedElement(true, result);
    }

    @Test
    public void testImportedTypeWithXmlParameter()
    {
        Map<MetadataType, MetadataType> imports = new HashMap<>();
        imports.put(TYPE_LOADER.load(SimpleFieldsType.class), TYPE_LOADER.load(ExtensionForImportsDeclaresXml.class));

        when(extension.getModelProperty(ImportedTypesModelProperty.class)).thenReturn(
                Optional.of(new ImportedTypesModelProperty(imports)));

        when(parameterModel.getType()).thenReturn(TYPE_LOADER.load(SimpleFieldsType.class));
        DslElementDeclaration result = new DslElementResolver(extension).resolve(parameterModel);

        assertThat(result.getElementNamespace(), is(IMPORT_NAMESPACE));
        assertChildElementDeclarationIs(true, result);
        assertIsWrappedElement(false, result);
    }

    @Test
    public void testImportedTypeWithoutXmlParameter()
    {
        Map<MetadataType, MetadataType> imports = new HashMap<>();
        imports.put(TYPE_LOADER.load(SimpleFieldsType.class), TYPE_LOADER.load(ExtensionForImportsNoXml.class));

        when(extension.getModelProperty(ImportedTypesModelProperty.class)).thenReturn(
                Optional.of(new ImportedTypesModelProperty(imports)));

        when(parameterModel.getType()).thenReturn(TYPE_LOADER.load(SimpleFieldsType.class));
        DslElementDeclaration result = new DslElementResolver(extension).resolve(parameterModel);

        assertThat(result.getElementNamespace(), is(defaultNamespace(IMPORT_EXTENSION_NAME)));
        assertChildElementDeclarationIs(true, result);
        assertIsWrappedElement(false, result);
    }

    @Test
    public void testExtensibleImportParameter()
    {
        Map<MetadataType, MetadataType> imports = new HashMap<>();
        imports.put(TYPE_LOADER.load(ExtensibleType.class), TYPE_LOADER.load(ExtensionForImportsDeclaresXml.class));

        when(extension.getModelProperty(ImportedTypesModelProperty.class)).thenReturn(
                Optional.of(new ImportedTypesModelProperty(imports)));

        when(parameterModel.getType()).thenReturn(TYPE_LOADER.load(ExtensibleType.class));
        DslElementDeclaration result = new DslElementResolver(extension).resolve(parameterModel);

        assertThat(result.getElementNamespace(), is(IMPORT_NAMESPACE));
        assertChildElementDeclarationIs(true, result);
        assertIsWrappedElement(true, result);
    }

    @Test
    public void testMapOfSimpleTypeParameter()
    {
        MetadataType keyType = TYPE_BUILDER.stringType().id(String.class.getName()).build();
        MetadataType valueType = TYPE_BUILDER.stringType().id(String.class.getName()).build();

        when(parameterModel.getType()).thenReturn(TYPE_BUILDER.dictionaryType().id(Map.class.getName())
                                                          .ofKey(keyType)
                                                          .ofValue(valueType)
                                                          .build());
        DslElementDeclaration result = new DslElementResolver(extension).resolve(parameterModel);

        assertAttributeName(PARAMETER_NAME, result);
        assertElementName(hyphenize(pluralize(PARAMETER_NAME)), result);
        assertElementNamespace(NAMESPACE, result);
        assertChildElementDeclarationIs(true, result);
        assertIsWrappedElement(false, result);

        Optional<DslElementDeclaration> genericDsl = result.getGeneric(valueType);
        assertThat(genericDsl.isPresent(), is(true));

        DslElementDeclaration innerElement = genericDsl.get();
        assertThat(innerElement.getElementName(), is(hyphenize(PARAMETER_NAME)));
        assertChildElementDeclarationIs(false, innerElement);
        assertIsWrappedElement(false, innerElement);
    }
    //
    //@Test
    //public void testMapOfComplexTypeParameter()
    //{
    //    MetadataType keyType = TYPE_BUILDER.stringType().id(String.class.getName()).build();
    //    MetadataType valueType = TYPE_BUILDER.stringType().id(String.class.getName()).build();
    //
    //    when(parameterModel.getType()).thenReturn(TYPE_BUILDER.dictionaryType().id(Map.class.getName())
    //                                                      .ofKey(TYPE_BUILDER.stringType().id(String.class.getName()))
    //                                                      .ofValue(TYPE_LOADER.load(SimpleFieldsType.class))
    //                                                      .build());
    //    DslElementDeclaration result = new DslElementResolver(extension).resolve(parameterModel);
    //
    //    assertAttributeName(PARAMETER_NAME, result);
    //    assertElementName(hyphenize(pluralize(PARAMETER_NAME)), result);
    //    assertElementNamespace(NAMESPACE, result);
    //    assertChildElementDeclarationIs(true, result);
    //    assertIsWrappedElement(false, result);
    //
    //    assertThat(result.getGeneric(TYPE_LOADER.load(String.class)).isPresent(), is(true));
    //    DslElementDeclaration innerElement = result.getGenericsDsl().get(0);
    //    assertThat(innerElement.getElementName(), is(hyphenize(PARAMETER_NAME)));
    //    assertChildElementDeclarationIs(true, innerElement);
    //    assertIsWrappedElement(false, innerElement);
    //}
    //
    //@Test
    //public void testMapOfWrappedTypeParameter()
    //{
    //    MetadataType keyType = TYPE_BUILDER.stringType().id(String.class.getName()).build();
    //    MetadataType valueType = TYPE_BUILDER.stringType().id(String.class.getName()).build();
    //
    //    when(parameterModel.getType()).thenReturn(TYPE_BUILDER.dictionaryType().id(Map.class.getName())
    //                                                      .ofKey(TYPE_BUILDER.stringType().id(String.class.getName()))
    //                                                      .ofValue(TYPE_LOADER.load(ExtensibleType.class))
    //                                                      .build());
    //    DslElementDeclaration result = new DslElementResolver(extension).resolve(parameterModel);
    //
    //    assertAttributeName(PARAMETER_NAME, result);
    //    assertElementName(hyphenize(pluralize(PARAMETER_NAME)), result);
    //    assertElementNamespace(NAMESPACE, result);
    //    assertChildElementDeclarationIs(true, result);
    //    assertIsWrappedElement(false, result);
    //
    //    assertThat(result.getGeneric(TYPE_LOADER.load(String.class)).isPresent(), is(true));
    //    DslElementDeclaration innerElement = result.getGenericsDsl().get(0);
    //    assertThat(innerElement.getElementName(), equalTo(hyphenize(PARAMETER_NAME)));
    //    assertChildElementDeclarationIs(true, innerElement);
    //    assertIsWrappedElement(true, innerElement);
    //}
    //
    //@Test
    //public void testMapOfNonInstantiableValueTypeParameter()
    //{
    //    MetadataType keyType = TYPE_BUILDER.stringType().id(String.class.getName()).build();
    //    MetadataType valueType = TYPE_BUILDER.stringType().id(String.class.getName()).build();
    //
    //    when(parameterModel.getType()).thenReturn(TYPE_BUILDER.dictionaryType().id(Map.class.getName())
    //                                                      .ofKey(TYPE_BUILDER.stringType().id(String.class.getName()))
    //                                                      .ofValue(TYPE_LOADER.load(InterfaceDeclaration.class))
    //                                                      .build());
    //    DslElementDeclaration result = new DslElementResolver(extension).resolve(parameterModel);
    //
    //    assertAttributeName(PARAMETER_NAME, result);
    //    assertElementName(hyphenize(pluralize(PARAMETER_NAME)), result);
    //    assertElementNamespace(NAMESPACE, result);
    //    assertChildElementDeclarationIs(true, result);
    //    assertIsWrappedElement(false, result);
    //
    //    assertThat(result.getGeneric(TYPE_LOADER.load(String.class)).isPresent(), is(true));
    //    DslElementDeclaration innerElement = result.getGenericsDsl().get(0);
    //    assertThat(innerElement.getElementName(), is(hyphenize(singularize(PARAMETER_NAME))));
    //    assertChildElementDeclarationIs(false, innerElement);
    //    assertIsWrappedElement(false, innerElement);
    //}
    //
    //@Test
    //public void testCollectionOfSimpleTypeParameter()
    //{
    //    MetadataType itemType = TYPE_BUILDER.stringType().id(String.class.getName()).build();
    //
    //    when(parameterModel.getType()).thenReturn(TYPE_BUILDER.arrayType().id(List.class.getName())
    //                                                      .of(TYPE_BUILDER.stringType().id(String.class.getName()))
    //                                                      .build());
    //    DslElementDeclaration result = new DslElementResolver(extension).resolve(parameterModel);
    //
    //    assertAttributeName(PARAMETER_NAME, result);
    //    assertElementName(hyphenize(PARAMETER_NAME), result);
    //    assertElementNamespace(NAMESPACE, result);
    //    assertChildElementDeclarationIs(true, result);
    //    assertIsWrappedElement(false, result);
    //
    //    assertThat(result.getGeneric(TYPE_LOADER.load(String.class)).isPresent(), is(true));
    //    DslElementDeclaration innerElement = result.getGenericsDsl().get(0);
    //    assertThat(innerElement.getElementName(), is(hyphenize(singularize(PARAMETER_NAME))));
    //    assertChildElementDeclarationIs(false, innerElement);
    //    assertIsWrappedElement(false, innerElement);
    //}
    //
    //@Test
    //public void testCollectionOfComplexTypeParameter()
    //{
    //    MetadataType itemType = TYPE_LOADER.load(SimpleFieldsType.class);
    //
    //    when(parameterModel.getType()).thenReturn(
    //            TYPE_BUILDER.arrayType().id(List.class.getName()).of(itemType).build());
    //
    //    DslElementDeclaration result = new DslElementResolver(extension).resolve(parameterModel);
    //
    //    assertAttributeName(PARAMETER_NAME, result);
    //    assertElementName(hyphenize(PARAMETER_NAME), result);
    //    assertElementNamespace(NAMESPACE, result);
    //    assertChildElementDeclarationIs(true, result);
    //    assertIsWrappedElement(false, result);
    //
    //    assertThat(result.getGeneric(TYPE_LOADER.load(String.class)).isPresent(), is(true));
    //    DslElementDeclaration innerElement = result.getGenericsDsl().get(0);
    //    assertThat(innerElement.getElementName(), equalTo(getTopLevelTypeName(itemType)));
    //    assertChildElementDeclarationIs(true, innerElement);
    //    assertIsWrappedElement(false, innerElement);
    //}
    //
    //@Test
    //public void testCollectionOfWrappedTypeParameter()
    //{
    //    MetadataType itemType = TYPE_LOADER.load(ExtensibleType.class);
    //    when(parameterModel.getType()).thenReturn(TYPE_BUILDER.arrayType().id(List.class.getName())
    //                                                      .of(itemType)
    //                                                      .build());
    //    DslElementDeclaration result = new DslElementResolver(extension).resolve(parameterModel);
    //
    //    assertAttributeName(PARAMETER_NAME, result);
    //    assertElementName(hyphenize(PARAMETER_NAME), result);
    //    assertElementNamespace(NAMESPACE, result);
    //    assertChildElementDeclarationIs(true, result);
    //    assertIsWrappedElement(false, result);
    //
    //    assertThat(result.getGeneric(TYPE_LOADER.load(String.class)).isPresent(), is(true));
    //    DslElementDeclaration innerElement = result.getGenericsDsl().get(0);
    //    assertThat(innerElement.getElementName(), equalTo(getTopLevelTypeName(itemType)));
    //    assertChildElementDeclarationIs(true, innerElement);
    //    assertIsWrappedElement(false, innerElement);
    //}
    //
    //@Test
    //public void testCollectionOfNonInstantiableTypeParameter()
    //{
    //    when(parameterModel.getType()).thenReturn(TYPE_BUILDER.arrayType().id(List.class.getName())
    //                                                      .of(TYPE_LOADER.load(InterfaceDeclaration.class))
    //                                                      .build());
    //    DslElementDeclaration result = new DslElementResolver(extension).resolve(parameterModel);
    //
    //    assertAttributeName(PARAMETER_NAME, result);
    //    assertElementName(hyphenize(PARAMETER_NAME), result);
    //    assertElementNamespace(NAMESPACE, result);
    //    assertChildElementDeclarationIs(false, result);
    //    assertIsWrappedElement(false, result);
    //
    //    assertThat(result.getGenericsDsl().isEmpty(), is(true));
    //}
    //
    @Xml(namespace = IMPORT_NAMESPACE, namespaceLocation = IMPORT_NAMESPACE_URI)
    @Extension(name = IMPORT_EXTENSION_NAME)
    private static final class ExtensionForImportsDeclaresXml
    {

    }

    @Extension(name = IMPORT_EXTENSION_NAME)
    private static final class ExtensionForImportsNoXml
    {

    }
}
