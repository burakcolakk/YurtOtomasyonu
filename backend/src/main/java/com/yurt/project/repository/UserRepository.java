package com.yurt.project.repository;

import com.yurt.project.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    // Kullanıcı adına göre bul (Login işlemi için)
    Optional<User> findByUsername(String username);

    // Kullanıcı var mı kontrolü (Register işlemi için)
    boolean existsByUsername(String username);
}