package httptest;

import GDMASTERB1.util.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class HttpParserTest {

    private HttpParser httpParser;

    @BeforeAll
    public void beforeClass() {
        httpParser = new HttpParser();
    }

    @Test
    void parseHttpRequest() {
        HttpRequest request = null;
        try {
            request = httpParser.parseHttpRequest(
                    generateValidTest()
            );
        } catch (HttpException e) {
            fail(e);
        }
        assertNotNull(request);
        assertEquals(request.getMethod(), HttpMethod.GET);
        assertEquals(request.getRequestTarget(), "/");
    }

    @Test
    void parseHttpPOSTRequest() {
        HttpRequest request = null;
       try {
            request = httpParser.parseHttpRequest(
                    generateValidPOSTTest()
            );
        } catch (HttpException e) {
            fail(e);
        }
        assertNotNull(request);
        assertEquals(request.getMethod(), HttpMethod.POST);
        assertEquals(request.getRequestTarget(), "/test");
    }

    @Test
    void parseBadMethod0HttpRequest() {
        try {
            HttpRequest request = httpParser.parseHttpRequest(
                    generateBadMethod0Test()
            );
            fail();
        } catch (HttpException e) {
            assertEquals(e.getErrorCode(), HttpStatusCode.SERVER_ERROR_501_NOT_IMPLEMENTED);
        }
    }

    @Test
    void parseBadMethod1HttpRequest() {
        try {
            HttpRequest request = httpParser.parseHttpRequest(
                    generateBadMethod1Test()
            );
            fail();
        } catch (HttpException e) {
            assertEquals(e.getErrorCode(), HttpStatusCode.SERVER_ERROR_501_NOT_IMPLEMENTED);
        }
    }

    @Test
    void parseBadMethod2HttpRequest() {
        try {
            HttpRequest request = httpParser.parseHttpRequest(
                    generateBadMethod2Test()
            );
            fail();
        } catch (HttpException e) {
            assertEquals(e.getErrorCode(), HttpStatusCode.CLIENT_ERROR_400_BAD_REQUEST);
        }
    }

    @Test
    void parseEmptyHttpRequest() {
        try {
            HttpRequest request = httpParser.parseHttpRequest(
                    generateEmptyRequestTest()
            );
            fail();
        } catch (HttpException e) {
            assertEquals(e.getErrorCode(), HttpStatusCode.CLIENT_ERROR_400_BAD_REQUEST);
        }
    }

    @Test
    void parseNoLFHttpRequest() {
        try {
            HttpRequest request = httpParser.parseHttpRequest(
                    generateBadRequestNoLFTest()
            );
            fail();
        } catch (HttpException e) {
            assertEquals(e.getErrorCode(), HttpStatusCode.CLIENT_ERROR_400_BAD_REQUEST);
        }
    }

    private BufferedReader generateValidTest() {
        String s =
                "GET / HTTP/1.1\r\n" +
                        "Host: localhost:8043\r\n" +
                        "Connection: keep-alive" +
                        "\r\n";

        InputStream IStest = new ByteArrayInputStream(s.getBytes(StandardCharsets.US_ASCII));
        BufferedReader result = (new BufferedReader(new InputStreamReader(IStest)));
        return result;
    }

    private BufferedReader generateValidPOSTTest() {
        String s = "POST /test HTTP/1.1\r\n" +
                "Host: foo.example\r\n" +
                "Content-Type: application/x-www-form-urlencoded\r\n" +
                "Content-Length: 27\r\n" +
                "\r\n" +
                "field1=value1&field2=value2\r\n";

        InputStream IStest = new ByteArrayInputStream(s.getBytes(StandardCharsets.US_ASCII));
        BufferedReader result = (new BufferedReader(new InputStreamReader(IStest)));
        return result;
    }

    private BufferedReader generateBadMethod0Test() {
        String s =
                "Get /login HTTP/1.1\r\n" +
                        "Host: localhost:8043\r\n" +
                        "Accept-Language: fr-FR,fr;q=0.9,en-US;q=0.8,en;q=0.7;" +
                        "\r\n";

        InputStream IStest = new ByteArrayInputStream(s.getBytes(StandardCharsets.US_ASCII));
        BufferedReader result = (new BufferedReader(new InputStreamReader(IStest)));
        return result;
    }

    private BufferedReader generateBadMethod1Test() {
        String s =
                "GETTTTTTTTTTTTTTT /login HTTP/1.1\r\n" +
                        "Host: localhost:8043\r\n" +
                        "Accept-Language: fr-FR,fr;q=0.9,en-US;q=0.8,en;q=0.7;" +
                        "\r\n";

        InputStream IStest = new ByteArrayInputStream(s.getBytes(StandardCharsets.US_ASCII));
        BufferedReader result = (new BufferedReader(new InputStreamReader(IStest)));
        return result;
    }

    private BufferedReader generateBadMethod2Test() {
        String s =
                "GET / login HTTP/1.1\r\n" +
                        "Host: localhost:8043\r\n" +
                        "Accept-Language: fr-FR,fr;q=0.9,en-US;q=0.8,en;q=0.7;" +
                        "\r\n";

        InputStream IStest = new ByteArrayInputStream(s.getBytes(StandardCharsets.US_ASCII));
        BufferedReader result = (new BufferedReader(new InputStreamReader(IStest)));
        return result;
    }

    private BufferedReader generateEmptyRequestTest() {
        String s = "\r\n";
        InputStream IStest = new ByteArrayInputStream(s.getBytes(StandardCharsets.US_ASCII));
        BufferedReader result = (new BufferedReader(new InputStreamReader(IStest)));
        return result;
    }

    private BufferedReader generateBadRequestNoLFTest() {
        String s =
                "GET / login HTTP/1.1\r" +
                        "Host: localhost:8043\r\n" +
                        "Accept-Language: fr-FR,fr;q=0.9,en-US;q=0.8,en;q=0.7;" +
                        "\r\n";

        InputStream IStest = new ByteArrayInputStream(s.getBytes(StandardCharsets.US_ASCII));
        BufferedReader result = (new BufferedReader(new InputStreamReader(IStest)));
        return result;
    }


}