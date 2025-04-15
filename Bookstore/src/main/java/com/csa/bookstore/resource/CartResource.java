/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.csa.bookstore.resource;

import com.csa.bookstore.database.BookstoreDatabase;
import com.csa.bookstore.entity.Book;
import com.csa.bookstore.entity.Cart;
import com.csa.bookstore.entity.CartItem;
import com.csa.bookstore.entity.Customer;
import com.csa.bookstore.exception.BookNotFoundException;
import com.csa.bookstore.exception.CartNotFoundException;
import com.csa.bookstore.exception.CustomerNotFoundException;
import com.csa.bookstore.exception.InvalidInputException;
import com.csa.bookstore.exception.OutOfStockException;
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

@Path("/customers/{customerId}/cart")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class CartResource {
    
    @POST
    @Path("/items")
    public Response addItemToCart(@PathParam("customerId") int customerId, CartItem item) {
        
        // Cross checking whether the customer is existing
        Customer customer = BookstoreDatabase.getCustomerById(customerId);
        if (customer == null) {
            throw new CustomerNotFoundException("Customer with ID " + customerId + " does not exist");
        }
        
        // Cross checking whether the book is existing
        Book book = BookstoreDatabase.getBookById(item.getBookId());
        if (book == null) {
            throw new BookNotFoundException("Book with ID " + item.getBookId() + " does not exist");
        }
        
        // Performing validation for the item
        if (item.getQuantity() <= 0) {
            throw new InvalidInputException("Quantity must be greater than zero");
        }
        
        // Checking for stock availability
        if (item.getQuantity() > book.getStock()) {
            throw new OutOfStockException("Requested quantity exceeds available stock for book ID " + item.getBookId());
        }
        
        // Checking if there is an existing cart if not create a new cart
        Cart cart = BookstoreDatabase.getCartByCustomerId(customerId);
        if (cart == null){
            cart = new Cart(customerId);
        }
        
        // Adding item to the cart
        cart.addItem(item);
        BookstoreDatabase.updateCart(cart);
        return Response.status(Response.Status.CREATED)
                     .entity(cart)
                     .build();
    }
    
    @GET
    public Response getCart(@PathParam("customerId") int customerId) {
        
        // Checking whether customer exists
        Customer customer = BookstoreDatabase.getCustomerById(customerId);
        if (customer == null) {
            throw new CustomerNotFoundException("Customer with ID " + customerId + " does not exist");
        }
        
        // Geting the cart
        Cart cart = BookstoreDatabase.getCartByCustomerId(customerId);
        if (cart == null) {
            throw new CartNotFoundException("Cart for customer with ID " + customerId + " does not exist");
        }
        
        return Response.ok(cart).build();
    }
    
    @PUT
    @Path("/items/{bookId}")
    public Response updateCart(@PathParam("customerId") int customerId, @PathParam("bookId") int bookId, CartItem cartItem) {
        Customer existingCustomer = BookstoreDatabase.getCustomerById(customerId);
        if (existingCustomer == null) {
            throw new CustomerNotFoundException("Customer with ID " + customerId + " does not exist");
        }
        
        Book book = BookstoreDatabase.getBookById(bookId);
        if (book == null) {
            throw new BookNotFoundException("Book with ID " + bookId + " does not exist");
        }
        
        
    }
    
    @DELETE
    @Path("/{id}")
    public Response deleteBook(@PathParam("id") int id) {
        
        return Response.status(Response.Status.NO_CONTENT).build();
    }
}
