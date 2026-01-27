package com.directoTelmarkFormacion.plataforma_lms.controller;

import com.directoTelmarkFormacion.plataforma_lms.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/courses")
public class CourseController {
    @Autowired
    private CourseService courseService;

    @GetMapping
    public String listarCursos(Model model){
        model.addAttribute("cursos", courseService.obtenerTodos());
        return "course-list";
    }
}
