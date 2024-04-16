package it.einjojo.akani.core;

import com.google.gson.Gson;
import it.einjojo.akani.core.api.AkaniCore;
import it.einjojo.akani.core.handler.AbstractChatHandler;
import it.einjojo.akani.core.handler.AbstractCommandHandler;
import it.einjojo.akani.core.handler.AbstractPositionHandler;
import it.einjojo.akani.core.handler.ConnectionHandler;
import it.einjojo.akani.core.player.AkaniPlayerFactory;

public interface InternalAkaniCore extends AkaniCore {

    AbstractCommandHandler commandHandler();

    AkaniPlayerFactory playerFactory();

    ConnectionHandler connectionHandler();

    AbstractChatHandler chatHandler();

    AbstractPositionHandler positionHandler();


    Gson gson();
}
