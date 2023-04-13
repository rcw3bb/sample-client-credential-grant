package xyz.ronella.sample.oauth.clientcred.service.impl;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.google.gson.JsonArray;
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
import java.util.Base64;
import java.util.Map;
import java.util.Optional;

public class AuthServiceImpl implements IAuthService {

    private String getJWKSURI(final String issuer) throws IOException, InterruptedException {
        final var gson = new Gson();
        final var confURL = issuer + "/.well-known/openid-configuration";

        final var requestConf = HttpRequest.newBuilder()
                .uri(URI.create(confURL))
                .GET()
                .build();

        final var responseConf = HttpClient.newBuilder().build().send(requestConf, HttpResponse.BodyHandlers.ofString());
        final var jsonConf = gson.fromJson(responseConf.body(), JsonObject.class);
        return jsonConf.get("jwks_uri").getAsString();
    }

    private JsonObject findKeyFromJWKS(final JsonArray keys, final String keyId) {
        JsonObject key = null;
        for (int i = 0; i < keys.size(); i++) {
            final var k = keys.get(i);
            final var kObj = k.getAsJsonObject();
            if (kObj.get("kid").getAsString().equals(keyId)) {
                key = kObj;
                break;
            }
        }
        if (key == null) {
            throw new RuntimeException("RSA public key not found");
        }
        return key;
    }

    private RSAPublicKey getPublicKey(final String accessToken, final String jwksUri) throws IOException, InterruptedException, NoSuchAlgorithmException, InvalidKeySpecException {
        final var gson = new Gson();
        final var rawHeader = accessToken.split("\\.")[0];
        final var header = new String(Base64.getUrlDecoder().decode(rawHeader), StandardCharsets.UTF_8);
        final var jsonHeader = gson.fromJson(header, JsonObject.class);
        final var keyId = jsonHeader.get("kid").getAsString();
        final var request = HttpRequest.newBuilder().uri(URI.create(jwksUri)).GET().build();
        final var response = HttpClient.newBuilder().build().send(request, HttpResponse.BodyHandlers.ofString());
        final var jsonResponse = gson.fromJson(response.body(), JsonObject.class);
        final var keys = jsonResponse.getAsJsonArray("keys");
        final var key = findKeyFromJWKS(keys, keyId);
        final var modulus = key.get("n").getAsString();
        final var exponent = key.get("e").getAsString();
        final var n = new BigInteger(1, Base64.getUrlDecoder().decode(modulus));
        final var e = new BigInteger(1, Base64.getUrlDecoder().decode(exponent));
        final var spec = new RSAPublicKeySpec(n, e);
        final var factory = KeyFactory.getInstance("RSA");

        return (RSAPublicKey) factory.generatePublic(spec);
    }

    private Map<String, Claim> getClaims(final String accessToken, final RSAPublicKey publicKey, final String issuer) {
        final var audience = AppConfig.INSTANCE.getAuthAudience();
        final var algorithm = Algorithm.RSA256(publicKey, null);
        final var jwtDecoded = JWT.require(algorithm).build().verify(accessToken);
        final var claims = jwtDecoded.getClaims();

        try {
            final var jwtIssuer = claims.get("iss").asString();
            final var jwtAudience = claims.get("aud").asString();

            if (issuer.equals(jwtIssuer) && audience.equals(jwtAudience)) {
                return claims;
            }
        }
        catch (NullPointerException npe) {
            throw new RuntimeException(npe);
        }

        return null;
    }

    @Override
    public Optional<Map<String, Claim>> getValidatedClaims(final String accessToken) {
        final var issuer = AppConfig.INSTANCE.getAuthIssuer();

        try {
            final var jwksUri = getJWKSURI(issuer);
            final var publicKey = getPublicKey(accessToken, jwksUri);

            return Optional.ofNullable(getClaims(accessToken, publicKey, issuer));
        } catch (IOException | InterruptedException | NoSuchAlgorithmException | InvalidKeySpecException |
                 JWTVerificationException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<String> getUsername(final String accessToken) {
        final var claims = getValidatedClaims(accessToken);
        return claims.map(stringClaimMap -> stringClaimMap.get("preferred_username").asString());
    }

}
