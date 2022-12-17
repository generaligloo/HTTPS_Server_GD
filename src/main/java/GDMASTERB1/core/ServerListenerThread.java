package GDMASTERB1.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.*;
import java.io.*;
import java.net.InetAddress;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.KeyStore;
import java.util.Scanner;

import static java.lang.System.out;

public class ServerListenerThread extends Thread {
    private int port;
    private String webroot;
    private final char[] ksPass = "123456".toCharArray();
    private final char[] ctPass = "My1stKey".toCharArray();
    private SSLServerSocket ServerSocket;
    private SSLSocket socket;

    private final static Logger LOGGER = LoggerFactory.getLogger(ServerListenerThread.class);
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_BLUE = "\u001B[34m";

    public ServerListenerThread(int port, String WR) throws Exception {
        this.port = port;
        this.webroot = WR;
        Path path = Paths.get("src\\main\\java\\GDMASTERB1\\store\\server.jks");
        String ksName = path.toAbsolutePath().toString();
        out.println("JKS server Path:"+ksName);
        KeyStore ks = KeyStore.getInstance("JKS");
        ks.load(new FileInputStream(ksName), ksPass);
        KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
        kmf.init(ks, ctPass);
        SSLContext sc = SSLContext.getInstance("TLS");
        sc.init(kmf.getKeyManagers(), null, null);
        SSLServerSocketFactory ssf = sc.getServerSocketFactory();
        this.ServerSocket = (SSLServerSocket) ssf.createServerSocket(this.port, 10, InetAddress.getByName("localhost"));
        this.ServerSocket.setSoTimeout(0);
        printServerSocketInfo(this.ServerSocket);
    }

    @Override
    public void run() {
        // Someone is calling this server
        try {
            int count = 0;
            HttpsConnectionWorkerThread worker;
            do {
                //while (ServerSocket.isBound() && !ServerSocket.isClosed()) {
                socket = (SSLSocket) ServerSocket.accept();
                count++;
                LOGGER.info(ANSI_BLUE + "Connection #: " + count + ANSI_RESET);
                printSocketInfo(socket);
                worker = new HttpsConnectionWorkerThread(socket);
                worker.start();
                worker.join();
                //}
            }while (!worker.isInterrupted());
        }
        catch (IOException e) {
            LOGGER.error(ANSI_RED + "Problème accept ServerListenerThread: " + e + ANSI_RESET);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            if(ServerSocket!= null)
            {
                try
                {
                    ServerSocket.close();
                }
                catch (IOException e)
                {
                    LOGGER.error(ANSI_RED +"Problème fermeture ServerListenerThread" +ANSI_RESET, e);
                }
            }
        }
    }


    private static void printSocketInfo(SSLSocket s) {
        LOGGER.info("Socket class: " + s.getClass());
        LOGGER.info("   Remote address = "
                + s.getInetAddress().toString());
        LOGGER.info("   Remote port = " + s.getPort());
        LOGGER.info("   Local socket address = "
                + s.getLocalSocketAddress().toString());
        LOGGER.info("   Local address = "
                + s.getLocalAddress().toString());
        LOGGER.info("   Local port = " + s.getLocalPort());
    }

    private static void printServerSocketInfo(SSLServerSocket s) {
        LOGGER.info("Server socket class: " + s.getClass());
        LOGGER.info("   Socker address = "
                + s.getInetAddress().toString());
        LOGGER.info("   Socker port = "
                + s.getLocalPort());
        LOGGER.info("   Need client authentication = "
                + s.getNeedClientAuth());
        LOGGER.info("   Want client authentication = "
                + s.getWantClientAuth());
        LOGGER.info("   Use client mode = "
                + s.getUseClientMode());
    }
}
