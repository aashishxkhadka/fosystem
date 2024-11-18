/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ckf.pds.model;

/**
 *
 * @author Sanjok
 */
import java.io.Serializable;
import java.util.Date;

public class Feedback implements Serializable {


    private int id;
    private int userId;
    private String title;
    private String description;
    private Date date;

    // Constructors
    public Feedback() {
    }
    public Feedback (int userId,String title,String description,Date date){
        
        this.userId = userId;
        this.title = title;
        this.description = description;
        this.date = date;
    }
   
    public Feedback (int id, int userId,String title,String description,Date date){
        this.id = id;
        this.userId = userId;
        this.title = title;
        this.description = description;
        this.date = date;
    }
    

    
    // Getters and Setters
        public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    // Other methods as needed
}

