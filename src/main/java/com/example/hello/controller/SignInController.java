package com.example.hello.controller;

import com.example.hello.dto.SignInRequest;
import com.example.hello.entity.User;
import com.example.hello.repository.UserRepository;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Optional;

@RestController
@RequestMapping("/hello/signIn")
public class SignInController {

    @Autowired
    private UserRepository userRepository;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE , produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional
    public String SignIn(@RequestBody SignInRequest signInRequest)throws IOException {


        JsonObject responseJson = new JsonObject();
        responseJson.addProperty("message","Error");
        Gson gson = new Gson();

        String mobile = signInRequest.getMobile();
        String password = signInRequest.getPassword();

        if (mobile.isEmpty()) {
            responseJson.addProperty("message", "please enter mobile number");
            return responseJson.toString();
        } else if (password.isEmpty()) {
            responseJson.addProperty("message", "please enter password");
            return responseJson.toString();
        } else {

           Optional<User> userOptional = userRepository.findByMobileAndPassword(mobile,password);
           if (userOptional.isPresent()){

              User user = userOptional.get();
               String userJson = gson.toJson(user);

               responseJson.add("user", gson.fromJson(userJson, JsonObject.class));
               responseJson.addProperty("success", true);
               responseJson.addProperty("message", "signin Complete");
               return responseJson.toString();
           }else{
               responseJson.addProperty("message", "Invalid details");

           }

        }
        return responseJson.toString();
    }
}
