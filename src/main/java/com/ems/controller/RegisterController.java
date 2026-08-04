package com.ems.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.ems.entity.User;
import com.ems.service.UserService;

@Controller
public class RegisterController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/register")
    public String showRegisterForm(Model model) {

        model.addAttribute("user", new User());

        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") User user,
                               Model model) {

        // Username already exists
        if (userService.usernameExists(user.getUsername())) {

            model.addAttribute("error",
                    "Username already exists.");

            return "register";
        }

        // Email already exists
        if (userService.emailExists(user.getEmail())) {

            model.addAttribute("error",
                    "Email already exists.");

            return "register";
        }

        // Encrypt password
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Default Role
        user.setRole("USER");

        // Enable account
        user.setEnabled(true);

        // Save user
        System.out.println("Saving user...");

        userService.saveUser(user);

        System.out.println("User saved.");
        
        return "redirect:/login?registered";
    }

}