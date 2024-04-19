package it.einjojo.akani.core.api.messaging;


public record ChannelReceiver(Type t, String n) {

    private static final ChannelReceiver ALL = new ChannelReceiver(Type.ALL, null);

    public static ChannelReceiver all() {
        return ALL;
    }

    public static ChannelReceiver group(String name) {
        return new ChannelReceiver(Type.GROUP, name);
    }

    public static ChannelReceiver server(String name) {
        return new ChannelReceiver(Type.SERVER, name);
    }

    public Type type() {
        return t;
    }

    public String name() {
        return n;
    }


    public enum Type {
        ALL,
        GROUP,
        SERVER
    }

}
