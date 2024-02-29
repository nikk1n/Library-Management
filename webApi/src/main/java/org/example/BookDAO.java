package org.example;

import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
public class BookDAO {
    private static final String URL=PropertyLoader.getProperty("database.url");
    private static final String USER=PropertyLoader.getProperty("database.username");
    private static final String PASSWORD=PropertyLoader.getProperty("database.password");

    DataSource dataSource=createDataSource();
    private static DataSource createDataSource() {
        HikariDataSource ds = new HikariDataSource();
        ds.setJdbcUrl(URL);
        ds.setUsername(USER);
        ds.setPassword(PASSWORD);
        return ds;
    }
    @Test
    void testAddUpdateDeleteGetBook() {
        // Test addBook(), updateBook(), deleteBook(), and getBook() methods

        // Create a book object
        Book book = new Book("Test Book", "Test Author", 2024, "Test Image", "Test Genre");

        // Add the book to the database
        assertDoesNotThrow(() -> addBook(book));

        // Get the ID of the added book
        int addedBookId = book.getId();

        // Update the book
        Book updatedBook = new Book("Updated Title", "Updated Author", 2023, "Updated Image", "Updated Genre");
        assertDoesNotThrow(() -> updateBook(addedBookId, updatedBook));

        // Retrieve the updated book from the database and verify
        Book retrievedBook = assertDoesNotThrow(() -> getBook(addedBookId));
        assertNotNull(retrievedBook);
        assertEquals("Updated Title", retrievedBook.getTitle());
        assertEquals("Updated Author", retrievedBook.getAuthor());
        assertEquals(2023, retrievedBook.getPublishedYear());
        assertEquals("Updated Image", retrievedBook.getImageUrl());
        assertEquals("Updated Genre", retrievedBook.getGenre());

        // Delete the book from the database
        assertDoesNotThrow(() -> deleteBook(addedBookId));

        // Verify the book is deleted
        assertNull(getBook(addedBookId));
    }

    @Test
    void testGetAllBooks() {
        // Test getAllBooks() method

        // Add some books to the database (for testing getAllBooks())
        Book book1 = new Book("Book 1", "Author 1", 2022, "image 1", "Genre 1");
        Book book2 = new Book("Book 2", "Author 2", 2023, "image 2", "Genre 2");

        assertDoesNotThrow(() -> addBook(book1));
        assertDoesNotThrow(() -> addBook(book2));

        // Retrieve all books from the database and verify
        List<Book> allBooks = assertDoesNotThrow(this::getAllBooks);

        assertNotNull(allBooks);
        assertFalse(allBooks.isEmpty());
        assertTrue(allBooks.size() >= 2); // At least two books should be added

        // Clean up: delete the added books
        for (Book book : allBooks) {
            assertDoesNotThrow(() -> deleteBook(book.getId()));
        }
    }

    public void addBook(Book book) {
        System.out.println(book);
       String query = "INSERT INTO books (title, author, publishyear, imageurl, genre) VALUES (?, ?, ?, ?, ?)";
         try (Connection connection = dataSource.getConnection();
              PreparedStatement preparedStatement = connection.prepareStatement(query,Statement.RETURN_GENERATED_KEYS)) {
              System.out.println("Connected to database");
              preparedStatement.setString(1, book.getTitle());
              preparedStatement.setString(2, book.getAuthor());
              preparedStatement.setInt(3, book.getPublishedYear());
              preparedStatement.setString(4, book.getImageUrl());
              preparedStatement.setString(5, book.getGenre());
              preparedStatement.execute();
             System.out.println("Data saved in db");
             try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                 if (generatedKeys.next()) {
                     book.setId(generatedKeys.getInt(1)); // Set the ID in the Book object
                 } else {
                     throw new RuntimeException("Failed to obtain generated ID for book");
                 }
             }
         } catch (SQLException e) {
              throw new RuntimeException("Error occurred while adding a book in db: "+e.getMessage());
         }
    }
    public void updateBook(int id, Book book) {
        String query = "UPDATE books SET title = ?, author = ?, publishyear = ?, imageurl = ?, genre = ? WHERE id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, book.getTitle());
            preparedStatement.setString(2, book.getAuthor());
            preparedStatement.setInt(3, book.getPublishedYear());
            preparedStatement.setString(4, book.getImageUrl());
            preparedStatement.setString(5, book.getGenre());
            preparedStatement.setInt(6, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error occurred while updating a book in db: "+e.getMessage());
        }
    }
    public void deleteBook(int id) {
        String query = "DELETE FROM books WHERE id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error occurred while deleting a book in db: "+e.getMessage());
        }
    }
    public Book getBook(int id) {
        String query = "SELECT * FROM books WHERE id = ?";

        Book book = null;
        try (Connection connection = dataSource.getConnection();
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
            throw new RuntimeException("Error occurred while getting a book in db: "+e.getMessage());
        }
        return book;
    }
    public List<Book> getAllBooks() {
        var books = new ArrayList<Book>();
        String query = "SELECT * FROM books";
        try (Connection connection = dataSource.getConnection();
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
            throw new RuntimeException("Error occurred while getting all books in db: "+e.getMessage());
        }
        return books;
    }
}
