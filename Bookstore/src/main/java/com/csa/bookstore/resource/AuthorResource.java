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
import javax.ws.rs.core.UriInfo;

/**
 *
 * @author faaeq
 */

@Path("/authors")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AuthorResource {
    
    // Creating a new author
    @POST
    public Response createAuthor(Author author){
        // Proceeding with validation
        if (author.getName() == null || author.getName().trim().isEmpty()){
            throw new InvalidInputException("Author name cannot be empty");
        }
        
        // Adding the author into the database
        Author createdAuthor =  BookstoreDatabase.addAuthor(author);
        return Response.status(Response.Status.CREATED)
                .entity(createdAuthor)
                .build();
    }
    
    // Getting all the author's from the book stores database
    @GET
    public Response getAllAuthors(){
        List<Author> authors = BookstoreDatabase.getAllAuthors();
        return Response.ok(authors).build();
    }
    
    // Retriving a author with their id
    @GET
    @Path("/{id}")
    public Response getAuthorById(@PathParam("id") int id){
        Author author = BookstoreDatabase.getAuthorById(id);
        if (author == null){
            throw new AuthorNotFoundException("Author with ID " + id + " does not exist");
        }
        return Response.ok(author).build();
    }
    
    @PUT
    @Path("/{id}")
    public Response updateAuthor(@PathParam("id") int id, Author author){
        Author existingAuthor = BookstoreDatabase.getAuthorById(id);
        if (existingAuthor == null){
            throw new AuthorNotFoundException("Author with ID " + id + " does not exist");
        }
        
        // Proceeding with validation
        if (author.getName() == null || author.getName().trim().isEmpty()){
            throw new InvalidInputException("Author name cannot be empty");
        }
        
        author.setId(id);
        author.setBookIds(existingAuthor.getBookIds());
        Author updateAuthor = BookstoreDatabase.updateAuthor(author);
        return Response.ok(updateAuthor).build();
    }
    
    @DELETE
    @Path("/{id}")
    public Response deleteAuthor(@PathParam("id") int id){
        Author author = BookstoreDatabase.getAuthorById(id);
        if (author == null){
            throw new AuthorNotFoundException("Author with ID " + id + " does not exist");
        }
        BookstoreDatabase.deleteAuthor(id);
        return Response.status(Response.Status.NO_CONTENT).build();
    }
    
    @GET
    @Path("/{id}/books")
    public Response getAuthorBooks(@PathParam("id") int id){ 
        Author author = BookstoreDatabase.getAuthorById(id);
        if (author == null){
            throw new AuthorNotFoundException("Author with ID " + id + " does not exist");
        }
        List<Book> books = BookstoreDatabase.getBooksByAuthor(id);
        return Response.ok(books).build();
    }
    
}
