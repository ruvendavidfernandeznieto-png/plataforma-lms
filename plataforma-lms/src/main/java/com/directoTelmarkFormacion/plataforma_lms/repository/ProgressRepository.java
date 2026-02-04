package com.directoTelmarkFormacion.plataforma_lms.repository;

import com.directoTelmarkFormacion.plataforma_lms.model.Lesson;
import com.directoTelmarkFormacion.plataforma_lms.model.Progress;
import com.directoTelmarkFormacion.plataforma_lms.model.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Optional;

public interface ProgressRepository extends JpaRepository<Progress, Long> {

    Optional<Progress> findByUserAndLesson(User user, Lesson lesson);

    @Query(value = "SELECT COUNT(*) FROM progress p " +
            "JOIN lessons l ON p.lesson_id = l.id " +
            "WHERE p.user_id = :userId AND l.course_id = :courseId AND p.completed = true",
            nativeQuery = true)
    long countCompletedLessons(@Param("userId") Long userId, @Param("courseId") Long courseId);

    @Transactional
    void deleteByLesson(Lesson lesson);
}

