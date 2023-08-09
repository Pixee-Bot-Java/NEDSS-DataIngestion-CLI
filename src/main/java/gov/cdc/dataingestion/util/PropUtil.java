package gov.cdc.dataingestion.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropUtil {

    Properties properties = new Properties();
    String propertiesFileName = "config.properties";
    public Properties loadPropertiesFile() {
        InputStream propertiesInput = PropUtil.class.getClassLoader().getResourceAsStream(propertiesFileName);

        try {
            if (propertiesInput == null) {
                System.err.println("Unable to load config.properties. Please check the err trace.");
            }
            properties.load(propertiesInput);

        } catch (IOException e) {
            System.err.println("Exception occurred: " + e.getMessage());
        }
        return properties;
    }
}
