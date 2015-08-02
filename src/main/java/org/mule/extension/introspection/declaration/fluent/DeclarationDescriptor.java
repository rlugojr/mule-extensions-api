/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.introspection.declaration.fluent;

/**
 * A {@link Descriptor} which allows configuring a {@link Declaration}
 * through a fluent API
 *
 * @since 1.0
 */
public class DeclarationDescriptor implements Descriptor, HasCapabilities<DeclarationDescriptor>
{

    private final Declaration declaration;

    /**
     * Constructor for this descriptor
     *
     * @param name    a non blank name
     * @param version a non blank version
     */
    public DeclarationDescriptor(String name, String version)
    {
        declaration = new Declaration(name, version);
    }

    /**
     * Adds a description
     *
     * @param description a description
     * @return this descriptor
     */
    public DeclarationDescriptor describedAs(String description)
    {
        declaration.setDescription(description);
        return this;
    }

    /**
     * Adds a config of the given name
     *
     * @param name a non blank name
     * @return a {@link ConfigurationDescriptor} which allows describing the created configuration
     */
    public ConfigurationDescriptor withConfig(String name)
    {
        ConfigurationDeclaration config = new ConfigurationDeclaration(name);
        declaration.addConfig(config);

        return new ConfigurationDescriptor(config, this);
    }

    /**
     * Adds an operation of the given name
     *
     * @param name a non blank name
     * @return a {@link OperationDescriptor} which allows describing the created operation
     */
    public OperationDescriptor withOperation(String name)
    {
        OperationDeclaration operation = new OperationDeclaration(name);
        declaration.addOperation(operation);

        return new OperationDescriptor(operation, this);
    }

    /**
     * Adds the given capability to this declaration
     *
     * @param capability a not {@code null} capability
     * @return this descriptor
     */
    @Override
    public DeclarationDescriptor withCapability(Object capability)
    {
        declaration.addCapability(capability);
        return this;
    }

    /**
     * @return {@value this}
     */
    @Override
    public DeclarationDescriptor getRootDeclaration()
    {
        return this;
    }

    /**
     * @return the configured {@link Declaration}
     */
    public Declaration getDeclaration()
    {
        return declaration;
    }
}