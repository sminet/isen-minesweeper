package fr.yncrea.cir3.minesweeper.form;

import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GameForm {
	@NotNull
	private Long mode;
}
