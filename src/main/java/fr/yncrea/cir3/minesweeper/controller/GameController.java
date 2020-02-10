package fr.yncrea.cir3.minesweeper.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import fr.yncrea.cir3.minesweeper.form.GameForm;
import fr.yncrea.cir3.minesweeper.form.ScoreForm;
import fr.yncrea.cir3.minesweeper.model.Game;
import fr.yncrea.cir3.minesweeper.model.GameStatus;
import fr.yncrea.cir3.minesweeper.model.Mode;
import fr.yncrea.cir3.minesweeper.repository.GameRepository;
import fr.yncrea.cir3.minesweeper.repository.ModeRepository;
import fr.yncrea.cir3.minesweeper.service.MineSweeperService;
import fr.yncrea.cir3.minesweeper.service.ScoreService;

@Controller
@RequestMapping("/game")
public class GameController {
	@Autowired
	private GameRepository games;

	@Autowired
	private ModeRepository modes;

	@Autowired
	private MineSweeperService service;

	@Autowired
	private ScoreService scoreService;

	@GetMapping("/start")
	public String start(Model model) {
		// recherche si une partie est en cours
		List<Game> matches = games.findByStatus(GameStatus.OPEN);
		if (matches.size() > 0) {
			return "redirect:/game/play/" + matches.get(0).getId();
		}

		// Sinon, affichage du formulaire de nouvelle partie
		model.addAttribute("modes", modes.findAll());
		model.addAttribute("game", new GameForm());

		return "game/start";
	}

	@PostMapping("/start")
	public String create(@Valid @ModelAttribute("game") GameForm form, BindingResult result, Model model) {
		// TODO vérifier qu'une partie n'est pas déjà en cours

		if (result.hasErrors()) {
			model.addAttribute("game", form);
			return "game/start";
		}

		// Création d'une nouvelle partie
		Mode mode = modes.getOne(form.getMode());
		Game game = service.create(mode);

		// Sauvegarde de la partie
		games.save(game);

		// redirige vers la partie
		return "redirect:/game/play/" + game.getId();
	}

	@GetMapping("/play/{id}")
	public String display(@PathVariable Long id, Model model, @RequestParam(required = false) String action) {
		if (action == null || (!"discover".equals(action) && !"flag".equals(action))) {
			action = "discover";
		}

		Game game = games.getOne(id);
		model.addAttribute("game", game);
		model.addAttribute("action", action);

		return "game/play";
	}

	@GetMapping("/surrender/{id}")
	public String surrender(@PathVariable Long id) {
		Game game = games.getOne(id);
		game.setStatus(GameStatus.FORFAIT);
		games.save(game);

		return "redirect:/";
	}

	@GetMapping("/play/{id}/{x}/{y}")
	public String play(@PathVariable Long id, @PathVariable Long x, @PathVariable Long y,
			@RequestParam(required = false) String action) {
		if (action == null || (!"discover".equals(action) && !"flag".equals(action))) {
			action = "discover";
		}

		// récupère la partie
		Game game = games.getOne(id);

		// effectue l'action désirée
		switch (action) {
		case "discover":
			service.discoverAt(game, x, y);
			break;
		case "flag":
			service.flagAt(game, x, y);
			break;
		}

		// sauvegarde la partie
		games.save(game);

		// si la partie est gagnée, redirige l'utilisateur vers la page de victoire
		if (game.getStatus() == GameStatus.WON) {
			return "redirect:/game/victory/" + game.getId();
		}

		return "redirect:/game/play/" + game.getId() + "?action=" + action;
	}

	@GetMapping("/victory/{id}")
	public String victory(@PathVariable Long id, Model model) {
		Game game = games.getOne(id);
		model.addAttribute("game", game);

		// recheche si la partie est dans le top 5
		ScoreForm score = null;
		if (scoreService.inTop5(game)) {
			score = new ScoreForm();
		}
		model.addAttribute("score", score);

		return "game/victory";
	}

	@PostMapping("/victory/{id}")
	public String registerVictory(@PathVariable Long id, @Valid @ModelAttribute("score") ScoreForm score, BindingResult result, Model model) {
		// sauvegarde le score
		// en fait, inutile de vérifie si l'utilisateur est en train d'enregistrer un score
		// invalide, puisqu'on affichera que le top 5 de toute façon
		Game game = games.getOne(id);
		model.addAttribute("game", game);

		if (result.hasErrors()) {
			model.addAttribute("score", score);
			return "game/victory";
		}
		
		game.setPlayer(score.getName());
		games.save(game);
		
		return "redirect:/";
	}
}
