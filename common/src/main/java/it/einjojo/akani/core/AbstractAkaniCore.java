package it.einjojo.akani.core;

import it.einjojo.akani.core.api.InternalAkaniCore;
import it.einjojo.akani.core.api.database.DatabaseProvider;
import it.einjojo.akani.core.api.economy.EconomyManager;
import it.einjojo.akani.core.api.message.MessageManager;
import it.einjojo.akani.core.api.messaging.BrokerService;
import it.einjojo.akani.core.handler.AbstractCommandHandler;

import java.util.logging.Logger;

public abstract class AbstractAkaniCore implements InternalAkaniCore {

    private final Logger logger;

    protected AbstractAkaniCore(Logger pluginLogger) {
        this.logger = pluginLogger;
    }

    public void load() {

    }

    public void unload() {

    }


    @Override
    public Logger logger() {
        return logger;
    }

    @Override
    public DatabaseProvider databaseProvider() {
        return null;
    }

    @Override
    public BrokerService brokerService() {
        return null;
    }

    @Override
    public EconomyManager coinsManager() {
        return null;
    }

    @Override
    public EconomyManager thalerManager() {
        return null;
    }

    @Override
    public MessageManager messageManager() {
        return null;
    }


}
