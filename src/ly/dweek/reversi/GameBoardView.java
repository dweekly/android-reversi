package ly.dweek.reversi;

// This implements the graphical display of the Othello board.

import android.content.Context;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Color;
import android.widget.Toast;

public class GameBoardView extends View {
	// here were some hardcoded assumptions that worked well for the emulator
	int BOARD_SCREEN_SIZE = 500;
	int BOARD_DIMS = 8;
	int CELL_SIZE = BOARD_SCREEN_SIZE/BOARD_DIMS;
	int PIECE_RADIUS = 4*CELL_SIZE /10;
	int CELL_PADDING = (CELL_SIZE)/2;
	
	Paint paint = new Paint();
	GameState state;
	Context context;
	
	public GameBoardView(Context context) {
		super(context);
		Log.e("GameBoardView", "Starting");
		this.context = context;
		state = new GameState(BOARD_DIMS, this);
		
		// listen to touch events so we can handle the user's move.
		this.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// on touch down, calculate what square the user tried to touch.
				if(event.getAction() == MotionEvent.ACTION_DOWN) {
					int x = (int)(event.getX() * BOARD_DIMS / BOARD_SCREEN_SIZE);
					int y = (int)(event.getY() * BOARD_DIMS / BOARD_SCREEN_SIZE);
					if (x >= BOARD_DIMS || y >= BOARD_DIMS || x<0 || y<0) {
						return false;
					}
					// pass the board touch on
					handleUserMove(x, y);
					return true;
				}
				return false;
			}
		});
	}

	// handle an attempted user move.
	public void handleUserMove(int x, int y) {
		Log.v("GameBoardView", "User tried to move at " + x + ", " + y);
		
		// is it the user's turn?
		if(state.currentPlayer != OthelloSideType.WHITE) {
			Log.e("GameBoardView", "It wasn't the user's turn! Reprimanding ;)");
			Toast.makeText(this.context, R.string.toast_not_your_turn, Toast.LENGTH_SHORT).show();
			return;
		}
		
		// is the selected square a valid play? (unoccupied, would result in flips)
		int captured = state.move(x, y, true);
		if (captured == 0) {
			Log.e("GameBoardView", "User's move at " + x + ", " + y + " was not valid.");
			Toast.makeText(this.context, R.string.toast_cant_move_there, Toast.LENGTH_SHORT).show();
			return;
		}
		Log.i("GameBoardView", "User's move at " + x + "," + y + " was valid w/take of " + captured + " piece(s)");

		// TODO: just invalidate the screen area around the flipped/new pieces?
		this.invalidate();
		
		state.nextTurn(false);
	}
	
	@Override 
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
		Log.i("GameBoardView", "onMeasure called with " + widthMeasureSpec + "x" + heightMeasureSpec);
		
		// The square board should fully fill the smaller dimension
	   int parentWidth = MeasureSpec.getSize(widthMeasureSpec);
	   int parentHeight = MeasureSpec.getSize(heightMeasureSpec);
	   if(parentHeight > parentWidth) {
		   BOARD_SCREEN_SIZE = parentWidth;
	   } else {
		   BOARD_SCREEN_SIZE = parentHeight;
	   }

	   Log.i("GameBoardView", "board size of " + BOARD_SCREEN_SIZE);
	   this.setMeasuredDimension(BOARD_SCREEN_SIZE, BOARD_SCREEN_SIZE);
	   CELL_SIZE = BOARD_SCREEN_SIZE/BOARD_DIMS;
	   PIECE_RADIUS = 4 * CELL_SIZE / 10;
	   CELL_PADDING = CELL_SIZE / 2;
	}
	

	@Override
	public void onDraw(Canvas canvas) {
		int i, j;
		
		// draw vertical board lines
		paint.setColor(Color.WHITE);
		paint.setStrokeWidth(2);
		for(i=0; i<BOARD_DIMS; i++) {
			canvas.drawLine(i*CELL_SIZE, 0, i*CELL_SIZE, BOARD_SCREEN_SIZE, paint);
		}
		canvas.drawLine(BOARD_SCREEN_SIZE, 0, BOARD_SCREEN_SIZE, BOARD_SCREEN_SIZE, paint);

		// draw horizontal board lines
		for(i=0; i<BOARD_DIMS; i++) {
			canvas.drawLine(0, i*CELL_SIZE, BOARD_SCREEN_SIZE, i*CELL_SIZE, paint);
		}
		canvas.drawLine(0, BOARD_SCREEN_SIZE, BOARD_SCREEN_SIZE, BOARD_SCREEN_SIZE, paint);
	
		
		// now draw the pieces on the board
		for(i=0; i<BOARD_DIMS; i++){
			for(j=0; j<BOARD_DIMS; j++){
				OthelloSideType piece = state.board[i][j];
				if(piece == OthelloSideType.WHITE || piece == OthelloSideType.BLACK) {
					if(piece == OthelloSideType.WHITE) {
						paint.setColor(Color.WHITE);
					}
					if(piece == OthelloSideType.BLACK) {
						paint.setColor(Color.GRAY);
					}
					canvas.drawCircle(
							(i * CELL_SIZE) + CELL_PADDING,
							(j * CELL_SIZE) + CELL_PADDING,
							PIECE_RADIUS, // radius
							paint);
				}
			}
		}
	}
}
