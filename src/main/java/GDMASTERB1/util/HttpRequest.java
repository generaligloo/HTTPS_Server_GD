package GDMASTERB1.util;

import java.util.Map;

public class HttpRequest extends HttpMessage
{
    private HttpMethod method;
    private String requestTarget;
    private String originalHttpVersion;
    private Map<String, String> headers;
    private HttpVersion httpVersion;

    private String HttpPayload;

    public HttpRequest() {

    }

    public String getRequestTarget() {
        return requestTarget;
    }
    public void setRequestTarget(String requestTarget) throws HttpException {
        if(requestTarget != null || requestTarget.length() != 0)
            this.requestTarget = requestTarget;
        else
            throw new HttpException(HttpStatusCode.SERVER_ERROR_500_INTERNAL_SERVER_ERROR);
    }

    public String getHttpPayload() {
        return HttpPayload;
    }

    public void setHttpPayload(String httpPayload) {
        HttpPayload = httpPayload;
    }

    public HttpMethod getMethod() {
        return method;
    }
    void setMethod(String methodN) throws HttpException
    {
        for (HttpMethod method: HttpMethod.values())
        {
            if (methodN.equals(method.name()))
            {
                this.method = method;
                return;
            }
        }
        throw new HttpException(
                HttpStatusCode.SERVER_ERROR_501_NOT_IMPLEMENTED
        );
    }

    public HttpVersion getHttpVersion() {
        return httpVersion;
    }

    public void setHttpVersion(String originalHttpVersion) throws VersionException, HttpException {
        this.originalHttpVersion = originalHttpVersion;
        this.httpVersion = HttpVersion.getCompatible(originalHttpVersion);
        if (this.httpVersion == null)
        {
            throw new HttpException(HttpStatusCode.SERVER_ERROR_505_HTTP_VERSION_NOT_SUPPORTED);
        }
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }
}
