package com.example.hello.controller;

import com.example.hello.entity.User;
import com.example.hello.repository.UserRepository;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@RestController
@RequestMapping("/hello/ProfileUpdate")
public class ProfileUpDateController {

    @Autowired
    private UserRepository userRepository;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public String updateProfile(
            @RequestParam("mobile") String mobile,
            @RequestParam("firstName") String firstName,
            @RequestParam("lastName") String lastName,
            @RequestParam("avatarImage") MultipartFile avatarImage
    ) {

        Gson gson = new Gson();
        JsonObject responsejson = new JsonObject();
        responsejson.addProperty("message", "Error");

        if (mobile.isEmpty()) {
            responsejson.addProperty("message", "mobile is empty");
        } else if (firstName.isEmpty()) {
            responsejson.addProperty("message", "first Name is empty");
        } else if (lastName.isEmpty()) {
            responsejson.addProperty("message", "last Name is empty");
        } else {
            User user = userRepository.findByMobile(mobile).orElse(null);
            if (user == null) {
                responsejson.addProperty("message", "User not found");
                return gson.toJson(responsejson);
            }

            user.setFirst_name(firstName);
            user.setLast_name(lastName);

            // Save the avatar image if present
            if (avatarImage != null && !avatarImage.isEmpty()) {
                try {
                    // Define the path for the AvatarImage folder
                    Path avatarImagePath = Paths.get("src/main/resources/static/AvatarImage/" + mobile + ".png");

                    // Ensure the directories exist
                    Files.createDirectories(avatarImagePath.getParent());

                    // Save the file, replacing any existing file with the same name
                    Files.copy(avatarImage.getInputStream(), avatarImagePath, StandardCopyOption.REPLACE_EXISTING);

                } catch (IOException e) {
                    responsejson.addProperty("message", "Failed to save avatar image: " + e.getMessage());
                    return gson.toJson(responsejson);
                }
            }


            userRepository.save(user);
            responsejson.addProperty("success", true);
            responsejson.addProperty("message", "Profile updated successfully");
        }

        return gson.toJson(responsejson);
    }
}
