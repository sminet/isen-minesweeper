package fr.yncrea.cir3.minesweeper.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;

import fr.yncrea.cir3.minesweeper.model.Game;
import fr.yncrea.cir3.minesweeper.model.GameStatus;
import fr.yncrea.cir3.minesweeper.model.Mode;
import fr.yncrea.cir3.minesweeper.model.Point;

@Service
public class MineSweeperService {
	public Game create(Mode mode) {
		Game game = new Game();

		// initialise la partie
		game.setStatus(GameStatus.OPEN);
		game.setMode(mode);

		// Place les mines
		game.setMines(setupMines(mode.getWidth(), mode.getHeight(), mode.getMines()));

		return game;
	}

	public Set<Point> setupMines(long width, long height, long number) {
		// Place une bombe sur chaque emplacement
		List<Point> mines = allPoints(width, height);

		// Maintenant, supprime aléatoirement des bombes pour arriver au résultat
		// attendu
		while (mines.size() > number) {
			// retire une mine aléatoirement
			mines.remove(random(mines.size()));
		}

		return new LinkedHashSet<>(mines);
	}

	/**
	 * Retourne un point sur chaque case Utilise pour initialiser les mines, ou en
	 * cas de game over
	 * 
	 * @param width
	 * @param height
	 * @return
	 */
	private List<Point> allPoints(long width, long height) {
		List<Point> mines = new ArrayList<>();

		for (long x = 0; x < width; ++x) {
			for (long y = 0; y < height; ++y) {
				mines.add(new Point(x, y));
			}
		}

		return mines;
	}

	/**
	 * Retourne un nombre aléatoire du maximun indiqué
	 * 
	 * @param max
	 * @return
	 */
	private int random(int max) {
		return (int) (Math.random() * max);
	}

	/**
	 * Découvre une case
	 * 
	 * @param game
	 * @param x
	 * @param y
	 */
	public void discoverAt(Game game, Long x, Long y) {
		// si la case est protégée, on ne fait rien
		if (game.isFlagged(x, y)) {
			return;
		}

		// si la case est une mine, on déclenche le game over
		if (game.isTrapped(x, y)) {
			gameOver(game);
		}

		// sinon, lance la découverte des cases
		floodFill(game, x, y);
		
		// vérifie la condition de victoire
		if (game.isWon()) {
			victory(game);
		}
	}

	private void floodFill(Game game, Long x, Long y) {
		// ne repasse pas sur une case déjà découverte
		if (game.isDiscovered(x, y))
			return;

		// ne découvre pas les cases hors du champ de mine
		if (x < 0 || y < 0 || x >= game.getMode().getWidth() || y >= game.getMode().getHeight())
			return;

		// ajoute la nouvelle case
		game.getDiscovered().add(new Point(x, y));

		// s'il y avait au moins une bombe, s'arrête
		if (game.minesAround(x, y) != 0) {
			return;
		}

		// puis continue l'exploration
		floodFill(game, x, y + 1);
		floodFill(game, x, y - 1);
		floodFill(game, x + 1, y);
		floodFill(game, x - 1, y);
		floodFill(game, x + 1, y + 1);
		floodFill(game, x + 1, y - 1);
		floodFill(game, x - 1, y + 1);
		floodFill(game, x - 1, y - 1);
	}

	/**
	 * Déclenche un game over
	 * 
	 * @param game
	 */
	private void gameOver(Game game) {
		game.setStatus(GameStatus.LOST);
		game.setEnded(LocalDateTime.now());
		game.getDiscovered().addAll(allPoints(game.getMode().getWidth(), game.getMode().getHeight()));
	}
	
	/**
	 * Déclanche une victoire
	 * @param game
	 */
	private void victory(Game game) {
		game.setStatus(GameStatus.WON);
		game.setEnded(LocalDateTime.now());
		game.setTime(game.getTime()); // le getter est intelligent
	}

	/**
	 * Place un flag
	 * 
	 * @param game
	 * @param x
	 * @param y
	 */
	public void flagAt(Game game, Long x, Long y) {
		Point flag = new Point(x, y);

		if (game.isFlagged(x, y)) {
			game.getFlags().remove(flag);
		} else {
			game.getFlags().add(flag);
		}
	}
}
