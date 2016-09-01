/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.manifest;


import org.mule.runtime.api.MuleVersion;
import org.mule.runtime.extension.api.introspection.Described;
import org.mule.runtime.extension.api.introspection.ExtensionModel;
import org.mule.runtime.extension.api.introspection.Named;

import java.util.List;

/**
 * A manifest which enunciates the main properties of a {@link ExtensionModel}
 * which is yet to be instantiated.
 *
 * @see ExtensionManifestBuilder
 * @since 1.0
 */
public interface ExtensionManifest extends Named, Described {

  /**
   * @return the extension's version
   */
  String getVersion();

  /**
   * @return the extension minimum Mule Runtime version which the extension requires to work correctly
   */
  MuleVersion getMinMuleVersion();

  /**
   * @return a {@link DescriberManifest}
   */
  DescriberManifest getDescriberManifest();

  /**
   * @return The {@link List} of java package names that the extension exposes
   */
  List<String> getExportedPackages();

  /**
   * @return The {@link List} of resources paths that the extension exposes
   */
  List<String> getExportedResources();

  /**
   * @return The {@link List} of plugins that the extension is dependant on.
   */
  List<String> getPluginDependencies();
}
