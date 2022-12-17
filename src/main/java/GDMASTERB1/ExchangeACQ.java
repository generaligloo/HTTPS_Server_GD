package GDMASTERB1;

import GDMASTERB1.util.Ansi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.*;

public class ExchangeACQ implements Runnable {

    private final static Logger LOGGER = LoggerFactory.getLogger(HTTPS_Server.class);

    @Override
    public void run() {

        try
        {
            SSLSocketFactory sslsocketfactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
            SSLSocket sslsocket = (SSLSocket) sslsocketfactory.createSocket("localhost", 29190);
            LOGGER.info(Ansi.GREEN + "Connexion au serveur ACQ réussi !");
            InputStream inputstream = sslsocket.getInputStream();
            InputStreamReader inputstreamreader = new InputStreamReader(inputstream);
            BufferedReader bufferedreader = new BufferedReader(inputstreamreader);
            OutputStream outputstream = sslsocket.getOutputStream();
            OutputStreamWriter outputstreamwriter = new OutputStreamWriter(outputstream);
            BufferedWriter bufferedwriter = new BufferedWriter(outputstreamwriter);

            LOGGER.info(Ansi.BLUE + "Envoi des informations au serveur ACQ...");
            bufferedwriter.write("Ceci est un message pour l'ACQ" + '\n');
            bufferedwriter.flush();
            LOGGER.info(Ansi.BLUE + "En attente de la réponse du serveur ACQ...");
            String response = bufferedreader.readLine();
            LOGGER.info(Ansi.GREEN + "Résponse de l'ACQ: "+ response);
            sslsocket.close();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}
