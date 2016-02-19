/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.api.introspection.impl;

import org.mule.extension.api.introspection.IDataType;
import org.mule.extension.api.introspection.ParameterModel;
import org.mule.extension.api.introspection.SourceModel;
import org.mule.extension.api.runtime.InterceptorFactory;
import org.mule.extension.api.runtime.source.SourceFactory;

import java.util.List;

/**
 * Default implementation of {@link SourceModel}
 *
 * @since 1.0
 */
public class DefaultSourceModel extends AbstractBaseDefaultExtensionModel implements SourceModel
{

    private List<ParameterModel> parameterModels;
    private IDataType returnType;
    private IDataType attributesType;
    private SourceFactory sourceFactory;

    public List<ParameterModel> getParameterModels()
    {
        return parameterModels;
    }

    public void setParameterModels(List<ParameterModel> parameterModels)
    {
        this.parameterModels = parameterModels;
    }

    public IDataType getReturnType()
    {
        return returnType;
    }

    public void setReturnType(IDataType returnType)
    {
        this.returnType = returnType;
    }

    public IDataType getAttributesType()
    {
        return attributesType;
    }

    public void setAttributesType(IDataType attributesType)
    {
        this.attributesType = attributesType;
    }

    public SourceFactory getSourceFactory()
    {
        return sourceFactory;
    }

    public void setSourceFactory(SourceFactory sourceFactory)
    {
        this.sourceFactory = sourceFactory;
    }

    public List<InterceptorFactory> getInterceptorFactories()
    {
        return null;
    }
}
