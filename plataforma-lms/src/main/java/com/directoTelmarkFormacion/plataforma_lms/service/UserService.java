package com.directoTelmarkFormacion.plataforma_lms.service;

import com.directoTelmarkFormacion.plataforma_lms.model.Role;
import com.directoTelmarkFormacion.plataforma_lms.model.Course;
import com.directoTelmarkFormacion.plataforma_lms.model.User;
import com.directoTelmarkFormacion.plataforma_lms.repository.UserRepository;
import com.directoTelmarkFormacion.plataforma_lms.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository UserRepository;

    public List<User> obtenerTodos(){
        return UserRepository.findAll();
    }

    public User guardarUsuario(User usuario){
        return UserRepository.save(usuario);
    }

    public Optional<User> buscarPorEmail(String email){
        return UserRepository.findByEmail(email);
    }
}
