package xyz.ronella.sample.oauth.clientcred.service;

import com.auth0.jwt.interfaces.Claim;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

public interface IAuthService {

    Optional<Map<String, Claim>> getValidatedClaims(final String accessToken);

    Optional<String> getUsername(final String accessToken);

    Optional<String> getOIDConf(final String issuer) throws IOException, InterruptedException;

    String getJwksURI(final String issuer);

    String getTokenEndpoint(final String issuer);

}
