package xyz.ronella.sample.oauth.clientcred.service.impl;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import org.slf4j.LoggerFactory;
import xyz.ronella.logging.LoggerPlus;
import xyz.ronella.sample.oauth.clientcred.config.AppConfig;
import xyz.ronella.sample.oauth.clientcred.service.IAuthService;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.math.BigInteger;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;
import java.util.Arrays;
import java.util.Base64;
import java.util.Map;
import java.util.Optional;

/**
 * An implementation of IAuthService.
 *
 * @author Ron Webb
 */
public class AuthServiceImpl implements IAuthService {

    private static final LoggerPlus LOGGER_PLUS = new LoggerPlus(LoggerFactory.getLogger(AuthServiceImpl.class));

    private static String JSON_OID_CONF;

    @Override
    public Optional<String> getOIDCConf(final String issuer) {
        try (final var mLOG = LOGGER_PLUS.groupLog("JsonObject findKeyFromJWKS(final JsonArray keys, final String keyId)")) {
            try {
                if (JSON_OID_CONF == null) {
                    final var confURL = issuer + "/.well-known/openid-configuration";

                    final var requestConf = HttpRequest.newBuilder()
                            .uri(URI.create(confURL))
                            .GET()
                            .build();

                    final var responseConf = HttpClient.newBuilder().build().send(requestConf, HttpResponse.BodyHandlers.ofString());
                    JSON_OID_CONF = responseConf.body();
                }
                return Optional.ofNullable(JSON_OID_CONF);
            } catch (IOException | InterruptedException exception) {
                mLOG.error(LOGGER_PLUS.getStackTraceAsString(exception));
                throw new RuntimeException(exception);
            }
        }
    }

    @Override
    public String getJwksURI(String issuer) {
        final var oidConf = getOIDCConf(issuer);
        return oidConf.map(s -> new Gson().fromJson(s, JsonObject.class).get("jwks_uri").getAsString()).orElse(null);
    }

    @Override
    public String getTokenEndpoint(String issuer) {
        final var oidConf = getOIDCConf(issuer);
        return oidConf.map(s -> new Gson().fromJson(s, JsonObject.class).get("token_endpoint").getAsString()).orElse(null);
    }

    private JsonObject findKeyFromJWKS(final JsonArray keys, final String keyId) {
        try (final var mLOG = LOGGER_PLUS.groupLog("JsonObject findKeyFromJWKS(final JsonArray keys, final String keyId)")) {
            final var optKey = keys.asList().stream()
                    .map(JsonElement::getAsJsonObject)
                    .filter(___key -> ___key.get("kid").getAsString().equals(keyId))
                    .findFirst();

            if (optKey.isPresent()) {
                return optKey.get();
            }

            {
                final var message = "RSA public key not found";
                mLOG.error(message);
                throw new RuntimeException(message);
            }
        }
    }

    private RSAPublicKey getPublicKey(final String accessToken, final String jwksUri) throws IOException, InterruptedException, NoSuchAlgorithmException, InvalidKeySpecException {
        final var gson = new Gson();
        final var rawHeader = accessToken.split("\\.")[0];
        final var header = new String(Base64.getUrlDecoder().decode(rawHeader), StandardCharsets.UTF_8);
        final var jsonHeader = gson.fromJson(header, JsonObject.class);
        final var rawKeyId = jsonHeader.get("kid").getAsString();
        final var request = HttpRequest.newBuilder().uri(URI.create(jwksUri)).GET().build();
        final var response = HttpClient.newBuilder().build().send(request, HttpResponse.BodyHandlers.ofString());
        final var jsonResponse = gson.fromJson(response.body(), JsonObject.class);
        final var rawKeys = jsonResponse.getAsJsonArray("keys");
        final var key = findKeyFromJWKS(rawKeys, rawKeyId);
        final var rawModulus = key.get("n").getAsString();
        final var rawExponent = key.get("e").getAsString();
        final var modulus = new BigInteger(1, Base64.getUrlDecoder().decode(rawModulus));
        final var exponent = new BigInteger(1, Base64.getUrlDecoder().decode(rawExponent));
        final var spec = new RSAPublicKeySpec(modulus, exponent);
        final var factory = KeyFactory.getInstance("RSA");

        return (RSAPublicKey) factory.generatePublic(spec);
    }

    private Map<String, Claim> getClaims(final String accessToken, final RSAPublicKey publicKey, final String issuer) {
        final var audience = AppConfig.INSTANCE.getAuthAudience();
        final var algorithm = Algorithm.RSA256(publicKey, null);
        final var jwtDecoded = JWT.require(algorithm).build().verify(accessToken);
        final var claims = jwtDecoded.getClaims();

        final var jwtIssuer = claims.get("iss").asString();
        final var jwtAudience = Arrays.stream(claims.get("aud").asArray(String.class))
                .filter(audience::equals)
                .findFirst().orElse("");

        if (issuer.equals(jwtIssuer) && audience.equals(jwtAudience)) {
            return claims;
        }

        return Map.of();
    }

    @Override
    public Optional<Map<String, Claim>> getValidatedClaims(final String accessToken) {
        try (final var mLOG = LOGGER_PLUS.groupLog("Optional<Map<String, Claim>> getValidatedClaims(final String accessToken)")) {
            final var issuer = AppConfig.INSTANCE.getAuthIssuer();

            try {
                final var rawJwksUri = getJwksURI(issuer);
                final var publicKey = getPublicKey(accessToken, rawJwksUri);

                return Optional.ofNullable(getClaims(accessToken, publicKey, issuer));
            } catch (IOException | InterruptedException | NoSuchAlgorithmException | InvalidKeySpecException |
                     JWTVerificationException exception) {
                mLOG.error(LOGGER_PLUS.getStackTraceAsString(exception));
                throw new RuntimeException(exception);
            }
        }
    }

    @Override
    public Optional<String> getUsername(final String accessToken) {
        final var claims = getValidatedClaims(accessToken);
        return claims.map(stringClaimMap -> stringClaimMap.get("preferred_username").asString());
    }

}
