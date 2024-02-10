package com.example.ninemensmorrisapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;


/**
 * This class represents the settings page of the game, 9 Apples Morris, and allows the user to
 * choose the difficulty of the computer (if chosen to play against a computer) to be either easy
 * or hard.
 */
public class SettingsActivity extends AppCompatActivity {
    int difficulty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
    } // onCreate

    /**
     * Sends the player back to the previous page, the choose mode activity.
     * @param aView - the current View of the game
     */
    public void toChooseModeActivity(View aView) {
        ImageButton backArrowSettings = (ImageButton) aView;
        Intent toChooseModeActivity = new Intent(this, ChooseModeActivity.class);
        startActivity(toChooseModeActivity);
    } // toChooseModeActivity

    /**
     * Sets the difficulty of the computer to be either easy or hard.
     */
    public void chooseDifficultyHard(View aView) {
        difficulty = 2;
        toCustomizeActivity(aView);

    }
    public void chooseDifficultyEasy(View aView){
        difficulty = 1;
        toCustomizeActivity(aView);

    }

    /**
     * Takes the player to the customize page where they are able to customize their apple.
     * @param aView the current View of the game (the settings activity view)
     */
    public void toCustomizeActivity(View aView) {
        Button difficultyButton = (Button) aView;
        Intent toCustomizeActivity = new Intent(this, CustomizeActivity.class);
        // chooseDifficulty(); <-- changes the string value of the difficulty
        /* creates a new computer class and sends it to the next activity, and will send it from
        THAT activity to the game activity */
        toCustomizeActivity.putExtra("difficulty",difficulty);
        toCustomizeActivity.putExtra("cpuEnabled",true);
        startActivity(toCustomizeActivity);

  } // toCustomizeActivity

} // class