package xyz.ronella.sample.oauth.clientcred.config;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.multibindings.Multibinder;
import com.google.inject.name.Names;
import xyz.ronella.sample.oauth.clientcred.controller.IResource;
import xyz.ronella.sample.oauth.clientcred.controller.IResources;
import xyz.ronella.sample.oauth.clientcred.controller.impl.auth.AuthResources;
import xyz.ronella.sample.oauth.clientcred.controller.impl.auth.Authenticate;
import xyz.ronella.sample.oauth.clientcred.controller.impl.person.PersonResources;
import xyz.ronella.sample.oauth.clientcred.service.IAuthService;
import xyz.ronella.sample.oauth.clientcred.service.IPersonService;
import xyz.ronella.sample.oauth.clientcred.service.impl.AuthServiceImpl;
import xyz.ronella.sample.oauth.clientcred.service.impl.PersonServiceImpl;

/**
 * The configuration to wiring all Authentication related resources.
 *
 * @author Ron Webb
 * @since 1.0.0
 */
final public class AuthModule extends AbstractModule {

    protected void configure() {
        bind(IResources.class)
                .annotatedWith(Names.named(AuthResources.RESOURCE_NAME))
                .to(AuthResources.class);

        final var resourceBinder = Multibinder.newSetBinder(binder(), IResource.class, Names.named(AuthResources.RESOURCE_NAME));
        resourceBinder.addBinding().to(Authenticate.class);
    }

    private static Injector getInjector() {
        return Guice.createInjector(new AuthModule());
    }

    /**
     * Returns an instance of the target interface that is fully wired.
     * @param clazz The target class.
     * @return An instance of the wired class.
     * @param <T> The target actual time.
     */
    public static <T> T getInstance(final Class<T> clazz) {
        return getInjector().getInstance(Key.get(clazz, Names.named(AuthResources.RESOURCE_NAME)));
    }

}
