package com.directoTelmarkFormacion.plataforma_lms.repository;

import com.directoTelmarkFormacion.plataforma_lms.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CourseRepository extends  JpaRepository<Course, Long> {
    List<Course> findByPublishedTrue();
    List<Course> findByTitleContainingIgnoreCase(String title);

}
