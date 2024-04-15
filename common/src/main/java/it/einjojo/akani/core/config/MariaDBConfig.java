package it.einjojo.akani.core.config;

import dev.dejvokep.boostedyaml.YamlDocument;

public interface MariaDBConfig {

    String host();

    int port();

    String database();

    String username();

    String password();

    int minIdle();

    int maxPoolSize();

    public static class Impl implements MariaDBConfig {
        private final YamlDocument yamlDocument;

        public Impl(YamlDocument yamlDocument) {
            this.yamlDocument = yamlDocument;
        }

        @Override
        public String host() {
            return yamlDocument.getString("mariaDB.host");
        }

        @Override
        public int port() {
            return yamlDocument.getInt("mariaDB.port");
        }

        @Override
        public String database() {
            return yamlDocument.getString("mariaDB.database");
        }

        @Override
        public String username() {
            return yamlDocument.getString("mariaDB.username");
        }

        @Override
        public String password() {
            return yamlDocument.getString("mariaDB.password");
        }

        @Override
        public int minIdle() {
            return yamlDocument.getInt("mariaDB.minIdle");
        }

        @Override
        public int maxPoolSize() {
            return yamlDocument.getInt("mariaDB.maxPoolSize");
        }
    }

}
