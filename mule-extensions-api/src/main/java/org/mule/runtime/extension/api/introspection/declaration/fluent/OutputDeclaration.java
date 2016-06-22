/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.introspection.declaration.fluent;

import org.mule.metadata.api.model.MetadataType;
import org.mule.runtime.extension.api.introspection.OutputModel;

/**
 * A declaration object for an {@link OutputModel}. It contains raw,
 * unvalidated data which is used to declare the structure of an {@link OutputModel}.
 *
 * @since 1.0
 */
public class OutputDeclaration extends BaseDeclaration<OutputDeclaration>
{

    private MetadataType type;
    private boolean hasDynamicType;

    public void setType(MetadataType type, boolean isDynamic)
    {
        this.type = type;
        this.hasDynamicType = isDynamic;
    }

    public MetadataType getType()
    {
        return type;
    }

    public boolean hasDynamicType()
    {
        return hasDynamicType;
    }
}