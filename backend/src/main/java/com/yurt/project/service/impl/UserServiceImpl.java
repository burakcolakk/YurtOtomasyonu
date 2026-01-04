package com.yurt.project.service.impl;

import com.yurt.project.entity.Role;
import com.yurt.project.entity.User;
import com.yurt.project.exception.BusinessException;
import com.yurt.project.repository.RoleRepository;
import com.yurt.project.repository.UserRepository;
import com.yurt.project.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional // İlişkisel verileri yönettiğimiz için Transactional önemli
@Slf4j // Loglama için (Lombok)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Override
    public User saveUser(User user) {
        log.info("Yeni kullanıcı kaydediliyor: {}", user.getUsername());
        // Şimdilik şifreyi düz kaydediyoruz, Security adımında BCrypt ile hashleyeceğiz.
        return userRepository.save(user);
    }

    @Override
    public Role saveRole(Role role) {
        log.info("Yeni rol kaydediliyor: {}", role.getName());
        return roleRepository.save(role);
    }

    @Override
    public void addRoleToUser(String username, String roleName) {
        log.info("{} kullanıcısına {} rolü ekleniyor", username, roleName);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new BusinessException("Kullanıcı bulunamadı"));

        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new BusinessException("Rol bulunamadı"));

        user.getRoles().add(role);
        // Transactional anotasyonu sayesinde user.save() demeye gerek kalmadan DB güncellenir.
    }

    @Override
    public User getUser(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new BusinessException("Kullanıcı bulunamadı"));
    }

    @Override
    public List<User> getUsers() {
        return userRepository.findAll();
    }
}