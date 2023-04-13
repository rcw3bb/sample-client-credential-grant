package xyz.ronella.sample.oauth.clientcred.service;

import com.auth0.jwt.interfaces.Claim;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

/**
 * The contract on how to implement the IAuthService.
 *
 * @author Ron Webb
 */
public interface IAuthService {

    /**
     * Must have the implementation to collect the claims from the access token.
     * @param accessToken The access token.
     * @return The claims from the access token.
     */
    Optional<Map<String, Claim>> getValidatedClaims(final String accessToken);

    /**
     * Must hold a convenience implementation for retrieving the claimed username from the access token.
     * @param accessToken The access token.
     * @return The claimed username.
     */
    Optional<String> getUsername(final String accessToken);

    /**
     * Must hold the implementation of retrieving the openID connection configuration using the issuer.
     * @param issuer The issuer.
     * @return The configuration based on the issue.
     */
    Optional<String> getOIDConf(final String issuer);


    /**
     * Must hold the implementation on retrieving the JwksURI based on the response of getIODConf method.
     * @param issuer The issuer.
     * @return A JWKS URI
     */
    String getJwksURI(final String issuer);

    /**
     * Must hold the implementation on retrieving the token endpoint based on the response of getIODConf method.
     * @param issuer The issuer.
     * @return The token endpoint.
     */
    String getTokenEndpoint(final String issuer);

}
