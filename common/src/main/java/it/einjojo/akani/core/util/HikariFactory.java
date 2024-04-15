package it.einjojo.akani.core.util;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import it.einjojo.akani.core.config.MariaDBConfig;

public class HikariFactory {

    public static HikariDataSource create(MariaDBConfig mariaconfig) {
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
