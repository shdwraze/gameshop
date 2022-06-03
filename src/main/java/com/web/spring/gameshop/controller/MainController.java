package com.web.spring.gameshop.controller;

import com.web.spring.gameshop.entity.Details;
import com.web.spring.gameshop.entity.Role;
import com.web.spring.gameshop.entity.User;
import com.web.spring.gameshop.repository.GameRepository;
import com.web.spring.gameshop.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class MainController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private GameRepository gameRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping({"/", ""})
    public String index() {
        return "index";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String register(Model userModel, Model userDetailsModel) {
        User user = new User();
        Details details = new Details();
        userModel.addAttribute("user", user);
        userDetailsModel.addAttribute("userDetails", details);

        return "register";
    }

    @PostMapping("/register")
    public String addNewUser(User user, Details details) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(Role.USER);
        details.setUser(user);
        user.setDetails(details);
        userRepository.save(user);

        return "redirect:/";
    }
}
