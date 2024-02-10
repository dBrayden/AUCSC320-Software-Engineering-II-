package com.example.ninemensmorrisapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupWindow;

/**
 * This class represents the main menu page of the game, 9 Apples Morris, and allows the player
 * to start a new game, continue an old game (if such a game exists), and go through the rules of
 * 9 Apples Morris.
 */
public class MainActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
  } // onCreate

  /**
   * Takes the player to the next activity, where they are able to
   * choose which mode they would like to the play the game in.
   *
   * @param aView current View of the game
   */
  public void toChooseModeActivity(View aView) {
    Button startNewGameButton = (Button) aView;
    Intent chooseModeActivity = new Intent(this, ChooseModeActivity.class);
    startActivity(chooseModeActivity);
  } // toChooseModeActivity

  /**
   * Takes the player to the first page of rules.
   *
   * @param aView current View of the game
   */
  public void toRulesPage1(View aView) {
    Button rulesButton = (Button) aView;
    Intent toRulesPage1Activity = new Intent(this, RulesPage1Activity.class);
    startActivity(toRulesPage1Activity);
  } // toRulesPage1

} // class