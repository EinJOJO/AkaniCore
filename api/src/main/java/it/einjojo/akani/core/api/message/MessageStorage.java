package it.einjojo.akani.core.api.message;

import java.util.Map;

public interface MessageStorage {

    boolean isRegistered(String lang, String key);

    Map<String, String> readMessages(String lang);

    String readMessage(String lang, String key);

    void registerMessage(String lang, String key, String message);


}
