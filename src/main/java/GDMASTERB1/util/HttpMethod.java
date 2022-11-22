package GDMASTERB1.util;

public enum HttpMethod {

    GET,
    HEAD,
    POST,
    DELETE,
    PATCH,
    OPTIONS,
    CONNECT,
    TRACE;
    public static final int MAX;

    static {
        int tmpMax = -1;
        for (HttpMethod method : values())
        {
            if (method.name().length() > tmpMax)
            {
                tmpMax = method.name().length();
            }
        }
        MAX = tmpMax;
    }
}
