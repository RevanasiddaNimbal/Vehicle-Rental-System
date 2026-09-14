package config;

import java.io.InputStream;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DbPropertiesConfig {
    private static final Pattern PLACEHOLDER_PATTERN = Pattern.compile("\\$\\{([^}]+)}");
    private final Properties properties = new Properties();

    public DbPropertiesConfig() {
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
        String value = properties.getProperty(key);
        return resolvePlaceholders(value);
    }

    private String resolvePlaceholders(String value) {
        if (value == null) {
            return null;
        }

        Matcher matcher = PLACEHOLDER_PATTERN.matcher(value);
        StringBuffer resolved = new StringBuffer();

        while (matcher.find()) {
            String placeholder = matcher.group(1);
            String replacement = System.getenv(placeholder);
            if (replacement == null) {
                replacement = System.getProperty(placeholder);
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
