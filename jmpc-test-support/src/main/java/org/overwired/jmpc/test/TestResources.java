package org.overwired.jmpc.test;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Provide convenient access to test resources.
 */
public class TestResources {

    private static final Logger LOGGER = LoggerFactory.getLogger(TestResources.class);

    public static Map<String, String> loadProperties(String qualifiedFilename) {
        Properties properties = new Properties();
        InputStream inputStream = TestResources.class.getClassLoader().getResourceAsStream(qualifiedFilename);
        try {
            properties.load(inputStream);
        } catch (IOException e) {
            LOGGER.error("failed to load loadProperties from classpath: '{}'", qualifiedFilename);
        }
        return propertiesToMap(properties);
    }

    private static Map<String, String> propertiesToMap(Properties properties) {
        Map<String, String> map = new HashMap(properties.size());
        for (Map.Entry<Object, Object> entry : properties.entrySet()) {
            map.put((String) entry.getKey(), (String) entry.getValue());
        }
        return map;
    }

}
