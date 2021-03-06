/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.internal.loader.validator;

import static java.lang.String.format;
import static java.util.Collections.singleton;
import static java.util.stream.Collectors.toList;
import static org.mule.metadata.java.api.utils.JavaTypeUtils.getType;
import static org.mule.runtime.api.meta.model.parameter.ParameterModel.RESERVED_NAMES;
import static org.mule.runtime.extension.api.util.ExtensionMetadataTypeUtils.getId;
import static org.mule.runtime.extension.api.util.NameUtils.getComponentModelTypeName;
import static org.mule.runtime.extension.api.util.NameUtils.getTopLevelTypeName;
import static org.mule.runtime.extension.api.util.NameUtils.hyphenize;
import static org.mule.runtime.extension.api.util.NameUtils.singularize;
import org.mule.metadata.api.model.ArrayType;
import org.mule.metadata.api.model.MetadataType;
import org.mule.metadata.api.model.ObjectFieldType;
import org.mule.metadata.api.model.ObjectType;
import org.mule.metadata.api.visitor.MetadataTypeVisitor;
import org.mule.runtime.api.meta.model.ExtensionModel;
import org.mule.runtime.api.meta.model.config.ConfigurationModel;
import org.mule.runtime.api.meta.model.connection.ConnectionProviderModel;
import org.mule.runtime.api.meta.model.operation.OperationModel;
import org.mule.runtime.api.meta.model.parameter.ParameterGroupModel;
import org.mule.runtime.api.meta.model.parameter.ParameterModel;
import org.mule.runtime.api.meta.model.parameter.ParameterizedModel;
import org.mule.runtime.api.meta.model.util.ExtensionWalker;
import org.mule.runtime.api.meta.type.TypeCatalog;
import org.mule.runtime.extension.api.declaration.type.annotation.XmlHintsAnnotation;
import org.mule.runtime.extension.api.dsl.syntax.DslElementSyntax;
import org.mule.runtime.extension.api.dsl.syntax.resolver.DslSyntaxResolver;
import org.mule.runtime.extension.api.dsl.syntax.resolver.SingleExtensionImportTypesStrategy;
import org.mule.runtime.extension.api.exception.IllegalParameterModelDefinitionException;
import org.mule.runtime.extension.api.loader.ExtensionModelValidator;
import org.mule.runtime.extension.api.loader.Problem;
import org.mule.runtime.extension.api.loader.ProblemsReporter;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Validates that all {@link ParameterModel parameters} provided by the {@link ConfigurationModel configurations},
 * {@link ConnectionProviderModel connection providers} and {@link OperationModel operations} from the {@link ExtensionModel
 * extension} complies with:
 * <ul>
 * <li>The name must not be one of the reserved ones</li>
 * <li>If the parameter is a {@link ArrayType} the name should be plural</li>
 * <li>The {@link MetadataType metadataType} must be provided</li>
 * <li>If required, cannot provide a default value</li>
 * <li>The {@link Class} of the parameter must be valid too, that implies that the class shouldn't contain any field with a
 * reserved name.
 * </ul>
 *
 * @since 1.0
 */
public final class ParameterModelValidator implements ExtensionModelValidator {

  private TypeCatalog typeCatalog;
  private DslSyntaxResolver dsl;

  @Override
  public void validate(ExtensionModel extensionModel, ProblemsReporter problemsReporter) {
    typeCatalog = TypeCatalog.getDefault(singleton(extensionModel));
    dsl = DslSyntaxResolver.getDefault(extensionModel, new SingleExtensionImportTypesStrategy());

    new ExtensionWalker() {

      @Override
      public void onParameter(ParameterizedModel owner, ParameterGroupModel groupModel, ParameterModel model) {
        String ownerName = owner.getName();
        String ownerModelType = getComponentModelTypeName(owner);
        validateParameter(model, ownerName, ownerModelType, problemsReporter);
        validateNameCollisionWithTypes(model, ownerName, ownerModelType,
                                       owner.getAllParameterModels().stream().map(p -> hyphenize(p.getName())).collect(toList()),
                                       problemsReporter);
      }
    }.walk(extensionModel);
  }

