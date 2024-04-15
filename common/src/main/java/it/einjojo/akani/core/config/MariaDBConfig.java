package it.einjojo.akani.core.config;

public interface MariaDBConfig {

    String host();
    int port();
    String database();
    String username();
    String password();

    int minIdle();

    int maxPoolSize();




}
