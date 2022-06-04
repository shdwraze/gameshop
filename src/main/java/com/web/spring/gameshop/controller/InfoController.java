package com.web.spring.gameshop.controller;

import com.web.spring.gameshop.entity.*;
import com.web.spring.gameshop.repository.GameRepository;
import com.web.spring.gameshop.repository.OrderRepository;
import com.web.spring.gameshop.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/info")
public class InfoController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private GameRepository gameRepository;
    @Autowired
    private OrderRepository orderRepository;


    @GetMapping({"/", ""})
    public String getUserInfo(Principal principal, Model model) {
        User user = userRepository.findByLogin(principal.getName());
        Details details = user.getDetails();
        List<Order> orders = user.getOrders();
        int sum = 0;

        if (!orders.isEmpty()) {
            Order order = orders.get(orders.size() - 1);

            if (order.getStatus() == Status.NOT_PAID) {
                List<Game> games = order.getGames();

                for (Game game : games) {
                    sum += game.getPrice();
                }
            }
        }

        model.addAttribute("userInfo", details);
        model.addAttribute("user", user);
        model.addAttribute("userOrders", orders);
        model.addAttribute("sum", sum);

        return "info";
    }

    @GetMapping("/edit")
    public String getEditPage(Principal principal, Model model) {
        User user = userRepository.findByLogin(principal.getName());
        Details details = user.getDetails();
        model.addAttribute("userDetails", details);
        model.addAttribute("user", user);

        return "edit";
    }

    @PostMapping("/edit")
    public String updateUser(Principal principal, Details details, User user) {
        User u = userRepository.findByLogin(principal.getName());
        u.getDetails().setName(details.getName());
        u.getDetails().setSurname(details.getSurname());
        u.getDetails().setAge(details.getAge());
        u.setEmail(user.getEmail());

        userRepository.save(u);

        return "redirect:/info";
    }

    @PostMapping("/delete/{id}")
    public String deleteGameFromOrder(@PathVariable int id, Principal principal) {
        Game currentGame = gameRepository.findById(id);
        User user = userRepository.findByLogin(principal.getName());
        List<Order> orders = user.getOrders();
        Order order = orders.get(orders.size() - 1);
        List<Game> games = order.getGames();

        games.remove(currentGame);
        if (games.isEmpty()) {
            int orderID = order.getId();
            orders.remove(order);
            orderRepository.deleteById(orderID);
        }

        userRepository.save(user);

        return "redirect:/info";
    }
}
