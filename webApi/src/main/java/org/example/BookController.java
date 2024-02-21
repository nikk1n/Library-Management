package org.example;

import com.google.gson.Gson;
import org.springframework.web.bind.annotation.CrossOrigin;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/books/*")
@CrossOrigin(origins = "http://localhost:3000/")
public class BookController extends HttpServlet {
    private BookService bookService;
    private Gson gson;

    @Override
    public void init() throws ServletException {
        super.init();
        bookService = new BookService();
        gson = new Gson();
    }
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        setAccessControlHeaders(resp);
        BufferedReader reader = req.getReader();
        Book book = gson.fromJson(reader, Book.class);
        System.out.println("doPost call");
        try {
            bookService.addBook(book);
            resp.setStatus(201);
        } catch (Exception e) {
            resp.setStatus(400);
            resp.getWriter().write(e.getMessage());
        }
    }
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        setAccessControlHeaders(resp);
        String pathInfo = req.getPathInfo();

        // If the request path is "/books", it's a request to get all books
        if (pathInfo == null || pathInfo.equals("/")) {
            try {
                List<Book> books = bookService.getBooks();
                String json = gson.toJson(books);
                resp.setContentType("application/json");
                resp.setCharacterEncoding("UTF-8");
                resp.getWriter().write(json);
            } catch (SQLException e) {
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                System.out.println(e.getMessage());
            }
        } else { // Otherwise, it's a request to get a book by ID
            try {
                int id = Integer.parseInt(pathInfo.substring(1));
                Book book = bookService.getBook(id);
                if (book != null) {
                    String json = gson.toJson(book);
                    resp.setContentType("application/json");
                    resp.setCharacterEncoding("UTF-8");
                    resp.getWriter().write(json);
                } else {
                    resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                }
            } catch (NumberFormatException e) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }
        }
    }
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        setAccessControlHeaders(resp);
        String pathInfo = req.getPathInfo();
        if (pathInfo != null) {
            try {
                int id = Integer.parseInt(pathInfo.substring(1));
                BufferedReader reader = req.getReader();
                Book book = gson.fromJson(reader, Book.class);
                bookService.updateBook(id, book);
                resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
            } catch (NumberFormatException e) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }
        } else {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
    }
    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) {
        setAccessControlHeaders(resp);
        String pathInfo = req.getPathInfo();
        if (pathInfo != null) {
            try {
                int id = Integer.parseInt(pathInfo.substring(1));
                bookService.deleteBook(id);
                resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
            } catch (NumberFormatException e) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }
        } else {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @Override
    protected void doOptions(HttpServletRequest req, HttpServletResponse resp) {
        setAccessControlHeaders(resp);
        resp.setStatus(HttpServletResponse.SC_OK);
    }

    private void setAccessControlHeaders(HttpServletResponse resp) {
        resp.setHeader("Access-Control-Allow-Origin", "*");
        resp.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        resp.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");
    }
}
