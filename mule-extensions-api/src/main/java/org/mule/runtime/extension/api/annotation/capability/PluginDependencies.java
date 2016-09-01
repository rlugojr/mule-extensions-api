/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.annotation.capability;

import org.mule.runtime.extension.api.annotation.Extension;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Allows the capability to specify what plugins is the annotated {@link Extension} dependant on.
 * <p>
 * The specified dependencies are bounded in the generated extension-manifest.xml file.
 *
 * @since 1.0
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface PluginDependencies {

  /**
   * The names of the plugin dependencies used by the extension.
   */
  String[] value();
}
