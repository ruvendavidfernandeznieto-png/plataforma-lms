package com.directoTelmarkFormacion.plataforma_lms.service;

import com.directoTelmarkFormacion.plataforma_lms.model.Role;
import com.directoTelmarkFormacion.plataforma_lms.model.User;
import com.directoTelmarkFormacion.plataforma_lms.repository.UserRepository;
import com.directoTelmarkFormacion.plataforma_lms.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;
import java.time.LocalDateTime;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<User> obtenerTodos(){
        return userRepository.findAll();
    }

    public User guardarUsuario(User usuario){
        return userRepository.save(usuario);
    }

    public Optional<User> buscarPorEmail(String email){
        return userRepository.findByEmail(email);
    }
    public boolean registrarUsuario(String email, String password){
        if (userRepository.findByEmail(email).isPresent()){
            return false;
        }
    User nuevoUsuario = new User();
    nuevoUsuario.setEmail(email);
    nuevoUsuario.setPassword(passwordEncoder.encode(password));
    nuevoUsuario.setActive(true);
    nuevoUsuario.setCreatedAt(LocalDateTime.now());

    Optional<Role> rolUsuario = roleRepository.findById(2L);
    if (rolUsuario.isPresent()){
        nuevoUsuario.setRole(rolUsuario.get());
    }else {
        return false;
    }
    userRepository.save(nuevoUsuario);
    return true;
    }
}
