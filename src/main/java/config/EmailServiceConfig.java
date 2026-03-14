package config;

import java.io.InputStream;
import java.util.Properties;

public class EmailServiceConfig {

    private final Properties properties = new Properties();

    public EmailServiceConfig() {
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

            throw new RuntimeException("Failed to load email config", e);
        }
    }

    public String getApiKey() {
        return properties.getProperty("brevo.api.key");
    }

    public String getSenderEmail() {
        return properties.getProperty("brevo.sender.email");
    }

    public String getSenderName() {
        return properties.getProperty("brevo.sender.name");
    }

    public String getApiUrl() {
        return properties.getProperty("brevo.url");
    }
}
