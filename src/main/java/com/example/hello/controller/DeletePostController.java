package com.example.hello.controller;

import com.example.hello.entity.Post;
import com.example.hello.repository.PostRepository;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hello/DeletePost")
public class DeletePostController {

    @Autowired
    private PostRepository postRepository;

    @GetMapping
    public  String delete(@RequestParam("id") String id){
        Gson gson = new Gson();
        JsonObject responsejson = new JsonObject();
        responsejson.addProperty("message", "Error");
        try {
            Post post = postRepository.findById(Integer.parseInt(id)).get();
            postRepository.delete(post);
            responsejson.addProperty("success", true);
            responsejson.addProperty("message", "delete success");
        }catch (Exception e){

        }
        return gson.toJson(responsejson);
    }
}
