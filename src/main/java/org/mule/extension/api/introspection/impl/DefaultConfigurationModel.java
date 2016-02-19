/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.api.introspection.impl;

import org.mule.extension.api.introspection.ConfigurationFactory;
import org.mule.extension.api.introspection.ConfigurationModel;
import org.mule.extension.api.introspection.ExtensionModel;
import org.mule.extension.api.introspection.ParameterModel;
import org.mule.extension.api.runtime.InterceptorFactory;

import java.util.Collections;
import java.util.List;

/**
 * Default implementation of {@link ConfigurationModel}
 *
 * @since 1.0
 */
public class DefaultConfigurationModel extends AbstractBaseDefaultExtensionModel implements ConfigurationModel
{

    private ExtensionModel extensionModel;
    private List<ParameterModel> parameterModels;
    private ConfigurationFactory configurationFactory;

    public ExtensionModel getExtensionModel()
    {
        return extensionModel;
    }

    public void setExtensionModel(ExtensionModel extensionModel)
    {
        this.extensionModel = extensionModel;
    }

    public List<ParameterModel> getParameterModels()
    {
        return parameterModels;
    }

    public void setParameterModels(List<ParameterModel> parameterModels)
    {
        this.parameterModels = parameterModels;
    }

    public ConfigurationFactory getConfigurationFactory()
    {
        return configurationFactory;
    }

    public void setConfigurationFactory(ConfigurationFactory configurationFactory)
    {
        this.configurationFactory = configurationFactory;
    }

    public List<InterceptorFactory> getInterceptorFactories()
    {
        return Collections.emptyList();
    }
}
