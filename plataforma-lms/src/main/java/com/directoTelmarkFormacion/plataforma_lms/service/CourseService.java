package com.directoTelmarkFormacion.plataforma_lms.service;

import com.directoTelmarkFormacion.plataforma_lms.model.Course;
import com.directoTelmarkFormacion.plataforma_lms.model.Module;
import com.directoTelmarkFormacion.plataforma_lms.model.Lesson;
import com.directoTelmarkFormacion.plataforma_lms.repository.CourseRepository;
import com.directoTelmarkFormacion.plataforma_lms.repository.ModuleRepository;
import com.directoTelmarkFormacion.plataforma_lms.repository.LessonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;

@Service
public class CourseService {

    @Autowired
    private CourseRepository courseRepository;


    @Autowired
    private ModuleRepository moduleRepository;

    @Autowired
    private LessonRepository lessonRepository;

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
    public void guardarModulo(Long courseId, String titulo) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Curso no encontrado"));

        Module modulo = new Module();
        modulo.setTitle(titulo);
        modulo.setCourse(course);

        int orden = (course.getModules() != null) ? course.getModules().size() + 1 : 1;
        modulo.setOrderIndex(orden);

        moduleRepository.save(modulo);
    }
    public void guardarLeccion(Long moduleId, String titulo,MultipartFile file) throws IOException{
        Module modulo = moduleRepository.findById(moduleId)
                .orElseThrow(() -> new RuntimeException("Módulo no encontrado"));
        Lesson lesson = new Lesson();
        lesson.setTitle(titulo);
        lesson.setModule(modulo);

        int ordenar = (modulo.getLessons()!=null) ? modulo.getLessons().size() + 1 : 1;
        lesson.setOrderIndex(ordenar);

        if (file != null && !file.isEmpty()){
            String fileName = StringUtils.cleanPath(file.getOriginalFilename());
            String uniqueFileName = System.currentTimeMillis() + "_" + fileName;

            lesson.setFileUrl(uniqueFileName);
            lesson.setFileType(file.getContentType());
            lesson.setContentType("FILE");

            String uploadDir = "uploads/";
            Path uploadPath = Paths.get(uploadDir);

            if (!Files.exists(uploadPath)){
                Files.createDirectories(uploadPath);
            }
            try (InputStream inputStream = file.getInputStream()){
                Path filePath = uploadPath.resolve(uniqueFileName);
                Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
            }
        }
        lessonRepository.save(lesson);
    }
    public void borrarModulo(Long moduleId){
        moduleRepository.deleteById(moduleId);
    }
    public void actualizarModulo(Long moduleId, String newTitle){
        Module module = moduleRepository.findById(moduleId)
                .orElseThrow(() -> new RuntimeException("Módulo no encontrado"));
        module.setTitle(newTitle);
        moduleRepository.save(module);
    }
}
