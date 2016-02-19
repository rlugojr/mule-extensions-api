/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.api.introspection.impl;

import org.mule.extension.api.introspection.ExpressionSupport;
import org.mule.extension.api.introspection.IDataType;
import org.mule.extension.api.introspection.ParameterModel;

/**
 * Default implementation of {@link ParameterModel}
 *
 * @since 1.0
 */
public class DefaultParameterModel extends AbstractBaseDefaultExtensionModel implements ParameterModel
{

    private IDataType type;
    private boolean required;
    private ExpressionSupport expressionSupport;
    private Object defaultValue;

    public IDataType getType()
    {
        return type;
    }

    public void setType(IDataType type)
    {
        this.type = type;
    }

    public boolean isRequired()
    {
        return required;
    }

    public void setRequired(boolean required)
    {
        this.required = required;
    }

    public ExpressionSupport getExpressionSupport()
    {
        return expressionSupport;
    }

    public void setExpressionSupport(ExpressionSupport expressionSupport)
    {
        this.expressionSupport = expressionSupport;
    }

    public Object getDefaultValue()
    {
        return defaultValue;
    }

    public void setDefaultValue(Object defaultValue)
    {
        this.defaultValue = defaultValue;
    }
}
