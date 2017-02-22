/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.connectivity;

import org.mule.runtime.api.connection.ConnectionException;
import org.mule.runtime.api.connection.ConnectionProvider;
import org.mule.runtime.api.connection.ConnectionValidationResult;
import org.mule.runtime.extension.api.annotation.connectivity.oauth.AuthorizationCode;
import org.mule.runtime.extension.api.annotation.connectivity.oauth.OAuthCallbackParameter;
import org.mule.runtime.extension.api.annotation.param.Optional;
import org.mule.runtime.extension.api.annotation.param.Parameter;

@AuthorizationCode(
    authorizationUrl = "https://login.salesforce.com/services/oauth2/authorize",
    accessTokenUrl = "https://login.salesforce.com/services/oauth2/token")
public class OAuth2ConnectionProvider<C> implements ConnectionProvider<C> {

  /**
   * Your application's client identifier (consumer key in Remote Access Detail).
   */
  @Parameter
  @OAuthConsumerKey
  private String consumerKey;

  /**
   * Your application's client secret (consumer secret in Remote Access Detail).
   */
  @Parameter
  @OAuthConsumerSecret
  private String consumerSecret;

  /**
   * Tailors the login page to the user's device type.
   */
  @Parameter
  private String display;

  /**
   * Avoid interacting with the user
   */
  @Parameter
  @Optional(defaultValue = "false")
  private boolean immediate;

  /**
   * Specifies how the authorization server prompts the user for reauthentication and reapproval
   */
  @Parameter
  @Optional(defaultValue = "true")
  private boolean prompt;

  @OAuthCallbackParameter(expression = "#[payload.instance_url]")
  private String instanceId;

  @OAuthCallbackParameter(expression = "#[payload.id]")
  private String userId;

  


  @Override
  public C connect() throws ConnectionException {
    return null;
  }

  @Override
  public void disconnect(C connection) {

  }

  @Override
  public ConnectionValidationResult validate(C connection) {
    return null;
  }
}
