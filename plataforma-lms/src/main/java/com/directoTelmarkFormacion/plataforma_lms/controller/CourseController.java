package com.directoTelmarkFormacion.plataforma_lms.controller;

import com.directoTelmarkFormacion.plataforma_lms.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller; // <--- OJO: No puede ser @RestController
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/courses")
public class CourseController {

    @Autowired
    private CourseService courseService;

    @GetMapping
    public String listarCursos(Model model) {
       model.addAttribute("cursos", courseService.obtenerTodos());
       return "course-list";
    }

    @GetMapping("/new")
    public String mostrarFormulario(Model model){
        model.addAttribute("curso", new com.directoTelmarkFormacion.plataforma_lms.model.Course());
        return "course-form";
    }

    @org.springframework.web.bind.annotation.PostMapping("/save")
    public String guardarCurso(@org.springframework.web.bind.annotation.ModelAttribute("curso") com.directoTelmarkFormacion.plataforma_lms.model.Course course){
        courseService.guardarCurso(course);
        return "redirect:/courses";
    }

    @GetMapping("/edit/{id}")
    public String editarFormulario(@org.springframework.web.bind.annotation.PathVariable Long id, Model model){
        com.directoTelmarkFormacion.plataforma_lms.model.Course cursoExistente = courseService.obtenerPorId(id).orElse(null);
        model.addAttribute("curso", cursoExistente);
        return "course-form";
    }

    @GetMapping("/delete/{id}")
    public String eliminarCurso(@org.springframework.web.bind.annotation.PathVariable Long id){
        courseService.borrarCurso(id);
        return "redirect:/courses";
    }
}