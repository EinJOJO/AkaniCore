package it.einjojo.akani.core.config;

import dev.dejvokep.boostedyaml.YamlDocument;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface RedisCredentials {
    @NotNull
    String host();

    int port();

    @Nullable
    String username();

    @Nullable
    String password();


    public static class Impl implements RedisCredentials {
        private final YamlDocument yamlDocument;

        public Impl(YamlDocument yamlDocument) {
            this.yamlDocument = yamlDocument;
        }

        @Override
        public @NotNull String host() {
            return yamlDocument.getString("redis.host");
        }

        @Override
        public int port() {
            return yamlDocument.getInt("redis.port");
        }

        @Override
        public @Nullable String username() {
            return yamlDocument.getString("redis.username");
        }

        @Override
        public @Nullable String password() {
            return yamlDocument.getString("redis.password");
        }
    }

}
