/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.api.introspection.impl;

import org.mule.api.connection.ConnectionException;
import org.mule.api.connection.ConnectionHandlingStrategy;
import org.mule.api.connection.ConnectionHandlingStrategyFactory;
import org.mule.api.connection.ConnectionValidationResult;
import org.mule.extension.api.introspection.ConnectionProviderFactory;
import org.mule.extension.api.introspection.ConnectionProviderModel;
import org.mule.extension.api.introspection.ParameterModel;

import java.util.List;

/**
 * Default implementation of {@link ConnectionProviderModel}
 *
 * @since 1.0
 */
public class DefaultConnectionProviderModel<Config, Connection> extends AbstractBaseDefaultExtensionModel implements ConnectionProviderModel<Config, Connection>
{

    private List<ParameterModel> parameterModels;
    private Class<Config> configurationType;
    private Class<Connection> connectionType;

    public List<ParameterModel> getParameterModels()
    {
        return parameterModels;
    }

    public void setParameterModels(List<ParameterModel> parameterModels)
    {
        this.parameterModels = parameterModels;
    }

    public Class<Config> getConfigurationType()
    {
        return configurationType;
    }

    public void setConfigurationType(Class<Config> configurationType)
    {
        this.configurationType = configurationType;
    }

    public Class<Connection> getConnectionType()
    {
        return connectionType;
    }

    public void setConnectionType(Class<Connection> connectionType)
    {
        this.connectionType = connectionType;
    }

    public Connection connect(Config config) throws ConnectionException
    {
        return null;
    }

    public void disconnect(Connection connection)
    {

    }

    public ConnectionValidationResult validate(Connection connection)
    {
        return null;
    }

    public ConnectionHandlingStrategy<Connection> getHandlingStrategy(ConnectionHandlingStrategyFactory<Config, Connection> connectionHandlingStrategyFactory)
    {
        return null;
    }

    @Override
    public ConnectionProviderFactory getConnectionProviderFactory()
    {
        return null;
    }
}
