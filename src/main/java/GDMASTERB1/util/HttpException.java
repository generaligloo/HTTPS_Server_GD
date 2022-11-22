package GDMASTERB1.util;

public class HttpException extends Exception
{
    private HttpStatusCode errorCode;

    public HttpException(HttpStatusCode errorCode) {
        super(errorCode.MESSAGE);
        this.errorCode = errorCode;
    }

    public HttpStatusCode getErrorCode() {
        return errorCode;
    }
}
