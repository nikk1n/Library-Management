package org.example;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
public class Main {
    private static final int serverPort= Integer.parseInt(PropertyLoader.getProperty("server.port"));
    // Strategy design pattern
    // Observer design pattern
    // Abstract Factory design pattern
    // Factory method design pattern
    public static void main(String[] args) {
        System.out.println(serverPort);
        try {
            Server server = new Server(serverPort);
            ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
            context.setContextPath("/");
            context.addServlet(new ServletHolder(new BookController()), "/books/*");
            server.setHandler(context);
            server.start();
            System.out.println("Server started on port "+serverPort);
            server.join();
        } catch (Exception e) {
            throw new RuntimeException("Error occurred while starting the server: "+e.getMessage());
        }
    }
}
