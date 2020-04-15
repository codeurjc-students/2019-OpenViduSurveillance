package com.example.controller;


import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@CrossOrigin
@RestController
public class Controller {

    @GetMapping("/saludo")
    public String index() {
        return "Funcionando";
    }


    @RequestMapping("/newSession")
    public void start() {

    }
}
