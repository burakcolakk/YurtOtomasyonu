package com.yurt.project.service;

import com.yurt.project.entity.User;
import com.yurt.project.entity.Role;
import java.util.List;

public interface UserService {
    User saveUser(User user);
    Role saveRole(Role role);
    void addRoleToUser(String username, String roleName);
    User getUser(String username);
    List<User> getUsers();
}