/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.api.introspection.impl;

import org.mule.extension.api.introspection.ExceptionEnricherFactory;
import org.mule.extension.api.introspection.IDataType;
import org.mule.extension.api.introspection.OperationModel;
import org.mule.extension.api.introspection.ParameterModel;
import org.mule.extension.api.runtime.InterceptorFactory;
import org.mule.extension.api.runtime.OperationExecutorFactory;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Default implementation of {@link OperationModel}
 *
 * @since 1.0
 */
public class DefaultOperationModel extends AbstractBaseDefaultExtensionModel implements OperationModel
{

    private List<ParameterModel> parameterModels;
    private IDataType returnType;

    public List<ParameterModel> getParameterModels()
    {
        return parameterModels;
    }

    public void setParameterModels(List<ParameterModel> parameterModels)
    {
        this.parameterModels = parameterModels;
    }

    public OperationExecutorFactory getExecutor()
    {
        return null;
    }

    public IDataType getReturnType()
    {
        return returnType;
    }

    public void setReturnType(IDataType returnType)
    {
        this.returnType = returnType;
    }

    public Optional<ExceptionEnricherFactory> getExceptionEnricherFactory()
    {
        return null;
    }

    public List<InterceptorFactory> getInterceptorFactories()
    {
        return Collections.emptyList();
    }
}
