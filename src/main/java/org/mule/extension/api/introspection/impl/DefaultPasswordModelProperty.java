/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.api.introspection.impl;

import org.mule.extension.api.introspection.property.PasswordModelProperty;

/**
 * Default implementation of {@link PasswordModelProperty}
 *
 * @since 1.0
 */
public class DefaultPasswordModelProperty implements PasswordModelProperty
{

    private String key;

    public String getKey()
    {
        return key;
    }

    public void setKey(String key)
    {
        this.key = key;
    }
}
