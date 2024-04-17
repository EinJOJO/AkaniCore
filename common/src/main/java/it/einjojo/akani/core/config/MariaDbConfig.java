package it.einjojo.akani.core.config;

import dev.dejvokep.boostedyaml.YamlDocument;

public interface MariaDbConfig {

    String host();

    int port();

    String database();

    String username();

    String password();

    int minIdle();

    int maxPoolSize();

    public static class Impl implements MariaDbConfig {
        private final YamlDocument yamlDocument;

        public Impl(YamlDocument yamlDocument) {
            this.yamlDocument = yamlDocument;
        }

        @Override
        public String host() {
            return yamlDocument.getString("mariadb.host");
        }

        @Override
        public int port() {
            return yamlDocument.getInt("mariadb.port");
        }

        @Override
        public String database() {
            return yamlDocument.getString("mariadb.database");
        }

        @Override
        public String username() {
            return yamlDocument.getString("mariadb.username");
        }

        @Override
        public String password() {
            return yamlDocument.getString("mariadb.password");
        }

        @Override
        public int minIdle() {
            return yamlDocument.getInt("mariadb.minIdle");
        }

        @Override
        public int maxPoolSize() {
            return yamlDocument.getInt("mariadb.maxPoolSize");
        }
    }

}
