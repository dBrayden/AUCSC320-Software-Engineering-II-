package com.example.ninemensmorrisapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * This class represents the customize screen of the game, 9 Apples Morris, and allows the player
 * (or players if chosen to play against another player) to customize their apple before starting
 * the game. If the player has chosen to play against a computer, the computer will be assigned a
 * red apple, unless the player has chosen a red apple. In that case, the computer will be
 * assigned a green apple instead. If the player has chosen to play against another player, they
 * are taken to the next customize activity, where the next player can customize their apple. In
 * the event the first player does not choose an apple and clicks on the start game button,
 * their apple will be red by default.
 */
public class CustomizeActivity extends AppCompatActivity {
    int difficulty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Bundle infoIncoming = getIntent().getExtras();
        difficulty = infoIncoming.getInt("difficulty");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customize);
        makeListOfIDsOfApples();
    } // onCreate

    /**
     * Checks if the player chose to play against another player from the choose activity mode.
     * @return true if the player decided to play against another player, false otherwise
     */
    public boolean didPlayerChoosePlayerInChooseMode() {
        Bundle infoComingIn = getIntent().getExtras();
        if (infoComingIn != null){
            return infoComingIn.getBoolean("The value of chosePlayer"); // true
        } else {
            return false; // the player clicked on the computer option instead
        }
    } // didPlayerChoosePlayerInChooseMode

    /**
     * Sends the player back to the previous activity, depending on whether they are playing
     * against a player or computer.
     * @param aView - the current View of the game (the choose mode activity view)
     */
    public void toPreviousActivity(View aView) {
        boolean chosePlayer = didPlayerChoosePlayerInChooseMode();
        ImageButton backArrowCustomize = (ImageButton) aView;
        if (chosePlayer) {
            Intent toChooseModeActivity = new Intent(this, ChooseModeActivity.class);
            startActivity(toChooseModeActivity);
        } else {
            Intent toSettingsActivity = new Intent(this, SettingsActivity.class);
            startActivity(toSettingsActivity);
        }
    } // toPreviousActivity

    /**
     * Creates a list of possible apples that the player(s) can choose from, and allows them to
     * view each apple in the list.
     */
    public void makeListOfIDsOfApples() {
        DCircularLinkedList<Integer> listOfIDsOfApples = new DCircularLinkedList<Integer>();
        DCircularLinkedList<String> namesOfApples = makeNamesOfApples();
        listOfIDsOfApples.addLast(R.drawable.redapple);
        listOfIDsOfApples.addLast(R.drawable.greenapple);
        listOfIDsOfApples.addLast(R.drawable.yellowapple);
        displayPreviousAppleColour(listOfIDsOfApples, namesOfApples);
        displayNextAppleColour(listOfIDsOfApples, namesOfApples);
    } // makeListOfIDsOfApples

    /**
     * Creates a list of apples names to display them to the corresponding apple.
     */
    public DCircularLinkedList<String> makeNamesOfApples() {
        DCircularLinkedList<String> namesOfApples = new DCircularLinkedList<String>();
        namesOfApples.addLast("Red");
        namesOfApples.addLast("Green");
        namesOfApples.addLast("Yellow");
        return namesOfApples;
    } // makeNamesOfApples

    /**
     * Displays the next colour of the apple that the player can select as their own apple for the
     * game.
     */
    public void displayNextAppleColour(DCircularLinkedList<Integer> listOfIDsOfApples, DCircularLinkedList<String> namesOfApples) {
        ImageButton nextButton = (ImageButton) findViewById(R.id.nextAppleColour);
        TextView currentAppleText = (TextView) findViewById(R.id.currentAppleColourText);
        ImageView chosenAppleOfPlayer = (ImageView) findViewById(R.id.chosenAppleOfPlayer1);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listOfIDsOfApples.toNextNodeInList();
                namesOfApples.toNextNodeInList();;
                chosenAppleOfPlayer.setImageResource(listOfIDsOfApples.getCurrentElement());
                chosenAppleOfPlayer.setTag(listOfIDsOfApples.getCurrentElement());
                currentAppleText.setText(namesOfApples.getCurrentElement());
            }
        });
    } // displayNextAppleColour

    /**
     * Displays the previous colour of the apple that the player can select as their own apple for
     * the game.
     */
    public void displayPreviousAppleColour(DCircularLinkedList<Integer> listOfIDsOfApples, DCircularLinkedList<String> namesOfApples) {
        ImageButton previousButton = (ImageButton) findViewById(R.id.previousAppleColour);
        TextView currentAppleText = (TextView) findViewById(R.id.currentAppleColourText);
        ImageView chosenAppleOfPlayer = (ImageView) findViewById(R.id.chosenAppleOfPlayer1);
        previousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listOfIDsOfApples.toPreviousNodeInList();
                namesOfApples.toPreviousNodeInList();
                chosenAppleOfPlayer.setImageResource(listOfIDsOfApples.getCurrentElement());
                chosenAppleOfPlayer.setTag(listOfIDsOfApples.getCurrentElement());
                currentAppleText.setText(namesOfApples.getCurrentElement());
            }
        });
    } // displayPreviousAppleColour

    /**
     * Checks if the apple the first player has chosen is red.
     * @return - true if the apple the player chose is red, false otherwise
     */
    public boolean didPlayer1ChooseARedApple() {
        TextView currentAppleText = (TextView) findViewById(R.id.currentAppleColourText);
        if (currentAppleText.getText().equals("Red")) {
            System.out.println("True - Red Apple");
            return true;
        } else {
            System.out.println("False - Not Red Apple");
            return false;
        }
    } // didPlayer1ChooseARedApple

    public Player playingAgainstComputer() {
        Intent toGameActivity = new Intent(this, GameActivity.class);
        if (didPlayer1ChooseARedApple()) {
            Player player2 = new Player(R.drawable.greenapple);
            return player2;  // possibility of player 1 and the computer (which is player initially) having the same apple, so will need to
        } else {
            Player player2 = new Player();
            return player2;
        }
    } // playingAgainstComputer

    /**
     * Checks if the player has not chosen an apple.
     * @return true if no apple has been selected, false otherwise
     */
    private boolean didPlayerNotChooseAnApple() {
        ImageView chosenApplePlayer = (ImageView) findViewById(R.id.chosenAppleOfPlayer1);
        if (chosenApplePlayer.getTag() == null) {
            return true;
        } else {
            return false;
        }
    } // didPlayerNotChooseAnApple

    /**
     * Creates Player 1 of 9 Apples Morris, setting the apple of the player to the apple that they
     * chose in the activity.
     * @return - the first player of the game
     */
    public Player createPlayer1() {
        ImageView chosenApplePlayer = (ImageView) findViewById(R.id.chosenAppleOfPlayer1);
        if (didPlayerNotChooseAnApple()) { // if they click on start game without choosing an
            // apple, player's apple is red as default
            Player player1 = new Player();
            return player1;
        } else {
            Player player1 = new Player((Integer) chosenApplePlayer.getTag());
            return player1;
        }
    } // createPlayer1

    public boolean cpuMode() {
        Bundle infoIncoming = getIntent().getExtras();
        boolean cpuEnable = infoIncoming.getBoolean("cpuEnabled");
        return cpuEnable;
    }

    /**
     * Starts a new game of 9 Apple's Morris, with the apple that player (or players) has chosen.
     * @param aView
     */
    public void startGame(View aView) {
        boolean chosePlayer = didPlayerChoosePlayerInChooseMode();
        Button startGameButton = (Button) aView;
        ImageButton nextButton = (ImageButton) findViewById(R.id.nextAppleColour);
        ImageButton previousButton = (ImageButton) findViewById(R.id.previousAppleColour);
        Intent toGameActivity = new Intent(this, GameActivity.class);
        Intent toCustomizeActivity2 = new Intent(this, CustomizeActivity2.class);
        Player player1 = createPlayer1();
        if (!chosePlayer) {
            Player player2 = playingAgainstComputer();
            toGameActivity.putExtra("difficulty",difficulty);
            toGameActivity.putExtra("Player1", player1);
            toGameActivity.putExtra("Player2", player2);
            toGameActivity.putExtra("cpuEnabled",cpuMode());
            startActivity(toGameActivity);
        } else {
            toCustomizeActivity2.putExtra("Player1", player1);
            startActivity(toCustomizeActivity2);
        }
    } // startGame

} // class