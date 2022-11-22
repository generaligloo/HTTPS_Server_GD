package GDMASTERB1.util;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.stream.Stream;

public class HttpParser {

    private final static Logger LOGGER = LoggerFactory.getLogger(HttpParser.class);

    private static final int SP = 0x20; //Space 32
    private static final int CR = 0x0D; //Carriage return 13
    private static final int LF = 0x0A; //Line Feed 10

    private static HttpRequest session;

    public HttpParser() {

    }


    public HttpRequest parseHttpRequest(BufferedReader Stream) throws HttpException {
        HttpRequest request = new HttpRequest();
        try {
            int byteLengthRequest = parseHttpRequestLine(Stream, request);
            LOGGER.info("Requete Taille: " + byteLengthRequest);
            String m = "";
            parseHeaders(Stream, request);
            if(request.getMethod() == HttpMethod.POST)
            {
                parsePayload(Stream, request);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return request;
    }

    private int parseHttpRequestLine(BufferedReader stream, HttpRequest request) throws IOException, HttpException
    {
        boolean MethodOK = false;
        boolean RequestOK = false;
        // "GET /login HTTP/1.1\r\n"
        int byte_length = 0;
        StringBuilder SB = new StringBuilder();
        int _byte;
        while ((_byte = stream.read()) >= 0)
        {
            if(_byte == CR) {
                _byte = stream.read();
                if (_byte == LF) {
                    LOGGER.debug("Résultat Version: {}", SB.toString());
                    if (!MethodOK || !RequestOK)
                    {
                        throw new HttpException(HttpStatusCode.CLIENT_ERROR_400_BAD_REQUEST);
                    }
                    try {
                        request.setHttpVersion(SB.toString());
                    } catch (VersionException e) {
                        throw new HttpException(HttpStatusCode.CLIENT_ERROR_400_BAD_REQUEST);
                    }
                    byte_length += (SB.length()+2);
                    return byte_length;
                }
                else
                {
                    throw new HttpException(HttpStatusCode.CLIENT_ERROR_400_BAD_REQUEST);
                }
            }

            if(_byte == SP)
            {
                if (!MethodOK) {
                    LOGGER.debug("Résultat Methode: {}", SB.toString());
                    request.setMethod(SB.toString());
                    MethodOK = true;
                }
                else if (!RequestOK)
                {
                    LOGGER.debug("Résultat Requete: {}", SB.toString());
                    request.setRequestTarget(SB.toString());
                    RequestOK = true;
                } else
                {
                    throw new HttpException(HttpStatusCode.CLIENT_ERROR_400_BAD_REQUEST);
                }
                //LOGGER.debug("Résultat: {}", SB.toString());
                byte_length += (SB.length()+1);
                SB.delete(0, SB.length());
            }
            else
            {
                SB.append((char) _byte);
                if (!MethodOK)
                {
                    if (SB.length() > HttpMethod.MAX)
                    {
                        throw new HttpException(HttpStatusCode.SERVER_ERROR_501_NOT_IMPLEMENTED);
                    }
                }
            }
        }
        return 0;
    }
    private void parsePayload(BufferedReader stream, HttpRequest request) throws IOException, HttpException {
        int _byte = stream.read();
        StringBuilder line = new StringBuilder();
        line.append((char)_byte);
        while (stream.ready()) {
            _byte= stream.read();
            line.append((char)_byte);
        }
        LOGGER.info("Fin du payload");
        LOGGER.debug("Payload: " + line);
        request.setHttpPayload(line.toString());
    }

    private void parseHeaders(BufferedReader stream, HttpRequest request) throws IOException, HttpException {
        ArrayList<String> HeadersList = new ArrayList<>();
        String line = stream.readLine();
        if (request.getMethod() == HttpMethod.POST) {
            while (line != null) {
                HeadersList.add(line);
                line = stream.readLine();
                if (line.isEmpty()) {
                    break;
                }
            }
        }
        else if(request.getMethod() == HttpMethod.GET)
        {
            while (line.getBytes().length != 0 )
            {
                HeadersList.add(line);
                line = stream.readLine();
            }
        }
        if (HeadersList == null || HeadersList.isEmpty()) {
            throw new HttpException(HttpStatusCode.CLIENT_ERROR_400_BAD_REQUEST);
        }

        int index = -1;
        HashMap<String, String> headersMap = new HashMap<>();

        for (String header : HeadersList) {
            if (header == null || header.isEmpty()) {
                continue;
            }

            index = header.indexOf(':');
            if (index == -1) {
                continue;
            }

            headersMap.put(header.substring(0, index).trim(), header.substring(index + 1).trim());
        }
        request.setHeaders(headersMap);
        Iterator<Map.Entry<String, String>> entries = headersMap.entrySet().iterator();
        while (entries.hasNext()) {
            Map.Entry<String, String> entry = entries.next();
            LOGGER.debug("Var = " + entry.getKey() + ", Value = " + entry.getValue());
        }
        return;
    }

}
