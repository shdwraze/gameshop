package com.web.spring.gameshop.controller;

import com.web.spring.gameshop.entity.*;
import com.web.spring.gameshop.repository.GameRepository;
import com.web.spring.gameshop.repository.OrderRepository;
import com.web.spring.gameshop.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
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
    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping({"/", ""})
    public String getUserInfo(Principal principal, Model model) {
        User user = userRepository.findByLogin(principal.getName());
        Details details = user.getDetails();

        model.addAttribute("userInfo", details);
        model.addAttribute("user", user);

        return "user-pa";
    }

    @GetMapping("/edit")
    public String getEditPage(Principal principal, Model model) {
        User user = userRepository.findByLogin(principal.getName());
        Details details = user.getDetails();
        model.addAttribute("userDetails", details);
        model.addAttribute("user", user);

        return "user-settings";
    }

    @PostMapping("/edit")
    public String updateUser(Principal principal, User user, BindingResult result) {
        User u = userRepository.findByLogin(principal.getName());

        if (!user.getPassword().equals("")) {
            if (!passwordEncoder.matches(user.getPassword(), u.getPassword())) {
                u.setPassword(passwordEncoder.encode(user.getPassword()));
            } else {
                ObjectError error = new ObjectError("globalError",
                        "The password must not be the same as the old one.");
                result.addError(error);
                System.out.println(result.hasErrors());
                return "user-settings";
            }
        }

        u.getDetails().setName(user.getDetails().getName());
        u.getDetails().setSurname(user.getDetails().getSurname());
        u.getDetails().setAge(user.getDetails().getAge());
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

        return "redirect:/info/basket";
    }

    @GetMapping("/history")
    public String showPurchaseHistory(Principal principal, Model model) {
        if (principal != null) {
            List<Order> orders = userRepository.findByLogin(principal.getName()).getOrders();
            if (!orders.isEmpty()) {

                if (orders.get(orders.size() - 1).getStatus() == Status.NOT_PAID) {
                    orders.remove(orders.get(orders.size() - 1));
                }

                model.addAttribute("orders", orders);
            } else {
                model.addAttribute("orders", null);
            }
        }

        return "purchase-history";
    }

    @GetMapping("/basket")
    public String showBasket(Principal principal, Model model) {
        if (principal != null) {
            List<Order> orders = userRepository.findByLogin(principal.getName()).getOrders();
            Order order = null;
            if (!orders.isEmpty()) {
                order = orders.get(orders.size() - 1);
            }

            if (order != null && order.getStatus() != Status.PAID) {
                int sum = 0;
                List<Game> games = order.getGames();

                for (Game game : games) {
                    sum += game.getPrice();
                }

                model.addAttribute("sum", sum);
                model.addAttribute("basket", order);
            } else {
                model.addAttribute("basket", null);
            }
        }

        return "basket";
    }
}
