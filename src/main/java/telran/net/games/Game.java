package telran.net.games;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "game")
public class Game {
	@Id
	private Integer id;
	private LocalDate date;
	private boolean is_finished;
	private String sequence;
	public Game() {
		
	}
	public Game(int id, LocalDate date, boolean is_finished, String sequence) {
		this.id = id;
		this.date = date;
		this.is_finished = is_finished;
		this.sequence = sequence;
	}
	public int getId() {
		return id;
	}
	public LocalDate getDate() {
		return date;
	}
	public boolean isIs_finished() {
		return is_finished;
	}
	public String getSequence() {
		return sequence;
	}
	@Override
	public String toString() {
		return "Game [id=" + id + ", date=" + date + ", is_finished=" + is_finished + ", sequence=" + sequence + "]";
	}
	
}
