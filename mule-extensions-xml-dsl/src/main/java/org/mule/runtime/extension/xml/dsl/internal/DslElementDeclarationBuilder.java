/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package org.mule.runtime.extension.xml.dsl.internal;

import org.mule.metadata.api.model.MetadataType;
import org.mule.runtime.extension.xml.dsl.api.DslElementDeclaration;

import java.util.HashMap;
import java.util.Map;

/**
 * @since 1.0
 */
public final class DslElementDeclarationBuilder
{

    private String attributeName;
    private String elementName;
    private String elementNameSpace;
    private boolean isWrapped = false;
    private boolean supportsChildDeclaration = false;
    //private DslElementDeclaration innerElement = null;
    //private List<DslElementDeclaration> genericChilds = new LinkedList<>();
    private Map<MetadataType, DslElementDeclaration> genericChilds = new HashMap<>();
    private Map<String, DslElementDeclaration> namedChilds = new HashMap<>();


    private DslElementDeclarationBuilder()
    {
        attributeName = "";
        elementName = "";
        elementNameSpace = "";
    }

    public static DslElementDeclarationBuilder create()
    {
        return new DslElementDeclarationBuilder();
    }

    /**
     * @return
     */
    public DslElementDeclarationBuilder withAttributeName(String attributeName)
    {
        this.attributeName = attributeName;
        return this;
    }

    /**
     * @return
     */
    public DslElementDeclarationBuilder withElementName(String elementName)
    {
        this.elementName = elementName;
        return this;
    }

    /**
     * @return
     */
    public DslElementDeclarationBuilder withNamespace(String namespace)
    {
        this.elementNameSpace = namespace;
        return this;
    }


    public DslElementDeclarationBuilder asWrappedElement(boolean isWrapped)
    {
        this.isWrapped = isWrapped;
        return this;
    }

    public DslElementDeclarationBuilder supportsChildDeclaration(boolean supportsChild)
    {
        this.supportsChildDeclaration = supportsChild;
        return this;
    }

    public DslElementDeclarationBuilder withGeneric(MetadataType type, DslElementDeclaration child)
    {
        if (child == null)
        {
            throw new IllegalArgumentException("Invalid child declaration, child element should not be null");
        }

        this.genericChilds.put(type, child);
        return this;
    }

    public DslElementDeclarationBuilder withChild(String name, DslElementDeclaration child)
    {
        if (child == null)
        {
            throw new IllegalArgumentException("Invalid child declaration, child element should not be null");
        }

        this.namedChilds.put(name, child);
        return this;
    }

    public DslElementDeclaration build()
    {
        return new DslElementDeclaration(attributeName, elementName, elementNameSpace, isWrapped, supportsChildDeclaration, genericChilds, namedChilds);
    }

}

