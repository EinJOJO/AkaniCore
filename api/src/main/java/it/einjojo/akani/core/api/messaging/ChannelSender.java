package it.einjojo.akani.core.api.messaging;


import it.einjojo.akani.core.api.AkaniCoreProvider;

import java.util.Objects;

/**
 * @param n der Name des Senders
 */
public record ChannelSender(String n) {

    private static ChannelSender self;

    public static ChannelSender of(String name) {
        return new ChannelSender(name);
    }

    public static ChannelSender self() {
        if (self == null) {
            self = new ChannelSender(AkaniCoreProvider.get().brokerService().brokerName());
        }
        return self;
    }

    public String name() {
        return n;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChannelSender that = (ChannelSender) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

}
