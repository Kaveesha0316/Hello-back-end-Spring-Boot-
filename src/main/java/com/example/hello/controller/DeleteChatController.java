package com.example.hello.controller;

import com.example.hello.entity.Chat;
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

@RestController
@RequestMapping("/hello/DeleteMessage")
public class DeleteChatController {


    @Autowired
    private ChatRepository chatRepository;



    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public String deleteMessage(@RequestParam("id") String id,@RequestParam("side") String side,@RequestParam("msg") String msg){
        Gson gson = new Gson();
        JsonObject responseJson = new JsonObject();

        if (side.equals("right")) {

            if (msg.equals("You deleted this message")) {

//                Chat chat = chatRepository.findById(Integer.parseInt(id)).get();
                chatRepository.deleteById(Integer.parseInt(id));
                responseJson.addProperty("message", "delete");
                responseJson.addProperty("success", true);

            } else {
                Chat chat = chatRepository.findById(Integer.parseInt(id)).get();
                chat.setMessage("You deleted this message");

                chatRepository.save(chat);

                responseJson.addProperty("message", "updeted");
                responseJson.addProperty("success", true);

            }

        } else {

//            Chat chat = chatRepository.findById(Integer.parseInt(id)).get();
            chatRepository.deleteById(Integer.parseInt(id));
            responseJson.addProperty("message", "other user delete");
            responseJson.addProperty("success", true);
        }
        return gson.toJson(responseJson);
    }
}
