package fr.yncrea.cir3.minesweeper.form;

import javax.validation.constraints.NotEmpty;

import org.hibernate.validator.constraints.Length;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ScoreForm {
	@NotEmpty
	@Length(min = 0, max = 50)
	String name;
}
