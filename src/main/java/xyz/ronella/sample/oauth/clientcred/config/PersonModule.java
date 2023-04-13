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
import xyz.ronella.sample.oauth.clientcred.controller.impl.person.*;
import xyz.ronella.sample.oauth.clientcred.repository.IPersonRepository;
import xyz.ronella.sample.oauth.clientcred.service.IAuthService;
import xyz.ronella.sample.oauth.clientcred.service.impl.AuthServiceImpl;
import xyz.ronella.sample.oauth.clientcred.service.impl.PersonServiceImpl;
import xyz.ronella.sample.oauth.clientcred.repository.impl.PersonListRepository;
import xyz.ronella.sample.oauth.clientcred.service.IPersonService;

/**
 * The configuration to wiring all Person related resources.
 *
 * @author Ron Webb
 * @since 1.0.0
 */
final public class PersonModule extends AbstractModule {

    @Override
    protected void configure() {

        bind(IPersonService.class)
                .annotatedWith(Names.named(PersonResources.RESOURCE_NAME))
                .to(PersonServiceImpl.class);

        bind(IAuthService.class)
                .annotatedWith(Names.named(AuthResources.RESOURCE_NAME))
                .to(AuthServiceImpl.class);

        bind(IPersonRepository.class)
                .annotatedWith(Names.named(PersonResources.RESOURCE_NAME))
                .to(PersonListRepository.class);

        bind(IResources.class)
                .annotatedWith(Names.named(PersonResources.RESOURCE_NAME))
                .to(PersonResources.class);

        final var resourceBinder = Multibinder.newSetBinder(binder(), IResource.class, Names.named(PersonResources.RESOURCE_NAME));
        resourceBinder.addBinding().to(PersonCreate.class);
        resourceBinder.addBinding().to(PersonDeleteById.class);
        resourceBinder.addBinding().to(PersonRetrieveAll.class);
        resourceBinder.addBinding().to(PersonRetrieveById.class);
        resourceBinder.addBinding().to(PersonUpdateById.class);
    }

    private static Injector getInjector() {
        return Guice.createInjector(new PersonModule());
    }

    /**
     * Returns an instance of the target interface that is fully wired.
     * @param clazz The target class.
     * @return An instance of the wired class.
     * @param <T> The target actual time.
     */
    public static <T> T getInstance(final Class<T> clazz) {
        return getInjector().getInstance(Key.get(clazz, Names.named(PersonResources.RESOURCE_NAME)));
    }

}
