package connector;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public interface Connector {
    Connection getConnection() throws SQLException;
}
