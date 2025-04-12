package org.example;

import org.apache.log4j.Logger;

import java.io.InputStream;
import java.util.Properties;

public class Config {
    private static final Properties props = new Properties();
    private static final Logger log = Logger.getLogger(Config.class);

    static {
        try (InputStream input = Config.class.getClassLoader().getResourceAsStream("config.properties")) {
            props.load(input);
            log.debug("Properties are loaded");
        } catch (Exception e) {
            throw new RuntimeException("Could not load config.properties", e);
        }
    }

    public static String getProp(String key) {
        return props.getProperty(key);
    }
}
