package com.directoTelmarkFormacion.plataforma_lms.controller;

import com.directoTelmarkFormacion.plataforma_lms.model.Lesson;
import com.directoTelmarkFormacion.plataforma_lms.model.Progress;
import com.directoTelmarkFormacion.plataforma_lms.model.User;
import com.directoTelmarkFormacion.plataforma_lms.repository.LessonRepository;
import com.directoTelmarkFormacion.plataforma_lms.repository.ProgressRepository;
import com.directoTelmarkFormacion.plataforma_lms.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;

@Controller
public class LessonController {

    @Autowired
    private LessonRepository lessonRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProgressRepository progressRepository;

    @PostMapping("/lesson/complete/{id}")
    public String completLessons(@PathVariable Long id, Principal principal) {

        Lesson lesson = lessonRepository.findById(id).orElseThrow();
        String email = principal.getName();
        User user = userRepository.findByEmail(email).orElseThrow();

        Progress progress = progressRepository.findByUserAndLesson(user, lesson)
                .orElse(new Progress(user, lesson, false));

        progress.setCompleted(!progress.isCompleted());
        progressRepository.save(progress);

        Long courseId = lesson.getModule().getCourse().getId();

        return "redirect:/courses/view/" + courseId;
    }
}