  private void validateParameter(ParameterModel parameterModel, String ownerName,
                                 String ownerModelType, ProblemsReporter problemsReporter) {
    if (RESERVED_NAMES.contains(parameterModel.getName())) {
      problemsReporter.addError(new Problem(parameterModel, format("Parameter '%s' in the %s '%s' is named after a reserved one",
                                                                   parameterModel.getName(), ownerModelType, ownerName)));
    }

    if (parameterModel.getType() == null) {
      problemsReporter.addError(new Problem(parameterModel, format("Parameter '%s' in the %s '%s' must provide a type",
                                                                   parameterModel.getName(),
                                                                   ownerModelType, ownerName)));
    }

    if (parameterModel.isRequired() && parameterModel.getDefaultValue() != null) {
      problemsReporter
          .addError(new Problem(parameterModel,
                                format("Parameter '%s' in the %s '%s' is required, and must not provide a default value",
                                       parameterModel.getName(), ownerModelType, ownerName)));
    }

    if (parameterModel.getType() == null) {
      problemsReporter
          .addError(new Problem(parameterModel,
                                format("Parameter '%s' in the %s '%s' doesn't specify a return type",
                                       parameterModel.getName(), ownerModelType, ownerName)));
    } else {

      parameterModel.getType().accept(new MetadataTypeVisitor() {

        private Set<MetadataType> visitedTypes = new HashSet<>();

        @Override
        public void visitArrayType(ArrayType arrayType) {
          arrayType.getType().accept(this);
        }

        @Override
        public void visitObject(ObjectType objectType) {
          DslElementSyntax paramDsl = dsl.resolve(parameterModel);
          if (objectType.isOpen()) {
            objectType.getOpenRestriction().get().accept(this);
          } else if ((paramDsl.supportsTopLevelDeclaration() || paramDsl.supportsChildDeclaration())
              && visitedTypes.add(objectType)) {
            for (ObjectFieldType field : objectType.getFields()) {

              String fieldName = field.getKey().getName().getLocalPart();
              if (RESERVED_NAMES.contains(fieldName)) {
                throw new IllegalParameterModelDefinitionException(
                                                                   format("The field named '%s' [%s] from class [%s] cannot have that name since it is a reserved one",
                                                                          fieldName, getId(field.getValue()), getId(objectType)));
              }

              if (supportsGlobalReferences(field)) {
                field.getValue().accept(this);
              }
            }
          }
        }
      });

      validateParameterIsPlural(parameterModel, ownerModelType, ownerName, problemsReporter);
    }
  }

  private void validateParameterIsPlural(final ParameterModel parameterModel, String ownerModelType, String ownerName,
                                         ProblemsReporter problemsReporter) {
    parameterModel.getType().accept(new MetadataTypeVisitor() {

      @Override
      public void visitArrayType(ArrayType arrayType) {
        if (parameterModel.getName().equals(singularize(parameterModel.getName()))) {
          problemsReporter
              .addError(new Problem(parameterModel,
                                    format("Parameter '%s' in the %s '%s' is a collection and its name should be plural",
                                           parameterModel.getName(), ownerModelType, ownerName)));
        }
      }
    });
  }

  private void validateNameCollisionWithTypes(ParameterModel parameterModel, String ownerName, String ownerModelType,
                                              List<String> parameterNames, ProblemsReporter problemsReporter) {
    if (parameterModel.getType() instanceof ObjectType) {
      typeCatalog.getSubTypes((ObjectType) parameterModel.getType())
          .stream()
          .filter(subtype -> parameterNames.contains(getTopLevelTypeName(subtype)))
          .findFirst()
          .ifPresent(metadataType -> problemsReporter.addError(
                                                               new Problem(parameterModel, format(
                                                                                                  "Parameter '%s' in the %s [%s] can't have the same name as the ClassName or Alias of the declared subType [%s] for parameter [%s]",
                                                                                                  getTopLevelTypeName(metadataType),
                                                                                                  ownerModelType, ownerName,
                                                                                                  getType(metadataType)
                                                                                                      .getSimpleName(),
                                                                                                  parameterModel.getName()))));
    }
  }

  private boolean supportsGlobalReferences(ObjectFieldType field) {
    return dsl.resolve(field.getValue()).map(DslElementSyntax::supportsTopLevelDeclaration)
        .orElseGet(() -> field.getAnnotation(XmlHintsAnnotation.class).map(XmlHintsAnnotation::allowsReferences)
            .orElse(true));
  }

}
