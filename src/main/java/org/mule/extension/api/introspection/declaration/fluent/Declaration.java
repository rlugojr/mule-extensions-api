/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.api.introspection.declaration.fluent;

import org.mule.extension.api.introspection.ExtensionModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * A declaration object for a {@link ExtensionModel}. It contains raw, unvalidated
 * data which is used to declare the structure of a {@link ExtensionModel}
 *
 * @since 1.0
 */
public class Declaration extends BaseDeclaration<Declaration>
{

    private String name;
    private String version;
    private String description;

    private final List<ConfigurationDeclaration> configurations = new ArrayList<>();
    private final List<OperationDeclaration> operations = new LinkedList<>();

    Declaration()
    {
    }

    /**
     * Returns an immutable list with the {@link ConfigurationDeclaration} instances
     * that have been declared so far.
     *
     * @return an unmodifiable list. May be empty but will never be {@code null}
     */
    public List<ConfigurationDeclaration> getConfigurations()
    {
        return Collections.unmodifiableList(configurations);
    }

    /**
     * Adds a {@link ConfigurationDeclaration}
     *
     * @param config a not {@code null} {@link ConfigurationDeclaration}
     * @return this declaration
     * @throws {@link IllegalArgumentException} if {@code config} is {@code null}
     */
    public Declaration addConfig(ConfigurationDeclaration config)
    {
        if (config == null)
        {
            throw new IllegalArgumentException("Can't add a null config");
        }

        configurations.add(config);
        return this;
    }

    /**
     * @return an unmodifiable {@link List} with
     * the available {@link OperationDeclaration}s
     */
    public List<OperationDeclaration> getOperations()
    {
        return Collections.unmodifiableList(operations);
    }

    /**
     * Adds a {@link OperationDeclaration}
     *
     * @param operation a not {@code null} {@link OperationDeclaration}
     * @return this declaration
     * @throws {@link IllegalArgumentException} if {@code operation} is {@code null}
     */
    public Declaration addOperation(OperationDeclaration operation)
    {
        if (operation == null)
        {
            throw new IllegalArgumentException("Can't add a null operation");
        }

        operations.add(operation);
        return this;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public String getVersion()
    {
        return version;
    }

    void setVersion(String version)
    {
        this.version = version;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }
}