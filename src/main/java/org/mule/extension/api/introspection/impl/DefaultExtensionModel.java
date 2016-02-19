/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.api.introspection.impl;

import org.mule.extension.api.exception.NoSuchConfigurationException;
import org.mule.extension.api.exception.NoSuchMessageSourceException;
import org.mule.extension.api.exception.NoSuchOperationException;
import org.mule.extension.api.introspection.ConfigurationModel;
import org.mule.extension.api.introspection.ConnectionProviderModel;
import org.mule.extension.api.introspection.ExceptionEnricherFactory;
import org.mule.extension.api.introspection.ExtensionModel;
import org.mule.extension.api.introspection.OperationModel;
import org.mule.extension.api.introspection.SourceModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Default implementation of {@link ExtensionModel}
 *
 * @since 1.0
 */
public class DefaultExtensionModel extends AbstractBaseDefaultExtensionModel implements ExtensionModel
{

    private String version;
    private Map<String, ConfigurationModel> configurationModels = new HashMap<>();
    private Map<String, OperationModel> operationModels = new HashMap<>();
    private List<ConnectionProviderModel> connectionProviders;
    private Map<String, SourceModel> sourceModels = new HashMap<>();
    private String vendor;
    private Optional<ExceptionEnricherFactory> exceptionEnricherFactory;

    public String getVersion()
    {
        return version;
    }

    public void setVersion(String version)
    {
        this.version = version;
    }

    public List<ConfigurationModel> getConfigurationModels()
    {
        return new ArrayList(configurationModels.values());
    }

    public void setConfigurationModels(List<ConfigurationModel> configurationModels)
    {
        for (ConfigurationModel config : configurationModels)
        {
            this.configurationModels.put(config.getName(), config);
        }
    }

    public ConfigurationModel getConfigurationModel(String key) throws NoSuchConfigurationException
    {
        return configurationModels.get(key);
    }

    public List<OperationModel> getOperationModels()
    {
        return new ArrayList(operationModels.values());
    }

    public void setOperationModels(List<OperationModel> operationModels)
    {
        for (OperationModel operation : operationModels)
        {
            this.operationModels.put(operation.getName(), operation);
        }
    }

    public OperationModel getOperationModel(String key) throws NoSuchOperationException
    {
        return operationModels.get(key);
    }

    public List<ConnectionProviderModel> getConnectionProviders()
    {
        return connectionProviders;
    }

    public void setConnectionProviders(List<ConnectionProviderModel> connectionProviders)
    {
        this.connectionProviders = connectionProviders;
    }

    public List<SourceModel> getSourceModels()
    {
        return new ArrayList(sourceModels.values());
    }

    public void setSourceModels(List<SourceModel> sourceModels)
    {
        for (SourceModel source : sourceModels)
        {
            this.sourceModels.put(source.getName(), source);
        }
    }

    public SourceModel getSourceModel(String key) throws NoSuchMessageSourceException
    {
        return sourceModels.get(key);
    }

    public String getVendor()
    {
        return vendor;
    }

    public void setVendor(String vendor)
    {
        this.vendor = vendor;
    }

    public Optional<ExceptionEnricherFactory> getExceptionEnricherFactory()
    {
        return exceptionEnricherFactory;
    }

    public void setExceptionEnricherFactory(Optional<ExceptionEnricherFactory> exceptionEnricherFactory)
    {
        this.exceptionEnricherFactory = exceptionEnricherFactory;
    }
}
