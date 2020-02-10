package fr.yncrea.cir3.minesweeper.model;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Game {
	@Id
	@Column
	@GeneratedValue(generator = "seqGame")
	@SequenceGenerator(name = "seqGame", sequenceName = "seq_game")
	private Long id;

	@Enumerated(EnumType.STRING)
	private GameStatus status;

	@ManyToOne
	private Mode mode;

	@ElementCollection
	@JoinTable(name = "game_mines")
	private Set<Point> mines;

	@ElementCollection
	@JoinTable(name = "game_flags")
	private Set<Point> flags;

	@ElementCollection
	@JoinTable(name = "game_discovered")
	private Set<Point> discovered;

	@Column(length = 50)
	private String player;
	
	private LocalDateTime created = LocalDateTime.now();
	
	private LocalDateTime ended = null;
	
	private Long time = null;

	public boolean isDiscovered(long x, long y) {
		if (discovered.contains(new Point(x, y))) {
			return true;
		}

		return false;
	}

	public boolean isTrapped(long x, long y) {
		if (mines.contains(new Point(x, y))) {
			return true;
		}

		return false;
	}

	public boolean isFlagged(long x, long y) {
		if (flags.contains(new Point(x, y))) {
			return true;
		}

		return false;
	}
	
	public boolean isWon() {
		long total = mode.getHeight() * mode.getHeight();
		if (mines.size() == total - discovered.size()) {
			return true;
		}
		
		return false;
	}

	public int minesAround(long x, long y) {
		int mineCount = 0;

		for (long cx = x - 1; cx <= x + 1; cx++) {
			for (long cy = y - 1; cy <= y + 1; cy++) {
				if (mines.contains(new Point(cx, cy)))
					mineCount++;
			}
		}

		return mineCount;
	}

	public PointStatus getStatus(long x, long y) {
		boolean discovered = isDiscovered(x, y);
		boolean flagged = isFlagged(x, y);
		boolean trapped = isTrapped(x, y);
		
		if (discovered) {
			if (trapped && flagged) {
				return PointStatus.DISCOVERED_MINE_FLAG;
			} else if (trapped) {
				return PointStatus.DISCOVERED_MINE;
			} else if (flagged) {
				return PointStatus.DISCOVERED_FLAG;
			}
			
			return PointStatus.DISCOVERED;
		}
		
		if (flagged) {
			return PointStatus.UNKNOWN_FLAG;
		}
		
		return PointStatus.UNKNOWN;
	}

	public long getTime() {
		if (time != null) {
			return time;
		}
		
		ZoneOffset offset = ZoneOffset.UTC;
		long creation = created.toEpochSecond(offset);
		long end = (LocalDateTime.now()).toEpochSecond(offset);
		if (status != GameStatus.OPEN && ended != null) {
			end = ended.toEpochSecond(offset);
		}
		
		return end - creation;
	}
}
