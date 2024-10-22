package com.example.hello.controller;

import com.example.hello.entity.Chat;
import com.example.hello.entity.Chat_status;
import com.example.hello.entity.User;
import com.example.hello.entity.User_status;
import com.example.hello.repository.ChatRepository;
import com.example.hello.repository.ChatStatusRepository;
import com.example.hello.repository.UserRepository;
import com.example.hello.repository.UserStatusRepository;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import jakarta.servlet.ServletContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/hello/LoadHomeData")
public class LoadHomeDataController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserStatusRepository userStatusRepository;

    @Autowired
    private ChatRepository chatRepository;

    @Autowired
    private ChatStatusRepository chatStatusRepository;

    @Autowired
    private ServletContext servletContext;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public String loadData(@RequestParam("id") String userId, @RequestParam("searchtext") String name) {
        Gson gson = new Gson();
        JsonObject responseJson = new JsonObject();
        responseJson.addProperty("success", false);
        responseJson.addProperty("message", "Unable to process");

        User user = userRepository.findById(Integer.parseInt(userId)).orElse(null);
        if (user == null) {
            return responseJson.toString(); // Handle user not found
        }

        User_status userStatus = userStatusRepository.findById(1).orElse(null);
        if (userStatus == null) {
            return responseJson.toString(); // Handle user status not found
        }

        user.setUser_status(userStatus);
        userRepository.save(user);

        try {
            List<User> otherUsers = userRepository.findAllByIdNotAndFirstnameStartingWith(user.getId(), name);
            JsonArray jsonArray = new JsonArray();
            for (User otherUser : otherUsers) {
                List<Chat> chatList = chatRepository.findLastChatBetweenUsers(user, otherUser);
                JsonObject jsonchatItem = new JsonObject();
                jsonchatItem.addProperty("other_user_id", otherUser.getId());
                jsonchatItem.addProperty("other_user_mobile", otherUser.getMobile());
                jsonchatItem.addProperty("other_user_name", otherUser.getFirst_name() + " " + otherUser.getLast_name());
                jsonchatItem.addProperty("other_user_status", otherUser.getUser_status().getId());


                String mobile = otherUser.getMobile();

                String avatarImagePath = "src/main/resources/static/AvatarImage/" + mobile + ".png";

                File otherUserAvatarImageFile = new File(avatarImagePath);

                jsonchatItem.addProperty("avatar_image_found", otherUserAvatarImageFile.exists());

                if (!otherUserAvatarImageFile.exists()) {
                    jsonchatItem.addProperty("other_user_avatar_letters",
                            otherUser.getFirst_name().charAt(0) + "" + otherUser.getLast_name().charAt(0));
                }



                SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm a");

                if (!chatList.isEmpty()) {
                    // Found last chat
                    Chat chat = chatList.get(0); // Get the most recent chat
                    jsonchatItem.addProperty("message", chat.getMessage());
                    jsonchatItem.addProperty("dateTime", dateFormat.format(chat.getDate_time()));
                    jsonchatItem.addProperty("chat_status_id", chat.getChat_staus().getId()); // 1=seen, 2=unseen

                    if (chat.getFrom_user() == user) {
                        jsonchatItem.addProperty("tick_from", true);
                    }
                } else {
                    // No last chat
                    jsonchatItem.addProperty("message", "Let's start a new chat");
                    jsonchatItem.addProperty("dateTime", dateFormat.format(user.getRegistered_date_time()));
                    jsonchatItem.addProperty("chat_status_id", 1);
                }

                Chat_status chatStatus = chatStatusRepository.findById(2).orElse(null);
                if (chatStatus != null) {
                    List<Chat> unSeenChatList = chatRepository.findUnseenChatCount(otherUser, user, chatStatus);
                    int unseenMsgCount = unSeenChatList.size();
                    jsonchatItem.addProperty("unseen_message_count", unseenMsgCount);
                }

                // Add other relevant fields as needed
                jsonArray.add(jsonchatItem);
            }
            responseJson.add("jsonchatarray", gson.toJsonTree(jsonArray));
            responseJson.add("user", gson.toJsonTree(user));
            responseJson.addProperty("success", true);
            responseJson.addProperty("message", "success");

            return responseJson.toString();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return responseJson.toString();
    }
}
