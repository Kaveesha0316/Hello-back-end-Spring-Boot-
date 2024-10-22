package com.example.hello.controller;

import com.example.hello.entity.Chat;
import com.example.hello.entity.Chat_status;
import com.example.hello.entity.User;
import com.example.hello.repository.ChatRepository;
import com.example.hello.repository.ChatStatusRepository;
import com.example.hello.repository.UserRepository;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.List;

@RestController
@RequestMapping("/hello/LoadChat")
public class LoadChatController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ChatRepository chatRepository;

    @Autowired
    private ChatStatusRepository chatStatusRepository;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public String loadChat(@RequestParam("logged_user_id") String logged_user_id ,@RequestParam("other_user_id") String other_user_id ){

        Gson gson =  new Gson();

        User logged_user = userRepository.findById(Integer.parseInt(logged_user_id)).orElse(null);
        User other_user = userRepository.findById(Integer.parseInt(other_user_id)).orElse(null);


        List<Chat> chatList = chatRepository.findChatBetweenUsers(logged_user,other_user);

        Chat_status chatStatus = chatStatusRepository.findById(1).get();

        JsonArray chatArray = new JsonArray();

        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm a");

        for (Chat chat: chatList ){
            JsonObject chatObject = new JsonObject();
            chatObject.addProperty("message", chat.getMessage());
            chatObject.addProperty("id", chat.getId());
            chatObject.addProperty("date_time", dateFormat.format(chat.getDate_time()));

            if (chat.getFrom_user().getId() == other_user.getId()) {

                //add side to chat object
                chatObject.addProperty("side", "left");

                //get only unseen chats
                if (chat.getChat_staus().getId() == 2) {

                    //update chat status to seen
                    chat.setChat_staus(chatStatus);
                   chatRepository.save(chat);

                }

            } else {
                //get chat from logged user

                //add side to chat object
                chatObject.addProperty("side", "right");
                chatObject.addProperty("status", chat.getChat_staus().getId()); //1 = seen, 2 = unseen

            }
            //add chat object into chatArray
            chatArray.add(chatObject);
        }
        return gson.toJson(chatArray);
    }
}
