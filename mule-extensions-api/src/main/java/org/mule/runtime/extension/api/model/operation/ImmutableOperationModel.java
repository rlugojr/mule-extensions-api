/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.model.operation;

import static org.mule.runtime.extension.api.util.ExtensionModelUtils.resolveOutputModelType;
import static org.mule.runtime.extension.api.util.ExtensionModelUtils.resolveParameterGroupModelType;
import org.mule.runtime.api.message.Message;
import org.mule.runtime.api.meta.model.ExecutionType;
import org.mule.runtime.api.meta.model.ModelProperty;
import org.mule.runtime.api.meta.model.OutputModel;
import org.mule.runtime.api.meta.model.display.DisplayModel;
import org.mule.runtime.api.meta.model.error.ErrorModel;
import org.mule.runtime.api.meta.model.operation.OperationModel;
import org.mule.runtime.api.meta.model.parameter.ParameterGroupModel;
import org.mule.runtime.api.metadata.descriptor.InputMetadataDescriptor;
import org.mule.runtime.api.metadata.descriptor.OutputMetadataDescriptor;
import org.mule.runtime.extension.api.model.AbstractComponentModel;

import java.util.List;
import java.util.Set;

/**
 * Immutable concrete implementation of {@link OperationModel}
 *
 * @since 1.0
 */
public class ImmutableOperationModel extends AbstractComponentModel<OperationModel> implements OperationModel {

  private final boolean blocking;
  private final ExecutionType executionType;
  private final Set<ErrorModel> errors;

  /**
   * Creates a new instance with the given state
   *
   * @param name                 the operation's name. Cannot be blank
   * @param description          the operation's descriptor
   * @param parameterGroupModels a {@link List} with the operation's {@link ParameterGroupModel parameter group models}
   * @param output               an {@link OutputModel} which represents the operation's output content
   * @param outputAttributes     an {@link OutputModel} which represents the attributes on the output {@link Message}
   * @param blocking             whether this operation executes in a blocking manner
   * @param executionType        describes the type of processing this operation performs
   * @param requiresConnection   whether this component requires connectivity
   * @param transactional        whether this component supports transactions
   * @param displayModel         a model which contains directive about how this operation is displayed in the UI
   * @param errors               A {@link Set} with all the {@link ErrorModel} that are declared to be thrown by
   *                             the operation
   * @param modelProperties      A {@link Set} of custom properties which extend this model
   * @throws IllegalArgumentException if {@code name} is blank or {@code executorFactory} is {@code null}
   */
  public ImmutableOperationModel(String name,
                                 String description,
                                 List<ParameterGroupModel> parameterGroupModels,
                                 OutputModel output,
                                 OutputModel outputAttributes,
                                 boolean blocking,
                                 ExecutionType executionType,
                                 boolean requiresConnection,
                                 boolean transactional,
                                 DisplayModel displayModel,
                                 Set<ErrorModel> errors,
                                 Set<ModelProperty> modelProperties) {
    super(name, description, parameterGroupModels, output, outputAttributes, requiresConnection, transactional, displayModel,
          modelProperties);
    this.blocking = blocking;
    this.executionType = executionType;
    this.errors = errors;
  }

  /**
   * {@inheritDoc}
   */
  public Set<ErrorModel> getErrorModels() {
    return errors;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isBlocking() {
    return blocking;
  }

  /**
   * {@inheritDoc}
   */
  public ExecutionType getExecutionType() {
    return executionType;
  }

  @Override
  public OperationModel getTypedModel(InputMetadataDescriptor inputMetadataDescriptor,
                                      OutputMetadataDescriptor outputMetadataDescriptor) {
    OutputModel typedOutputModel = resolveOutputModelType(getOutput(), outputMetadataDescriptor.getPayloadMetadata());
    OutputModel typedAttributesModel =
        resolveOutputModelType(getOutputAttributes(), outputMetadataDescriptor.getAttributesMetadata());

    return new ImmutableOperationModel(getName(), getDescription(),
                                       resolveParameterGroupModelType(getParameterGroupModels(),
                                                                      inputMetadataDescriptor
                                                                          .getAllParameters()),
                                       typedOutputModel, typedAttributesModel, isBlocking(),
                                       getExecutionType(), requiresConnection(),
                                       isTransactional(),
                                       getDisplayModel().orElse(null),
                                       getErrorModels(),
                                       getModelProperties());

  }
}
