package connector;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class PostgresConnector implements Connector {
    private final String url;
    private final String user;
    private final String password;

    private PostgresConnector(Builder builder) {
        this.url = builder.url;
        this.user = builder.user;
        this.password = builder.password;
    }

    @Override
    public Connection getConnection() throws SQLException {
        // Регистрация драйвера PostgreSQL
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("PostgreSQL Driver not found", e);
        }
        return DriverManager.getConnection(url, user, password);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String url;
        private String user;
        private String password;

        public Builder url(String url) {
            this.url = url;
            return this;
        }

        public Builder user(String user) {
            this.user = user;
            return this;
        }

        public Builder password(String password) {
            this.password = password;
            return this;
        }

        public PostgresConnector build() {
            return new PostgresConnector(this);
        }
    }
}
