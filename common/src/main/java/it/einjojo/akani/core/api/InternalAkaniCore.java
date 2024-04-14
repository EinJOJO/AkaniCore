package it.einjojo.akani.core.api;

import it.einjojo.akani.core.factory.AkaniPlayerFactory;
import it.einjojo.akani.core.handler.AbstractCommandHandler;

public interface InternalAkaniCore extends AkaniCore {

    AbstractCommandHandler commandHandler();

    AkaniPlayerFactory playerFactory();
}
