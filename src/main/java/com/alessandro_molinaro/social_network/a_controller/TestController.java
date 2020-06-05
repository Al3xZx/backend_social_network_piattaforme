package com.alessandro_molinaro.social_network.a_controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api")
public class TestController {

    @GetMapping(value = "/prova")
    public String test(@RequestParam(name = "nome") String n){
        return "ciao "+n;
    }
}
