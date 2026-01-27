package com.directoTelmarkFormacion.plataforma_lms.repository;

import com.directoTelmarkFormacion.plataforma_lms.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface RoleRepository {
    Optional<Role> findByname(String name);
}
