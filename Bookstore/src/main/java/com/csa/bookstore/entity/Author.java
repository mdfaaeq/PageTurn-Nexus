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
public class Author {
    
    private int id;
    private String name;
    private String biography;
    
    // Stores the ids of the books that has been writen by the author
    private List<Integer> bookIds;

    public Author() {
        this.bookIds = new ArrayList<>();
    }

    public Author(int id, String name, String biography) {
        this.id = id;
        this.name = name;
        this.biography = biography;
        this.bookIds = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBiography() {
        return biography;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

    public List<Integer> getBookIds() {
        return bookIds;
    }

    public void setBookIds(List<Integer> bookIds) {
        this.bookIds = bookIds;
    }
    
    public void addBookId(int bookId) {
        this.bookIds.add(bookId);
    }
    
    public void removeBookId(int bookId) {
        this.bookIds.remove(Integer.valueOf(bookId));
    }
    
}
