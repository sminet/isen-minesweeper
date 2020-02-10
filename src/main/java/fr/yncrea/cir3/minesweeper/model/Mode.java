package fr.yncrea.cir3.minesweeper.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Mode {
	@Id	@Column
	@GeneratedValue(generator = "seqMode")
	@SequenceGenerator(name = "seqMode", sequenceName = "seq_mode")
	private Long id;
	
	@Column(length = 50)
	private String label;
	
	private Long width;
	
	private Long height;
	
	private Long mines;
}
