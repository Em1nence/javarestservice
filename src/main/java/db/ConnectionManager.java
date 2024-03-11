package db;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionManager {
    private String url;
    private String username;
    private String password;

    public ConnectionManager() {
        try {
            Properties properties = loadProperties();
            this.url = properties.getProperty("database.url");
            this.username = properties.getProperty("database.login");
            this.password = properties.getProperty("database.pass");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setCredentials(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, username, password);
    }

    private Properties loadProperties() throws IOException {
        Properties properties = new Properties();
        try (InputStream inputStream = ConnectionManager.class.getClassLoader().getResourceAsStream("sql.properties")) {
            if (inputStream == null) {
                throw new FileNotFoundException("Property file 'sql.properties' not found in the classpath");
            }
            properties.load(inputStream);
        }
        return properties;
    }
}