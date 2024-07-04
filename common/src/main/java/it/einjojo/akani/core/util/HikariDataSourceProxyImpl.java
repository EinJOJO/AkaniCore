package it.einjojo.akani.core.util;

import com.zaxxer.hikari.HikariDataSource;
import it.einjojo.akani.core.api.util.HikariDataSourceProxy;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.function.Consumer;

public class HikariDataSourceProxyImpl implements HikariDataSourceProxy {
    private final HikariDataSource dataSource;

    public HikariDataSourceProxyImpl(HikariDataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public HikariDataSource hiakriDataSource() {
        return dataSource;
    }

    @Override
    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    @Override
    public ResultSet prepareStatement(String sql, Consumer<PreparedStatement> psConsumer) throws SQLException {
        try (Connection connection = getConnection(); PreparedStatement ps = connection.prepareStatement(sql)) {
            psConsumer.accept(ps);
            return ps.executeQuery();
        }

    }

    @Override
    public void query(PreparedStatement ps, Consumer<ResultSet> resultSet) throws SQLException {
        try (ResultSet rs = ps.executeQuery()) {
            resultSet.accept(rs);
        }
    }


}
