package GDMASTERB1;

import GDMASTERB1.util.Ansi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.*;

public class ExchangeACQ implements Runnable {

    private final static Logger LOGGER = LoggerFactory.getLogger(HTTPS_Server.class);

    private String Token;
    private String Response = "";

    public ExchangeACQ(String Token) {
        this.Token = Token;
    }

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

            LOGGER.info(Ansi.BLUE + "Envoi du token au serveur ACQ..." + Ansi.SANE);
            bufferedwriter.write(getToken());
            bufferedwriter.flush();
            LOGGER.info(Ansi.BLUE + "En attente de la réponse du serveur ACQ..."+ Ansi.SANE);
            setResponse(bufferedreader.readLine());
            LOGGER.info(Ansi.GREEN + "Résponse de l'ACQ: "+ getResponse() + Ansi.SANE);
            sslsocket.close();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public String getToken() {
        return Token;
    }

    public void setToken(String token) {
        Token = token;
    }

    public String getResponse() {
        return Response;
    }

    public void setResponse(String response) {
        Response = response;
    }
}
