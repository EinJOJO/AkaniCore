package it.einjojo.akani.core;

import com.google.gson.Gson;
import it.einjojo.akani.core.api.AkaniCore;
import it.einjojo.akani.core.handler.*;
import it.einjojo.akani.core.player.AkaniPlayerFactory;

public interface InternalAkaniCore extends AkaniCore {

    CommandHandler commandHandler();

    AkaniPlayerFactory playerFactory();

    ConnectionHandler connectionHandler();

    ChatHandler chatHandler();

    PositionHandler positionHandler();

    boolean shuttingDown();

    Gson gson();
}
