package config;

import java.io.InputStream;
import java.util.Properties;

public class DbConfig {
    private final Properties properties = new Properties();

    public DbConfig() {
        load();
    }

    private void load() {
        try (InputStream input =
                     getClass().getClassLoader()
                             .getResourceAsStream("config.properties")) {
            if (input == null) {
                throw new RuntimeException("config.properties not found");
            }

            properties.load(input);

        } catch (Exception e) {

            throw new RuntimeException("Failed to load config.properties", e);
        }
    }

    public String get(String key) {
        return properties.getProperty(key);
    }
}
