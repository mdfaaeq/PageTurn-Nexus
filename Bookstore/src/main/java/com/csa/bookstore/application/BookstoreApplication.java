/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.csa.bookstore.application;

/**
 *
 * @author faaeq
 */

import com.csa.bookstore.exception.mapper.AuthorNotFoundExceptionMapper;
import com.csa.bookstore.exception.mapper.BookNotFoundExceptionMapper;
import com.csa.bookstore.exception.mapper.CartNotFoundExceptionMapper;
import com.csa.bookstore.exception.mapper.CustomerNotFoundExceptionMapper;
import com.csa.bookstore.exception.mapper.InvalidInputExceptionMapper;
import com.csa.bookstore.exception.mapper.OrderNotFoundExceptionMapper;
import com.csa.bookstore.exception.mapper.OutOfStockExceptionMapper;
import com.csa.bookstore.resource.AuthorResource;
import com.csa.bookstore.resource.BookResource;
import com.csa.bookstore.resource.CartResource;
import com.csa.bookstore.resource.CustomerResource;
import com.csa.bookstore.resource.OrderResource;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import java.util.HashSet;
import java.util.Set;

@ApplicationPath("/api")
public class BookstoreApplication extends Application {
    
    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> classes = new HashSet<>();
        
        // Resources
        classes.add(BookResource.class);
        classes.add(AuthorResource.class);
        classes.add(CustomerResource.class);
        classes.add(CartResource.class);
        classes.add(OrderResource.class);
        
        // Exception mappers
        classes.add(BookNotFoundExceptionMapper.class);
        classes.add(AuthorNotFoundExceptionMapper.class);
        classes.add(CustomerNotFoundExceptionMapper.class);
        classes.add(InvalidInputExceptionMapper.class);
        classes.add(OutOfStockExceptionMapper.class);
        classes.add(CartNotFoundExceptionMapper.class);
        classes.add(OrderNotFoundExceptionMapper.class);
        
        return classes;
    }
}