package com.salesforce.framework.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class ConfigReader {

    private static final String CONFIG_FILE = "config.properties";
    private static final Properties PROPERTIES = new Properties();

    static {
        try (InputStream inputStream = ConfigReader.class.getClassLoader().getResourceAsStream(CONFIG_FILE)) {
            if (inputStream == null) {
                throw new IllegalStateException("Configuration file not found on classpath: " + CONFIG_FILE);
            }
            PROPERTIES.load(inputStream);
        } catch (IOException exception) {
            throw new IllegalStateException("Unable to load configuration file: " + CONFIG_FILE, exception);
        }
    }

    private ConfigReader() {
    }

    public static String get(String key) {
        try {
            String systemValue = System.getProperty(key);
            if (systemValue != null && !systemValue.isBlank()) {
                return systemValue.trim();
            }
            String environmentValue = System.getenv(key);
            if (environmentValue != null && !environmentValue.isBlank()) {
                return environmentValue.trim();
            }
            String fileValue = PROPERTIES.getProperty(key);
            if (fileValue == null) {
                throw new IllegalArgumentException("Missing configuration key: " + key);
            }
            return fileValue.trim();
        } catch (RuntimeException exception) {
            throw new RuntimeException("Failed to resolve configuration key: " + key, exception);
        }
    }

    public static int getInt(String key) {
        try {
            return Integer.parseInt(get(key));
        } catch (NumberFormatException exception) {
            throw new RuntimeException("Configuration key is not a valid integer: " + key, exception);
        }
    }

    public static boolean getBoolean(String key) {
        return Boolean.parseBoolean(get(key));
    }
}
