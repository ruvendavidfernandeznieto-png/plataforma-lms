package com.directoTelmarkFormacion.plataforma_lms.controller;

import com.directoTelmarkFormacion.plataforma_lms.model.Course;
import com.directoTelmarkFormacion.plataforma_lms.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
        model.addAttribute("course", new Course());
        return "course-form";
    }

    @PostMapping("/save")
    public String guardarCurso(@ModelAttribute("course") Course course){
        courseService.guardarCurso(course);
        return "redirect:/courses";
    }

    @GetMapping("/edit/{id}")
    public String editarFormulario(@PathVariable Long id, Model model){
        Course cursoExistente = courseService.obtenerPorId(id).orElse(null);
        model.addAttribute("course", cursoExistente);
        return "course-form";
    }

    @GetMapping("/delete/{id}")
    public String eliminarCurso(@PathVariable Long id){
        courseService.borrarCurso(id);
        return "redirect:/courses";
    }

    @PostMapping("/{courseId}/add-module")
    public String addModule(@PathVariable Long courseId, @RequestParam("title") String title){
        courseService.guardarModulo(courseId, title);

        return "redirect:/courses/edit/" + courseId;
    }

    @PostMapping("/modules/{moduleId}/add-lesson")
    public String addLesson(@PathVariable Long moduleId,
                            @RequestParam("title") String title,
                            @RequestParam("file") MultipartFile file){
        try {
            courseService.guardarLeccion(moduleId, title, file);
        } catch (Exception e){
            e.printStackTrace();
        }
        return "redirect:/courses";
    }

    @GetMapping("/{courseId}/modules/delete/{moduleId}")
    public String borrarModulo(@PathVariable Long courseId, @PathVariable Long moduleId){
        courseService.borrarModulo(moduleId);

        return "redirect:/courses/edit/" + courseId;
    }

    @PostMapping("/{courseId}/modules/update/{moduleId}")
    public String actualizarModulo(@PathVariable Long courseId,
                                   @PathVariable Long moduleId,
                                   @RequestParam("title") String title){
        courseService.actualizarModulo(moduleId, title);

        return "redirect:/courses/edit/" + courseId;
    }
}