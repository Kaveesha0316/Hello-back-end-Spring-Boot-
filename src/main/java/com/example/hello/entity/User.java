package com.example.hello.entity;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "user")
public class User implements Serializable {
    public User() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "mobile", length = 10, nullable = false)
    private String mobile;

    @Column(name = "first_name", length = 45, nullable = false)
    private String firstname;

    @Column(name = "last_name", length = 45, nullable = false)
    private String lastname;

    @Column(name = "password", length = 20, nullable = false)
    private String password;

    @Column(name = "registered_date_time", nullable = false)
    private Date registereddatetime;

    @ManyToOne
    @JoinColumn(name = "user_status_id")
    private User_status user_status;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getFirst_name() {
        return firstname;
    }

    public void setFirst_name(String first_name) {
        this.firstname = first_name;
    }

    public String getLast_name() {
        return lastname;
    }

    public void setLast_name(String last_name) {
        this.lastname = last_name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getRegistered_date_time() {
        return registereddatetime;
    }

    public void setRegistered_date_time(Date registered_date_time) {
        this.registereddatetime = registered_date_time;
    }

    public User_status getUser_status() {
        return user_status;
    }

    public void setUser_status(User_status user_status) {
        this.user_status = user_status;
    }
}
