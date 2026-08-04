package com.ems.service;

import java.util.List;

import com.ems.entity.User;

public interface UserService {

    User saveUser(User user);

    User getUserByUsername(String username);

    List<User> getAllUsers();

    boolean usernameExists(String username);

    boolean emailExists(String email);

}