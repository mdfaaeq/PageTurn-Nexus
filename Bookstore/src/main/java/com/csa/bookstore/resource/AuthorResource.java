/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.csa.bookstore.resource;

import com.csa.bookstore.database.BookstoreDatabase;
import com.csa.bookstore.entity.Author;
import com.csa.bookstore.entity.Book;
import com.csa.bookstore.exception.AuthorNotFoundException;
import com.csa.bookstore.exception.InvalidInputException;
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

/**
 *
 * @author faaeq
 */

@Path("/authors")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AuthorResource {
    
    private static final Logger logger = Logger.getLogger(AuthorResource.class.getName());
    // Creating a new author
    @POST
    public Response createAuthor(Author author){
        logger.info("Creating a new author...");
        // Proceeding with validation
        if (author.getFirstName() == null || author.getFirstName().trim().isEmpty() ||
            author.getLastName() == null || author.getLastName().trim().isEmpty()){
            logger.warning("Author name validation failed.");
            throw new InvalidInputException("Author first name and last name cannot be empty");
        }
        
        // Adding the author into the database
        Author createdAuthor =  BookstoreDatabase.addAuthor(author);
        logger.log(Level.INFO, "Author created with ID: {0}", createdAuthor.getId());
        return Response.status(Response.Status.CREATED)
                .entity(createdAuthor)
                .build();
    }
    
    // Getting all the author's from the book stores database
    @GET
    public Response getAllAuthors(){
        logger.info("Fetching all authors...");
        List<Author> authors = BookstoreDatabase.getAllAuthors();
        return Response.ok(authors).build();
    }
    
    // Retriving a author with their id
    @GET
    @Path("/{id}")
    public Response getAuthorById(@PathParam("id") int id){
        logger.log(Level.INFO, "Fetching author with ID: {0}", id);
        Author author = BookstoreDatabase.getAuthorById(id);
        if (author == null){
            logger.log(Level.WARNING, "Author with ID {0} not found.", id);
            throw new AuthorNotFoundException("Author with ID " + id + " does not exist");
        }
        return Response.ok(author).build();
    }
    
    @PUT
    @Path("/{id}")
    public Response updateAuthor(@PathParam("id") int id, Author author){
        logger.log(Level.INFO, "Updating author with ID: {0}", id);
        Author existingAuthor = BookstoreDatabase.getAuthorById(id);
        if (existingAuthor == null){
            logger.log(Level.WARNING, "Author with ID {0} not found for update.", id);
            throw new AuthorNotFoundException("Author with ID " + id + " does not exist");
        }
        
        // Proceeding with validation
        if (author.getFirstName() == null || author.getFirstName().trim().isEmpty() ||
            author.getLastName() == null || author.getLastName().trim().isEmpty()){
            logger.warning("Author name validation failed during update.");
            throw new InvalidInputException("Author first name and last name cannot be empty");
        }
        
        author.setId(id);
        author.setBookIds(existingAuthor.getBookIds());
        Author updateAuthor = BookstoreDatabase.updateAuthor(author);
        logger.log(Level.INFO, "Author with ID {0} updated successfully.", id);
        return Response.ok(updateAuthor).build();
    }
    
    @DELETE
    @Path("/{id}")
    public Response deleteAuthor(@PathParam("id") int id){
        logger.log(Level.INFO, "Attempting to delete author with ID: {0}", id);
        Author author = BookstoreDatabase.getAuthorById(id);
        if (author == null){
            logger.log(Level.WARNING, "Author with ID {0} not found for deletion.", id);
            throw new AuthorNotFoundException("Author with ID " + id + " does not exist");
        }
        BookstoreDatabase.deleteAuthor(id);
        logger.log(Level.INFO, "Author with ID {0} deleted successfully.", id);
        return Response.status(Response.Status.NO_CONTENT).build();
    }
    
    @GET
    @Path("/{id}/books")
    public Response getAuthorBooks(@PathParam("id") int id){ 
        logger.log(Level.INFO, "Fetching books for author with ID: {0}", id);
        Author author = BookstoreDatabase.getAuthorById(id);
        if (author == null){
            logger.log(Level.WARNING, "Author with ID {0} not found.", id);
            throw new AuthorNotFoundException("Author with ID " + id + " does not exist");
        }
        List<Book> books = BookstoreDatabase.getBooksByAuthor(id);
        logger.info("Books retrieved for author ID " + id + ": " + books.size() + " books found.");
        return Response.ok(books).build();
    }
    
}
