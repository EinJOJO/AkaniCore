package it.einjojo.akani.core.api.util;

import com.zaxxer.hikari.HikariDataSource;

import java.sql.*;
import java.util.function.Consumer;

/**
 * Proxy interface for hikari connection pool because of linking issues
 */
public interface HikariDataSourceProxy {

    HikariDataSource hiakriDataSource();

    Connection getConnection() throws SQLException;

    void prepareStatement(String sql, Consumer<PreparedStatement> psConsumer) throws SQLException;

    void query(PreparedStatement ps, Consumer<ResultSet> resultSet) throws SQLException;


}
