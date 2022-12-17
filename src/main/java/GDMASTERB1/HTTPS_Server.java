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
        //init method
        System.setProperty("javax.net.ssl.keyStore","src\\main\\java\\GDMASTERB1\\store\\serverendtoend.jks");
        System.setProperty("javax.net.ssl.keyStorePassword","123456HTTPS");
        System.setProperty("javax.net.ssl.trustStore","src\\main\\java\\GDMASTERB1\\store\\serverendtoend.jks");
        System.setProperty("javax.net.ssl.trustStorePassword","123456HTTPS");

        LOGGER.info("Démarrage serveur ...");
        Configuration conf = new Configuration();
        JSONParser parser = new JSONParser();
        JSONObject object = (JSONObject) parser.parse(new FileReader("src\\main\\java\\GDMASTERB1\\config\\httpsconfig.json"));
        Long port = (Long) object.get("port");
        conf.setPort( Integer.parseInt(port.toString()));
        LOGGER.info("Configuation port: " + conf.getPort());
        String WR = (String) object.get("webroot");
        conf.setWebroot(WR.toString());
        LOGGER.info("Webroot: "+conf.getWebroot());

        try
        {
            ExchangeACQ ExchangeACQ = new ExchangeACQ();
            Thread ExchangeACQThread = new Thread(ExchangeACQ);
            ServerListenerThread SLT = new ServerListenerThread(conf.getPort(), conf.getWebroot());
            SLT.start();
            ExchangeACQThread.start();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
