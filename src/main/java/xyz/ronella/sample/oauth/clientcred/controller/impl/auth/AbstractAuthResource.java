package xyz.ronella.sample.oauth.clientcred.controller.impl.auth;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import xyz.ronella.sample.oauth.clientcred.config.AuthModule;
import xyz.ronella.sample.oauth.clientcred.config.PersonModule;
import xyz.ronella.sample.oauth.clientcred.controller.impl.AbstractResource;
import xyz.ronella.sample.oauth.clientcred.model.AccessToken;
import xyz.ronella.sample.oauth.clientcred.service.IAuthService;
import xyz.ronella.sample.oauth.clientcred.service.IPersonService;
import xyz.ronella.sample.oauth.clientcred.wrapper.SimpleJson;

/**
 * A partial implementation of IResource.
 *
 * @author Ron Webb
 * @since 1.0.0
 */
public abstract class AbstractAuthResource extends AbstractResource {

    /**
     * Converts a json text to AccessToken object.
     * @param json The json text.
     * @return An instance of AccessToken object.
     */
    protected AccessToken jsonToAccessToken(String json) {
        final var simpleJson = new SimpleJson<AccessToken>();
        return simpleJson.toObjectType(json, AccessToken.class);
    }

    /**
     * Converts the AccessToken object to json text.
     * @param accessToken An instance of AccessToken.
     * @return The json text.
     */
    protected String accessTokenToJson(AccessToken accessToken) {
        final var mapper = new SimpleJson<AccessToken>();
        return mapper.toJsonText(accessToken);
    }


}
