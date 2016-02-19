/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.api.introspection.impl;

import org.mule.extension.api.introspection.DataQualifier;
import org.mule.extension.api.introspection.IDataType;

/**
 * Default implementation {@link IDataType}
 *
 * @since 1.0
 */
public class DefaultDataType implements IDataType
{

    private String name;
    private Class<?> rawType;
    private IDataType[] genericTypes;
    private DataQualifier qualifier;

    @Override
    public Class<?> getRawType()
    {
        return rawType;
    }

    public void setRawType(Class<?> rawType)
    {
        this.rawType = rawType;
    }

    @Override
    public IDataType[] getGenericTypes()
    {
        return genericTypes;
    }

    public void setGenericTypes(IDataType[] genericTypes)
    {
        this.genericTypes = genericTypes;
    }

    @Override
    public DataQualifier getQualifier()
    {
        return qualifier;
    }

    public void setQualifier(DataQualifier qualifier)
    {
        this.qualifier = qualifier;
    }

    @Override
    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }
}
