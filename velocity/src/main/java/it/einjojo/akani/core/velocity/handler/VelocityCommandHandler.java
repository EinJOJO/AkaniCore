package it.einjojo.akani.core.velocity.handler;

import it.einjojo.akani.core.api.messaging.BrokerService;
import it.einjojo.akani.core.handler.AbstractCommandHandler;

import java.util.UUID;
import java.util.logging.Logger;

public class VelocityCommandHandler extends AbstractCommandHandler {


    public VelocityCommandHandler(BrokerService brokerService, Logger logger) {
        super(brokerService, logger);
    }

    @Override
    protected void runServerCommandLocally(String command) {

    }

    @Override
    protected void runPlayerCommandLocally(UUID player, String command) {

    }
}
