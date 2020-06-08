package com.alessandro_molinaro.social_network.a_controller;

import com.alessandro_molinaro.social_network.b_service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/post")
public class PostController {

    @Autowired
    PostService postService;


}
