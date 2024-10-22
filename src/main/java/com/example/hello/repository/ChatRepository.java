package com.example.hello.repository;

import com.example.hello.entity.Chat;
import com.example.hello.entity.Chat_status;
import com.example.hello.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ChatRepository extends JpaRepository<Chat, Integer> {

    @Query("SELECT c FROM Chat c " +
            "WHERE (c.from_user = :user1 AND c.to_user = :user2) " +
            "   OR (c.from_user = :user2 AND c.to_user = :user1) " +
            "ORDER BY c.date_time DESC") // Assuming date_time is the timestamp of the chat
    List<Chat> findLastChatBetweenUsers(@Param("user1") User user1, @Param("user2") User user2);

    @Query("SELECT c FROM Chat c " +
            "WHERE (c.from_user = :user1 AND c.to_user = :user2) " +
            "   OR (c.from_user = :user2 AND c.to_user = :user1) " +
            "ORDER BY c.date_time ASC") // Assuming date_time is the timestamp of the chat
    List<Chat> findChatBetweenUsers(@Param("user1") User user1, @Param("user2") User user2);

    @Query("SELECT c FROM Chat c WHERE c.from_user = :user3 AND c.to_user = :user4 AND c.chat_staus = :chatStatus")
    List<Chat> findUnseenChatCount(@Param("user3") User user3, @Param("user4") User user4, @Param("chatStatus") Chat_status chatStatus);
}
