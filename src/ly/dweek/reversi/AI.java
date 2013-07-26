package ly.dweek.reversi;

// All A.I.s must implement this interface that returns
// their desired move given a game state.
public interface AI {
	public OthelloPosition computerMove(GameState state);
}
