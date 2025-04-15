/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.csa.bookstore.resource;

import com.csa.bookstore.database.BookstoreDatabase;
import com.csa.bookstore.entity.Author;
import com.csa.bookstore.exception.AuthorNotFoundException;
import com.csa.bookstore.exception.InvalidInputException;
import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
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
    public Response createAuthor(Author author, @Context UriInfo uriInfo){
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
    
}
