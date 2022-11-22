package httptest;

import GDMASTERB1.util.HttpVersion;
import GDMASTERB1.util.VersionException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class HttpVersionTest {

    @Test
    void getVersionMatch()
    {
        HttpVersion version = null;
        try {
            version = HttpVersion.getCompatible("HTTP/1.1");
        } catch (VersionException e) {
            fail(e);
        }
        assertNotNull(version);
        assertEquals(version, HttpVersion.HTTP_1_1);
    }

    @Test
    void getBadVersionMatch()
    {
        HttpVersion version = null;
        try {
            version = HttpVersion.getCompatible("http/1.1");
            fail();
        } catch (VersionException e) {
            e.printStackTrace();
        }
    }

    @Test
    void getHigherVersionMatch()
    {
        HttpVersion version = null;
        try {
            version = HttpVersion.getCompatible("HTTP/1.2");
            assertNotNull(version);
            assertEquals(version, HttpVersion.HTTP_1_1);
        } catch (VersionException e) {
            fail();
        }
    }
}
