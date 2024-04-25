
# Akani Core API
Basis API für alles.

Verwaltet:
- MariaDB Connections `AkaniCore#dataSource`
- Redis Connections `AkaniCore#jedisPool`
- Nachrichten `AkaniCore#messageManager`
- BrokerService ("Redis PubSub") `AkaniCore#brokerService()`
    - Sende eine `ChannelMessage` an verschiedene Adressaten.
    - Oder stelle eine Anfrage, die von einem anderem Server beantwortet wird.
- Spieler `AkaniCore#playerManager`
    - Coins & Taler (`EconomyHolder`)
    - Aktueller Server
    - Server-Position (`NetworkLocation`)

- Server `AkaniCore#networkManager`




## API Usage
Konkretes Anwendungsbeispiel ist das [Essentials Plugin](https://github.com/EinJOJO/AkaniEssentials).




#### API Verfügbar machen: `build.gradle.kts`
```kotlin
repositories {
    maven("https://repo.akani.dev/releases")
}

dependencies {
    // Verfügbare Module: paper | velocity | api
    compileOnly("it.einjojo.akani.core:paper:1.2.0")
}

```
In der `plugin.yml` als `depend` setzen:
```
depend: [ AkaniCore ]
```


#### Holen einer Akani Core Instanz
```java
// Es gibt VelocityAkaniCore, PaperAkaniCore und ein basic AkaniCore
// Paper bietet Bukkit-freundlichere Implementationen. 
PaperAkaniCore core = (PaperAkaniCore) AkaniCoreProvider.get(); 

```





### Umgang mit dem Message-System

Ermöglicht übersetzbare Nachrichten an einem Ort zu haben.
Relevante Klassen sind `MessageProvider`, `AkaniCore`, `MessageManager<PLATFORM_PLAYER>`

Als Plugin kann man eigene Nachrichten hinzufügen:
```java
// Optional wenn man sich den Message-Prefix sparen will
public interface MessageKey {
    String PREFIX = "essentials.";
    String GENERIC_ERROR = PREFIX + "generic_error";
    String SPECIFY_PLAYER = PREFIX + "essentials.specify_player";

    static String of(String key) {
        return PREFIX + key;
    }
}

// 2. Man erstellt einen MessageProvider,
public class EssentialsMessageProvider implements MessageProvider {
    @Override
    public String providerName() {
        return "Essentials";
    }

    @Override
    public boolean shouldInsert(MessageStorage messageStorage) {
        return true; // Nur für die Entwicklung. 
        // Es bietet sich an, im Storage zu gucken, ob eine Nachricht bereits existiert, um dann nicht jede einzelene Nachricht in die Datenbank zu jagen. 
        // Bei DuplicateKey passiert zwar nichts, man kann sich dann aber die Anfrage auch sparen. 
    }

    @Override
    public void insertMessages(MessageStorage s) {
        s.registerMessage("de", MessageKey.GENERIC_ERROR, "%prefix% <red>Ein Fehler ist aufgetreten!");
        s.registerMessage("de", MessageKey.SPECIFY_PLAYER, "%prefix% <red>Du musst einen Spieler angeben!");
        s.registerMessage("de", MessageKey.of("teleport.not_self"), "%prefix% <red>Du kannst dich nicht zu dir selbst teleportieren!");
        s.registerMessage("de", MessageKey.of("teleport.teleporting"), "%prefix% <yellow>Du wirst zu %player% teleportiert!");
    }
}
// 3
public class PluginMain extends JavaPlugin {

    private PaperAkaniCore core;
    public void onEnable() {
        core = (PaperAkaniCore) AkaniCoreProvider.get();
    }

    public PaperAkaniCore core() {
        return core;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        // Sende eine Nachricht beim Joinen. Ersetze aber vorher potentielle Placeholder. 
        // Es ist auch möglich die PlaceholderAPI dazwischen zu packen.
        core.messageManager().sendMessage(event.getPlayer(), "pluginname.message.key", (plainMessage) -> {
            String changed = plainMessage.replaceAll("%player%", "Mit der Nachrichten faxxen machen");
            changed += "<red>Modifiziert</red>";
            return changed;
        });
    } 
}
```






## Authors

- [einjojo](https://einjojo.it) [![wakatime](https://wakatime.com/badge/user/8604eeb7-fa00-4008-be52-a3985d373289/project/018eddda-f31d-4fb8-8286-377a60533271.svg)](https://wakatime.com/badge/user/8604eeb7-fa00-4008-be52-a3985d373289/project/018eddda-f31d-4fb8-8286-377a60533271)

