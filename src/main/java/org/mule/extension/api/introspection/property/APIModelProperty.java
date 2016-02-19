/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.api.introspection.property;

/**
 * Interface marker used to mark a model property as a property that can be serialized
 *
 * @since 1.0
 */
public interface APIModelProperty
{
    String getKey();
}
