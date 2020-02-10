package fr.yncrea.cir3.minesweeper.service.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import fr.yncrea.cir3.minesweeper.form.ModeForm;

public class ModeValidator implements ConstraintValidator<ModeConstraint, ModeForm> {
	@Override
	public boolean isValid(ModeForm value, ConstraintValidatorContext context) {
		// nécessite au moins que les autres champs soient renseignés
		if (value.getHeight() != null && value.getHeight() != null && value.getMines() != null) {
			// vérifie le nombre de mine par rapport à la taille de la grille
			if (value.getHeight() * value.getWidth() <= value.getMines()) {
				// ajoute une erreur sur le champ de la mine
				context.disableDefaultConstraintViolation();
				context.buildConstraintViolationWithTemplate(context.getDefaultConstraintMessageTemplate())
						.addPropertyNode("mines").addConstraintViolation();
				return false;
			}
		}

		return true;
	}
}
