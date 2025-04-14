/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.csa.bookstore.resource;

import com.csa.bookstore.database.Database;
import com.csa.bookstore.entity.Author;
import com.csa.bookstore.entity.Book;
import com.csa.bookstore.exception.AuthorNotFoundException;
import com.csa.bookstore.exception.BookNotFoundException;
import com.csa.bookstore.exception.InvalidInputException;
import java.util.Calendar;
import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

/**
 *
 * @author faaeq
 */

@Path("/books")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class BookResource {
    
    @POST
    public Response createBook(Book book, @Context UriInfo uriInfo) {
        // Validation
        if (book.getTitle() == null || book.getTitle().trim().isEmpty()) {
            throw new InvalidInputException("Book title cannot be empty");
        }
        
        if (book.getIsbn() == null || book.getIsbn().trim().isEmpty()) {
            throw new InvalidInputException("ISBN cannot be empty");
        }
        
        Calendar calendar = Calendar.getInstance();
        int currentYear = calendar.get(Calendar.YEAR);
        if (book.getPublicationYear() > currentYear) {
            throw new InvalidInputException("Publication year cannot be in the future");
        }
        
        if (book.getPrice() <= 0) {
            throw new InvalidInputException("Price must be greater than zero");
        }
        
        if (book.getStock() < 0) {
            throw new InvalidInputException("Stock cannot be negative");
        }
        
        // Check if author exists
        Author author = Database.getAuthorById(book.getAuthorId());
        if (author == null) {
            throw new AuthorNotFoundException("Author with ID " + book.getAuthorId() + " does not exist");
        }
        
        Book createdBook = Database.addBook(book);
        return Response.status(Status.CREATED)
                     .entity(createdBook)
                     .build();
    }
    
    @GET
    public Response getAllBooks() {
        List<Book> books = Database.getAllBooks();
        return Response.ok(books).build();
    }
    
    @GET
    @Path("/{id}")
    public Response getBookById(@PathParam("id") int id) {
        Book book = Database.getBookById(id);
        if (book == null) {
            throw new BookNotFoundException("Book with ID " + id + " does not exist");
        }
        return Response.ok(book).build();
    }
    
    @PUT
    @Path("/{id}")
    public Response updateBook(@PathParam("id") int id, Book book) {
        Book existingBook = Database.getBookById(id);
        if (existingBook == null) {
            throw new BookNotFoundException("Book with ID " + id + " does not exist");
        }
        
        // Validation
        if (book.getTitle() == null || book.getTitle().trim().isEmpty()) {
            throw new InvalidInputException("Book title cannot be empty");
        }
        
        if (book.getIsbn() == null || book.getIsbn().trim().isEmpty()) {
            throw new InvalidInputException("ISBN cannot be empty");
        }
        
        Calendar calendar = Calendar.getInstance();
        int currentYear = calendar.get(Calendar.YEAR);
        if (book.getPublicationYear() > currentYear) {
            throw new InvalidInputException("Publication year cannot be in the future");
        }
        
        if (book.getPrice() <= 0) {
            throw new InvalidInputException("Price must be greater than zero");
        }
        
        if (book.getStock() < 0) {
            throw new InvalidInputException("Stock cannot be negative");
        }
        
        // Check if author exists
        Author author = Database.getAuthorById(book.getAuthorId());
        if (author == null) {
            throw new AuthorNotFoundException("Author with ID " + book.getAuthorId() + " does not exist");
        }
        
        book.setId(id);
        Book updatedBook = Database.updateBook(book);
        return Response.ok(updatedBook).build();
    }
    
    @DELETE
    @Path("/{id}")
    public Response deleteBook(@PathParam("id") int id) {
        Book book = Database.getBookById(id);
        if (book == null) {
            throw new BookNotFoundException("Book with ID " + id + " does not exist");
        }
        
        Database.deleteBook(id);
        return Response.status(Status.NO_CONTENT).build();
    }
}
