package main.ca.carleton.sysc.util;

import main.ca.carleton.sysc.communication.ArduinoTransceiver;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

public class ConfigManager {

    private final String CONFIG = "config.properties";

    /**
     * Get a config property from the CONFIG properties file
     * @param property the property to change
     * @return the value fo the property
     */
    public String getConfig(final String property) {
        String val = "";
        try {
            final InputStream input = ArduinoTransceiver.class.getClassLoader().getResourceAsStream(CONFIG);
            final Properties prop = new Properties();

            // load a properties file
            if(input != null) {
                prop.load(input);
            }

            // get the property value and print it out
            val = prop.getProperty(property);

        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return val;
    }
}
