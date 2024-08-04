package com.musala.services.booking.models;

import java.util.Date;

import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private int id;
    @Column
    private String name;
    @Column
    private String email;
    @Column
    private String password;
    @Column
    private String role = "user";

    @Column(name = "created_at")
    private Date createdOn;
    @Column(name = "updated_at")
    private Date updatedOn;
    @Column(name = "is_active")
    private boolean isActive;

    public User() {
        // Default constructor
    }

    public User(String name, String email, String password, String role) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public User( int id, String name, String email, String password, String role) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.id = id;
        this.role = role;
    }

    public User( int id, String name, String email, String password, String role, Date dateCreated, Date lastUpdated, boolean isActive) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.id = id;
        this.role = role;
        this.createdOn = dateCreated;
        this.updatedOn = lastUpdated;
        this.isActive = isActive;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public String getRole() {
        return role;
    }

    public int getId() {
        return id;
    }

    public Date getDateCreated() {
        return createdOn;
    }

    public Date getLastUpdated() {
        return updatedOn;
    }

    public Boolean getIsActive() {
        return isActive;
    }
}
