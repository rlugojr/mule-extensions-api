/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.introspection.declaration.fluent.util;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;
import org.mule.runtime.extension.api.introspection.declaration.fluent.ConfigurationDeclaration;
import org.mule.runtime.extension.api.introspection.declaration.fluent.ConnectedDeclaration;
import org.mule.runtime.extension.api.introspection.declaration.fluent.ConnectionProviderDeclaration;
import org.mule.runtime.extension.api.introspection.declaration.fluent.ExtensionDeclaration;
import org.mule.runtime.extension.api.introspection.declaration.fluent.OperationDeclaration;
import org.mule.runtime.extension.api.introspection.declaration.fluent.ParameterDeclaration;
import org.mule.runtime.extension.api.introspection.declaration.fluent.ParameterizedInterceptableDeclaration;
import org.mule.runtime.extension.api.introspection.declaration.fluent.SourceDeclaration;
import org.mule.runtime.extension.api.introspection.declaration.fluent.WithOperationsDeclaration;
import org.mule.runtime.extension.api.introspection.declaration.fluent.WithSourcesDeclaration;

import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class DeclarationWalkerTestCase
{

    @Mock
    private ExtensionDeclaration extension;

    @Mock
    private ConfigurationDeclaration configuration;

    @Mock
    private OperationDeclaration operation;

    @Mock
    private ConnectionProviderDeclaration connectionProvider;

    @Mock
    private ParameterDeclaration parameterModel;

    @Mock
    private SourceDeclaration source;

    @Before
    public void before()
    {
        when(extension.getConfigurations()).thenReturn(asList(configuration));
        when(extension.getOperations()).thenReturn(asList(operation));
        when(extension.getMessageSources()).thenReturn(asList(source));
        when(extension.getConnectionProviders()).thenReturn(asList(connectionProvider));

        when(configuration.getOperations()).thenReturn(asList(operation));
        when(configuration.getMessageSources()).thenReturn(asList(source));
        when(configuration.getConnectionProviders()).thenReturn(asList(connectionProvider));

        addParameter(configuration, operation, connectionProvider, source);
    }

    private void addParameter(ParameterizedInterceptableDeclaration... declarations)
    {
        for (ParameterizedInterceptableDeclaration declaration : declarations)
        {
            when(declaration.getParameters()).thenReturn(asList(parameterModel));
        }
    }

    @Test
    public void walk()
    {
        AtomicInteger configs = new AtomicInteger(0);
        AtomicInteger operations = new AtomicInteger(0);
        AtomicInteger sources = new AtomicInteger(0);
        AtomicInteger parameters = new AtomicInteger(0);
        AtomicInteger providers = new AtomicInteger(0);

        new DeclarationWalker()
        {
            @Override
            public void onConfiguration(ConfigurationDeclaration declaration)
            {
                configs.incrementAndGet();
            }

            @Override
            public void onOperation(WithOperationsDeclaration owner, OperationDeclaration declaration)
            {
                operations.incrementAndGet();
            }

            @Override
            public void onConnectionProvider(ConnectedDeclaration owner, ConnectionProviderDeclaration declaration)
            {
                providers.incrementAndGet();
            }

            @Override
            public void onSource(WithSourcesDeclaration owner, SourceDeclaration declaration)
            {
                sources.incrementAndGet();
            }

            @Override
            public void onParameter(ParameterizedInterceptableDeclaration owner, ParameterDeclaration declaration)
            {
                parameters.incrementAndGet();
            }
        }.walk(extension);

        assertCount(configs, 1);
        assertCount(operations, 2);
        assertCount(sources, 2);
        assertCount(providers, 2);
        assertCount(parameters, 7);
    }

    private void assertCount(AtomicInteger actual, int expected)
    {
        assertThat(actual.get(), is(expected));
    }
}