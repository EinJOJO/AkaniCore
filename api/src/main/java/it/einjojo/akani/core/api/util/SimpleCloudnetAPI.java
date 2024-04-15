package it.einjojo.akani.core.api.util;

import dev.derklaro.aerogel.Injector;
import eu.cloudnetservice.driver.inject.InjectionLayer;
import eu.cloudnetservice.driver.provider.CloudServiceProvider;
import eu.cloudnetservice.driver.registry.ServiceRegistry;
import eu.cloudnetservice.modules.bridge.player.PlayerManager;
import eu.cloudnetservice.wrapper.holder.WrapperServiceInfoHolder;

public class SimpleCloudnetAPI {
    private final InjectionLayer<Injector> injector;
    private PlayerManager playerManager;

    public static boolean isAvailable() {
        try {
            Class.forName("eu.cloudnetservice.driver.inject.InjectionLayer");
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    public SimpleCloudnetAPI() {
        if (!isAvailable()) {
            throw new IllegalStateException("CloudNet-API Injector is unavailable.");
        }
        this.injector = InjectionLayer.ext();
        playerManager = injector.instance(ServiceRegistry.class).firstProvider(PlayerManager.class);
    }


    public WrapperServiceInfoHolder wrapperServiceInfoHolder() {
        return injector.instance(WrapperServiceInfoHolder.class);
    }

    public CloudServiceProvider cloudServiceProvider() {
        return injector.instance(CloudServiceProvider.class);
    }

    public PlayerManager getCloudNetPlayerManager() {
        return playerManager;
    }


    public String getServiceName() {
        return wrapperServiceInfoHolder().serviceInfo().name();
    }

    public String getServiceTaskName() {
        return wrapperServiceInfoHolder().serviceInfo().serviceId().taskName();
    }

    public String getServiceUniqueId() {
        return wrapperServiceInfoHolder().serviceInfo().serviceId().uniqueId().toString();
    }


}
