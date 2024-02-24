import React, { useState, useEffect } from 'react';
import axios from 'axios';
import './App.css';
function BookCRUD() {
  const [books, setBooks] = useState([]);
  const [filteredBooks, setFilterBooks] = useState([]);
  const [searchTerm, setSearchTerm] = useState ('');
  const [searchResults, setSearchResults] = useState([]);
  const [formData, setFormData] = useState({
    Title: '',
    Author: '',
    Genre: '',
    PublishYear: '',
    ImageUrl: ''
  });
  const [editMode, setEditMode] = useState(null);

  useEffect(() => {
    fetchBooks();
  }, []);

  const fetchBooks = async () => {
    try {
      const response = await axios.get('http://localhost:8080/books');
      setBooks(response.data);
    } catch (error) {
      console.error('Error fetching books:', error);
    }
  };

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setFormData({ ...formData, [name]: value });
  };

  const handleChange = (e) => {
    setSearchTerm(e.target.value);
    searchBooks(searchTerm);
  }

  const handleSearchSubmit = (e) => {
    e.preventDefault();
    searchBooks(searchTerm);
  }

  const searchBooks = (query) => {
    const results = books.filter(
        (book) =>
            book.Title.toLowerCase().includes(query.toLowerCase()) ||
            book.Author.toLowerCase().includes(query.toLowerCase()) ||
            book.Genre.toLowerCase().includes(query.toLowerCase()) ||
            book.PublishYear.toString().toLowerCase().includes(query.toLowerCase())
    )
    setSearchResults(results);
  }

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      if (editMode) {
        await axios.put(`http://localhost:8080/books/${editMode}`, formData);
      } else {
        await axios.post('http://localhost:8080/books', formData);
      }
      fetchBooks();
      setFormData({
        Title: '',
        Author: '',
        Genre: '',
        PublishYear: '',
        ImageUrl: ''
      });
      setEditMode(null);
    } catch (error) {
      console.error('Error adding/updating book:', error);
    }
  };

  const handleFilter = (filter) => {
    console.log("filter work");
    let filtered = [...books];
    switch (filter) {
      case "Title":
        filtered.sort((a,b) => a.Title.localeCompare(b.Title));
        break;
      case "Author":
        filtered.sort((a, b) => a.Author.localeCompare(b.Author));
        break;
    }
    setFilterBooks(filtered);
    setBooks(filtered);
  }


  const handleEdit = (bookId) => {
    console.log("bookId: ", bookId)
    const bookToEdit = books.find(book => book.Id === bookId);
    setFormData({
      Title: bookToEdit.Title,
      Author: bookToEdit.Author,
      Genre: bookToEdit.Genre,
      PublishYear: bookToEdit.PublishYear,
      ImageUrl: bookToEdit.ImageUrl
    });
    setEditMode(bookId);
  };

  const handleDelete = async (id) => {
    try {
      await axios.delete(`http://localhost:8080/books/${id}`);
      fetchBooks();
      setFormData({
        Title: '',
        Author: '',
        Genre: '',
        PublishYear: '',
        ImageUrl: ''
      });
      setEditMode(null);

    } catch (error) {
      console.error('Error deleting book:', error);
    }
  };

  return (
      <div className="container">
        <h1>Library management system</h1>
        <form onSubmit={handleSubmit}>
          <input type="text" name="Title" value={formData.Title} onChange={handleInputChange} placeholder="Title" required />
          <input type="text" name="Author" value={formData.Author} onChange={handleInputChange} placeholder="Author" required />
          <input type="text" name="Genre" value={formData.Genre} onChange={handleInputChange} placeholder="Genre" required />
          <input type="number" name="PublishYear" value={formData.PublishYear} onChange={handleInputChange} placeholder="Publish Year" required />
          <input type="text" name="ImageUrl" value={formData.ImageUrl} onChange={handleInputChange} placeholder="Image URL" />
          <button type="submit">{editMode ? 'Update Book' : 'Add Book'}</button>
        </form>
        <button type="button" onClick={() => handleFilter("Title")}>Sort by Title</button>
        <button type="button" onClick={() => handleFilter("Author")}>Sort by Author</button>
        <form onSubmit={handleSearchSubmit}>
          <input
              className="search"
              type="text"
              name="Search"
              value={searchTerm}
              placeholder="Search by Title, Author, Genre, Year"
              onChange={handleChange}/>
        </form>
        {searchTerm ? (
            <ul>
              {searchResults.map((book) => (
                  <li key={book.Id}>
                  <div> Title: {book.Title}</div>
                    <div> Author: {book.Author}</div>
                    <div>Genre: {book.Genre}</div>
                    <div>PublishYear: {book.PublishYear}</div>
                    <div>
                      <img src={book.ImageUrl} alt={book.Title} width="200px" height="320px"/>
                    </div>
                    <div className="ud-buttons">
                      <button onClick={() => handleEdit(book.Id)}>Edit</button>
                      <button onClick={() => handleDelete(book.Id)}>Delete</button>
                    </div>
                  </li>
              ))}
            </ul>
        ) : (
            <ul>
              {books.map((book) => (
                  <li key={book.Id}>
                    <div> Title: {book.Title}</div>
                    <div> Author: {book.Author}</div>
                    <div>Genre: {book.Genre}</div>
                    <div>PublishYear: {book.PublishYear}</div>
                    <div>
                      <img src={book.ImageUrl} alt={book.Title} width="200px" height="320px"/>
                    </div>
                    <div className="ud-buttons">
                      <button onClick={() => handleEdit(book.Id)}>Edit</button>
                      <button onClick={() => handleDelete(book.Id)}>Delete</button>
                </div>
              </li>
          ))}
        </ul>
        )}
      </div>
  );
}

export default BookCRUD;
