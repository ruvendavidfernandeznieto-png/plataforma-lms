package com.directoTelmarkFormacion.plataforma_lms.service;

import com.directoTelmarkFormacion.plataforma_lms.model.Course;
import com.directoTelmarkFormacion.plataforma_lms.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CourseService {

    @Autowired
    private CourseRepository courseRepository;

    public List<Course> obtenerTodos(){
        return courseRepository.findAll();
    }

    public List<Course> obtenerPublicados(){
        return courseRepository.findByPublishedTrue();
    }

    public void guardarCurso(Course course){
        courseRepository.save(course);
    }

    public Optional<Course> obtenerPorId(Long id){
        return courseRepository.findById(id);
    }

    public void borrarCurso(Long id){
        courseRepository.deleteById(id);
    }
}
