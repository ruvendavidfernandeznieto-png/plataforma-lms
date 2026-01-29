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
    private com.directoTelmarkFormacion.plataforma_lms.repository.CourseRepository courseRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception{
        System.out.println("---INICIANDO CARGA DE DATOS---");

        Role adminRole = roleRepository.findByName("ROLE_ADMIN").orElse(null);
        String emailAdmin = "admin@empresa.com";
        if (adminRole == null){
            adminRole = new Role("ROLE_ADMIN");
            roleRepository.save(adminRole);
        }

        if (!userRepository.existsByEmail(emailAdmin)) {
            User admin = new User();
            admin.setEmail(emailAdmin);
            admin.setPassword(passwordEncoder.encode("admin123")); // Contraseña: admin123
            admin.setRole(adminRole);
            admin.setActive(true);
            userRepository.save(admin);
            System.out.println(">>> Usuario ADMIN creado: " + emailAdmin);
        } else {
            User admin = userRepository.findByEmail(emailAdmin).get();
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRole(adminRole);
            admin.setActive(true);
            userRepository.save(admin);
            System.out.println(">>> Usuario ADMIN existente actualizado. Contraseña reseteada a: admin123");
        }
        System.out.println("--- CARGA DE DATOS FINALIZADA ---");
        if (courseRepository.count()==0){
            com.directoTelmarkFormacion.plataforma_lms.model.Course cursoJava = new com.directoTelmarkFormacion.plataforma_lms.model.Course();
            cursoJava.setTitle("Curso Intensivo de Java Spring Boot");
            cursoJava.setDescription("Aprende a crear aplicaciones web profesionales desde 0.");
            cursoJava.setPublished(true);

            courseRepository.save(cursoJava);
            System.out.println(">>> Curso de prueba Creado: Java Spring Boot");
        }
    }
}
