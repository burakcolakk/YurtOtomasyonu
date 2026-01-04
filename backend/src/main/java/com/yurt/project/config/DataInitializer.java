package com.yurt.project.config;

import com.yurt.project.entity.Role;
import com.yurt.project.entity.User;
import com.yurt.project.repository.RoleRepository;
import com.yurt.project.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import java.util.HashSet;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder; // Hashlemek için

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.count() == 0) { // Kullanıcı yoksa

            // Rolleri Oluştur
            Role adminRole = roleRepository.save(new Role("ROLE_ADMIN", "Yönetici"));
            roleRepository.save(new Role("ROLE_STUDENT", "Öğrenci"));
            roleRepository.save(new Role("ROLE_STAFF", "Personel"));

            // Admin Oluştur
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("12345")); // ARTIK HASH'Lİ!
            admin.setEmail("admin@yurt.com");

            Set<Role> roles = new HashSet<>();
            roles.add(adminRole);
            admin.setRoles(roles);

            userRepository.save(admin);
            System.out.println("--- Admin Oluşturuldu (Pass: 12345) ---");
        }
    }
}