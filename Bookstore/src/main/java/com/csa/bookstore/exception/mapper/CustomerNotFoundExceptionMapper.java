/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.csa.bookstore.exception.mapper;

import com.csa.bookstore.entity.ErrorResponse;
import com.csa.bookstore.exception.CustomerNotFoundException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 *
 * @author faaeq
 */

@Provider
public class CustomerNotFoundExceptionMapper implements ExceptionMapper<CustomerNotFoundException> {
    
    @Override
    public Response toResponse(CustomerNotFoundException exception) {
        ErrorResponse errorResponse = new ErrorResponse("Customer Not Found", exception.getMessage());
        return Response.status(Response.Status.NOT_FOUND)
                      .entity(errorResponse)
                      .type(MediaType.APPLICATION_JSON)
                      .build();
    }
    
}
