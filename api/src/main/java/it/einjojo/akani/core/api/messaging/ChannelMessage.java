package it.einjojo.akani.core.api.messaging;

import org.jetbrains.annotations.ApiStatus;

import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.List;

/**
 * Eine Nachricht, die über den BrokerService versendet wird. Sie enthält den Inhalt, den Sender, die Empfänger und den Kanal.
 *
 * @param c Der Kanal, über den die Nachricht gesendet wird.
 * @param m Die ID des Nachrichtentyps, welche zum Identifizieren der Nachricht genutzt wird.
 * @param b Der Inhalt der Nachricht. Das kann ein JSON-String sein, oder ein einfacher Text, oder eine UUID.
 * @param s Der Sender der Nachricht.
 * @param r Die Empfänger der Nachricht.
 * @param i Wird beim Senden einer Anfrage gesetzt und bei der Antwort übernommen.
 */
public record ChannelMessage(
        String c, //channel
        String m, // messageTypeID
        byte[] b, // contentBytes
        ChannelSender s, // sender
        Collection<ChannelReceiver> r, // recipients
        String i // requestID
) {

    public static Builder builder() {
        return new Builder();
    }

    @ApiStatus.Internal
    public static Builder responseTo(ChannelMessage message) {
        return new Builder(message)
                .content("")
                .recipient(ChannelReceiver.server(message.sender().name()));
    }

    public String channel() {
        return c;
    }

    public String messageTypeID() {
        return m;
    }

    public byte[] contentBytes() {
        return b;
    }

    public ChannelSender sender() {
        return s;
    }

    public Collection<ChannelReceiver> recipients() {
        return r;
    }

    public String requestID() {
        return i;
    }

    public String content() {
        return new String(contentBytes(), StandardCharsets.UTF_8);
    }

    public boolean isRequest() {
        return requestID() != null && !requestID().isEmpty();
    }

    public static class Builder {
        private String messageTypeID;
        private ChannelSender sender;
        private String channel = BrokerService.DEFAULT_CHANNEL;
        private String requestID;
        private byte[] content = new byte[]{};
        private Collection<ChannelReceiver> recipients = List.of(ChannelReceiver.all());

        public Builder() {
            this.requestID = null;
        }

        public Builder(ChannelMessage other) {
            this.messageTypeID = other.messageTypeID();
            this.sender = other.sender();
            this.requestID = other.requestID();
            this.channel = other.channel();
            this.content = other.contentBytes();
            recipients = other.recipients();

        }


        /**
         * In welchen Channel soll die Nachricht gesendet werden
         *
         * @return this
         */
        public Builder channel(String channel) {
            this.channel = channel;
            return this;
        }

        public Builder sender(ChannelSender sender) {
            this.sender = sender;
            return this;
        }

        /**
         * @param messageTypeID Die ID des Nachrichtentyps, welche zum Identifizieren der Nachricht genutzt wird.
         *                      Das ist wichtig um die Verarbeitung des contents zu steuern.
         */
        public Builder messageTypeID(String messageTypeID) {
            this.messageTypeID = messageTypeID;
            return this;
        }

        /**
         * @param content Der Inhalt der Nachricht. Das kann ein JSON-String sein, oder ein einfacher Text, oder eine UUID.
         */
        public Builder content(String content) {
            this.content = content.getBytes(StandardCharsets.UTF_8);
            return this;
        }

        public Builder content(byte[] content) {
            this.content = content;
            return this;
        }


        public Builder recipient(ChannelReceiver recipient) {
            this.recipients = List.of(recipient);
            return this;
        }

        public Builder recipients(ChannelReceiver... recipients) {
            this.recipients = List.of(recipients);
            return this;
        }

        public Builder recipients(Collection<ChannelReceiver> recipients) {
            this.recipients = recipients;
            return this;
        }

        @ApiStatus.Internal
        public Builder requestID(String requestID) {
            this.requestID = requestID;
            return this;
        }

        public ChannelMessage build() {
            if (messageTypeID == null) throw new IllegalStateException("messageTypeID not set");
            if (sender == null) sender = ChannelSender.self();
            return new ChannelMessage(channel, messageTypeID, content, sender, recipients, requestID);
        }
    }

}
