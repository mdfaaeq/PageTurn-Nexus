/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.csa.bookstore.exception.mapper;

import com.csa.bookstore.entity.ErrorResponse;
import com.csa.bookstore.exception.AuthorNotFoundException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 *
 * @author faaeq
 */

@Provider
public class AuthorNotFoundExceptionMapper implements ExceptionMapper<AuthorNotFoundException> {
    
    @Override
    public Response toResponse(AuthorNotFoundException exception) {
        ErrorResponse errorResponse = new ErrorResponse("Author Not Found", exception.getMessage());
        return Response.status(Response.Status.NOT_FOUND)
                      .entity(errorResponse)
                      .type(MediaType.APPLICATION_JSON)
                      .build();
    }
    
}

