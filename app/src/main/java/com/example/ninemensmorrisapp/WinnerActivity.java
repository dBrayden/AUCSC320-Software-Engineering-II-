package com.example.ninemensmorrisapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * This class represents the winner page, where the winner of the game is shown, along with
 * their apple. The player has the option to play again, sending them back to the choose mode page
 * or to simply return to the main menu.
 */
public class WinnerActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_winner);
    // Receiving Data from Board Class
    Bundle winnerData = getIntent().getExtras();
    String winningTurn = winnerData.getString("Turn");
    int winningPlayerApple = winnerData.getInt("Winner");
    // Finding Views
    TextView displayWinner = findViewById(R.id.displayWinnerText2);
    ImageView displayWinnersApple = findViewById(R.id.displayWinnersApple);
    displayWinner.setText("Congratulations, Player " + winningTurn + "!");
    displayWinnersApple.setImageResource(winningPlayerApple);
  } // onCreate

  /**
   * Sends the player back to the main menu page.
   *
   * @param aView the current View of the game
   */
  public void toMainMenu(View aView) {
    Button mainMenuButton = (Button) aView;
    Intent toMainMenuActivity = new Intent(this, MainActivity.class);
    startActivity(toMainMenuActivity);
  } // toMainMenu

  /**
   * Sends the player back to the choose mode page.
   *
   * @param aView the current View of the game
   */
  public void toChooseModePage(View aView) {
    Button playAgainButton = (Button) aView;
    Intent toChooseModeActivity = new Intent(this, ChooseModeActivity.class);
    startActivity(toChooseModeActivity);
  } // toChooseModeActivity

} // class