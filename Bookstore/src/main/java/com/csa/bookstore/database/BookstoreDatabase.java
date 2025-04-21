/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.csa.bookstore.database;

import com.csa.bookstore.entity.Author;
import com.csa.bookstore.entity.Book;
import com.csa.bookstore.entity.Cart;
import com.csa.bookstore.entity.Customer;
import com.csa.bookstore.entity.Order;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *
 * @author faaeq
 */

public class BookstoreDatabase {
    
    private static Map<Integer, Book> books = new HashMap<>();
    private static Map<Integer, Author> authors = new HashMap<>();
    private static Map<Integer, Customer> customers = new HashMap<>();
    private static Map<Integer, Cart> carts = new HashMap<>();
    private static Map<Integer, Order> orders = new HashMap<>();
    
    private static int bookIdCounter = 1;
    private static int authorIdCounter = 1;
    private static int customerIdCounter = 1;
    private static int orderIdCounter = 1;
    
    // Initializing a few sample values in the database
    static {
        // Creating authors of books
        Author author1 = new Author(authorIdCounter++, "J.K.", "Rowling", "British author best known for the Harry Potter series");
        Author author2 = new Author(authorIdCounter++, "Chugong", "", "South Korean author, best known for the web novel 'Solo Leveling'");
        
        authors.put(author1.getId(), author1);
        authors.put(author2.getId(), author2);
        
        // Create books with authors
        Book book1 = new Book(bookIdCounter++, "Harry Potter and the Philosopher's Stone", author1.getId(), "978-0-7475-3269-9", 1997, 19.99, 100);
        Book book2 = new Book(bookIdCounter++, "Solo Leveling, Vol. 1", author2.getId(), "978-1-9753-2554-9", 2018, 13.99, 80);
        
        books.put(book1.getId(), book1);
        books.put(book2.getId(), book2);
        
        // Updating author's book list
        author1.addBookId(book1.getId());
        author2.addBookId(book2.getId());
        
        // Creating new customers in the bookstore
        Customer customer1 = new Customer(customerIdCounter++, "Faaeq", "Fazal", "faaeq@gmail.com", "PasswordCSA1");
        Customer customer2 = new Customer(customerIdCounter++, "Hatim", "Tai", "Hatim@gmail.com", "PasswordCSA2");
        
        customers.put(customer1.getId(), customer1);
        customers.put(customer2.getId(), customer2);
        
        // Creating carts for customers
        Cart cart1 = new Cart(customer1.getId());
        Cart cart2 = new Cart(customer2.getId());
        
        carts.put(customer1.getId(), cart1);
        carts.put(customer2.getId(), cart2);
        
    }
    
    // Methods of the book
    public static List<Book> getAllBooks(){
        return new ArrayList<>(books.values());
    }
    
    public static Book getBookById(int id){
        return books.get(id);
    }
    
    public static Book addBook(Book book) {
        book.setId(bookIdCounter++);
        books.put(book.getId(), book);
        // Adding book to author's book list
        Author author = authors.get(book.getAuthorId());
        if (author != null) {
            author.addBookId(book.getId());
        }
        return book;
    }
    
    public static Book updateBook(Book book) {
        Book existingBook = books.get(book.getId());
        if (existingBook != null) {
            // If author is changed, update author's book lists
            if (existingBook.getAuthorId() != book.getAuthorId()) {
                Author oldAuthor = authors.get(existingBook.getAuthorId());
                if (oldAuthor != null) {
                    oldAuthor.removeBookId(book.getId());
                }
                Author newAuthor = authors.get(book.getAuthorId());
                if (newAuthor != null) {
                    newAuthor.addBookId(book.getId());
                }
            }
            books.put(book.getId(), book);
        }
        return book;
    }
    
    public static void deleteBook(int id) {
        Book book = books.get(id);
        if (book != null) {
            // Removing book from author's book list
            Author author = authors.get(book.getAuthorId());
            if (author != null) {
                author.removeBookId(id);
            }
            books.remove(id);
        }
    }
    
    public static List<Book> getBooksByAuthor(int authorId) {
        return books.values().stream()
                   .filter(book -> book.getAuthorId() == authorId)
                   .collect(Collectors.toList());
    }
    
    // Methods of the Author
    public static List<Author> getAllAuthors(){
        return new ArrayList<>(authors.values());
    }
    
    public static Author getAuthorById(int id){
        return authors.get(id);
    }
    
    public static Author addAuthor(Author author){
        author.setId(authorIdCounter++);
        authors.put(author.getId(), author);
        return author;
    }
    
    public static Author updateAuthor(Author author){
        if (authors.containsKey(author.getId())) {
            authors.put(author.getId(), author);
        }
        return author;
    }
    
    public static void deleteAuthor(int id) {
        authors.remove(id);
        // Removing author ID from books as well
        for (Book book : books.values()) {
            if (book.getAuthorId() == id) {
                // Setting an default value as for 0
                book.setAuthorId(0); 
            }
        }
    }
    
    // Methods of the cutomers
    public static List<Customer> getAllCustomers() {
        return new ArrayList<>(customers.values());
    }
    
    public static Customer getCustomerById(int id) {
        return customers.get(id);
    }
    
    public static Customer addCustomer(Customer customer) {
        customer.setId(customerIdCounter++);
        customers.put(customer.getId(), customer);
        // Creating a cart for the new customer
        Cart cart = new Cart(customer.getId());
        carts.put(customer.getId(), cart);
        return customer;
    }
    
    public static Customer updateCustomer(Customer customer) {
        if (customers.containsKey(customer.getId())) {
            customers.put(customer.getId(), customer);
        }
        return customer;
    }
    
    public static void deleteCustomer(int id) {
        customers.remove(id);
        // Removing relevant customer's cart
        carts.remove(id);
        // Removing relevant customer's orders
        orders.values().removeIf(order -> order.getCustomerId() == id);
    }
    
    // Methods of the cart
    public static Cart getCartByCustomerId(int customerId) {
        return carts.get(customerId);
    }
    
    public static void updateCart(Cart cart) {
        carts.put(cart.getCustomerId(), cart);
    }
    
    // Methods of the order
    public static Order createOrder(int customerId) {
        Cart cart = carts.get(customerId);
        if (cart != null && !cart.getItems().isEmpty()) {
            Order order = new Order();
            order.setId(orderIdCounter++);
            order.setCustomerId(customerId);
            order.setItems(new ArrayList<>(cart.getItems()));
            order.setTotal(cart.calculateTotal(getAllBooks()));
            
            orders.put(order.getId(), order);
            
            // Clearing the cart
            cart.getItems().clear();
            updateCart(cart);
            
            return order;
        }
        return null;
    }
    
    public static List<Order> getOrdersByCustomerId(int customerId) {
        return orders.values().stream()
                    .filter(order -> order.getCustomerId() == customerId)
                    .collect(Collectors.toList());
    }
    
    public static Order getOrderById(int orderId) {
        return orders.get(orderId);
    }
    
}

