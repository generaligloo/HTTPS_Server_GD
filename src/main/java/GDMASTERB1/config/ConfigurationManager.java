package GDMASTERB1.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.FileReader;
import GDMASTERB1.util.json;
public class ConfigurationManager
{
        private static ConfigurationManager ConfigManagerInstance;
        private static Configuration myCurrentConfig;
        private ConfigurationManager()
        {}

        public static ConfigurationManager getInstance(){
           if (ConfigManagerInstance == null)
           {
               ConfigManagerInstance = new ConfigurationManager();
           }
           return ConfigManagerInstance;
        }

        public void loadConfigFile (String path) {
            FileReader FR = null;
            try {
                FR = new FileReader(path);
            } catch (FileNotFoundException e) {
                throw new HttpConfException(e);
            }
            StringBuffer SB = new StringBuffer();
            int i;
            while (true)
            {
                try {
                    if (!((i = FR.read()) !=-1)) break;
                } catch (IOException e) {
                    throw new HttpConfException(e);
                }
                SB.append((char)i);
            }
            JsonNode conf = null;
            try {
                conf = json.parse(SB.toString());
            } catch (IOException e) {
                throw new HttpConfException("Erreur de parsing");
            }
            try {
                myCurrentConfig = json.fromJson(conf, Configuration.class);
            } catch (JsonProcessingException e) {
                throw new RuntimeException("Erreur de parsing interne");
            }
        }

        public Configuration getCurrentConfig (){
            if ( myCurrentConfig == null)
            {
                throw  new HttpConfException("No current HTTP configuration set");
            }
            return myCurrentConfig;
        }
}
