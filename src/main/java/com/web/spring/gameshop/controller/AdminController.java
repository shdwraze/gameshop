package com.web.spring.gameshop.controller;

import com.web.spring.gameshop.entity.*;
import com.web.spring.gameshop.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private DeveloperRepository developerRepository;

    @Autowired
    private PublisherRepository publisherRepository;

    @Autowired
    private GenresRepository genresRepository;

    @Autowired
    private PlatformsRepository platformsRepository;

    @GetMapping({"", "/"})
    public String showAdminPanel() {
        Developer developer = developerRepository.findById(2);
        List<Game> games = developer.getGames();
        for (Game game : games) {
            System.out.println(game.getName());
        }

        return "admin";
    }

    @GetMapping("/games")
    public String gameManagement(Model model) {
        List<Game> games = gameRepository.findAll();
        model.addAttribute("games", games);

        return "game-management";
    }

    @GetMapping("/games/edit/{id}")
    public String updateGame(@PathVariable int id, Model model) {
        Game game = gameRepository.findById(id);
        SystemRequirements requirements = game.getSystemRequirements();

        List<Developer> developers = developerRepository.findAll();
        List<Publisher> publishers = publisherRepository.findAll();

        model.addAttribute("game", game);
        model.addAttribute("sys", requirements);
        model.addAttribute("gameGenres", game.getGenres());
        model.addAttribute("gamePlatforms", game.getPlatforms());

        model.addAttribute("developers", developers);
        model.addAttribute("publishers", publishers);

        return "game-edit";
    }

    @PostMapping("/games/edit/{id}")
    public String saveUpdateGame(@PathVariable int id, Game game, SystemRequirements requirements) {
        Game g = gameRepository.findById(id);
        Developer developer = developerRepository.findByName(game.getDeveloper().getName());
        Publisher publisher = publisherRepository.findByName(game.getPublisher().getName());

        g.setName(game.getName());
        g.setRelease(game.getRelease());
        g.setDescription(game.getDescription());
        g.setPrice(game.getPrice());
        g.setDeveloper(developer);
        g.setPublisher(publisher);

        g.getSystemRequirements().setProcessor(requirements.getProcessor());
        g.getSystemRequirements().setVideocard(requirements.getVideocard());
        g.getSystemRequirements().setDiskSize(requirements.getDiskSize());
        g.getSystemRequirements().setRamSize(requirements.getRamSize());

        List<Game> gamesPubl = publisher.getGames();
        List<Game> gamesDev = developer.getGames();
        gamesDev.add(g);
        gamesPubl.add(g);

        gameRepository.save(g);

        return "redirect:/admin/games";
    }

    @GetMapping("/add")
    public String addNewGame(Model model) {
        Game game = new Game();
        SystemRequirements requirements = new SystemRequirements();
        List<Developer> developers = developerRepository.findAll();
        List<Publisher> publishers = publisherRepository.findAll();
        List<Genre> genres = genresRepository.findAll();
        List<Platform> platforms = platformsRepository.findAll();

        model.addAttribute("game", game);
        model.addAttribute("sys", requirements);
        model.addAttribute("developers", developers);
        model.addAttribute("publishers", publishers);
        model.addAttribute("genres", genres);
        model.addAttribute("platforms", platforms);

        return "admin-add-game";
    }

    @PostMapping("/add")
    public String saveNewGame(@RequestParam(name = "dev") String devName,
                              @RequestParam(name = "publ") String publName,
                              @RequestParam(name = "genreChecked") List<Genre> genres,
                              @RequestParam(name = "platformChecked") List<Platform> platforms,
                              Game game,
                              SystemRequirements requirements) {
        Developer developer = developerRepository.findByName(devName);
        Publisher publisher = publisherRepository.findByName(publName);

        game.setDeveloper(developer);
        game.setPublisher(publisher);
        game.setGenres(genres);
        game.setPlatforms(platforms);
        game.setSystemRequirements(requirements);
        requirements.setGame(game);

        gameRepository.save(game);

        return "redirect:/admin/games";
    }

    @PostMapping("/games/delete/{id}")
    public String deleteGame(@PathVariable int id) {
        gameRepository.deleteById(id);

        return "redirect:/admin/games";
    }
}
