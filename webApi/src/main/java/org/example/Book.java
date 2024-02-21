package org.example;

public class Book {
    private int Id;
    private String Title;
    private String Author;
    private int PublishYear;

    private String ImageUrl;

    private String Genre;

    public Book() {
    }

    public String getGenre() {
        return Genre;
    }

    public void setGenre(String genre) {
        Genre = genre;
    }

    public String getImageUrl() {
        return ImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        ImageUrl = imageUrl;
    }

    public int getPublishedYear() {
        return PublishYear;
    }

    public void setPublishedYear(int publishYear) {
        PublishYear = publishYear;
    }

    public String getAuthor() {
        return Author;
    }

    public void setAuthor(String author) {
        Author = author;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }
}
