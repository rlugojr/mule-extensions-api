/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.api.introspection.impl;

import org.mule.extension.api.introspection.property.XmlModelProperty;

/**
 * Default implementation of {@link XmlModelProperty}
 *
 * @since 1.0
 */
public class DefaultXmlModelProperty implements XmlModelProperty
{

    private String schemaVersion;
    private String namespace;
    private String schemaLocation;
    private String key;

    public String getSchemaVersion()
    {
        return schemaVersion;
    }

    public void setSchemaVersion(String schemaVersion)
    {
        this.schemaVersion = schemaVersion;
    }

    public String getNamespace()
    {
        return namespace;
    }

    public void setNamespace(String namespace)
    {
        this.namespace = namespace;
    }

    public String getSchemaLocation()
    {
        return schemaLocation;
    }

    public void setSchemaLocation(String schemaLocation)
    {
        this.schemaLocation = schemaLocation;
    }

    public String getKey()
    {
        return key;
    }

    public void setKey(String key)
    {
        this.key = key;
    }
}
