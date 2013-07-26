package ly.dweek.reversi;

public class NegamaxAI implements AI {
	private static int maxPly = 3;
	private OthelloPosition best;

    // See http://www.generation5.org/content/2002/game02.asp
    private static int weighting[][] = {
        {50, -1,5,2,2,5, -1,50},
        {-1,-10,1,1,1,1,-10,-1},
        { 5,  1,1,1,1,1,  1, 5},
        { 2,  1,1,0,0,1,  1, 2},
        { 2,  1,1,0,0,1,  1, 2},
        { 5,  1,1,1,1,1,  1, 5},
        {-1,-10,1,1,1,1,-10,-1},
        {50, -1,5,2,2,5, -1,50}
    };
    
    
    // evaluate a given board state for desirability
	private int eval(GameState state) {

		// TODO: We're currently hardcoded for 8x8, though could explore a wider
		// range of weightings based on the above table....
		assert (state.dims == 8);
	    
	    // pretty highly value having the ability to move!
	    int mobilityValue = 10;
	    
	    int boardScore = 0;
	    int bestCapture = 0;
	    
	    for(int i=0; i<8; i++){
	        for(int j=0; j<8; j++){
	            OthelloSideType piece = state.board[i][j];
	            if(piece == OthelloSideType.EMPTY){
	                int capture = state.move(i, j, false);
	                if(capture > 0) boardScore += mobilityValue;
	                if(capture > bestCapture) bestCapture = capture;
	            } else {
	                int mul = (piece == state.currentPlayer)? 1 : -1;
	                boardScore += weighting[i][j] * mul;
	            }
	        }
	    }
	    
	    boardScore += bestCapture;
	    
	    return boardScore;
	}
	
	
	// perform a negamax N-ply search for the best move.
	private int negamax(GameState state, int ply) {
	    assert(ply >= 0);
	    if(ply == 0) { // we've gone as deep as we can, so just return the score for this node.
	        return eval(state);
	    }
	    
	    // TODO: order moves by desirability first to become negascout
	    int alpha = Integer.MIN_VALUE;	    
	    for(int i=0; i<8; i++){
	        for(int j=0; j<8; j++){
	            if(state.move(i, j, false) > 0) {
	                GameState gs = new GameState(state); // copy current game state
	                gs.move(i, j, true);
	                gs.swapSides();
	                int childWeight = -negamax(gs, ply-1);
	                if(childWeight > alpha) {
	                    alpha = childWeight;
	                    if(ply == maxPly) {
	                        best.i = i;
	                        best.j = j;
	                    }
	                }
	            }
	        }
	    }
	    
	    // if we just couldn't move, just try other side?
	    if(alpha == Integer.MIN_VALUE){
	        GameState gs = new GameState(state);
            gs.swapSides();
	        return -negamax(gs, ply-1);
	    }

	    return alpha;
	}
	
	@Override
	public OthelloPosition computerMove(GameState state) {
		assert(state.currentPlayer == OthelloSideType.BLACK);
		best = new OthelloPosition();
		negamax(state, maxPly);
		assert(best.i < state.dims && best.j < state.dims);
		// note that best.i & j MAY be -1 if there's no legal move we could
		// make from the current position! Trap it anyhow b/c it's interesting
		assert(best.i >= 0 && best.j >= 0);
		return best;
	}

}
