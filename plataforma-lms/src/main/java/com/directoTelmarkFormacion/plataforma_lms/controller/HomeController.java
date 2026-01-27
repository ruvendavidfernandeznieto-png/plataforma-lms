package com.directoTelmarkFormacion.plataforma_lms.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {
    @GetMapping("/")
    public String home(){
        return "Prueba de funcionamiento :D";
    }
}
