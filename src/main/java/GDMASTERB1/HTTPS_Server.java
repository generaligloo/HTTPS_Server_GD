package GDMASTERB1;
import GDMASTERB1.config.ConfigurationManager;
import java.io.*;
import GDMASTERB1.config.Configuration;
import GDMASTERB1.core.ServerListenerThread;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
public class HTTPS_Server
{
    private final static Logger LOGGER = LoggerFactory.getLogger(HTTPS_Server.class);
    public static void main(String[] args) throws Exception
    {
        LOGGER.info("Démarrage serveur ...");
        ConfigurationManager.getInstance().loadConfigFile("src\\main\\java\\GDMASTERB1\\config\\httpsconfig.json");
        Configuration conf = ConfigurationManager.getInstance().getCurrentConfig();
        LOGGER.info("Configuation port: " + conf.getPort());
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
