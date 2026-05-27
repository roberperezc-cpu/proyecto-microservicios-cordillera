package com.group_cordillera.controller;

import com.group_cordillera.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;

@RestController
@RequestMapping("api/v1/posts")
@CrossOrigin(origins = "*") // Habilitado para comunicación con tu Frontend
public class PostController {

    @Autowired
    public PostService postService;

    @GetMapping
    public String obtenerPost(){
        return postService.obtenerPost();
    }
}
