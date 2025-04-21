/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.csa.bookstore.resource;

import com.csa.bookstore.database.BookstoreDatabase;
import com.csa.bookstore.entity.Cart;
import com.csa.bookstore.entity.Customer;
import com.csa.bookstore.entity.Order;
import com.csa.bookstore.exception.CartNotFoundException;
import com.csa.bookstore.exception.CustomerNotFoundException;
import com.csa.bookstore.exception.InvalidInputException;
import com.csa.bookstore.exception.OrderNotFoundException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author faaeq
 */

@Path("/customers/{customerId}/orders")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class OrderResource {
    
    private static final Logger logger = Logger.getLogger(OrderResource.class.getName());
    
    @POST
    public Response createOrder(@PathParam("customerId") int customerId){
        logger.log(Level.INFO, "Creating order for customer ID: {0}", customerId);
        Customer customer = BookstoreDatabase.getCustomerById(customerId);
        if (customer == null){
            logger.log(Level.WARNING, "Customer with ID {0} not found.", customerId);
            throw new CustomerNotFoundException("Customer with ID " + customerId + " does not exist");
        }
        
        Cart cart = BookstoreDatabase.getCartByCustomerId(customerId);
        if (cart == null){
            logger.log(Level.WARNING, "Cart for customer ID {0} not found.", customerId);
            throw new CartNotFoundException("Cart for customer with ID " + customerId + " does not exist");
        }
        
        if (cart.getItems().isEmpty()){
            logger.warning("Attempted to create order with an empty cart.");
            throw new InvalidInputException("Cannot create an order with an empty cart");
        }
        
        Order order = BookstoreDatabase.createOrder(customerId);
        logger.info("Order created successfully for customer ID: " + customerId + ", Order ID: " + order.getId());
        return Response.status(Response.Status.CREATED).entity(order).build();
    }
    
    @GET
    public Response getOrdersByCustomer(@PathParam("customerId") int customerId) {
        logger.log(Level.INFO, "Fetching orders for customer ID: {0}", customerId);
        Customer customer = BookstoreDatabase.getCustomerById(customerId);
        if (customer == null) {
            logger.log(Level.WARNING, "Customer with ID {0} not found.", customerId);
            throw new CustomerNotFoundException("Customer with ID " + customerId + " does not exist");
        }
        
        List<Order> orders = BookstoreDatabase.getOrdersByCustomerId(customerId);
        logger.info("Found " + orders.size() + " orders for customer ID: " + customerId);
        return Response.ok(orders).build();
    }
    
    @GET
    @Path("/{orderId}")
    public Response getOrderById(@PathParam("customerId") int customerId, @PathParam("orderId") int orderId){
        logger.info("Fetching order ID: " + orderId + " for customer ID: " + customerId);
        Customer customer = BookstoreDatabase.getCustomerById(customerId);
        if (customer == null){
            logger.log(Level.WARNING, "Customer with ID {0} not found.", customerId);
            throw new CustomerNotFoundException("Customer with ID " + customerId + " does not exist");
        }
        
        Order order = BookstoreDatabase.getOrderById(orderId);
        if (order == null){
            logger.log(Level.WARNING, "Order with ID {0} not found.", orderId);
            throw new OrderNotFoundException("Order with ID " + orderId + " does not exist");
        }
        
        if (order.getCustomerId() != customerId){
            logger.warning("Order with ID " + orderId + " does not belong to customer ID " + customerId);
            throw new OrderNotFoundException("Order with ID " + orderId + " does not belong to customer with ID " + customerId);
        }
        
        logger.info("Order retrieved successfully: Order ID " + orderId + ", Customer ID " + customerId);
        return Response.ok(order).build();
    }
    
}
