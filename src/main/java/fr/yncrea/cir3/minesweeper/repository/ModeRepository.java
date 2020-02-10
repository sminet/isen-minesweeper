package fr.yncrea.cir3.minesweeper.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import fr.yncrea.cir3.minesweeper.model.Mode;

@Repository
public interface ModeRepository extends JpaRepository<Mode, Long> {
}
