package com.example.ninemensmorrisapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

/**
 * This class represents the choose mode page of the game, 9 Apples Morris, and allows the player
 * to choose to play against a player or computer.
 */
public class ChooseModeActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_choose_mode);
  } // onCreate

  /**
   * Sends the player back to the main menu page.
   *
   * @param aView the current View of the game
   */
  public void toMainActivity(View aView) {
    ImageButton backArrowChooseMode = (ImageButton) aView;
    Intent toMainActivity = new Intent(this, MainActivity.class);
    startActivity(toMainActivity);
  } // toMainActivity

  /**
   * Takes the user to the customize page where they are able to customize their apple.
   *
   * @param aView the current View of the game
   */
  public void toCustomizeActivity(View aView) {
    Button choosePlayerButton = (Button) aView;
    final boolean chosePlayer = true; // the player has chosen to play against another player
    Intent toCustomizeActivity = new Intent(this, CustomizeActivity.class);
    toCustomizeActivity.putExtra("The value of chosePlayer", chosePlayer);
    startActivity(toCustomizeActivity);
  } // toCustomizeActivity

  /**
   * Takes the player to the settings page, where they are able to choose the difficulty of the
   * computer.
   *
   * @param aView the current View of the game
   */
  public void toSettingsActivity(View aView) {
    Button chooseComputerButton = (Button) aView;
    Intent toSettingsActivity = new Intent(this, SettingsActivity.class);
    startActivity(toSettingsActivity);
  } // toSettingsActivity

} // class