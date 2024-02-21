package org.example;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

public class Main {
    public static void main(String[] args) {
        try {
            Server server = new Server(8080);
            ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
            context.setContextPath("/");
            context.addServlet(new ServletHolder(new BookController()), "/books/*");
            server.setHandler(context);
            server.start();
            System.out.println("Server started on port 8080");
            server.join();
        } catch (Exception e) {
            System.out.println("An error occurred while starting the server: " + e.getMessage());
        }
    }
}
