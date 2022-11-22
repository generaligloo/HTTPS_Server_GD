package GDMASTERB1.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLSocket;
import java.io.*;
import java.net.Socket;
import java.util.HashMap;
import java.util.Scanner;

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
        LOGGER.info("Début Worker Thread");
        BufferedReader dis = null;
        BufferedWriter dos = null;
        try {
            dis = new BufferedReader(new InputStreamReader
                    (socket.getInputStream()));
            dos = new BufferedWriter(new OutputStreamWriter
                    (socket.getOutputStream()));
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
                while ((m = dis.readLine()) != null) {
                    if (m.length() == 0) break; // End of a GET call
                    //System.out.println(m);
                    receive.append(m);
                    receive.append(CRLF);
                    //LOGGER.info(ANSI_BLUE + m + ANSI_RESET);
                }

                dos.flush();
            }
            LOGGER.info(ANSI_GREEN + "Fin Worker Thread" + ANSI_RESET);
        }
        catch (IOException e)
        {
            LOGGER.error(ANSI_RED + e.toString() + ANSI_RESET);

        }
        finally {
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
            if (socket != null)
            {
                try {
                    socket.close();
                }
                catch (IOException e) {}
            }
        }
    }
    protected String method   = null;
    protected String url      = null;
    protected String protocol = null;
    protected String parseError = "ERREUR DE PARSE";
    protected HashMap<String, String> headers = new HashMap<String, String>();
    public void ParseURL(String m)
    {
        if (m == null) {
            parseError();
        } else if (!m.contains(":")) {
            String[] lineParts = m.split(" ");
                method = lineParts[0];
            if (lineParts[1] != null)
                url = lineParts[1];
            if (lineParts[2] != null)
                protocol = lineParts[2];
        } else {
            String[] parts = m.split(": ");
            headers.put(parts[0], parts[1]);
        }
    }
    public String parseError() {
        return this.parseError;
    }
}
