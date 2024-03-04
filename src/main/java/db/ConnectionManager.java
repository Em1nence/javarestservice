package db;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

public class ConnectionManager {
    private final String url;
    private final String username;
    private final String password;

    public ConnectionManager(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;

    }

    public Connection getConnection() throws SQLException, IOException {
        Properties properties = new Properties();

        try (InputStream inputStream = ConnectionManager.class.getClassLoader().getResourceAsStream("sql.properties")) {
            if (inputStream == null) {
                throw new FileNotFoundException("Property file 'sql.properties' not found in the classpath");
            }
            properties.load(inputStream);
        }

        if (url != null && !url.isEmpty()) {
            properties.setProperty("database.url", url);
        }
        if (username != null && !username.isEmpty()) {
            properties.setProperty("database.login", username);
        }
        if (password != null && !password.isEmpty()) {
            properties.setProperty("database.pass", password);
        }

        return DriverManager.getConnection(
                properties.getProperty("database.url"),
                properties.getProperty("database.login"),
                properties.getProperty("database.pass")
        );

    }
}
