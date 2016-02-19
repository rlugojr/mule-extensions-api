/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.api.introspection.impl;

import org.mule.extension.api.introspection.Described;
import org.mule.extension.api.introspection.EnrichableModel;

import java.util.Map;

/**
 * Base class for the default implementations of the introspection model
 *
 * @since 1.0
 */
public class AbstractBaseDefaultExtensionModel implements EnrichableModel, Described
{

    private String name;
    private String description;
    private Map<String, Object> modelProperties;

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public <T> T getModelProperty(String key)
    {
        return (T) modelProperties.get(key);
    }

    public Map<String, Object> getModelProperties()
    {
        return modelProperties;
    }

    public void setModelProperties(Map<String, Object> modelProperties)
    {
        this.modelProperties = modelProperties;
    }
}
