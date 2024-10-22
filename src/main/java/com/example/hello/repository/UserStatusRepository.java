package com.example.hello.repository;

import com.example.hello.entity.User_status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserStatusRepository  extends JpaRepository<User_status,Integer>{
    //    List<User_status> findbyname(String name);
}
