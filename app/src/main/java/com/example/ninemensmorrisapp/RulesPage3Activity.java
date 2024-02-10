package com.example.ninemensmorrisapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

/**
 * This class displays the last set of rules for the game, 9 Apples Morris.
 */
public class RulesPage3Activity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_rules_page3);
  } // onCreate

  /**
   * Sends the player back to the second set of rules.
   *
   * @param aView the current View of the game
   */
  public void toPage2OfRules(View aView) {
    ImageButton backButton = (ImageButton) aView;
    Intent toRulesPage2Activity = new Intent(this, RulesPage2Activity.class);
    startActivity(toRulesPage2Activity);
  } // toPage2OfRules

  /**
   * Sends the player back to the main menu.
   *
   * @param aView the current View of the game
   */
  public void toMainMenu(View aView) {
    Button finishButton = (Button) aView;
    Intent toMainMenuActivity = new Intent(this, MainActivity.class);
    startActivity(toMainMenuActivity);
  } // toMainMenu

} // class