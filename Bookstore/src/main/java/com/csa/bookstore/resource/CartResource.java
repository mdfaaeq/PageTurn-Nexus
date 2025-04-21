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

@Path("/customers/{customerId}/cart")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class CartResource {
    
    private static final Logger logger = Logger.getLogger(CartResource.class.getName());
    
    @POST
    @Path("/items")
    public Response addItemToCart(@PathParam("customerId") int customerId, CartItem item) {
        logger.info("Attempting to add item to cart. Customer ID: " + customerId + ", Book ID: " + item.getBookId());
        
        // Cross checking whether the customer is existing
        Customer customer = BookstoreDatabase.getCustomerById(customerId);
        if (customer == null) {
            logger.log(Level.WARNING, "Customer not found: {0}", customerId);
            throw new CustomerNotFoundException("Customer with ID " + customerId + " does not exist");
        }
        
        // Cross checking whether the book is existing
        Book book = BookstoreDatabase.getBookById(item.getBookId());
        if (book == null) {
            logger.log(Level.WARNING, "Book not found: {0}", item.getBookId());
            throw new BookNotFoundException("Book with ID " + item.getBookId() + " does not exist");
        }
        
        // Performing validation for the item
        if (item.getQuantity() <= 0) {
            logger.log(Level.WARNING, "Invalid quantity: {0}", item.getQuantity());
            throw new InvalidInputException("Quantity must be greater than zero");
        }
        
        // Checking for stock availability
        if (item.getQuantity() > book.getStock()) {
            logger.log(Level.WARNING, "Requested quantity exceeds stock for book ID: {0}", item.getBookId());
            throw new OutOfStockException("Requested quantity exceeds available stock for book ID " + item.getBookId());
        }
        
        // Checking if there is an existing cart if not create a new cart
        Cart cart = BookstoreDatabase.getCartByCustomerId(customerId);
        if (cart == null){
            logger.log(Level.INFO, "No cart found for customer. Creating a new cart for ID: {0}", customerId);
            cart = new Cart(customerId);
        }
        
        // Adding item to the cart
        cart.addItem(item);
        BookstoreDatabase.updateCart(cart);
        logger.log(Level.INFO, "Item added successfully to cart for customer ID: {0}", customerId);
        return Response.status(Response.Status.CREATED)
                     .entity(cart)
                     .build();
    }
    
    @GET
    public Response getCart(@PathParam("customerId") int customerId) {
        logger.log(Level.INFO, "Fetching cart for customer ID: {0}", customerId);
        // Checking whether customer exists
        Customer customer = BookstoreDatabase.getCustomerById(customerId);
        if (customer == null) {
            logger.log(Level.WARNING, "Customer not found: {0}", customerId);
            throw new CustomerNotFoundException("Customer with ID " + customerId + " does not exist");
        }
        
        // Geting the cart
        Cart cart = BookstoreDatabase.getCartByCustomerId(customerId);
        if (cart == null) {
            logger.log(Level.WARNING, "Cart not found for customer ID: {0}", customerId);
            throw new CartNotFoundException("Cart for customer with ID " + customerId + " does not exist");
        }
        
        logger.log(Level.INFO, "Cart retrieved successfully for customer ID: {0}", customerId);
        return Response.ok(cart).build();
    }
    
    @PUT
    @Path("/items/{bookId}")
    public Response updateCart(@PathParam("customerId") int customerId, @PathParam("bookId") int bookId, CartItem cartItem) {
        logger.info("Updating cart item. Customer ID: " + customerId + ", Book ID: " + bookId);
        // Verifying whether the customer already exists
        Customer existingCustomer = BookstoreDatabase.getCustomerById(customerId);
        if (existingCustomer == null) {
            logger.log(Level.WARNING, "Customer not found: {0}", customerId);
            throw new CustomerNotFoundException("Customer with ID " + customerId + " does not exist");
        }
        
        // Checking whether the book is there
        Book book = BookstoreDatabase.getBookById(bookId);
        if (book == null) {
            logger.log(Level.WARNING, "Book not found: {0}", bookId);
            throw new BookNotFoundException("Book with ID " + bookId + " does not exist");
        }
        
        // Getting the cart of the relavent customer
        Cart cart = BookstoreDatabase.getCartByCustomerId(customerId);
        if (cart == null){
            logger.log(Level.WARNING, "Cart not found for customer ID: {0}", customerId);
            throw new CartNotFoundException("Cart for customer with ID " + customerId + " does not exist");
        }
        
        // Performing validation for the item
        if (cartItem.getQuantity() <= 0) {
            logger.log(Level.WARNING, "Invalid quantity for update: {0}", cartItem.getQuantity());
            throw new InvalidInputException("Quantity must be greater than zero");
        }
        
        // Checking for stock availability
        if (cartItem.getQuantity() > book.getStock()) {
            logger.log(Level.WARNING, "Requested quantity exceeds stock for book ID: {0}", bookId);
            throw new OutOfStockException("Requested quantity exceeds available stock for book ID " + bookId);
        }
        
        if (cartItem.getBookId() != bookId){
            logger.warning("Mismatched book IDs in path and body");
            throw new InvalidInputException("Book ID in the path does not match the one in the request body");
        }
        
        cart.updateItem(bookId, cartItem.getQuantity());
        BookstoreDatabase.updateCart(cart);
        logger.info("Cart updated successfully for customer ID: " + customerId + ", Book ID: " + bookId);
        return Response.ok(cart).build();
    }
    
    @DELETE
    @Path("/items/{bookId}")
    public Response removeCart(@PathParam("customerId") int customerId, @PathParam("bookId") int bookId) {
        logger.info("Removing item from cart. Customer ID: " + customerId + ", Book ID: " + bookId);
        Customer customer = BookstoreDatabase.getCustomerById(customerId);
        if (customer == null){
            logger.log(Level.WARNING, "Customer not found: {0}", customerId);
            throw new CustomerNotFoundException("Customer with ID " + customerId + " does not exist");
        }
        
        Cart cart = BookstoreDatabase.getCartByCustomerId(customerId);
        if (cart == null){
            logger.log(Level.WARNING, "Cart not found for customer ID: {0}", customerId);
            throw new CartNotFoundException("Cart for customer with ID " + customerId + " does not exist");
        }
        
        cart.removeItem(bookId);
        BookstoreDatabase.updateCart(cart);
        logger.info("Item removed successfully from cart for customer ID: " + customerId + ", Book ID: " + bookId);
        return Response.status(Response.Status.NO_CONTENT).build();
    }
}
