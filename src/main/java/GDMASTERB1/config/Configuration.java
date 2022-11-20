package GDMASTERB1.config;

public class Configuration {
    private int port;
    private String webroot;

    public void setWebroot(String webroot) {
        this.webroot = webroot;
    }
    public String getWebroot() {
        return webroot;
    }

    public int getPort() {
        return port;
    }
    public void setPort(int port)
    {
        this.port = port;
    }
}
