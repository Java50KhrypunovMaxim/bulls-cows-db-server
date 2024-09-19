package telran.net.games;
import java.time.*;
import java.util.List;

import jakarta.persistence.*;
public class JpqlQueriesRepository {
	private EntityManager em;

	public JpqlQueriesRepository(EntityManager em) {
		this.em = em;
	}
	public List<Game> getGamesFinished(boolean isFinished) {
		TypedQuery<Game> query = em.createQuery(
				"select game from Game game where is_finished=?1",
				Game.class);
		List<Game> res = query.setParameter(1, isFinished).getResultList();
		return res;
	}
	public List<DateTimeSequence> getDateTimeSequence(LocalTime time) {
		TypedQuery<DateTimeSequence> query =
				em.createQuery("select date, sequence "
						+ "from Game where cast(date as time) < :time",
						DateTimeSequence.class);
		List<DateTimeSequence> res = query.setParameter("time", time).getResultList();
		return res;
	}
	public List<Integer> getBullsInMovesGamersBornAfter(LocalDate afterDate) {
		TypedQuery<Integer> query = em.createQuery(
				"select bulls from Move where gameGamer.gamer.birthdate > "
				+ "?1", Integer.class);
		List<Integer> res = query.setParameter(1, afterDate).getResultList();
		return res;
		
	}
	public List<MinMaxAmount> getDistributionGamesMoves(int interval) {
//		select floor(game_moves / 5) * 5 as min_moves, floor(game_moves / 5) * 5 + 4 as max_moves,
//		count(*) as amount
//		from
//		(select count(*) as game_moves from game_gamer join move on game_gamer.id=game_gamer_id 
//		group by game_id) group by min_moves order by min_moves
		TypedQuery<MinMaxAmount> query = em.createQuery(
				"select floor(game_moves / :interval) * :interval as min_moves, "
				+ "floor(game_moves / :interval) * :interval + (:interval - 1) as max_moves, "
				+ "count(*) as amount "
				+ "from "
				+ "(select count(*) as game_moves from Move "
				+ "group by gameGamer.game.id) "
				+ "group by min_moves, max_moves order by min_moves", MinMaxAmount.class);
		List<MinMaxAmount> res = query.setParameter("interval", interval).getResultList();
		return res;
	}
	
	public List<Game> getGamesWithAverageGamerAgeGreaterThan(int age) {
	    TypedQuery<Game> query = em.createQuery(
	        "select game " +
	        "from Game game " +
	        "where game.id in (" +
	        "    select gameGamer.game.id " +
	        "    from GameGamer gameGamer " +
	        "    join gameGamer.gamer gamer " +
	        "    group by gameGamer.game.id " +
	        "    having avg(YEAR(current_date) - YEAR(gamer.birthdate)) > :age" +
	        ")", Game.class);
	    query.setParameter("age", age);
	    return query.getResultList();
	}
	
	public List<IdGameAndNumberofMoves> getWinnerMovesLessThan(int maxMoves) {
	    TypedQuery<IdGameAndNumberofMoves> query = em.createQuery(
	        "select game.id, count(move.id) " +
	        "from GameGamer gameGamer " +
	        "join Move move on gameGamer.id = move.gameGamer.id " +
	        "where gameGamer.is_winner = true " +
	        "group by gameGamer.game.id " +
	        "having count(move.id) < :maxMoves", IdGameAndNumberofMoves.class);
	    query.setParameter("maxMoves", maxMoves);
	    return query.getResultList();
	}
	
	
	public List<String> getGamerNamesWithMovesLessThan(int maxMoves) {
	    TypedQuery<String> query = em.createQuery(
	        "select distinct gameGamer.gamer.username " +
	        "from GameGamer gameGamer " +
	        "join Move move on gameGamer.id = move.gameGamer.id " +
	        "group by gameGamer.game.id, gameGamer.gamer.username " +
	        "having count(move.id) < :maxMoves", String.class);
	    query.setParameter("maxMoves", maxMoves);
	    return query.getResultList();
	}
	
	public List<IdGameAndAverageMoves> getGameIdAndAverageMoves() {
	    TypedQuery<IdGameAndAverageMoves> query = em.createQuery(
	        "select new telran.net.games.IdGameAndAverageMoves(gameGamer.game.id, round(avg(moveCounts.moveCount), 1)) " +
	        "from GameGamer gameGamer " +
	        "join (select count(move.id) as moveCount, gameGamer.game.id as gameId " +
	        "from Move move " +
	        "join move.gameGamer gameGamer " +
	        "group by gameGamer.game.id, gameGamer.gamer.id) moveCounts " +
	        "on gameGamer.game.id = moveCounts.gameId " +
	        "group by gameGamer.game.id " +
	        "order by gameGamer.game.id", 
	        IdGameAndAverageMoves.class
	    );

	    return query.getResultList();
	}
	
	
    }
	
