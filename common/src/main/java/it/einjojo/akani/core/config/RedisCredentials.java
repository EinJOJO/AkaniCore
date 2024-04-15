package it.einjojo.akani.core.config;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface RedisCredentials {
    @NotNull String host();

    int port();

    @Nullable String username();

    @Nullable String password();


}
