package com.web.spring.gameshop.controller;

import com.web.spring.gameshop.entity.Details;
import com.web.spring.gameshop.entity.Role;
import com.web.spring.gameshop.entity.User;
import com.web.spring.gameshop.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;

@Controller
public class MainController {

    @Autowired
    private UserRepository userRepository;
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

    @GetMapping("/info")
    public String getUserInfo(Principal principal, Model userDetailsModel, Model userModel) {
        User user = userRepository.findByLogin(principal.getName());
        Details details = user.getDetails();
        userDetailsModel.addAttribute("userInfo", details);
        userModel.addAttribute("user", user);

        return "info";
    }

    @GetMapping("/info/edit")
    public String getEditPage(Principal principal, Model userDetailsModel, Model userModel) {
        User user = userRepository.findByLogin(principal.getName());
        Details details = user.getDetails();
        userDetailsModel.addAttribute("userDetails", details);
        userModel.addAttribute("user", user);

        return "edit";
    }

    @PostMapping("/info/edit")
    public String updateUser(Principal principal, Details details, User user) {
        User u = userRepository.findByLogin(principal.getName());
        u.getDetails().setName(details.getName());
        u.getDetails().setSurname(details.getSurname());
        u.getDetails().setAge(details.getAge());

//        u.setLogin(user.getLogin());
        u.setEmail(user.getEmail());

        userRepository.save(u);

        return "redirect:/info";
    }

    @GetMapping("/games")
    public String getAllGames() {
        return "games";
    }
}
