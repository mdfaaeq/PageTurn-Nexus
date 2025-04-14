/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.csa.bookstore.entity;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author faaeq
 */

@XmlRootElement
public class Cart {
    
    private int customerId;
    private List<CartItem> items;

    public Cart() {
        this.items = new ArrayList<>();
    }

    public Cart(int customerId) {
        this.customerId = customerId;
        this.items = new ArrayList<>();
    }

    public Cart(List<CartItem> items) {
        this.items = items;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public List<CartItem> getItems() {
        return items;
    }

    public void setItems(List<CartItem> items) {
        this.items = items;
    }
    
    public void addItem(CartItem item){
        
        // Verifying whether the book is already in the cart
        for (CartItem existingItem : items){
            if (existingItem.getBookId() == item.getBookId()){
                existingItem.setQuantity(existingItem.getQuantity() + item.getQuantity());
                return;
            }
        }
        
        // If the item is not found add it as a new item
        items.add(item);
        
    }
    
    public void updateItem(int bookId, int quantity){
        for (CartItem item : items){
            if (item.getBookId() == bookId) {
                item.setQuantity(quantity);
                return;
            }
        }
    }
    
    public void removeItem(int bookId){
        items.removeIf(item -> item.getBookId() == bookId);
    }
    
    public double calculateTotal(List<Book> books) {
        double total = 0;
        for (CartItem item : items) {
            for (Book book : books) {
                if (book.getId() == item.getBookId()) {
                    total += book.getPrice() * item.getQuantity();
                    break;
                }
            }
        }
        return total;
    }
    
}

