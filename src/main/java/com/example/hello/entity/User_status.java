package com.example.hello.entity;

import jakarta.persistence.*;

import java.io.Serializable;

@Entity
@Table(name = "user_status")
public class User_status implements Serializable {

    public  User_status(){}

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name",length = 20, nullable = false)
    private String name;

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
}
