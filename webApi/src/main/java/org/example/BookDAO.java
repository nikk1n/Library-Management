package org.example;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookDAO {
    private static final String URL = "jdbc:postgresql://localhost:5432/postgres";
    private static final String USER = "postgres";
    private static final String PASSWORD = "ybrbnjc-.-.";

    public void addBook(Book book) {
        System.out.println(book);
        // ConnectionPool у которого есть n-ное количество соединений, которые будут переиспользоваться
        // пул соединений
       String query = "INSERT INTO books (title, author, publishyear, imageurl, genre) VALUES (?, ?, ?, ?, ?)";
         try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
              PreparedStatement preparedStatement = connection.prepareStatement(query)) {
              System.out.println("Connected to database");
              preparedStatement.setString(1, book.getTitle());
              preparedStatement.setString(2, book.getAuthor());
              preparedStatement.setInt(3, book.getPublishedYear());
              preparedStatement.setString(4, book.getImageUrl());
              preparedStatement.setString(5, book.getGenre());
              preparedStatement.execute();
             System.out.println("Data saved in db");
         } catch (SQLException e) {
              System.out.println(e.getMessage());
         }
    }
    public void updateBook(int id, Book book) {
        String query = "UPDATE books SET title = ?, author = ?, publishyear = ?, imageurl = ?, genre = ? WHERE id = ?";
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, book.getTitle());
            preparedStatement.setString(2, book.getAuthor());
            preparedStatement.setInt(3, book.getPublishedYear());
            preparedStatement.setString(4, book.getImageUrl());
            preparedStatement.setString(5, book.getGenre());
            preparedStatement.setInt(6, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    public void deleteBook(int id) {
        String query = "DELETE FROM books WHERE id = ?";
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    public Book getBook(int id) {
        String query = "SELECT * FROM books WHERE id = ?";

        Book book = null;
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    book = new Book();
                    book.setId(resultSet.getInt("id"));
                    book.setTitle(resultSet.getString("title"));
                    book.setAuthor(resultSet.getString("author"));
                    book.setPublishedYear(resultSet.getInt("publishyear"));
                    book.setImageUrl(resultSet.getString("imageurl"));
                    book.setGenre(resultSet.getString("genre"));
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return book;
    }
    public List<Book> getAllBooks() {
        var books = new ArrayList<Book>();
        String query = "SELECT * FROM books";
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                var book = new Book();
                book.setId(resultSet.getInt("id"));
                book.setTitle(resultSet.getString("title"));
                book.setAuthor(resultSet.getString("author"));
                book.setPublishedYear(resultSet.getInt("publishyear"));
                book.setImageUrl(resultSet.getString("imageurl"));
                book.setGenre(resultSet.getString("genre"));
                books.add(book);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return books;
    }
}
