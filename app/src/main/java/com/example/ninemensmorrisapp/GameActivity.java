package com.example.ninemensmorrisapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.Random;

/**
 * This class represents the game page of the game, 9 Apples Morris, where the functionality of the
 * game occurs.
 */
public class GameActivity extends AppCompatActivity {
  //Data ------------------------
  Player player1;
  Player player2;

  ImageButton[][] boardArray = new ImageButton[7][7];
  Board gameBoard;

  String turn = "";

  int cpuMovePhase = 1;
  int pieceClicked = 0;
  int xStart;
  int yStart;
  int yMove;
  int xMove;
  int difficulty;
  int clearHighlight;
  Computer cpu = new Computer();


  boolean computerEnabled;
  boolean computerEnabledMaster;

  //Data -------------------------------

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_game);
    Bundle playersFromCustomizeActivity = getIntent().getExtras();
    player1 = (Player) playersFromCustomizeActivity.getSerializable("Player1");
    player2 = (Player) playersFromCustomizeActivity.getSerializable("Player2");
    Bundle infoComingIn = getIntent().getExtras();
    computerEnabled = infoComingIn.getBoolean("cpuEnabled");
    difficulty = infoComingIn.getInt("difficulty");
    computerEnabledMaster = computerEnabled;
    initializeBoard();
    gameBoard = new Board(player1.getIdOfImageViewOfApple(), player2.getIdOfImageViewOfApple(), findViewById(R.id.displayTurn),
            findViewById(R.id.placeAppleOnTreeText), findViewById(R.id.numOfApplesLeftForPlayer1),
            findViewById(R.id.numOfApplesLeftForPlayer2), boardArray, getApplicationContext(), findViewById(R.id.displayCurrentApple));
    gameBoard.highlightLegalMovesPhase1();
    gameBoard.computerEnabled = computerEnabledMaster;
    cpu.cpuGameBoard = gameBoard;
    restartGame(this);
  } // onCreate

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflates the menu and adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.menu_main, menu);
    return true;
  } // onCreateOptionsMenu


  /**
   * Creates a 2d Array of buttons and assigns a button for every space on the board.
   */
  public void initializeBoard() {
    turn = "1";
    for (int x = 0; x < 7; x++) {
      for (int y = 0; y < 7; y++) {
        int tempX = x;
        int tempY = y;
        //Finds button associated with each coordinate point
        String buttonName = "button" + x + y;
        int buttonID = getResources().getIdentifier(buttonName, "id", getPackageName());
        //Assigns the two
        boardArray[x][y] = findViewById(buttonID);

        //If button exists on given coordinate add listener and make transparent
        try {
          boardArray[x][y].setBackground(null);
          boardArray[x][y].setBackgroundColor(Color.TRANSPARENT);
          boardArray[x][y].setTag("Initialized");
          boardArray[x][y].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              gameBoard.buttonPressed(view, tempX, tempY);
              if (gameBoard.computerEnabled && gameBoard.phase == 1 && gameBoard.turn == "2" && gameBoard.selectMethod != "remove") {
                if (difficulty == 1) {
                  cpuEasyPlace();
                }
                if (difficulty == 2) {
                  cpuHardPlace();
                }
              }
              if (gameBoard.computerEnabled && gameBoard.turn == "2" && gameBoard.selectMethod != "remove") {
                if (gameBoard.phase != 4) {
                  cpuMovePress();
                }
              }
              if (gameBoard.computerEnabled && gameBoard.turn == "1" && gameBoard.selectMethod == "remove") {
                cpuRemovePiece();
              }
            }
          });
        } catch (Exception e) {
          System.out.println("Button does not exist");
        }
      }
    }
  } // initializeBoard

  /**
   * Method controls how the easy computer places its pieces, backup for hard computer if there
   * are to mills to block.
   */
  public void cpuEasyPlace() {
    if (gameBoard.turn != "2") {
      return;
    }
    int x = 0;
    int y = 0;
    Random random = new Random();
    int tempX = random.nextInt(7);
    int tempY = random.nextInt(7);
    if (gameBoard.boardArray[tempX][tempY] != null) {
      if (gameBoard.boardArray[tempX][tempY].getTag() != "is1"
              && gameBoard.boardArray[tempX][tempY].getTag() != "is2") {
        gameBoard.boardArray[tempX][tempY].performClick();
      } else {
        cpuEasyPlace();
      }
    } else {
      cpuEasyPlace();
    }
  } // cpuEasyPlace

  /**
   * @param potentialX xCoord for potential piece
   * @param potentialY yCoord for potential piece
   * @return True if empty
   */
  public ImageButton checkIfEmpty(int potentialX, int potentialY) {
    ImageButton placeHere = null;
    String buttonName = "button" + potentialX + potentialY;
    int buttonID = getResources().getIdentifier(buttonName, "id",
            getPackageName());
    if (buttonID != 0) {
      placeHere = (ImageButton) findViewById(buttonID);
      if (placeHere.getTag() != "is2" && placeHere.getTag() != "is1") {
        return placeHere;
      }
    }
    return placeHere;
  } // checkIfEmpty

  /**
   * separates the steps for moving a piece
   */
  public void cpuMovePress() {
    if (cpuMovePhase == 1) {
      cpuPick();
    }
    if (cpuMovePhase == 2) {
      cpuMovePhase = 1;
      boardArray[xMove][yMove].performClick();
    }
  } // cpuMovePress()

  /**
   * Chooses the piece that the computer will move randomly/
   */
  public void cpuPick() {
    Random random = new Random();
    int x = random.nextInt(7);
    int y = random.nextInt(7);
    if (boardArray[x][y] != null) {
      if (boardArray[x][y].getTag() == "is2") {
        if (cpuCanMove(x, y)) {
          cpuMovePhase = 2;
          boardArray[x][y].performClick();
        } else {
          cpuPick();
        }
      } else {
        cpuPick();
      }
    } else {
      cpuPick();
    }
  } // cpuPick

  /**
   * Checks to see if the piece the computer wants to press has any spots to move to.
   *
   * @param x x value for the piece the computer wants to move
   * @param y y value for the piece the computer wants to move
   * @return false = piece cannot move
   */
  public Boolean cpuCanMove(int x, int y) {
    //================================== outerSquare ==============================//
    //topLeftCorner
    if (x == 0 && y == 6) {
      if (boardArray[3][6].getTag() != "is1"
              && boardArray[3][6].getTag() != "is2") {
        xMove = 3;
        yMove = 6;
        return true;
      }
      if (boardArray[0][3].getTag() != "is1"
              && boardArray[0][3].getTag() != "is2") {
        xMove = 0;
        yMove = 3;
        return true;
      }
    }
    //TopMiddle
    if (x == 3 && y == 6) {
      //Left
      if (boardArray[0][6].getTag() != "is1"
              && boardArray[0][6].getTag() != "is2") {
        xMove = 0;
        yMove = 6;
        return true;
      }
      //Down
      if (boardArray[3][5].getTag() != "is1"
              && boardArray[3][5].getTag() != "is2") {
        xMove = 3;
        yMove = 5;
        return true;
      }
      //Right
      if (boardArray[6][6].getTag() != "is1"
              && boardArray[6][6].getTag() != "is2") {
        xMove = 6;
        yMove = 6;
        return true;
      }
    }
    //TopRightCorner
    if (x == 6 && y == 6) {
      //Left
      if (boardArray[3][6].getTag() != "is1"
              && boardArray[3][6].getTag() != "is2") {
        xMove = 3;
        yMove = 6;
        return true;
      }//Down
      if (boardArray[6][3].getTag() != "is1"
              && boardArray[6][3].getTag() != "is2") {
        xMove = 6;
        yMove = 3;
        return true;
      }
    }
    //RightMiddle
    if (x == 6 && y == 3) {
      //Up
      if (boardArray[6][6].getTag() != "is1"
              && boardArray[6][6].getTag() != "is2") {
        xMove = 6;
        yMove = 6;
        return true;
      }
      //Left
      if (boardArray[5][3].getTag() != "is1"
              && boardArray[5][3].getTag() != "is2") {
        xMove = 5;
        yMove = 3;
        return true;
      }
      //Down
      if (boardArray[6][0].getTag() != "is1"
              && boardArray[6][0].getTag() != "is2") {
        xMove = 6;
        yMove = 0;
        return true;
      }
    }
    //BottomRightCorner
    if (x == 6 && y == 0) {
      //Up
      if (boardArray[6][3].getTag() != "is1"
              && boardArray[6][3].getTag() != "is2") {
        xMove = 6;
        yMove = 3;
        return true;
      }//Left
      if (boardArray[3][0].getTag() != "is1"
              && boardArray[3][0].getTag() != "is2") {
        xMove = 3;
        yMove = 0;
        return true;
      }
    }
    //BottomMiddle
    if (x == 3 && y == 0) {
      //Right
      if (boardArray[6][0].getTag() != "is1"
              && boardArray[6][0].getTag() != "is2") {
        xMove = 6;
        yMove = 0;
        return true;
      }
      //Up
      if (boardArray[3][1].getTag() != "is1"
              && boardArray[3][1].getTag() != "is2") {
        xMove = 3;
        yMove = 1;
        return true;
      }
      //Left
      if (boardArray[0][0].getTag() != "is1"
              && boardArray[0][0].getTag() != "is2") {
        xMove = 0;
        yMove = 0;
        return true;
      }
    }
    //BottomLeftCorner
    if (x == 0 && y == 0) {
      //Up
      if (boardArray[0][3].getTag() != "is1"
              && boardArray[0][3].getTag() != "is2") {
        xMove = 0;
        yMove = 3;
        return true;
      }//Right
      if (boardArray[3][0].getTag() != "is1"
              && boardArray[3][0].getTag() != "is2") {
        xMove = 3;
        yMove = 0;
        return true;
      }
    }
    //LeftMiddle
    if (x == 0 && y == 3) {
      //Up
      if (boardArray[0][6].getTag() != "is1"
              && boardArray[0][6].getTag() != "is2") {
        xMove = 0;
        yMove = 6;
        return true;
      }
      //Right
      if (boardArray[1][3].getTag() != "is1"
              && boardArray[1][3].getTag() != "is2") {
        xMove = 1;
        yMove = 3;
        return true;
      }
      //Down
      if (boardArray[0][0].getTag() != "is1"
              && boardArray[0][0].getTag() != "is2") {
        xMove = 0;
        yMove = 0;
        return true;
      }
    }
    //================================= MiddleSquare ==============================//
    //topLeftCorner
    if (x == 1 && y == 5) {
      //Right
      if (boardArray[3][5].getTag() != "is1"
              && boardArray[3][5].getTag() != "is2") {
        xMove = 3;
        yMove = 5;
        return true;
      }
      //Down
      if (boardArray[1][3].getTag() != "is1"
              && boardArray[1][3].getTag() != "is2") {
        xMove = 1;
        yMove = 3;
        return true;
      }
    }
    //TopMiddle
    if (x == 3 && y == 5) {
      //Left
      if (boardArray[1][5].getTag() != "is1"
              && boardArray[1][5].getTag() != "is2") {
        xMove = 1;
        yMove = 5;
        return true;
      }
      //Down
      if (boardArray[3][4].getTag() != "is1"
              && boardArray[3][4].getTag() != "is2") {
        xMove = 3;
        yMove = 4;
        return true;
      }
      //Right
      if (boardArray[5][5].getTag() != "is1"
              && boardArray[5][5].getTag() != "is2") {
        xMove = 5;
        yMove = 5;
        return true;
      }
      //Up
      if (boardArray[3][6].getTag() != "is1"
              && boardArray[3][6].getTag() != "is2") {
        xMove = 3;
        yMove = 6;
        return true;
      }
    }
    //TopRightCorner
    if (x == 5 && y == 5) {
      //Left
      if (boardArray[3][5].getTag() != "is1"
              && boardArray[3][5].getTag() != "is2") {
        xMove = 3;
        yMove = 5;
        return true;
      }//Down
      if (boardArray[5][3].getTag() != "is1"
              && boardArray[5][3].getTag() != "is2") {
        xMove = 5;
        yMove = 3;
        return true;
      }
    }
    //RightMiddle
    if (x == 5 && y == 3) {
      //Up
      if (boardArray[5][5].getTag() != "is1"
              && boardArray[5][5].getTag() != "is2") {
        xMove = 5;
        yMove = 5;
        return true;
      }
      //Left
      if (boardArray[4][3].getTag() != "is1"
              && boardArray[4][3].getTag() != "is2") {
        xMove = 4;
        yMove = 3;
        return true;
      }
      //Down
      if (boardArray[5][1].getTag() != "is1"
              && boardArray[5][1].getTag() != "is2") {
        xMove = 5;
        yMove = 1;
        return true;
      }
      //Right
      if (boardArray[6][3].getTag() != "is1"
              && boardArray[6][3].getTag() != "is2") {
        xMove = 6;
        yMove = 3;
        return true;
      }
    }
    //BottomRightCorner
    if (x == 5 && y == 1) {
      //Up
      if (boardArray[5][3].getTag() != "is1"
              && boardArray[5][3].getTag() != "is2") {
        xMove = 5;
        yMove = 3;
        return true;
      }//Left
      if (boardArray[3][1].getTag() != "is1"
              && boardArray[3][1].getTag() != "is2") {
        xMove = 3;
        yMove = 1;
        return true;
      }
    }
    //BottomMiddle
    if (x == 3 && y == 1) {
      //Right
      if (boardArray[5][1].getTag() != "is1"
              && boardArray[5][1].getTag() != "is2") {
        xMove = 5;
        yMove = 1;
        return true;
      }
      //Up
      if (boardArray[3][2].getTag() != "is1"
              && boardArray[3][2].getTag() != "is2") {
        xMove = 3;
        yMove = 2;
        return true;
      }
      //Left
      if (boardArray[1][1].getTag() != "is1"
              && boardArray[1][1].getTag() != "is2") {
        xMove = 1;
        yMove = 1;
        return true;
      }
      //Down
      if (boardArray[3][0].getTag() != "is1"
              && boardArray[3][0].getTag() != "is2") {
        xMove = 3;
        yMove = 0;
        return true;
      }
    }
    //BottomLeftCorner
    if (x == 1 && y == 1) {
      //Up
      if (boardArray[1][3].getTag() != "is1"
              && boardArray[1][3].getTag() != "is2") {
        xMove = 1;
        yMove = 3;
        return true;
      }//Right
      if (boardArray[3][1].getTag() != "is1"
              && boardArray[3][1].getTag() != "is2") {
        xMove = 3;
        yMove = 1;
        return true;
      }
    }
    //LeftMiddle
    if (x == 1 && y == 3) {
      //Up
      if (boardArray[1][5].getTag() != "is1"
              && boardArray[1][5].getTag() != "is2") {
        xMove = 1;
        yMove = 5;
        return true;
      }
      //Right
      if (boardArray[2][3].getTag() != "is1"
              && boardArray[2][3].getTag() != "is2") {
        xMove = 2;
        yMove = 3;
        return true;
      }
      //Down
      if (boardArray[1][1].getTag() != "is1"
              && boardArray[1][1].getTag() != "is2") {
        xMove = 1;
        yMove = 1;
        return true;
      }
      //Left
      if (boardArray[0][3].getTag() != "is1"
              && boardArray[0][3].getTag() != "is2") {
        xMove = 0;
        yMove = 3;
        return true;
      }
    }
    //================================== InnerSquare ==============================//
    //topLeftCorner
    if (x == 2 && y == 4) {
      //Right
      if (boardArray[3][4].getTag() != "is1"
              && boardArray[3][4].getTag() != "is2") {
        xMove = 3;
        yMove = 4;
        return true;
      }//Down
      if (boardArray[2][3].getTag() != "is1"
              && boardArray[2][3].getTag() != "is2") {
        xMove = 2;
        yMove = 3;
        return true;
      }
    }
    //TopMiddle
    if (x == 3 && y == 4) {
      //Left
      if (boardArray[2][4].getTag() != "is1"
              && boardArray[2][4].getTag() != "is2") {
        xMove = 2;
        yMove = 4;
        return true;
      }
      //Up
      if (boardArray[3][5].getTag() != "is1"
              && boardArray[3][5].getTag() != "is2") {
        xMove = 3;
        yMove = 5;
        return true;
      }
      //Right
      if (boardArray[4][4].getTag() != "is1"
              && boardArray[4][4].getTag() != "is2") {
        xMove = 4;
        yMove = 4;
        return true;
      }
    }
    //TopRightCorner
    if (x == 4 && y == 4) {
      //Left
      if (boardArray[3][4].getTag() != "is1"
              && boardArray[3][4].getTag() != "is2") {
        xMove = 3;
        yMove = 4;
        return true;
      }//Down
      if (boardArray[3][4].getTag() != "is1"
              && boardArray[3][4].getTag() != "is2") {
        xMove = 3;
        yMove = 4;
        return true;
      }
    }
    //RightMiddle
    if (x == 4 && y == 3) {
      //Up
      if (boardArray[4][4].getTag() != "is1"
              && boardArray[4][4].getTag() != "is2") {
        xMove = 4;
        yMove = 4;
        return true;
      }
      //Right
      if (boardArray[5][3].getTag() != "is1"
              && boardArray[5][3].getTag() != "is2") {
        xMove = 5;
        yMove = 3;
        return true;
      }
      //Down
      if (boardArray[4][2].getTag() != "is1"
              && boardArray[4][2].getTag() != "is2") {
        xMove = 4;
        yMove = 2;
        return true;
      }
    }
    //BottomRightCorner
    if (x == 4 && y == 2) {
      //Up
      if (boardArray[4][3].getTag() != "is1"
              && boardArray[4][3].getTag() != "is2") {
        xMove = 4;
        yMove = 3;
        return true;
      }//Left
      if (boardArray[3][2].getTag() != "is1"
              && boardArray[3][2].getTag() != "is2") {
        xMove = 3;
        yMove = 2;
        return true;
      }
    }
    //BottomMiddle
    if (x == 3 && y == 2) {
      //Right
      if (boardArray[4][2].getTag() != "is1"
              && boardArray[4][2].getTag() != "is2") {
        xMove = 4;
        yMove = 2;
        return true;
      }
      //Down
      if (boardArray[3][1].getTag() != "is1"
              && boardArray[3][1].getTag() != "is2") {
        xMove = 3;
        yMove = 1;
        return true;
      }
      //Left
      if (boardArray[2][2].getTag() != "is1"
              && boardArray[2][2].getTag() != "is2") {
        xMove = 2;
        yMove = 2;
        return true;
      }
    }
    //BottomLeftCorner
    if (x == 2 && y == 2) {
      //Up
      if (boardArray[2][3].getTag() != "is1"
              && boardArray[2][3].getTag() != "is2") {
        xMove = 2;
        yMove = 3;
        return true;
      }//Right
      if (boardArray[3][2].getTag() != "is1"
              && boardArray[3][2].getTag() != "is2") {
        xMove = 3;
        yMove = 2;
        return true;
      }
    }
    //LeftMiddle
    if (x == 2 && y == 3) {
      //Up
      if (boardArray[2][4].getTag() != "is1"
              && boardArray[2][4].getTag() != "is2") {
        xMove = 2;
        yMove = 4;
        return true;
      }
      //Left
      if (boardArray[1][3].getTag() != "is1"
              && boardArray[1][3].getTag() != "is2") {
        xMove = 1;
        yMove = 3;
        return true;
      }
      //Down
      if (boardArray[2][2].getTag() != "is1"
              && boardArray[2][2].getTag() != "is2") {
        xMove = 2;
        yMove = 2;
        return true;
      }
    }

    return false;
  } // cpuCanMove

  /**
   * Checks the board for all possible places that the player might have a rwo to two pieces,
   * if there is the computer will block the mill by placing a piece in the empty spot.
   * the method is broken up by square and then by column and row with each spot checked.
   * If there is no mills to block, runs cpuEasyPlace()
   */
  public void cpuHardPlace() {
    //============================== Outer Square =============================//
    //------------------------------ bottomRow
    //RightCorner Empty
    checkIfPlaceable(6, 0, 3, 0, 0, 0, "is1");
    //MiddleEmpty
    checkIfPlaceable(3, 0, 0, 6, 0, 0, "is1");
    //LeftCornerEmpty
    checkIfPlaceable(0, 0, 3, 6, 0, 0, "is1");
    //-----------------------------------leftColumn
    //TopCornerEmpty
    checkIfPlaceable(0, 6, 0, 0, 0, 3, "is1");
    //MiddleEmpty
    checkIfPlaceable(0, 3, 0, 0, 0, 6, "is1");
    //BottomCornerEmpty
    checkIfPlaceable(0, 0, 0, 0, 3, 6, "is1");
    //------------------------------------TopRow
    //RightCornerEmpty
    checkIfPlaceable(6, 6, 0, 3, 6, 6, "is1");
    //MiddleEmpty
    checkIfPlaceable(3, 6, 0, 6, 6, 6, "is1");
    //LeftCornerEmpty
    checkIfPlaceable(0, 6, 3, 6, 6, 6, "is1");
    //------------------------------------ RightColumn -------------------------------//
    //TopCornerEmpty
    checkIfPlaceable(6, 6, 6, 6, 3, 0, "is1");
    //MiddleEmpty
    checkIfPlaceable(6, 3, 6, 6, 6, 0, "is1");
    //BottomColumn
    checkIfPlaceable(6, 0, 6, 6, 6, 3, "is1");
    //==================================== Middle Square =============================//
    //------------------------------------ TopRow      -------------------------------//
    //LeftCorner
    checkIfPlaceable(1, 5, 3, 5, 5, 5, "is1");
    //MiddleEmpty
    checkIfPlaceable(3, 5, 1, 5, 5, 5, "is1");
    //RightCorner
    checkIfPlaceable(5, 5, 1, 3, 5, 5, "is1");
    //------------------------------------ RightColumn -------------------------------//
    //TopCorner
    checkIfPlaceable(5, 5, 5, 5, 3, 1, "is1");
    //MiddleEmpty
    checkIfPlaceable(5, 3, 5, 5, 5, 1, "is1");
    //BottomCorner
    checkIfPlaceable(5, 1, 5, 5, 5, 3, "is1");
    //------------------------------------ BottomRow   -------------------------------//
    //RightCorner
    checkIfPlaceable(5, 1, 3, 1, 1, 1, "is1");
    //MiddleEmpty
    checkIfPlaceable(3, 1, 5, 1, 1, 1, "is1");
    //LeftCorner
    checkIfPlaceable(1, 1, 5, 3, 1, 1, "is1");
    //------------------------------------ leftColumn  -------------------------------//
    //BottomCorner
    checkIfPlaceable(1, 1, 1, 1, 3, 5, "is1");
    //MiddleEmpty
    checkIfPlaceable(1, 3, 1, 1, 1, 5, "is1");
    //TopCorner
    checkIfPlaceable(1, 5, 1, 1, 3, 3, "is1");
    //==================================== inner Square =============================//
    //------------------------------------ TopRow      -------------------------------//
    //LeftCorner
    checkIfPlaceable(2, 4, 3, 4, 4, 4, "is1");
    //MiddleEmpty
    checkIfPlaceable(3, 4, 2, 4, 4, 4, "is1");
    //RightCorner
    checkIfPlaceable(4, 4, 2, 3, 4, 4, "is1");
    //------------------------------------ RightColumn -------------------------------//
    //TopCorner
    checkIfPlaceable(4, 4, 4, 4, 3, 2, "is1");
    //MiddleEmpty
    checkIfPlaceable(4, 3, 4, 4, 2, 4, "is1");
    //BottomCorner
    checkIfPlaceable(4, 2, 4, 4, 3, 4, "is1");
    //------------------------------------ BottomRow   -------------------------------//
    //RightCorner
    checkIfPlaceable(4, 2, 3, 2, 2, 2, "is1");
    //MiddleEmpty
    checkIfPlaceable(3, 2, 4, 2, 2, 2, "is1");
    //LeftCorner
    checkIfPlaceable(2, 2, 4, 3, 2, 2, "is1");
    //------------------------------------ leftColumn  -------------------------------//
    //BottomCorner
    checkIfPlaceable(2, 2, 2, 2, 3, 4, "is1");
    //MiddleEmpty
    checkIfPlaceable(2, 3, 2, 2, 2, 4, "is1");

    //TopCorner
    checkIfPlaceable(2, 4, 2, 2, 2, 3, "is1");
    //==================================== SquareConnectors  =========================//
    //------------------------------------ North  -------------------------------//
    //Top
    checkIfPlaceable(3, 6, 3, 3, 5, 4, "is1");
    //Middle
    checkIfPlaceable(3, 5, 3, 3, 6, 4, "is1");
    //Bottom
    checkIfPlaceable(3, 4, 3, 3, 6, 5, "is1");
    //------------------------------------ East  -------------------------------//
    //Left
    checkIfPlaceable(4, 3, 5, 6, 3, 3, "is1");
    //Middle
    checkIfPlaceable(5, 3, 4, 6, 3, 3, "is1");
    //Right
    checkIfPlaceable(6, 3, 4, 5, 3, 3, "is1");
    //------------------------------------ South  -------------------------------//
    //Top
    checkIfPlaceable(3, 2, 3, 3, 1, 0, "is1");
    //Middle
    checkIfPlaceable(3, 1, 3, 3, 2, 0, "is1");
    //Bottom
    checkIfPlaceable(3, 0, 3, 3, 2, 1, "is1");
    //------------------------------------ West  -------------------------------//
    //Right
    checkIfPlaceable(2, 3, 1, 0, 3, 3, "is1");
    //Middle
    checkIfPlaceable(1, 3, 2, 0, 3, 3, "is1");
    //Left
    checkIfPlaceable(0, 3, 2, 1, 3, 3, "is1");
    //if there are no mills to block, place randomly
    cpuEasyPlace();
  } // cpuHardPlace


  /**
   * Checks to see if the spot selected is empty. if it is empty places the computer piece.
   *
   * @param xEmpty   empty spot x value
   * @param yEmpty   empty spot y value
   * @param xOneFull player one's first piece x value
   * @param xTwoFull player one's second piece x value
   * @param yOneFull player one's first piece y value
   * @param yTwoFull player one's second piece y value
   * @param tag      identifier of the enemy piece, currently the player piece
   */
  public void checkIfPlaceable(int xEmpty, int yEmpty, int xOneFull, int xTwoFull, int yOneFull,
                               int yTwoFull, String tag) {
    if (gameBoard.turn == "2") {
      cpuMovePhase = 1;
    }
    if (gameBoard.boardArray[xOneFull][yOneFull].getTag() == tag
            && gameBoard.boardArray[xTwoFull][yTwoFull].getTag() == tag) {
      if (gameBoard.boardArray[xEmpty][yEmpty].getTag() != "is1"
              && gameBoard.boardArray[xEmpty][yEmpty].getTag() != "is2") {
        if (cpuMovePhase == 1) {
          if (gameBoard.phase == 1) {
            cpuMovePhase = 2;
          }
          gameBoard.boardArray[xEmpty][yEmpty].performClick();
        }
      }
    }
  } // checkIfPlaceable

  public void cpuRemovePiece() {
    Random random = new Random();
    int x = random.nextInt(7);
    int y = random.nextInt(7);

    if (boardArray[x][y] != null) {
      if (boardArray[x][y].getTag() == "is1") {
        if (gameBoard.checkMill(x, y) == false) {
          clearHighlight = 1;
          boardArray[x][y].performClick();
        } else {
          cpuRemovePiece();
        }
      } else {
        cpuRemovePiece();
      }
    } else {
      cpuRemovePiece();
    }
  } // cpuRemovePiece

  /**
   * Creates and displays the drop down popup menu.
   *
   * @param aView - current View of the application
   */
  public void showPopupMenu(View aView) {
    PopupMenu popupMenu = new PopupMenu(this, aView);
    MenuInflater menuInflater = popupMenu.getMenuInflater();
    Intent toMainActivity = new Intent(this, MainActivity.class);
    menuInflater.inflate(R.menu.menu_main, popupMenu.getMenu());
    popupMenu.show();
    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
      @Override
      public boolean onMenuItemClick(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
          case R.id.item_main_menu:
            startActivity(toMainActivity);
            return true;
          case R.id.item_rules:
            displayRulesPopup(aView);
            return true;
          default:
            return false;
        }
      } // onMenuItemClick
    });
  } // showPopupMenu

  /**
   * Restarts the game.
   *
   * @param gameActivity the current Game Activity
   */
  public void restartGame(Activity gameActivity) {
    ImageButton restartButton = (ImageButton) findViewById(R.id.restartButton);
    restartButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        gameActivity.recreate();
      }
    });
  } // restartGame

  /**
   * Creates and displays all of the rules popup windows.
   *
   * @param aView the current View of the game
   */
  public void displayRulesPopup(View aView) {
    PopupWindow rulesPage1 = createPopUp(R.layout.rules_game_popup_window);
    getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    rulesPage1.showAtLocation(aView, Gravity.CENTER, 0, 0);
    dimBehindPopUp(rulesPage1);
    dismissRulesPopup(rulesPage1, R.id.closeButtonRules);
  } // displayRulesPopup

  /**
   * Dismisses the popup displaying the last set of rules.
   *
   * @param currentPopup the current rules popup window
   * @param buttonId     the button ID of the close rules button
   */
  private void dismissRulesPopup(PopupWindow currentPopup, int buttonId) {
    ImageButton closeRulesButton = (ImageButton) currentPopup.getContentView().findViewById(buttonId);
    closeRulesButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        currentPopup.dismiss();
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
      }
    });
  } // dismissRulesPopup

  /**
   * Creates a popup window.
   *
   * @param layoutId the XML layout ID of the last rules popup window
   * @return - a popup window
   */
  private PopupWindow createPopUp(int layoutId) {
    // inflates layout of the popup window
    LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
    View popupView = inflater.inflate(layoutId, null);
    int width = 1000;
    int height = 1500;
    final PopupWindow popupWindow = new PopupWindow(popupView, width, height);
    return popupWindow;
  } // createPopUp

  /**
   * Dims the background behind the popup view, making it more readable and visible to the user.
   *
   * @param popupWindow the current popup window
   */
  public static void dimBehindPopUp(PopupWindow popupWindow) {
    View aView = popupWindow.getContentView().getRootView();
    // gets context of the current state of the object (popupWindow)
    Context context = popupWindow.getContentView().getContext();
    WindowManager aWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
    WindowManager.LayoutParams layoutParams = (WindowManager.LayoutParams) aView.getLayoutParams();
    layoutParams.flags |= WindowManager.LayoutParams.FLAG_DIM_BEHIND;
    layoutParams.dimAmount = 0.5f;
    aWindowManager.updateViewLayout(aView, layoutParams);
  } // dimBehindPopUp

} // class