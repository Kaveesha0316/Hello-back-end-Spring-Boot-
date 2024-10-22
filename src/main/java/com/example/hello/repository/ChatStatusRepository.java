package com.example.hello.repository;

import com.example.hello.entity.Chat_status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatStatusRepository  extends JpaRepository<Chat_status,Integer> {
}
