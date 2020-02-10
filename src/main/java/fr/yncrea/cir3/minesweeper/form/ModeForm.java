package fr.yncrea.cir3.minesweeper.form;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import fr.yncrea.cir3.minesweeper.service.validator.ModeConstraint;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ModeConstraint
public class ModeForm {
	private Long id;

	@Length(min = 2, max = 50)
	private String label;

	@NotNull
	@Range(min = 3, max = 30)
	private Long width;

	@NotNull
	@Range(min = 3, max = 30)
	private Long height;

	@NotNull
	private Long mines;
}
