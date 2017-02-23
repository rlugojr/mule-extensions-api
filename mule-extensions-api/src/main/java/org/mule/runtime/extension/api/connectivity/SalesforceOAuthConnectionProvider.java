/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.connectivity;

import static org.mule.runtime.api.connection.ConnectionValidationResult.success;
import org.mule.runtime.api.connection.ConnectionException;
import org.mule.runtime.api.connection.ConnectionProvider;
import org.mule.runtime.api.connection.ConnectionValidationResult;
import org.mule.runtime.extension.api.annotation.connectivity.oauth.AuthorizationCode;
import org.mule.runtime.extension.api.annotation.connectivity.oauth.OAuthCallbackValue;
import org.mule.runtime.extension.api.annotation.connectivity.oauth.OAuthConsumerKey;
import org.mule.runtime.extension.api.annotation.connectivity.oauth.OAuthConsumerSecret;
import org.mule.runtime.extension.api.annotation.connectivity.oauth.OAuthParameter;
import org.mule.runtime.extension.api.annotation.param.Optional;
import org.mule.runtime.extension.api.annotation.param.Parameter;
import org.mule.runtime.extension.api.connectivity.oauth.AuthorizationCodeState;

import java.text.MessageFormat;

@AuthorizationCode(
    authorizationUrl = "https://login.salesforce.com/services/oauth2/authorize",
    accessTokenUrl = "https://login.salesforce.com/services/oauth2/token")
public class SalesforceOAuthConnectionProvider<C> implements ConnectionProvider<C> {

  /**
   * Your application's client identifier (consumer key in Remote Access Detail).
   */
  @OAuthConsumerKey
  private String consumerKey;

  /**
   * Your application's client secret (consumer secret in Remote Access Detail).
   */
  @OAuthConsumerSecret
  private String consumerSecret;

  @Parameter
  @Optional(defaultValue = "34.0")
  private Double apiVersion;

  /**
   * Tailors the login page to the user's device type.
   */
  @OAuthParameter
  private String display;

  /**
   * Avoid interacting with the user
   */
  @OAuthParameter
  @Optional(defaultValue = "false")
  private boolean immediate;

  /**
   * Specifies how the authorization server prompts the user for reauthentication and reapproval
   */
  @OAuthParameter
  @Optional(defaultValue = "true")
  private boolean prompt;

  @OAuthCallbackValue(expression = "#[payload.instance_url]")
  private String instanceId;

  @OAuthCallbackValue(expression = "#[payload.id]")
  private String userId;


  private AuthorizationCodeState state;


  @Override
  public C connect() throws ConnectionException {
    if (state.getAccessToken() == null) {
      throw new SalesforceException(MessageFormat.format(COULD_NOT_EXTRACT_FIELD, "accessToken"));
    }

    if (instanceId == null) {
      throw new SalesforceException(MessageFormat.format(COULD_NOT_EXTRACT_FIELD, "instanceId"));
    }

    return new SalesforceClient(state.getAccessToken());
  }

  public void disconnect(C connection) {

  }

  @Override
  public ConnectionValidationResult validate(C connection) {
    return success();
  }
}
