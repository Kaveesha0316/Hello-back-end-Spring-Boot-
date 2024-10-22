package com.example.hello.controller;

import com.example.hello.entity.User;
import com.example.hello.entity.User_status;
import com.example.hello.model.Validation;
import com.example.hello.repository.UserRepository;
import com.example.hello.repository.UserStatusRepository;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Date;
import java.util.Optional;

@RestController
@RequestMapping("/hello/signUp")
public class SignUpController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserStatusRepository userStatusRepository;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional
    public String signUp(
            @RequestParam("mobile") String mobile,
            @RequestParam("firstName") String firstName,
            @RequestParam("lastName") String lastName,
            @RequestParam("password") String password

    ) throws IOException {

        JsonObject responseJson = new JsonObject();
        responseJson.addProperty("message", "error");

        if (firstName.isEmpty()) {
            responseJson.addProperty("message", "fill first Name");
        } else if (lastName.isEmpty()) {
            responseJson.addProperty("message", "fill last Name");
        } else if (mobile.isEmpty()) {
            responseJson.addProperty("message", "fill mobile number");
        } else if (!Validation.isMobileNumberValid(mobile)) {
            responseJson.addProperty("message", "invalid mobile");
        } else if (password.isEmpty()) {
            responseJson.addProperty("message", "fill password");
        } else {

            Optional<User> existingUser = userRepository.findByMobile(mobile);
            if (existingUser.isPresent()) {
                responseJson.addProperty("message", "Mobile already exists");
                return responseJson.toString();
            }

            User user = new User();
            user.setFirst_name(firstName);
            user.setLast_name(lastName);
            user.setMobile(mobile);
            user.setPassword(password);
            user.setRegistered_date_time(new Date());

            User_status userStatus = userStatusRepository.findById(2)
                    .orElseThrow(() -> new RuntimeException("User status not found"));

            user.setUser_status(userStatus);

            userRepository.save(user);


            responseJson.addProperty("success", true);
            responseJson.addProperty("message", "Registration Complete");

            return responseJson.toString();
        }

        return responseJson.toString(); // Add return for the general flow
    }
}
