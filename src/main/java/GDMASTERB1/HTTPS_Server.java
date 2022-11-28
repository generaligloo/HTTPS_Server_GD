package GDMASTERB1;

import GDMASTERB1.config.Configuration;
import GDMASTERB1.core.ServerListenerThread;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.FileReader;
import java.io.IOException;
public class HTTPS_Server
{
    private final static Logger LOGGER = LoggerFactory.getLogger(HTTPS_Server.class);
    public static void main(String[] args) throws Exception
    {
        LOGGER.info("Démarrage serveur ...");
        Configuration conf = new Configuration();
        JSONParser parser = new JSONParser();
        JSONObject object = (JSONObject) parser.parse(new FileReader("src\\main\\java\\GDMASTERB1\\config\\httpsconfig.json"));
        Long port = (Long) object.get("port");
        System.out.println(port.toString());
        conf.setPort( Integer.parseInt(port.toString()));
        LOGGER.info("Configuation port: " + conf.getPort());
        String WR = (String) object.get("webroot");
        conf.setWebroot(WR.toString());
        LOGGER.info("Webroot: "+conf.getWebroot());

        try
        {
            ServerListenerThread SLT = new ServerListenerThread(conf.getPort(), conf.getWebroot());
            SLT.start();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
