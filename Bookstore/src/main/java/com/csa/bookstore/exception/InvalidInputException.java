/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.csa.bookstore.exception;

/**
 *
 * @author faaeq
 */

public class InvalidInputException extends RuntimeException {
    
    private static final long serialVersionUID = 1L;

    public InvalidInputException(String message) {
        super(message);
    }
    
}
