package com.example.hello.controller;

import com.example.hello.entity.Post;
import com.example.hello.repository.ChatRepository;
import com.example.hello.repository.PostRepository;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import jakarta.servlet.ServletContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.List;

@RestController
@RequestMapping("/hello/LoadPost")
public class LoadPostController {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private ServletContext servletContext;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public String loadData(){
        Gson gson = new Gson();
        JsonObject responseJson = new JsonObject();
        responseJson.addProperty("message", "Unable to process");

        Sort sort = Sort.by(Sort.Order.desc("id"));

        List<Post> postList= postRepository.findAll(sort);

        JsonArray postArray = new JsonArray();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-dd-MM hh:mm a");

        for (Post post : postList) {
            JsonObject postObject = new JsonObject();
            postObject.addProperty("id", post.getId());
            postObject.addProperty("description", post.getDescription());
            postObject.addProperty("date_time", dateFormat.format(post.getDate_time()));
            postObject.addProperty("name", post.getUser().getFirst_name() + " " + post.getUser().getLast_name());
            postObject.addProperty("mobile", post.getUser().getMobile());
            postObject.addProperty("user_stutus", post.getUser().getUser_status().getId());




            String mobile = post.getUser().getMobile();

            String avatarImagePath = "src/main/resources/static/AvatarImage/" + mobile + ".png";

            File otherUserAvatarImageFile = new File(avatarImagePath);

            postObject.addProperty("avatar_image_found", otherUserAvatarImageFile.exists());

            if (!otherUserAvatarImageFile.exists()) {
                postObject.addProperty("other_user_avatar_letters",
                        post.getUser().getFirst_name().charAt(0) + "" + post.getUser().getLast_name().charAt(0));
            }


            postArray.add(postObject);
        }

        responseJson.addProperty("success", true);
        responseJson.addProperty("message", "success");
        responseJson.add("jsonPostarray", gson.toJsonTree(postArray));

        return gson.toJson(responseJson);

    }
}
