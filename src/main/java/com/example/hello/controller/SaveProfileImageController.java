package com.example.hello.controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@RestController
@RequestMapping("/hello/SaveProfileImage")
public class SaveProfileImageController {

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE , produces = MediaType.APPLICATION_JSON_VALUE)
    public String saveImage(
            @RequestParam("mobile") String mobile,
            @RequestParam("avatarImage")MultipartFile avatarImage
    ){
        Gson gson = new Gson();
        JsonObject responsejson = new JsonObject();
        responsejson.addProperty("message", "Error");

        if (mobile.isEmpty()) {
            responsejson.addProperty("message", "no mobile number");
        } else {
//             Save the avatar image if present
            if (avatarImage != null && !avatarImage.isEmpty()) {
                try {
                    // Define the path with the project name "hello" and the AvatarImage folder
                    Path avatarImagePath = Paths.get("src/main/resources/static/hello/AvatarImage/" + mobile + ".png");

// Ensure the directories exist
                    Files.createDirectories(avatarImagePath.getParent());

// Save the file
                    Files.copy(avatarImage.getInputStream(), avatarImagePath, StandardCopyOption.REPLACE_EXISTING);

                    responsejson.addProperty("success", true);
                    responsejson.addProperty("message", "image uploaded");
                } catch (IOException e) {
                    responsejson.addProperty("message", "Failed to save avatar image: " + e.getMessage());

                }
            }
        }
        return gson.toJson(responsejson);
    }
}
