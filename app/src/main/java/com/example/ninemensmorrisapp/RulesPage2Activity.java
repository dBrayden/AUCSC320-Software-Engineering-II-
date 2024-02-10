package com.example.ninemensmorrisapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

/**
 * This class displays the second set of rules for the game, 9 Apples Morris.
 */
public class RulesPage2Activity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_rules_page2);
  } // onCreate

  /**
   * Sends the player to the first set of rules.
   *
   * @param aView the current View of the game
   */
  public void toPage1OfRules(View aView) {
    ImageButton backButton = (ImageButton) aView;
    Intent toRulesPage1Activity = new Intent(this, RulesPage1Activity.class);
    startActivity(toRulesPage1Activity);
  } // toPage1OfRules

  /**
   * Sends the player to the last set of rules.
   *
   * @param aView the current View of the game
   */
  public void toPage3OfRules(View aView) {
    Button nextButton = (Button) aView;
    Intent toRulesPage3Activity = new Intent(this, RulesPage3Activity.class);
    startActivity(toRulesPage3Activity);
  } // toPage3OfRules

} // class