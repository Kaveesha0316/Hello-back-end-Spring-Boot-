package com.example.hello.controller;

import com.example.hello.entity.Chat;
import com.example.hello.entity.Chat_status;
import com.example.hello.entity.User;
import com.example.hello.repository.ChatRepository;
import com.example.hello.repository.ChatStatusRepository;
import com.example.hello.repository.UserRepository;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@RequestMapping("/hello/SendChat")
public class SendChatController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ChatRepository chatRepository;

    @Autowired
    private ChatStatusRepository chatStatusRepository;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public String saveChat(@RequestParam("logged_user_id") String logged_user_id,@RequestParam("other_user_id") String other_user_id,@RequestParam("message") String message){
        Gson gson =  new Gson();
        JsonObject responseObject = new JsonObject();
        responseObject.addProperty("success", false);

        User logged_user = userRepository.findById(Integer.parseInt(logged_user_id)).orElse(null);
        User other_user = userRepository.findById(Integer.parseInt(other_user_id)).orElse(null);

        Chat_status chatStatus = chatStatusRepository.findById(2).get();

        Chat chat = new Chat();
        chat.setChat_staus(chatStatus);
        chat.setDate_time(new Date());
        chat.setFrom_user(logged_user);
        chat.setTo_user(other_user);
        chat.setMessage(message);

        chatRepository.save(chat);
        responseObject.addProperty("success", true);
        return  gson.toJson(responseObject);
    }
}
