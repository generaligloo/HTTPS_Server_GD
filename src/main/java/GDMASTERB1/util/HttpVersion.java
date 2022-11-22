package GDMASTERB1.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum HttpVersion {
    HTTP_1_1("HTTP/1.1", 1, 1);

    public final String LITERAL;
    public final int MAJOR;
    public final int MINOR; // sardoche ?

    HttpVersion(String LITERAL, int MAJOR, int MINOR)
    {
        this.LITERAL=LITERAL;
        this.MAJOR = MAJOR;
        this.MINOR = MINOR;
    }

    private static Pattern VersionRegex = Pattern.compile("^HTTP/(?<major>\\d+).(?<minor>\\d+)");

    public static HttpVersion getCompatible(String literalVersion) throws VersionException
    {
        Matcher matcher = VersionRegex.matcher(literalVersion);
        if (!matcher.find() || matcher.groupCount() != 2)
        {
            throw new VersionException();
        }
        int major = Integer.parseInt(matcher.group("major"));
        int minor = Integer.parseInt(matcher.group("minor"));

        HttpVersion tmp = null;
        for (HttpVersion version: HttpVersion.values())
        {
            if (version.LITERAL.equals(literalVersion))
            {
                return version;
            }
            else
            {
                if (version.MAJOR == major)
                {
                    if(version.MINOR < minor)
                    {
                        tmp =version;
                    }
                }
            }
        }
        return tmp;
    }
}
