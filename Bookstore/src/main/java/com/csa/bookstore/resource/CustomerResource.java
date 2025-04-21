/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.csa.bookstore.resource;

import com.csa.bookstore.database.BookstoreDatabase;
import com.csa.bookstore.entity.Customer;
import com.csa.bookstore.exception.CustomerNotFoundException;
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
import javax.ws.rs.core.Response.Status;

/**
 *
 * @author faaeq
 */

@Path("/customers")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class CustomerResource {
    
    private static final Logger logger = Logger.getLogger(CustomerResource.class.getName());
    
    @POST
    public Response createCustomer(Customer customer) {
        
        logger.info("Creating a new customer...");
        // Proceeding with customer validation
        if (customer.getFirstName() == null || customer.getFirstName().trim().isEmpty() ||
            customer.getLastName() == null || customer.getLastName().trim().isEmpty()) {
            logger.warning("Customer name validation failed.");
            throw new InvalidInputException("Customer first name and last name cannot be empty");
        }
        
        if (customer.getEmail() == null || customer.getEmail().trim().isEmpty() || !customer.getEmail().contains("@")) {
            logger.warning("Customer email validation failed.");
            throw new InvalidInputException("Valid email is required");
        }
        
        if (customer.getPassword() == null || customer.getPassword().length() < 8) {
            logger.warning("Customer password validation failed.");
            throw new InvalidInputException("Password must be at least 8 characters long");
        }
        
        Customer createdCustomer = BookstoreDatabase.addCustomer(customer);
        logger.log(Level.INFO, "Customer created with ID: {0}", createdCustomer.getId());
        return Response.status(Status.CREATED)
                     .entity(createdCustomer)
                     .build();
    }
    
    @GET
    public Response getAllCustomers() {
        logger.info("Fetching all customers...");
        List<Customer> customers = BookstoreDatabase.getAllCustomers();
        return Response.ok(customers).build();
    }
    
    @GET
    @Path("/{id}")
    public Response getCustomerById(@PathParam("id") int id) {
        logger.log(Level.INFO, "Fetching customer with ID: {0}", id);
        Customer customer = BookstoreDatabase.getCustomerById(id);
        if (customer == null) {
            logger.log(Level.WARNING, "Customer with ID {0} not found.", id);
            throw new CustomerNotFoundException("Customer with ID " + id + " does not exist");
        }
        return Response.ok(customer).build();
    }
    
    @PUT
    @Path("/{id}")
    public Response updateCustomer(@PathParam("id") int id, Customer customer) {
        logger.log(Level.INFO, "Updating customer with ID: {0}", id);
        Customer existingCustomer = BookstoreDatabase.getCustomerById(id);
        if (existingCustomer == null) {
            logger.log(Level.WARNING, "Customer with ID {0} not found for update.", id);
            throw new CustomerNotFoundException("Customer with ID " + id + " does not exist");
        }
        
        // Proceeding with customer validation
        if (customer.getFirstName() == null || customer.getFirstName().trim().isEmpty() ||
            customer.getLastName() == null || customer.getLastName().trim().isEmpty()) {
            logger.warning("Customer name validation failed during update.");
            throw new InvalidInputException("Customer first name and last name cannot be empty");
        }
        
        if (customer.getEmail() == null || customer.getEmail().trim().isEmpty() || !customer.getEmail().contains("@")) {
            logger.warning("Customer email validation failed during update.");
            throw new InvalidInputException("Valid email is required");
        }
        
        if (customer.getPassword() == null || customer.getPassword().length() < 8) {
            logger.warning("Customer password validation failed during update.");
            throw new InvalidInputException("Password must be at least 8 characters long");
        }
        
        customer.setId(id);
        Customer updatedCustomer = BookstoreDatabase.updateCustomer(customer);
        logger.log(Level.INFO, "Customer with ID {0} updated successfully.", id);
        return Response.ok(updatedCustomer).build();
    }
    
    @DELETE
    @Path("/{id}")
    public Response deleteCustomer(@PathParam("id") int id) {
        logger.log(Level.INFO, "Attempting to delete customer with ID: {0}", id);
        Customer customer = BookstoreDatabase.getCustomerById(id);
        if (customer == null) {
            logger.log(Level.WARNING, "Customer with ID {0} not found for deletion.", id);
            throw new CustomerNotFoundException("Customer with ID " + id + " does not exist");
        }
        
        BookstoreDatabase.deleteCustomer(id);
        logger.log(Level.INFO, "Customer with ID {0} deleted successfully.", id);
        return Response.status(Status.NO_CONTENT).build();
    }
}
