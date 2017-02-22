package org.mule.runtime.extension.api.annotation.connectivity.oauth;

public @interface AuthorizationCode {

    String accessTokenUrl();

    String authorizationUrl();

    String verifierRegex() default "code=([^&]+)";

    String accessTokenExpr() default "#[(payload match /.*\"access_token\"[ ]*:[ ]*\"([^\\\"]*)\".*/)[1]]";

    String expirationRegex() default "#[(payload match /.*\"expires_in\"[ ]*:[ ]*\"([^\\\"]*)\".*/)[1]]";

    String refreshTokenExpr() default "#[(payload match /.*\"refresh_token\"[ ]*:[ ]*\"([^\\\"]*)\".*/)[1]]";

}
