package fr.yncrea.cir3.minesweeper.repository;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import fr.yncrea.cir3.minesweeper.model.Game;
import fr.yncrea.cir3.minesweeper.model.GameStatus;
import fr.yncrea.cir3.minesweeper.model.Mode;

@Repository
public interface GameRepository extends JpaRepository<Game, Long> {
	public List<Game> findByStatus(GameStatus status);
	public void deleteByMode(Mode mode);
	public Long countByModeAndTimeLessThan(Mode mode, Long time);
	public Set<Game> findTop5ByModeAndStatusOrderByTime(Mode mode, GameStatus status);
}
