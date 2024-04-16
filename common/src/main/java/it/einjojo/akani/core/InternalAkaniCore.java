package it.einjojo.akani.core;

import com.google.gson.Gson;
import it.einjojo.akani.core.api.AkaniCore;
import it.einjojo.akani.core.handler.ConnectionHandler;
import it.einjojo.akani.core.player.AkaniPlayerFactory;
import it.einjojo.akani.core.handler.AbstractCommandHandler;

public interface InternalAkaniCore extends AkaniCore {

    AbstractCommandHandler commandHandler();

    AkaniPlayerFactory playerFactory();

    ConnectionHandler playerConnectionHandler();

    Gson gson();
}
