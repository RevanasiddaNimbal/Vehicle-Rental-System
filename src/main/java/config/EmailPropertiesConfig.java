package config;

import java.io.InputStream;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmailPropertiesConfig {
    private static final Pattern PLACEHOLDER_PATTERN = Pattern.compile("\\$\\{([^}]+)}");
    private final Properties properties = new Properties();

    public EmailPropertiesConfig() {
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
        return resolve("brevo.api.key");
    }

    public String getSenderEmail() {
        return resolve("brevo.sender.email");
    }

    public String getSenderName() {
        return resolve("brevo.sender.name");
    }

    public String getApiUrl() {
        return resolve("brevo.url");
    }

    private String resolve(String key) {
        String value = properties.getProperty(key);
        if (value == null) {
            return null;
        }

        Matcher matcher = PLACEHOLDER_PATTERN.matcher(value);
        StringBuffer resolved = new StringBuffer();
        while (matcher.find()) {
            String envKey = matcher.group(1);
            String replacement = System.getenv(envKey);
            if (replacement == null) {
                replacement = System.getProperty(envKey);
            }
            if (replacement == null) {
                replacement = "";
            }
            matcher.appendReplacement(resolved, Matcher.quoteReplacement(replacement));
        }
        matcher.appendTail(resolved);
        return resolved.toString();
    }
}
