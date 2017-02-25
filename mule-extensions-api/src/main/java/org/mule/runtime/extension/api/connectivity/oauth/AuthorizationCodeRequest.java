package org.mule.runtime.extension.api.connectivity.oauth;

import java.util.Optional;

public interface AuthorizationCodeRequest {

  String getResourceOwnerId();

  String getAuthorizationUrl();

  String getTokenUrl();

  String getConsumerKey();

  String getConsumerSecret();

  String getScope();

  Optional<String> getState();
}
