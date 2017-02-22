/**
 * (c) 2003-2015 MuleSoft, Inc. This software is protected under international copyright
 * law. All use of this software is subject to MuleSoft's Master Subscription Agreement
 * (or other master license agreement) separately entered into in writing between you and
 * MuleSoft. If such an agreement is not in place, you may not use the software.
 */

package org.mule.runtime.extension.api.annotation.connectivity.oauth;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * A class annotated with {@link OAuth} or {@link OAuth2} needs to have exactly one field annotated with @OAuthConsumerKey
 * and this field must be of type String. It is a responsability of the developer to make sure this field contains the OAuth
 * Consumer Key as provided by the Service Provider and described in the OAuth specification.
 */
@Target(FIELD)
@Retention(RUNTIME)
@Documented
public @interface OAuthConsumerKey {

}