/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.csa.bookstore.exception.mapper;

import com.csa.bookstore.entity.ErrorResponse;
import com.csa.bookstore.exception.OutOfStockException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 *
 * @author faaeq
 */

@Provider
public class OutOfStockExceptionMapper implements ExceptionMapper<OutOfStockException> {
    
    @Override
    public Response toResponse(OutOfStockException exception) {
        ErrorResponse errorResponse = new ErrorResponse("Out of Stock", exception.getMessage());
        return Response.status(Response.Status.BAD_REQUEST)
                      .entity(errorResponse)
                      .type(MediaType.APPLICATION_JSON)
                      .build();
    }
    
}
