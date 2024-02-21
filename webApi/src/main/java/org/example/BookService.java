package org.example;

import java.sql.SQLException;
import java.util.List;

public class BookService {
    private final BookDAO bookDAO;

    public BookService() {
        bookDAO = new BookDAO();
    }
    public void addBook(Book book) {
        bookDAO.addBook(book);
    }

    public void updateBook(int id, Book book) {
        bookDAO.updateBook(id, book);
    }

    public void deleteBook(int id) {
        bookDAO.deleteBook(id);
    }

    public Book getBook(int id) {
        return bookDAO.getBook(id);
    }

    public List<Book> getBooks() throws SQLException {
        return bookDAO.getAllBooks();
    }

}

