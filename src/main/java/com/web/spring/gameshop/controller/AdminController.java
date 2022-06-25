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
        return "admin/admin";
    }

    @GetMapping("/games")
    public String gameManagement(Model model) {
        List<Game> games = gameRepository.findAll();
        model.addAttribute("games", games);

        return "admin/admin-game-management";
    }

    @GetMapping("/games/edit/{id}")
    public String updateGame(@PathVariable int id, Model model) {
        Game game = gameRepository.findById(id);
        SystemRequirements requirements = game.getSystemRequirements();

        List<Developer> developers = developerRepository.findAll();
        List<Publisher> publishers = publisherRepository.findAll();
        List<Genre> genres = genresRepository.findAll();
        List<Platform> platforms = platformsRepository.findAll();

        model.addAttribute("game", game);
        model.addAttribute("sys", requirements);
        model.addAttribute("gameGenres", game.getGenres());
        model.addAttribute("gamePlatforms", game.getPlatforms());

        model.addAttribute("developers", developers);
        model.addAttribute("publishers", publishers);
        model.addAttribute("genres", genres);
        model.addAttribute("platforms", platforms);

        return "admin/admin-game-edit";
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
        g.setGenres(game.getGenres());
        g.setPlatforms(game.getPlatforms());
        g.setDeveloper(developer);
        g.setPublisher(publisher);
        g.setCover(game.getCover());

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

        return "admin/admin-add-game";
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
        game.setAvailable(Available.AVAILABLE);
        requirements.setGame(game);

        gameRepository.save(game);

        return "redirect:/admin/games";
    }

    @PostMapping("/games/archive/{id}")
    public String archiveGame(@PathVariable int id) {
        Game game = gameRepository.findById(id);
        if (game.getAvailable() == Available.AVAILABLE) {
            game.setAvailable(Available.ARCHIVE);
        } else {
            game.setAvailable(Available.AVAILABLE);
        }
        gameRepository.save(game);

        return "redirect:/admin/games";
    }

    @PostMapping("/games/delete/{id}")
    public String deleteGame(@PathVariable int id) {
        gameRepository.deleteById(id);

        return "redirect:/admin/games";
    }

    @GetMapping("/genres")
    public String showGenresManagement(Model model) {
        List<Genre> genres = genresRepository.findAll();
        Genre genre = new Genre();
        model.addAttribute("genres", genres);
        model.addAttribute("genre", genre);

        return "admin/admin-genres";
    }

    @PostMapping("/genres")
    public String createGenre(Genre genre) {
        genresRepository.save(genre);

        return "redirect:/admin/genres";
    }

    @GetMapping("/platforms")
    public String showPlatformsManagement(Model model) {
        List<Platform> platforms = platformsRepository.findAll();
        Platform platform = new Platform();
        model.addAttribute("platforms", platforms);
        model.addAttribute("platform", platform);

        return "admin/admin-platforms";
    }

    @PostMapping("/platforms")
    public String createPlatform(Platform platform) {
        platformsRepository.save(platform);

        return "redirect:/admin/platforms";
    }

    @GetMapping("/developers")
    public String showDevelopersManagement(Model model) {
        List<Developer> developers = developerRepository.findAll();
        Developer developer = new Developer();
        model.addAttribute("developers", developers);
        model.addAttribute("developer", developer);

        return "admin/admin-developers";
    }

    @PostMapping("/developers")
    public String createDeveloper(Developer developer) {
        developerRepository.save(developer);

        return "redirect:/admin/developers";
    }

    @GetMapping("/publishers")
    public String showPublishersManagement(Model model) {
        List<Publisher> publishers = publisherRepository.findAll();
        Publisher publisher = new Publisher();
        model.addAttribute("publishers", publishers);
        model.addAttribute("publisher", publisher);

        return "admin/admin-publishers";
    }

    @PostMapping("/publishers")
    public String createPlatform(Publisher publisher) {
        publisherRepository.save(publisher);

        return "redirect:/admin/publishers";
    }

}
