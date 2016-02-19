/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.api.introspection.impl;

import org.mule.extension.api.introspection.PoolingSupport;
import org.mule.extension.api.introspection.property.ConnectionHandlingTypeModelProperty;

/**
 * Default implementation of {@link ConnectionHandlingTypeModelProperty}
 *
 * @since 1.0
 */
public class DefaultConnectionHandlingTypeModelProperty implements ConnectionHandlingTypeModelProperty
{

    private boolean cached;
    private boolean none;
    private boolean pooled;
    private PoolingSupport poolingSupport;
    private String key;

    @Override
    public boolean isCached()
    {
        return cached;
    }

    public void setCached(boolean cached)
    {
        this.cached = cached;
    }

    @Override
    public boolean isNone()
    {
        return none;
    }

    public void setNone(boolean none)
    {
        this.none = none;
    }

    @Override
    public boolean isPooled()
    {
        return pooled;
    }

    public void setPooled(boolean pooled)
    {
        this.pooled = pooled;
    }

    @Override
    public PoolingSupport getPoolingSupport()
    {
        return poolingSupport;
    }

    public void setPoolingSupport(PoolingSupport poolingSupport)
    {
        this.poolingSupport = poolingSupport;
    }

    @Override
    public String getKey()
    {
        return key;
    }

    public void setKey(String key)
    {
        this.key = key;
    }
}
