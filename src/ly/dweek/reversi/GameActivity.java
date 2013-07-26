package ly.dweek.reversi;

import android.app.Activity;
import android.os.Bundle;
import android.graphics.Color;

public class GameActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GameBoardView gameView = new GameBoardView(this);
        gameView.setBackgroundColor(Color.BLACK);
        setContentView(gameView);
    }
}
