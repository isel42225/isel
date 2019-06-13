package pt.isel.ls.apps.http;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FirstHttpServer {

    /* 
     * TCP port where to listen. 
     * Standard port for HTTP is 80 but might be already in use
     */
    private static final int LISTEN_PORT = 8080;
        
   public static void main(String[] args) throws Exception {

        System.out.println("Starting main...");

        String portDef = System.getenv("PORT");
        int port = portDef != null ? Integer.valueOf(portDef) : LISTEN_PORT;
        System.out.println("Listening on port " + port);

        Server server = new Server(port);
        ServletHandler handler = new ServletHandler();  //Atende os pedidos
        server.setHandler(handler);
        handler.addServletWithMapping(new ServletHolder(new TimeServlet()), "/*");
        server.start();
        System.out.println("Server started");
        server.join();
        System.out.println("main ends.");
    }    
}
