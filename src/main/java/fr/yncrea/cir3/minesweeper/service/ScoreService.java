package fr.yncrea.cir3.minesweeper.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.yncrea.cir3.minesweeper.model.Game;
import fr.yncrea.cir3.minesweeper.repository.GameRepository;

@Service
public class ScoreService {
	@Autowired
	private GameRepository games;
	
	public boolean inTop5(Game game) {
		Long betterScores = games.countByModeAndTimeLessThan(game.getMode(), game.getTime());
		if (betterScores < 5) {
			return true;
		}
		
		return false;
	}
}
