package com.yurt.project.config;

import com.yurt.project.entity.Building;
import com.yurt.project.entity.Role;
import com.yurt.project.entity.Room;
import com.yurt.project.entity.User;
import com.yurt.project.repository.BuildingRepository;
import com.yurt.project.repository.RoleRepository;
import com.yurt.project.repository.RoomRepository;
import com.yurt.project.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;

@Component
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BuildingRepository buildingRepository;
    private final RoomRepository roomRepository;
    private final PasswordEncoder passwordEncoder;

    public DataSeeder(UserRepository userRepository, RoleRepository roleRepository,
                      BuildingRepository buildingRepository, RoomRepository roomRepository,
                      PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.buildingRepository = buildingRepository;
        this.roomRepository = roomRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        // 1. ROLLERİ OLUŞTUR
        createRoleIfNotFound("ROLE_ADMIN", "Sistem Yöneticisi");
        createRoleIfNotFound("ROLE_USER", "Standart Kullanıcı");

        // 2. ADMIN KULLANICISINI OLUŞTUR
        if (!userRepository.existsByUsername("admin")) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("password")); // Şifre: password
            admin.setEmail("admin@yurt.com");
            admin.setActive(true);

            // Admin rolünü bul ve ata
            Optional<Role> adminRole = roleRepository.findByName("ROLE_ADMIN");
            adminRole.ifPresent(role -> admin.setRoles(new HashSet<>(Collections.singletonList(role))));

            userRepository.save(admin);
            System.out.println("✅ ADMIN KULLANICISI OLUŞTURULDU (Kullanıcı: admin, Şifre: password)");
        }

        // 3. VARSAYILAN BİNAYI OLUŞTUR (A Blok)
        Building building = buildingRepository.findByName("A Blok").orElseGet(() -> {
            Building newBuilding = new Building();
            newBuilding.setName("A Blok");
            newBuilding.setAddress("Merkez Kampüs");
            newBuilding.setTotalFloors(5);
            return buildingRepository.save(newBuilding);
        });

        // 4. VARSAYILAN ODAYI OLUŞTUR (Oda 101)
        if (roomRepository.findByRoomNumber("101").isEmpty()) {
            Room room = new Room();
            room.setRoomNumber("101");
            room.setFloorNumber(1);
            room.setCapacity(3);
            room.setCurrentOccupancy(0);
            room.setBuilding(building); // Odayı binaya bağla
            roomRepository.save(room);
            System.out.println("✅ A BLOK ve ODA 101 OLUŞTURULDU");
        }
    }

    private void createRoleIfNotFound(String name, String description) {
        if (roleRepository.findByName(name).isEmpty()) {
            Role role = new Role();
            role.setName(name);
            role.setDescription(description);
            roleRepository.save(role);
        }
    }
}