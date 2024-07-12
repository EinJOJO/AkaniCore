
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
    maven("https://repo.akani.dev/releases") // Keine Authentifizierung benötigt.
}

dependencies {
    // Verfügbare Module: api (paper | velocity nur für ganz wilde sachen)
    compileOnly("it.einjojo.akani.core:api:1.5.1")
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



## Authors

- [einjojo](https://einjojo.it) [![wakatime](https://wakatime.com/badge/user/8604eeb7-fa00-4008-be52-a3985d373289/project/018eddda-f31d-4fb8-8286-377a60533271.svg)](https://wakatime.com/badge/user/8604eeb7-fa00-4008-be52-a3985d373289/project/018eddda-f31d-4fb8-8286-377a60533271)

