package com.example.hello.controller;

import com.example.hello.entity.Post;
import com.example.hello.entity.User;
import com.example.hello.repository.PostRepository;
import com.example.hello.repository.UserRepository;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import jakarta.servlet.ServletContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Date;

@RestController
@RequestMapping("/hello/SavePost")
public class SavePostController {

    @Autowired
    private ServletContext servletContext;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public String savePost(
            @RequestParam("desc") String desc,
            @RequestParam("postImage") MultipartFile postImage,
            @RequestParam("user_id") String user_id
    ) {

        Gson gson = new Gson();
        JsonObject responsejson = new JsonObject();
        responsejson.addProperty("message", "Error");

        if (desc.isEmpty()) {
            responsejson.addProperty("message", "no description");
        } else if (postImage == null || postImage.isEmpty()) {
            responsejson.addProperty("message", "select Image");
        } else {

            // Retrieve the user based on user_id
            User user = userRepository.findById(Integer.parseInt(user_id)).orElse(null);

            if (user != null) {
                // Create and save the new post
                Post post = new Post();
                post.setDescription(desc);
                post.setDate_time(new Date());
                post.setUser(user);
                postRepository.save(post); // Saving to generate post ID

                try {
                    // Define the path for saving the image using the generated postId
                    String postId = String.valueOf(post.getId()); // Get the generated post ID
                    Path postImagePath = Paths.get("src/main/resources/static/PostImages/" + postId + ".png");

// Ensure the directories exist
                    Files.createDirectories(postImagePath.getParent());

// Save the image file, replacing any existing file with the same name
                    Files.copy(postImage.getInputStream(), postImagePath, StandardCopyOption.REPLACE_EXISTING);

                } catch (Exception e) {
                    e.printStackTrace();
                    responsejson.addProperty("message", "Error saving image");
                    return gson.toJson(responsejson);
                }

                // Return success with postId
                responsejson.addProperty("success", true);
                responsejson.addProperty("message", "Post saved successfully");
                responsejson.addProperty("postId", post.getId()); // Return the postId
            } else {
                responsejson.addProperty("message", "User not found");
            }
        }
        return gson.toJson(responsejson);
    }
}
