package it.einjojo.akani.core.util;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import it.einjojo.akani.core.config.MariaDbConfig;

public class HikariFactory {

    public HikariDataSource create(MariaDbConfig mariaconfig) {
        try {
            Class.forName("org.mariadb.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("MariaDB driver not found", e);
        }
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl("jdbc:mariadb://" + mariaconfig.host() + ":" + mariaconfig.port() + "/" + mariaconfig.database());
        hikariConfig.setUsername(mariaconfig.username());
        hikariConfig.setPassword(mariaconfig.password());
        hikariConfig.setMaximumPoolSize(mariaconfig.maxPoolSize());
        hikariConfig.setMinimumIdle(mariaconfig.minIdle());
        hikariConfig.setMaxLifetime(1800000); // 30 minutes
        hikariConfig.setConnectionTimeout(5000); // 5 seconds
        hikariConfig.setIdleTimeout(600000); // 10 minutes
        hikariConfig.setLeakDetectionThreshold(60000); // 1 minute
        return new HikariDataSource(hikariConfig);
    }

}
