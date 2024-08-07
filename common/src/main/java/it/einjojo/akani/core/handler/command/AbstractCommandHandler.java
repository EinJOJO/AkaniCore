package it.einjojo.akani.core.handler.command;

import com.google.common.io.ByteStreams;
import it.einjojo.akani.core.api.messaging.BrokerService;
import it.einjojo.akani.core.api.messaging.ChannelMessage;
import it.einjojo.akani.core.api.messaging.ChannelReceiver;
import it.einjojo.akani.core.api.messaging.MessageProcessor;
import org.jetbrains.annotations.ApiStatus;

import java.util.UUID;
import java.util.logging.Logger;

public abstract class AbstractCommandHandler implements MessageProcessor, CommandHandler {
    public static final String CHANNEL = "command";
    protected final Logger logger;
    private final BrokerService brokerService;

    public AbstractCommandHandler(BrokerService brokerService, Logger logger) {
        this.brokerService = brokerService;
        this.logger = logger;
        brokerService.registerMessageProcessor(this);
    }

    @Override
    public final void runCommandAsServer(String serverName, String command) {
        if (serverName.equals(brokerService.brokerName())) {
            runServerCommandLocally(command);
            return;
        }
        var message = ChannelMessage.builder().channel(CHANNEL).messageTypeID("cmds").content(command).recipient(ChannelReceiver.server(serverName)).build();
        brokerService().publish(message);
    }

    @Override
    public final void runCommandAsPlayer(UUID playerUuid, String serverName, String command) {
        if (serverName.equals(brokerService.brokerName())) {
            runPlayerCommandLocally(playerUuid, command);
            return;
        }
        var payload = ByteStreams.newDataOutput();
        payload.writeUTF(playerUuid.toString());
        payload.writeUTF(command);

        var message = ChannelMessage.builder().channel(CHANNEL).messageTypeID("cmdpl").content(payload.toByteArray()).recipient(ChannelReceiver.server(serverName)).build();
        brokerService().publish(message);
        logger.info("Published PlayerCommand:");
    }

    private BrokerService brokerService() {
        return brokerService;
    }

    @ApiStatus.Internal
    protected abstract void runServerCommandLocally(String command);

    @ApiStatus.Internal
    protected abstract void runPlayerCommandLocally(UUID player, String command);


    @Override
    public void processMessage(ChannelMessage message) {
        if (message.messageTypeID().equals("cmds")) {
            runServerCommandLocally(message.content());
        } else if (message.messageTypeID().equals("cmdpl")) {
            var payload = ByteStreams.newDataInput(message.contentBytes());
            var player = UUID.fromString(payload.readUTF());
            var command = payload.readUTF();
            runPlayerCommandLocally(player, command);
        }
    }

    @Override
    public String processingChannel() {
        return CHANNEL;
    }
}
