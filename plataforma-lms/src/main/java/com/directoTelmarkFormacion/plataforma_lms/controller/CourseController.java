package com.directoTelmarkFormacion.plataforma_lms.controller;

import com.directoTelmarkFormacion.plataforma_lms.model.*;
import com.directoTelmarkFormacion.plataforma_lms.model.Module;
import com.directoTelmarkFormacion.plataforma_lms.service.CourseService;
import com.directoTelmarkFormacion.plataforma_lms.service.UserService;
import com.directoTelmarkFormacion.plataforma_lms.repository.ProgressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.*;

@Controller
@RequestMapping("/courses")
public class CourseController {

    @Autowired
    private CourseService courseService;

    @Autowired
    private UserService userService;

    @Autowired
    private ProgressRepository progressRepository;

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
        // Redirigimos al edit del curso creado/guardado para seguir añadiendo temas
        if(false) {
            return "redirect:/courses/edit/" + course.getId();
        }
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

    @GetMapping("/{courseId}/modules/delete/{moduleId}")
    public String borrarModulo(@PathVariable("courseId") Long courseId,
                               @PathVariable("moduleId") Long moduleId){

        courseService.borrarModulo(moduleId);

        if (courseId == null) {
            return "redirect:/courses";
        }
        return "redirect:/courses/edit/" + courseId;
    }

    @PostMapping("/{courseId}/modules/update/{moduleId}")
    public String actualizarModulo(@PathVariable Long courseId,
                                   @PathVariable Long moduleId,
                                   @RequestParam("title") String title){
        courseService.actualizarModulo(moduleId, title);
        return "redirect:/courses/edit/" + courseId;
    }

    @GetMapping("/{courseId}/modules/toggle-visibility/{moduleId}")
    public String toggleVisibilidad(@PathVariable("courseId") Long courseId,
                                    @PathVariable("moduleId") Long moduleId) {
        if(courseId == null || moduleId == null){
            System.out.println("ERROR: ID nulo al intentar cambiar visibilidad");
            return "redirect:/courses";
        }
        courseService.toggleVisibilidadModulo(moduleId);
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

    @GetMapping("/{courseId}/lessons/delete/{lessonId}")
    public String borrarLeccion(@PathVariable("courseId") Long courseId,
                                @PathVariable("lessonId") Long lessonId) {
        if (courseId == null) {
            System.out.println("ERROR CRÍTICO: El courseId llegó nulo al borrar lección.");
            return "redirect:/courses";
        }
        courseService.borrarLeccion(lessonId);
        return "redirect:/courses/edit/" + courseId;
    }

    @GetMapping("/view/{id}")
    public String verCurso(@PathVariable Long id, Model model, Principal principal) {
        Course course = courseService.obtenerPorId(id)
                .orElseThrow(() -> new RuntimeException("Curso no encontrado"));

        String email = principal.getName();
        User user = userService.buscarPorEmail(email).orElseThrow();

        List<Lesson> todasLasLecciones = new ArrayList<>();
        if (course.getModules() != null) {
            for (Module m : course.getModules()) {
                if (m.getLessons() != null) {
                    todasLasLecciones.addAll(m.getLessons());
                }
            }
            todasLasLecciones.sort(Comparator.comparingInt(Lesson::getOrderIndex));
        }

        Map<Long, Boolean> estadoLecciones = new HashMap<>();
        int completadas = 0;

        for (Lesson lesson : todasLasLecciones) {
            boolean isCompleted = progressRepository.findByUserAndLesson(user, lesson)
                    .map(Progress::isCompleted)
                    .orElse(false);

            estadoLecciones.put(lesson.getId(), isCompleted);

            if (isCompleted) {
                completadas++;
            }
        }

        int totalLecciones = todasLasLecciones.size();
        int porcentaje = (totalLecciones > 0) ? (completadas * 100 / totalLecciones) : 0;

        model.addAttribute("curso", course);
        model.addAttribute("lecciones", todasLasLecciones);
        model.addAttribute("estadoLecciones", estadoLecciones);
        model.addAttribute("porcentaje", porcentaje);

        return "course-view";
    }
}