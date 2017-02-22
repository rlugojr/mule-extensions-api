/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.connectivity;

import static org.mule.runtime.api.connection.ConnectionValidationResult.failure;
import static org.mule.runtime.api.connection.ConnectionValidationResult.success;
import org.mule.runtime.api.connection.ConnectionException;
import org.mule.runtime.api.connection.ConnectionProvider;
import org.mule.runtime.api.connection.ConnectionValidationResult;
import org.mule.runtime.extension.api.annotation.connectivity.oauth.AuthorizationCode;
import org.mule.runtime.extension.api.annotation.connectivity.oauth.OAuthCallbackParameter;
import org.mule.runtime.extension.api.annotation.connectivity.oauth.OAuthConsumerKey;
import org.mule.runtime.extension.api.annotation.connectivity.oauth.OAuthConsumerSecret;
import org.mule.runtime.extension.api.annotation.param.Optional;
import org.mule.runtime.extension.api.annotation.param.Parameter;
import org.mule.runtime.extension.api.connectivity.oauth.AuthorizationCodeState;

@AuthorizationCode(
    authorizationUrl = "https://login.salesforce.com/services/oauth2/authorize",
    accessTokenUrl = "https://login.salesforce.com/services/oauth2/token")
public class SalesforceOAuthConnectionProvider<C> implements ConnectionProvider<C> {

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


  private AuthorizationCodeState state;


  @Override
  public C connect() throws ConnectionException {
    System.out.println(state.getAccessToken());
    return null;
  }

  public void disconnect(C connection) {

  }

  @Override
  public ConnectionValidationResult validate(C connection) {
    if (userId == null) {
      return failure("bleh", new IllegalArgumentException());
    }
    return success();
  }
}
