package com.directoTelmarkFormacion.plataforma_lms.loader;

import com.directoTelmarkFormacion.plataforma_lms.model.Role;
import com.directoTelmarkFormacion.plataforma_lms.model.User;
import com.directoTelmarkFormacion.plataforma_lms.repository.RoleRepository;
import com.directoTelmarkFormacion.plataforma_lms.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import java.util.Optional;

@Component
public class DataLoader implements CommandLineRunner {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception{
        System.out.println("---INICIANDO CARGA DE DATOS---");

        Role adminRole = roleRepository.findByname("ROLE_ADMIN").orElse(null);
        String emailAdmin = "admin@empresa.com";
        if (adminRole == null){
            adminRole = new Role("ROLE_ADMIN");
            roleRepository.save(adminRole);
        }

        if (!userRepository.existsByEmail(emailAdmin)) {
            // SI NO EXISTE: Lo crea de cero
            User admin = new User();
            admin.setEmail(emailAdmin);
            admin.setPassword(passwordEncoder.encode("admin123")); // Contraseña: admin123
            admin.setFullName("Administrador del Sistema");
            admin.setRole(adminRole);
            admin.setActive(true);
            userRepository.save(admin);
            System.out.println(">>> Usuario ADMIN creado: " + emailAdmin);
        } else {
            User admin = userRepository.findByEmail(emailAdmin).get();
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRole(adminRole); // Nos aseguramos de que sea admin
            admin.setActive(true);    // Nos aseguramos de que esté activo
            userRepository.save(admin);
            System.out.println(">>> Usuario ADMIN existente actualizado. Contraseña reseteada a: admin123");
        }
        System.out.println("--- CARGA DE DATOS FINALIZADA ---");
    }
}
