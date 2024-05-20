package it.einjojo.akani.core;

import com.google.gson.Gson;
import it.einjojo.akani.core.api.AkaniCore;
import it.einjojo.akani.core.handler.ChatHandler;
import it.einjojo.akani.core.handler.CommandHandler;
import it.einjojo.akani.core.handler.ConnectionHandler;
import it.einjojo.akani.core.handler.PositionHandler;
import it.einjojo.akani.core.home.HomeFactory;
import it.einjojo.akani.core.player.AkaniPlayerFactory;
import it.einjojo.akani.core.util.LuckPermsHook;
import net.kyori.adventure.text.minimessage.MiniMessage;

public interface InternalAkaniCore extends AkaniCore {

    MiniMessage miniMessage();

    CommandHandler commandHandler();

    AkaniPlayerFactory playerFactory();

    ConnectionHandler connectionHandler();

    ChatHandler chatHandler();

    PositionHandler positionHandler();

    LuckPermsHook luckPermsHook();

    HomeFactory createHomeFactory();

    boolean shuttingDown();

    Gson gson();
}
