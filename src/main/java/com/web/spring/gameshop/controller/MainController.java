package com.web.spring.gameshop.controller;

import com.web.spring.gameshop.entity.*;
import com.web.spring.gameshop.repository.GameRepository;
import com.web.spring.gameshop.repository.GenresRepository;
import com.web.spring.gameshop.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.List;

@Controller
public class MainController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private GenresRepository genresRepository;
    @Autowired
    private GameRepository gameRepository;

//    @GetMapping({"/", ""})
//    public String index(Model model) {
//        List<Genre> genres = genresRepository.findAll();
//        List<Game> games = gameRepository.findAll();
//        model.addAttribute("genres", genres);
//        model.addAttribute("games", games);
//
//        return "index";
//    }

    @GetMapping({"", "/"})
    public String index(Model model,
                              @RequestParam(name = "genre", required = false) String name) {
        List<Game> games;
        if (name != null && name.length() > 0) {
            games = gameRepository.findAllByGenres(genresRepository.findByName(name));
        } else {
            games = gameRepository.findAll();
        }
        List<Genre> genres = genresRepository.findAll();
        model.addAttribute("games", games);
        model.addAttribute("genres", genres);

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

    @GetMapping("/pay")
    public String payment(Principal principal, Model model) {
        User user = userRepository.findByLogin(principal.getName());
        List<Order> orders = user.getOrders();
        Order order = orders.get(orders.size() - 1);

        int sum = 0;
        for (Game game : order.getGames()) {
            sum += game.getPrice();
        }

        model.addAttribute("order", order);
        model.addAttribute("sum", sum);

        return "payment";
    }

    @PostMapping("/pay")
    public String successPayment(Principal principal) {
        User user = userRepository.findByLogin(principal.getName());
        List<Order> orders = user.getOrders();
        Order order = orders.get(orders.size() - 1);
        order.setStatus(Status.PAID);
        userRepository.save(user);

        return "redirect:/info";
    }
}
