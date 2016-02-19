/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.api.introspection.impl;

import org.mule.extension.api.introspection.property.StudioModelProperty;

/**
 * Default implementation of {@link StudioModelProperty}
 *
 * @since 1.0
 */
public class DefaultStudioModelProperty implements StudioModelProperty
{

    private String editorFileName;
    private boolean derived;
    private String key;

    public String getEditorFileName()
    {
        return editorFileName;
    }

    public void setEditorFileName(String editorFileName)
    {
        this.editorFileName = editorFileName;
    }

    public boolean isDerived()
    {
        return derived;
    }

    public void setDerived(boolean derived)
    {
        this.derived = derived;
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
