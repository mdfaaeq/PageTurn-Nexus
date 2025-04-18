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
    
    @POST
    public Response createOrder(@PathParam("customerId") int customerId){
        Customer customer = BookstoreDatabase.getCustomerById(customerId);
        if (customer == null){
            throw new CustomerNotFoundException("Customer with ID " + customerId + " does not exist");
        }
        
        Cart cart = BookstoreDatabase.getCartByCustomerId(customerId);
        if (cart == null){
            throw new CartNotFoundException("Cart for customer with ID " + customerId + " does not exist");
        }
        
        if (cart.getItems().isEmpty()){
            throw new InvalidInputException("Cannot create an order with an empty cart");
        }
        
        Order order = BookstoreDatabase.createOrder(customerId);
        return Response.status(Response.Status.CREATED).entity(order).build();
    }
    
    @GET
    public Response getOrdersByCustomer(@PathParam("customerId") int customerId) {
        Customer customer = BookstoreDatabase.getCustomerById(customerId);
        if (customer == null) {
            throw new CustomerNotFoundException("Customer with ID " + customerId + " does not exist");
        }
        
        List<Order> orders = BookstoreDatabase.getOrdersByCustomerId(customerId);
        return Response.ok(orders).build();
    }
    
    @GET
    @Path("/{orderId}")
    public Response getOrderById(@PathParam("customerId") int customerId, @PathParam("orderId") int orderId){
        Customer customer = BookstoreDatabase.getCustomerById(customerId);
        if (customer == null){
            throw new CustomerNotFoundException("Customer with ID " + customerId + " does not exist");
        }
        
        Order order = BookstoreDatabase.getOrderById(orderId);
        if (order == null){
            throw new OrderNotFoundException("Order with ID " + orderId + " does not exist");
        }
        
        if (order.getCustomerId() != customerId){
            throw new OrderNotFoundException("Order with ID " + orderId + " does not belong to customer with ID " + customerId);
        }
        
        return Response.ok(order).build();
    }
    
}
