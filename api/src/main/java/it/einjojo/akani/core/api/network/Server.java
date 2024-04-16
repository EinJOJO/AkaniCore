package it.einjojo.akani.core.api.network;

public interface Server {
    String name();
    String groupName();
    Group group();

    void runCommand(String command);
}
