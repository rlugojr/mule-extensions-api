/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.introspection.declaration;

import org.mule.extension.introspection.Extension;
import org.mule.extension.introspection.declaration.fluent.DeclarationDescriptor;
import org.mule.extension.introspection.declaration.spi.DescriberPostProcessor;

import java.util.Map;

/**
 * Used for propagating state across all the components
 * that may take part on the describing of an {@link Extension}
 * and its {@link DescriberPostProcessor}s
 * <p/>
 * Once the {@link Describer} finishes applying its logic,
 * it will propagate this context through all the found
 * {@link DescriberPostProcessor}s, which means that any
 * side effects applied by any of the before mentioned will be visible by the next ones.
 *
 * @since 1.0
 */
public interface DescribingContext
{

    /**
     * The {@link DeclarationDescriptor} in which
     * the extension is being described into
     *
     * @return a non {@code null} {@link DeclarationDescriptor}
     */
    DeclarationDescriptor getDeclarationDescriptor();

    /**
     * A {@link Map} to hold custom parameters that implementations of
     * {@link Describer} and {@link DescriberPostProcessor} might
     * want to share with each other
     *
     * @return a non {@code null} map. Not be assumed thread-safe
     */
    Map<String, Object> getCustomParameters();

    /**
     * Obtains the custom parameter registered under {@code key}
     * and verifies that (if not null) is of type {@code expectedType}.
     * If the obtained value if not an instance of such type, an
     * {@link IllegalArgumentException} is thrown. If no value is
     * registered under that {@code key}, then it simply returns
     * {@code null}
     *
     * @param key          the key under which the wanted value is registered
     * @param expectedType the type of the expected value
     * @param <T>          generic type of the expected value
     * @return an instance of {@code expectedType} or {@code null}
     */
    <T> T getCheckedParameter(String key, Class<T> expectedType);
}