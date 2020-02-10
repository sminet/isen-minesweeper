package fr.yncrea.cir3.minesweeper.controller;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import fr.yncrea.cir3.minesweeper.model.Game;
import fr.yncrea.cir3.minesweeper.model.GameStatus;
import fr.yncrea.cir3.minesweeper.model.Mode;
import fr.yncrea.cir3.minesweeper.repository.GameRepository;
import fr.yncrea.cir3.minesweeper.repository.ModeRepository;

@Controller
public class IndexController {
	@Autowired
	private GameRepository games;
	
	@Autowired
	private ModeRepository modes;
	
	@GetMapping("/")
	public String index(Model model) {
		Map<Mode, Set<Game>> scores = new LinkedHashMap<>();
		model.addAttribute("scores", scores);
		
		for (Mode mode: modes.findAll()) {
			Set<Game> best = games.findTop5ByModeAndStatusOrderByTime(mode, GameStatus.WON);
			scores.put(mode, best);
		}
		
		return "index";
	}
}
