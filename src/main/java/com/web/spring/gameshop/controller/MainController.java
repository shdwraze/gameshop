package com.web.spring.gameshop.controller;

import com.web.spring.gameshop.entity.*;
import com.web.spring.gameshop.repository.GameRepository;
import com.web.spring.gameshop.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;
import java.util.List;

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

    @GetMapping("/info")
    public String getUserInfo(Principal principal, Model model) {
        User user = userRepository.findByLogin(principal.getName());
        Details details = user.getDetails();
        List<Order> orders = user.getOrders();
        model.addAttribute("userInfo", details);
        model.addAttribute("user", user);
        model.addAttribute("userOrders", orders);

        return "info";
    }

    @GetMapping("/info/edit")
    public String getEditPage(Principal principal, Model model) {
        User user = userRepository.findByLogin(principal.getName());
        Details details = user.getDetails();
        model.addAttribute("userDetails", details);
        model.addAttribute("user", user);

        return "edit";
    }

    @PostMapping("/info/edit")
    public String updateUser(Principal principal, Details details, User user) {
        User u = userRepository.findByLogin(principal.getName());
        u.getDetails().setName(details.getName());
        u.getDetails().setSurname(details.getSurname());
        u.getDetails().setAge(details.getAge());
        u.setEmail(user.getEmail());

        userRepository.save(u);

        return "redirect:/info";
    }

    @GetMapping("/games")
    public String getAllGames(Model model) {
        List<Game> games = gameRepository.findAll();
        model.addAttribute("games", games);

        return "games";
    }

    @GetMapping("/games/{id}")
    public String getGameInfo(@PathVariable int id, Model model) {
        Game game = gameRepository.findById(id);
        model.addAttribute("game", game);
        model.addAttribute("gameGenres", game.getGenres());
        model.addAttribute("gamePlatforms", game.getPlatforms());
        model.addAttribute("sys", game.getSystemRequirements());

        return "game-info";
    }
}
