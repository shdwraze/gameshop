package com.web.spring.gameshop.controller;

import com.web.spring.gameshop.entity.*;
import com.web.spring.gameshop.repository.GameRepository;
import com.web.spring.gameshop.repository.GenresRepository;
import com.web.spring.gameshop.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/games")
public class GameController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private GameRepository gameRepository;
    @Autowired
    private GenresRepository genresRepository;

    @GetMapping({"", "/"})
    public String getAllGames(Model model,
                              @RequestParam(name = "selectGenre", required = false) String name) {
        List<Game> games;
        if (name != null) {
            games = gameRepository.findAllByGenres(genresRepository.findByName(name));
        } else {
            games = gameRepository.findAll();
        }
        List<Genre> genres = genresRepository.findAll();
        model.addAttribute("games", games);
        model.addAttribute("genres", genres);

        return "games";
    }

    @GetMapping("/{id}")
    public String getGameInfo(@PathVariable int id, Model model, Principal principal) {
        Game game = gameRepository.findById(id);
        User user = userRepository.findByLogin(principal.getName());
        List<Order> orders = user.getOrders();

        boolean isBought = false;
        for (Order order : orders) {
            List<Game> games = order.getGames();

            for (Game currentGame : games) {
                if (game == currentGame) {
                    isBought = true;
                    break;
                }
            }

            if (isBought) {
                break;
            }
        }

        model.addAttribute("game", game);
        model.addAttribute("gameGenres", game.getGenres());
        model.addAttribute("gamePlatforms", game.getPlatforms());
        model.addAttribute("sys", game.getSystemRequirements());
        model.addAttribute("isBought", isBought);

        return "game-info";
    }

    @PostMapping("/{id}")
    public String buyGame(@PathVariable int id, Principal principal) {
        User user = userRepository.findByLogin(principal.getName());
        List<Order> orders = user.getOrders();
        Order order;

        if (orders.isEmpty()) {
            order = new Order();
            List<Game> games = new ArrayList<>();

            order.setGames(games);
            order.setUser(user);
            order.setStatus(Status.NOT_PAID);
            orders.add(order);
        } else {
            order = orders.get(orders.size() - 1);
        }

        if (order.getStatus() != Status.PAID) {
            List<Game> games = order.getGames();
            games.add(gameRepository.findById(id));
        } else {
            Order newOrder = new Order();
            List<Game> games = new ArrayList<>();
            games.add(gameRepository.findById(id));

            newOrder.setGames(games);
            newOrder.setUser(user);
            newOrder.setStatus(Status.NOT_PAID);
            orders.add(newOrder);
        }

        userRepository.save(user);

        return "redirect:/info";
    }

}
