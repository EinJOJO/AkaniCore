package it.einjojo.akani.core;

import com.google.gson.Gson;
import it.einjojo.akani.core.api.AkaniCore;
import it.einjojo.akani.core.handler.chat.ChatHandler;
import it.einjojo.akani.core.handler.command.CommandHandler;
import it.einjojo.akani.core.handler.connection.ConnectionHandler;
import it.einjojo.akani.core.handler.permission.PermissionCheckHandler;
import it.einjojo.akani.core.handler.position.PositionHandler;
import it.einjojo.akani.core.home.HomeFactory;
import it.einjojo.akani.core.player.AkaniPlayerFactory;
import it.einjojo.akani.core.util.LuckPermsHook;
import net.kyori.adventure.text.minimessage.MiniMessage;

public interface InternalAkaniCore extends AkaniCore {

    MiniMessage miniMessage();

    CommandHandler commandHandler();

    AkaniPlayerFactory playerFactory();

    PermissionCheckHandler permissionCheckHandler();

    ConnectionHandler connectionHandler();

    ChatHandler chatHandler();

    PositionHandler positionHandler();

    LuckPermsHook luckPermsHook();

    HomeFactory createHomeFactory();

    boolean shuttingDown();

    Gson gson();
}
