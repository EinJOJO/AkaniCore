package it.einjojo.akani.core.handler;

import com.google.common.io.ByteStreams;
import it.einjojo.akani.core.api.messaging.BrokerService;
import it.einjojo.akani.core.api.messaging.ChannelMessage;
import it.einjojo.akani.core.api.messaging.ChannelReceiver;
import it.einjojo.akani.core.api.messaging.MessageProcessor;
import org.jetbrains.annotations.ApiStatus;

import java.util.UUID;
import java.util.logging.Logger;

public abstract class AbstractCommandHandler implements MessageProcessor {
    public static final String CHANNEL = "command";
    private final BrokerService brokerService;
    private final Logger logger;

    public AbstractCommandHandler(BrokerService brokerService, Logger logger) {
        this.brokerService = brokerService;
        this.logger = logger;
        brokerService.registerMessageProcessor(this);
    }

    /**
     * Run a command as the server.
     *
     * @param serverName Name of the server
     * @param command    Command to run
     */
    public void runCommandAsServer(String serverName, String command) {
        var message = ChannelMessage.builder()
                .channel(CHANNEL)
                .messageTypeID("cmds")
                .content(command)
                .recipient(ChannelReceiver.server(serverName))
                .build();
        brokerService().publish(message);
    }

    /**
     * Run a command as a player.
     *
     * @param playerUuid UUID of the player
     * @param serverName Name of the server
     * @param command    Command to run
     */
    public void runCommandAsPlayer(UUID playerUuid, String serverName, String command) {
        var payload = ByteStreams.newDataOutput();
        payload.writeUTF(playerUuid.toString());
        payload.writeUTF(command);

        var message = ChannelMessage.builder()
                .channel(CHANNEL)
                .messageTypeID("cmdpl")
                .content(payload.toByteArray())
                .recipient(ChannelReceiver.server(serverName))
                .build();
        brokerService().publish(message);
        logger.info("Published PlayerCommand:");
    }

    private BrokerService brokerService() {
        return brokerService;
    }

    @ApiStatus.Internal
    abstract void runServerCommandLocal(String command);

    @ApiStatus.Internal
    abstract void runPlayerCommandLocal(UUID player, String command);


    @Override
    public void processMessage(ChannelMessage message) {
        if (message.messageTypeID().equals("cmds")) {
            runServerCommandLocal(message.content());
        } else if (message.messageTypeID().equals("cmdpl")) {
            var payload = ByteStreams.newDataInput(message.content().getBytes());
            var player = UUID.fromString(payload.readUTF());
            var command = payload.readUTF();
            runPlayerCommandLocal(player, command);
        }
    }

    @Override
    public String processingChannel() {
        return CHANNEL;
    }
}
