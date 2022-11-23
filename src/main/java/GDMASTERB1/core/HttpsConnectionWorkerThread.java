package GDMASTERB1.core;

import GDMASTERB1.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLSocket;
import java.io.*;
import java.net.Socket;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

public class HttpsConnectionWorkerThread extends Thread
{
    private final static Logger LOGGER = LoggerFactory.getLogger(HttpsConnectionWorkerThread.class);
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_BLUE = "\u001B[34m";
    private SSLSocket socket;


    public HttpsConnectionWorkerThread(SSLSocket socket)
    {
        this.socket = socket;
    }

    @Override
    public void run()
    {
        LOGGER.info(ANSI_GREEN + "Debut Worker Thread" + ANSI_RESET);
        BufferedReader dis = null;
        BufferedWriter dos = null;
        InputStream testIn = null;
        PrintWriter out = null;
        try {
                dis = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                HttpParser ParserHTTP = new HttpParser();
                HttpRequest RequestHTTP = new HttpRequest();
                while (true) {
                    dis.mark(1);
                    int m = dis.read();
                    dis.reset();
                    if (m >= 0) {
                        try {
                            RequestHTTP = ParserHTTP.parseHttpRequest(dis);
                        } catch (HttpException e) {
                            e.printStackTrace();
                        }
                        //debug
                        if (RequestHTTP.getMethod() == HttpMethod.GET) {
                            LOGGER.debug(ANSI_GREEN + "Method: " + RequestHTTP.getMethod() + ANSI_RESET);
                            LOGGER.debug(ANSI_GREEN + "Targer: " + RequestHTTP.getRequestTarget() + ANSI_RESET);
                            LOGGER.debug(ANSI_GREEN + "Version: " + RequestHTTP.getHttpVersion() + ANSI_RESET);
                            Iterator<Map.Entry<String, String>> entries = RequestHTTP.getHeaders().entrySet().iterator();
                            while (entries.hasNext()) {
                                Map.Entry<String, String> entry = entries.next();
                                LOGGER.debug(ANSI_GREEN + "Param = " + entry.getKey() + ", Value = " + entry.getValue() + ANSI_RESET);
                            }
                            dos = new BufferedWriter( new OutputStreamWriter(socket.getOutputStream()));
                            File file = new File("src\\main\\java\\GDMASTERB1\\view\\test_GET.html");
                            Scanner scan = new Scanner(file);
                            String htmlString = "";
                            final String CRLF = "\r\n";
                            //read all lines of the text file
                            while (scan.hasNext()) {
                                htmlString += scan.nextLine();
                            }
                            String response =
                                    "HTTP/1.0 200 OK" + CRLF +
                                            "Content-Length:" + htmlString.getBytes().length + CRLF +
                                            CRLF +
                                            htmlString +
                                            CRLF + CRLF;
                            dos.write(response);
                            dos.flush();
                            break;
                        }
                        else if(RequestHTTP.getMethod() == HttpMethod.POST)
                        {
                            LOGGER.debug(ANSI_GREEN + "Method: " + RequestHTTP.getMethod() + ANSI_RESET);
                            LOGGER.debug(ANSI_GREEN + "Targer: " + RequestHTTP.getRequestTarget() + ANSI_RESET);
                            LOGGER.debug(ANSI_GREEN + "Version: " + RequestHTTP.getHttpVersion() + ANSI_RESET);
                            Iterator<Map.Entry<String, String>> entries = RequestHTTP.getHeaders().entrySet().iterator();
                            while (entries.hasNext()) {
                                Map.Entry<String, String> entry = entries.next();
                                LOGGER.debug(ANSI_GREEN + "Param = " + entry.getKey() + ", Value = " + entry.getValue() + ANSI_RESET);
                            }
                            dos = new BufferedWriter( new OutputStreamWriter(socket.getOutputStream()));
                            String payload = RequestHTTP.getHttpPayload();
                            String[] payloadArray = payload.split("&|=");
                            boolean isAuthenticated = Authentication.verifyAuthentication(payloadArray);
                            String htmlString = "";
                            String response = "";
                            final String CRLF = "\r\n";
                            if(isAuthenticated) {
                                htmlString = "<html>" +
                                        "<head><title>Bienvenue</title></head>" +
                                        "<body><h1>Bienvenue " + payloadArray[1] +"!</h1>" +
                                        "<h2>Bouton de paiment ici</h2></body>" +
                                        "</html>";
                                response = "HTTP/1.0 200 OK" + CRLF +
                                        "Content-Length:" + htmlString.getBytes().length + CRLF +
                                        CRLF +
                                        htmlString +
                                        CRLF + CRLF;
                            } else {
                                htmlString = "<html>" +
                                        "<head><title>Bienvenue</title></head>" +
                                        "<body><h1>Utilisateur ou mot de passe incorrect</h1></body>" +
                                        "</html>";
                                response = "HTTP/1.0 200 OK" + CRLF +
                                        "Content-Length:" + htmlString.getBytes().length + CRLF +
                                        CRLF +
                                        htmlString +
                                        CRLF + CRLF;
                            }
                            dos.write(response);
                            dos.flush();
                            break;
                        }
                    }
                }
            /*
            File file = new File("src\\main\\java\\GDMASTERB1\\view\\test_GET.html");
            Scanner scan = new Scanner(file);
            String htmlString = "";
            final String CRLF = "\r\n";
            //read all lines of the text file
            while (scan.hasNext()) {
                htmlString += scan.nextLine();
            }
            String m = dis.readLine();
            LOGGER.info(ANSI_GREEN + m + ANSI_RESET);

            if (m != null) {
                String response =
                        "HTTP/1.0 200 OK" + CRLF +
                                "Content-Length:" + htmlString.getBytes().length + CRLF +
                                CRLF +
                                htmlString +
                                CRLF + CRLF;
                dos.write(response);
                StringBuilder receive = new StringBuilder();
                receive.append(m);
                receive.append(System.getProperty("line.separator"));
                String line = dis.lines().collect(Collectors.joining(System.getProperty("line.separator")));
                long test=0;
                while ((m = dis.readLine()) != null) {
                    if (m.length == 0)
                        break; // End of a GET call
                    //System.out.println(m);
                    receive.append(m);
                    receive.append(CRLF);
                    test += m.length();
                    //LOGGER.info(ANSI_BLUE + m + ANSI_RESET);
                }
                LOGGER.info(ANSI_BLUE + line + ANSI_RESET);
                dos.flush();
            }
            */
            LOGGER.info(ANSI_GREEN + "Fin Worker Thread" + ANSI_RESET);
        }
        catch (IOException e)
        {
            LOGGER.error(ANSI_RED + e.toString() + ANSI_RESET);
        }
        finally {
            if (socket != null)
            {
                try {
                    socket.close();
                }
                catch (IOException e) {}
            }
            if (dos != null)
            {
                try {
                    dos.close();
                } catch (IOException e) {}
            }
            if (dis != null)
            {
                try {
                    dis.close();
                } catch (IOException e) {}
            }
        }
    }
}
