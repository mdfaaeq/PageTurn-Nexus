/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.csa.bookstore.resource;

import com.csa.bookstore.database.BookstoreDatabase;
import com.csa.bookstore.entity.Author;
import com.csa.bookstore.entity.Book;
import com.csa.bookstore.exception.AuthorNotFoundException;
import com.csa.bookstore.exception.BookNotFoundException;
import com.csa.bookstore.exception.InvalidInputException;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

/**
 *
 * @author faaeq
 */

@Path("/books")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class BookResource {
    
    private static final Logger logger = Logger.getLogger(BookResource.class.getName());
    
    @POST
    public Response createBook(Book book) {
        logger.info("Attempting to create a new book...");
        // Validation
        if (book.getTitle() == null || book.getTitle().trim().isEmpty()) {
            logger.warning("Book title validation failed.");
            throw new InvalidInputException("Book title cannot be empty");
        }
        
        if (book.getIsbn() == null || book.getIsbn().trim().isEmpty()) {
            logger.warning("ISBN validation failed.");
            throw new InvalidInputException("ISBN cannot be empty");
        }
        
        Calendar calendar = Calendar.getInstance();
        int currentYear = calendar.get(Calendar.YEAR);
        if (book.getPublicationYear() > currentYear) {
            logger.warning("Publication year is in the future.");
            throw new InvalidInputException("Publication year cannot be in the future");
        }
        
        if (book.getPrice() <= 0) {
            logger.warning("Book price is not greater than zero.");
            throw new InvalidInputException("Price must be greater than zero");
        }
        
        if (book.getStock() < 0) {
            logger.warning("Stock is negative.");
            throw new InvalidInputException("Stock cannot be negative");
        }
        
        // Check if author exists
        Author author = BookstoreDatabase.getAuthorById(book.getAuthorId());
        if (author == null) {
            logger.log(Level.WARNING, "Author with ID {0} not found.", book.getAuthorId());
            throw new AuthorNotFoundException("Author with ID " + book.getAuthorId() + " does not exist");
        }
        
        Book createdBook = BookstoreDatabase.addBook(book);
        logger.log(Level.INFO, "Book created successfully with ID: {0}", createdBook.getId());
        return Response.status(Status.CREATED)
                     .entity(createdBook)
                     .build();
    }
    
    @GET
    public Response getAllBooks() {
        logger.info("Fetching all books...");
        List<Book> books = BookstoreDatabase.getAllBooks();
        logger.log(Level.INFO, "Total books found: {0}", books.size());
        return Response.ok(books).build();
    }
    
    @GET
    @Path("/{id}")
    public Response getBookById(@PathParam("id") int id) {
        logger.log(Level.INFO, "Fetching book with ID: {0}", id);
        Book book = BookstoreDatabase.getBookById(id);
        if (book == null) {
            logger.log(Level.WARNING, "Book with ID {0} not found.", id);
            throw new BookNotFoundException("Book with ID " + id + " does not exist");
        }
        return Response.ok(book).build();
    }
    
    @PUT
    @Path("/{id}")
    public Response updateBook(@PathParam("id") int id, Book book) {
        logger.log(Level.INFO, "Updating book with ID: {0}", id);
        Book existingBook = BookstoreDatabase.getBookById(id);
        if (existingBook == null) {
            logger.log(Level.WARNING, "Book with ID {0} not found.", id);
            throw new BookNotFoundException("Book with ID " + id + " does not exist");
        }
        
        // Validation
        if (book.getTitle() == null || book.getTitle().trim().isEmpty()) {
            logger.warning("Book title validation failed.");
            throw new InvalidInputException("Book title cannot be empty");
        }
        
        if (book.getIsbn() == null || book.getIsbn().trim().isEmpty()) {
            logger.warning("ISBN validation failed.");
            throw new InvalidInputException("ISBN cannot be empty");
        }
        
        Calendar calendar = Calendar.getInstance();
        int currentYear = calendar.get(Calendar.YEAR);
        if (book.getPublicationYear() > currentYear) {
            logger.warning("Publication year is in the future.");
            throw new InvalidInputException("Publication year cannot be in the future");
        }
        
        if (book.getPrice() <= 0) {
            logger.warning("Book price is not greater than zero.");
            throw new InvalidInputException("Price must be greater than zero");
        }
        
        if (book.getStock() < 0) {
            logger.warning("Stock is negative.");
            throw new InvalidInputException("Stock cannot be negative");
        }
        
        // Check if author exists
        Author author = BookstoreDatabase.getAuthorById(book.getAuthorId());
        if (author == null) {
            logger.log(Level.WARNING, "Author with ID {0} not found.", book.getAuthorId());
            throw new AuthorNotFoundException("Author with ID " + book.getAuthorId() + " does not exist");
        }
        
        book.setId(id);
        Book updatedBook = BookstoreDatabase.updateBook(book);
        logger.log(Level.INFO, "Book with ID {0} updated successfully.", id);
        return Response.ok(updatedBook).build();
    }
    
    @DELETE
    @Path("/{id}")
    public Response deleteBook(@PathParam("id") int id) {
        logger.log(Level.INFO, "Deleting book with ID: {0}", id);
        Book book = BookstoreDatabase.getBookById(id);
        if (book == null) {
            logger.log(Level.WARNING, "Book with ID {0} not found.", id);
            throw new BookNotFoundException("Book with ID " + id + " does not exist");
        }
        
        BookstoreDatabase.deleteBook(id);
        logger.log(Level.INFO, "Book with ID {0} deleted successfully.", id);
        return Response.status(Status.NO_CONTENT).build();
    }
}
