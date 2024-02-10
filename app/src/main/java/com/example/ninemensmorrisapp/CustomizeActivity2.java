package com.example.ninemensmorrisapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * This class represents the second page of the customize screen of the game, 9 Apples Morris,
 * allowing the second player to customize their apple, checking if they've chosen the same
 * apple as player 1, and making them choose another apple. In the event player 2 doesn't choose an
 * apple and clicks on the start game button, their apple will be green by default.
 */
public class CustomizeActivity2 extends AppCompatActivity {

  Player player1;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_customize2);
    makeListOfIDsOfApples();
    Bundle playerFromCustomizeActivity = getIntent().getExtras();
    player1 = (Player) playerFromCustomizeActivity.getSerializable("Player1");
  } // onCreate

  /**
   * Sends the player back to the previous customize activity.
   *
   * @param aView - the current View of the game
   */
  public void toPreviousActivity(View aView) {
    ImageButton backArrowCustomize = (ImageButton) aView;
    Intent toCustomizeActivity = new Intent(this, CustomizeActivity.class);
    startActivity(toCustomizeActivity);
  } // toPreviousActivity

  /**
   * Creates a list of possible apples that the player(s) can choose from, and allows them to
   * view each apple in the list.
   */
  public void makeListOfIDsOfApples() {
    DCircularLinkedList<Integer> listOfIDsOfApples = new DCircularLinkedList<Integer>();
    DCircularLinkedList<String> namesOfApples = makeNamesOfApples();
    listOfIDsOfApples.addLast(R.drawable.greenapple);
    listOfIDsOfApples.addLast(R.drawable.yellowapple);
    listOfIDsOfApples.addLast(R.drawable.redapple);
    displayPreviousAppleColour(listOfIDsOfApples, namesOfApples);
    displayNextAppleColour(listOfIDsOfApples, namesOfApples);
  } // makeListOfIDsOfApples

  /**
   * Creates a list of apples names to display them to the corresponding apple.
   */
  public DCircularLinkedList<String> makeNamesOfApples() {
    DCircularLinkedList<String> namesOfApples = new DCircularLinkedList<String>();
    namesOfApples.addLast("Green");
    namesOfApples.addLast("Yellow");
    namesOfApples.addLast("Red");
    return namesOfApples;
  } // makeNamesOfApples

  /**
   * Displays the next colour of the apple that the player can select as their own apple for the
   * game.
   */
  public void displayNextAppleColour(DCircularLinkedList<Integer> listOfIDsOfApples, DCircularLinkedList<String> namesOfApples) {
    ImageButton nextButton = (ImageButton) findViewById(R.id.nextAppleColour2);
    TextView currentAppleText = (TextView) findViewById(R.id.currentAppleColourText2);
    ImageView chosenAppleOfPlayer = (ImageView) findViewById(R.id.chosenAppleOfPlayer2);
    nextButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        listOfIDsOfApples.toNextNodeInList();
        namesOfApples.toNextNodeInList();
        ;
        chosenAppleOfPlayer.setImageResource(listOfIDsOfApples.getCurrentElement());
        chosenAppleOfPlayer.setTag(listOfIDsOfApples.getCurrentElement());
        currentAppleText.setText(namesOfApples.getCurrentElement());
      }
    });
  } // showNextAppleColour

  /**
   * Displays the previous colour of the apple that the player can select as their own apple for
   * the game.
   */
  public void displayPreviousAppleColour(DCircularLinkedList<Integer> listOfIDsOfApples, DCircularLinkedList<String> namesOfApples) {
    ImageButton previousButton = (ImageButton) findViewById(R.id.previousAppleColour2);
    TextView currentAppleText = (TextView) findViewById(R.id.currentAppleColourText2);
    ImageView chosenAppleOfPlayer = (ImageView) findViewById(R.id.chosenAppleOfPlayer2);
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
  } // showPreviousAppleColour

  /**
   * Checks if the player has not chosen an apple.
   *
   * @return true if no apple has been selected, false otherwise
   */
  private boolean didPlayer2NotChooseAnApple() {
    ImageView chosenApplePlayer = (ImageView) findViewById(R.id.chosenAppleOfPlayer2);
    if (chosenApplePlayer.getTag() == null) {
      return true;
    } else {
      return false;
    }
  } // didPlayerNotChooseAnApple

  /**
   * Creates Player 2 of 9 Apples Morris, setting the apple of the player to the apple that they
   * chose in the activity.
   *
   * @return - the second player of the game
   */
  public Player createPlayer2() {
    ImageView chosenApplePlayer = (ImageView) findViewById(R.id.chosenAppleOfPlayer2);
    if (didPlayer2NotChooseAnApple()) { // if they click on start game without choosing an
      // apple, player's apple is red as default
      Player player2 = new Player(R.drawable.greenapple);
      chosenApplePlayer.setTag(R.drawable.greenapple);
      return player2;
    } else {
      Player player2 = new Player((Integer) chosenApplePlayer.getTag());
      return player2;
    }
  } // createPlayer2

  /**
   * Checks if Player 2 has the same apple as Player 1.
   *
   * @return - true if player 2 has the same apple as 1, false otherwise
   */
  public boolean doesPlayer2HaveSameAppleAsPlayer1() {
    ImageView chosenApplePlayer = (ImageView) findViewById(R.id.chosenAppleOfPlayer2);
    if (player1.getIdOfImageViewOfApple() == (Integer) chosenApplePlayer.getTag()) {
      return true;
    } else {
      return false;
    }
  } // doesPlayer2HaveSameAppleAsPlayer1

  /**
   * Starts a new game of 9 Apple's Morris, with the apple that player (or players) has chosen.
   *
   * @param aView
   */
  public void startGame(View aView) {
    Button startGameButton = (Button) aView;
    ImageButton nextButton = findViewById(R.id.nextAppleColour2);
    ImageButton previousButton = findViewById(R.id.previousAppleColour2);
    TextView errorText = (TextView) findViewById(R.id.errorText);
    Handler aHandler = new Handler();
    Intent toGameActivity = new Intent(this, GameActivity.class);
    Player player2 = createPlayer2();
    if (doesPlayer2HaveSameAppleAsPlayer1()) {
      errorText.setAlpha(1);
      startGameButton.setEnabled(false);
      nextButton.setEnabled(false);
      previousButton.setEnabled(false);
      aHandler.postDelayed(new Runnable() {
        public void run() {
          errorText.setAlpha(0);
          startGameButton.setEnabled(true);
          nextButton.setEnabled(true);
          previousButton.setEnabled(true);
          return;
        }
      }, 2500);
    } else {
      toGameActivity.putExtra("Player1", player1);
      toGameActivity.putExtra("Player2", player2);
      startActivity(toGameActivity);
    }
  } // startGame

} // class