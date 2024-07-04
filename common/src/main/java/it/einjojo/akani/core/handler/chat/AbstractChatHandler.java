package it.einjojo.akani.core.handler.chat;

import com.google.common.io.ByteStreams;
import it.einjojo.akani.core.api.messaging.BrokerService;
import it.einjojo.akani.core.api.messaging.ChannelMessage;
import it.einjojo.akani.core.api.messaging.ChannelReceiver;
import it.einjojo.akani.core.api.messaging.MessageProcessor;
import net.kyori.adventure.text.Component;

import java.util.UUID;

public abstract class AbstractChatHandler implements MessageProcessor, ChatHandler {
    public static final String PLAYER_CHAT_KEY = "pchat";
    public static final String SERVER_CHAT_KEY = "schat";
    public static final String GLOBAL_CHAT_KEY = "gchat";
    protected final BrokerService brokerService;


    protected AbstractChatHandler(BrokerService brokerService) {
        this.brokerService = brokerService;
        brokerService.registerMessageProcessor(this);
    }

    @Override
    public void processMessage(ChannelMessage message) {
        if (message.messageTypeID().equals(PLAYER_CHAT_KEY)) {
            var payload = ByteStreams.newDataInput(message.contentBytes());
            var receiver = UUID.fromString(payload.readUTF());
            var messageContent = payload.readUTF();
            sendMessageLocally(receiver, deserializeComponent(messageContent));
        } else if (message.messageTypeID().equals(SERVER_CHAT_KEY) || message.messageTypeID().equals(GLOBAL_CHAT_KEY)) {
            sendMessageToServerLocally(deserializeComponent(message.content()));
        }
    }

    public abstract String serializeComponent(Component component);

    public abstract Component deserializeComponent(String serializedComponent);

    protected abstract void sendMessageLocally(UUID receiver, Component message);

    protected abstract void sendMessageToServerLocally(Component message);

    @Override
    public final void sendMessageToPlayer(UUID specificPlayer, String playerServer, Component message) {
        if (playerServer.equals(brokerService.brokerName())) {
            sendMessageLocally(specificPlayer, message);
            return;
        }
        var payload = ByteStreams.newDataOutput();
        payload.writeUTF(specificPlayer.toString());
        payload.writeUTF(serializeComponent(message));
        var messageToSend = ChannelMessage.builder()
                .channel(processingChannel())
                .messageTypeID(PLAYER_CHAT_KEY)
                .content(payload.toByteArray())
                .recipient(ChannelReceiver.server(playerServer))
                .build();
        brokerService().publish(messageToSend);
    }

    @Override
    public final void sendMessageToServer(String serverName, Component message) {
        if (serverName.equals(brokerService.brokerName())) {
            sendMessageToServerLocally(message);
            return;
        }
        var messageToSend = ChannelMessage.builder()
                .channel(processingChannel())
                .messageTypeID(SERVER_CHAT_KEY)
                .content(serializeComponent(message))
                .recipient(ChannelReceiver.server(serverName))
                .build();
        brokerService().publish(messageToSend);
    }

    @Override
    public final void sendMessageGlobal(Component message) {
        var messageToSend = ChannelMessage.builder()
                .channel(processingChannel())
                .messageTypeID(GLOBAL_CHAT_KEY)
                .content(serializeComponent(message))
                .recipient(ChannelReceiver.all())
                .build();
        brokerService().publish(messageToSend);
    }


    public BrokerService brokerService() {
        return brokerService;
    }
}
