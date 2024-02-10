package com.example.ninemensmorrisapp;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

/**
 * This class creates the board and handles all the functionality for the game, 9 Apples Morris.
 */
public class Board extends AppCompatActivity {
  // Data ------------------------

  int phase = 1;
  int moveFrom2X;
  int moveFrom2Y;
  int moveFrom3X;
  int moveFrom3Y;
  int previouslyHighlightedPieceX = -1;
  int previouslyHighlightedPieceY = -1;
  int xRemove;
  int yRemove;
  Boolean computerEnabled;
  Player player1;
  Player player2;
  String turn = "1";
  String selectMethod = "";
  String player1Phase3MoveLock = "Locked";
  String player2Phase3MoveLock = "Locked";
  TextView displayTurn;
  TextView displayTurn2;
  TextView applesLeftPlayer1;
  TextView applesLeftPlayer2;
  ImageButton[][] boardArray;
  Context context;
  ImageView displayCurrentApple;
  ArrayList<ImageButton> listOfAdjacentSpacesPhase2 = new ArrayList<ImageButton>();
  ArrayList<ImageButton> listOfAdjacentSpacesPhase3;
  Computer cpu = new Computer();

  //Methods ---------------------

  /**
   * Creates the Board class for the game.
   *
   * @param player1Apple      the apple ID of the first player
   * @param player2Apple      the apple ID of the second player
   * @param displayTurn       TextView displaying whose turn it is
   * @param displayTurn2      TextView displaying which phase the game is at
   * @param applesLeftPlayer1 TextView displaying
   * @param applesLeftPlayer2
   * @param boardArray
   */
  public Board(int player1Apple, int player2Apple, TextView displayTurn, TextView displayTurn2,
               TextView applesLeftPlayer1, TextView applesLeftPlayer2, ImageButton[][] boardArray,
               Context context, ImageView displayCurrentApple) {
    player1 = new Player(player1Apple);
    player2 = new Player(player2Apple);
    this.displayTurn = displayTurn;
    this.displayTurn2 = displayTurn2;
    this.applesLeftPlayer1 = applesLeftPlayer1;
    this.applesLeftPlayer2 = applesLeftPlayer2;
    this.boardArray = boardArray;
    this.context = context;
    this.displayCurrentApple = displayCurrentApple;
    displayCurrentApple.setImageResource(player1.getIdOfImageViewOfApple());
  } // Board

  /**
   * Finds the current piece.
   */
  public void findPiece() {
    displayTurn2.setText("millMove");
    Random random = new Random();
    int x = random.nextInt(7);
    int y = random.nextInt(7);
    if (boardArray[x][y] != null) {
      if (boardArray[x][y].getTag() == "is1") {
        displayTurn2.setText("Remove " + x + y);
        xRemove = x;
        yRemove = y;

      } else {
        findPiece();
      }
    } else {
      findPiece();
    }
  } // findPiece

  /**
   * If button is pressed this method will direct program to appropriate method based
   * on which phase the game is in
   *
   * @param myView the button to be pressed
   * @param xCord  the x coordinate of the button pressed
   * @param yCord  the y coordinate of the button pressed
   */
  protected void buttonPressed(View myView, int xCord, int yCord) {
    ImageButton myButton = (ImageButton) myView;
    // Removes apple
    if (selectMethod == "remove") { //checks to see  selectedMethod is "remove"
      removeApple(myButton, xCord, yCord);
      return;
    } else if ((turn == "1" && player1Phase3MoveLock == "Unlocked") ||
            (turn == "2" && player2Phase3MoveLock == "Unlocked")) {
      doPhase3(xCord, yCord);
    } else if (phase == 1) {
      doPhase1(myButton, xCord, yCord);
    } else if (phase == 2) {
      doPhase2(xCord, yCord);
    }
  } // buttonPressed

  /**
   * Checks for errors and mills on placements in phase 1, then begins placement of apples
   *
   * @param myButton - The space chosen by the player
   * @param xCord    - The x coordinate of the space
   * @param yCord    - The y coordinate of the space
   */
  protected void doPhase1(ImageButton myButton, int xCord, int yCord) {
    // Checks if space occupied
    if (checkIfSpaceFull(xCord, yCord) == true) {
      displaySpaceOccupiedToast();
      return;
    }
    //Place Apples
    if (turn == "1") {
      resolveApplePlacement(player1, myButton);
    } else {
      resolveApplePlacement(player2, myButton);
    }
    // Check for mills at the end of placement
    if (checkMill(xCord, yCord)) {
      selectMethod = "remove";
      displayMadeABranchOfApplesText();
    }
    // Check for mill on last turn of phase 1
    if (checkLastMillPhase1(xCord, yCord)) {
      return;
    }
    highlightLegalMovesPhase1();
    checkPhase2();
    switchTurn();
  } // doPhase1

  /**
   * Decides whether player is selecting apple to move or space to move the apple to in phase 2
   *
   * @param xCord - The x coordinate of the space
   * @param yCord - The y coordinate of the space
   */
  protected void doPhase2(int xCord, int yCord) {
    //Selected apple should be moved
    if (selectMethod == "") {
      phase2Select(xCord, yCord);
    }
    //Selected button is destination for apple
    else if (selectMethod == "phase2MoveTo") {
      phase2Move(xCord, yCord);
    }
  } // doPhase2

  /**
   * Checks for errors with selected apple, then stores coordinates of apple to be moved
   *
   * @param xCord - The x coordinate of the space
   * @param yCord - The y coordinate of the space
   */
  protected void phase2Select(int xCord, int yCord) {
    // Check if stone is correct
    if (turn == "1" && boardArray[xCord][yCord].getTag() == "is2" || turn == "2" &&
            boardArray[xCord][yCord].getTag() == "is1") {
      displayWrongAppleToast();
      return;
    }
    // Check if space is empty
    if (!checkIfSpaceFull(xCord, yCord)) {
      return;
    }
    moveFrom2X = xCord;
    moveFrom2Y = yCord;
    highlightPiece(xCord, yCord);
    highlightLegalMovesPhase2();
    if (listOfAdjacentSpacesPhase2.size() == 0) {
      displayAppleHasNoMovesToast();
      return;
    }
    selectMethod = "phase2MoveTo";
    return;
  } // phase2Select

  /**
   * Places apple and then handles highlights. Also checks for mills
   *
   * @param xCord - The x coordinate of the space
   * @param yCord - The y coordinate of the space
   */
  protected void phase2Move(int xCord, int yCord) {
    if (phase2MoveTo(xCord, yCord, listOfAdjacentSpacesPhase2) == false) {
      return;
    }
    if (checkMill(xCord, yCord) == true) {
      selectMethod = "remove";
    } else {
      selectMethod = "";
    }
    clearHighlightedPiece();
    clearHighlightedMoves();
    checkPhase3();
    switchTurn();
  } // phase2Move

  /**
   * Handles movement and error checking for movements in phase 2
   *
   * @param xCord - x coordinate of where piece is getting moved
   * @param yCord - y coordinate of where piece is getting moved
   * @return - returns true if piece successfully moved, false otherwise
   */
  protected boolean phase2MoveTo(int xCord, int yCord, ArrayList adjacentSpaces) {
    //Gets ID of apple being moved
    int appleID;
    if (turn == "1") {
      appleID = player1.getAppleOfPlayer();
    } else {
      appleID = player2.getAppleOfPlayer();
    }
    // Checking whether spaces are adjacent and empty
    if (!listOfAdjacentSpacesPhase2.contains(boardArray[xCord][yCord])) {
      displayInvalidSpaceToast();
      return false;
    }
    //Clears space where apple was moved from
    boardArray[moveFrom2X][moveFrom2Y].setTag("Initialized");
    boardArray[moveFrom2X][moveFrom2Y].setBackgroundColor(Color.TRANSPARENT);
    //Set tag and apple image in new location
    if (turn == "1") {
      boardArray[xCord][yCord].setTag("is1");
    } else {
      boardArray[xCord][yCord].setTag("is2");
    }
    boardArray[xCord][yCord].setBackgroundResource(appleID);
    return true;
  }// phase2MoveTo

  /**
   * Decides whether player is selecting apple to move or space to move the apple to in phase 3
   *
   * @param xCord - The x coordinate of the space
   * @param yCord - The y coordinate of the space
   */
  protected void doPhase3(int xCord, int yCord) {
    // Check if stone is correct
    if (turn == "1" && boardArray[xCord][yCord].getTag() == "is2" || turn == "2" &&
            boardArray[xCord][yCord].getTag() == "is1") {
      displayWrongAppleToast();
      return;
    }
    if (selectMethod == "") {
      phase3Select(xCord, yCord);
    } else if (selectMethod == "phase3MoveTo") {
      phase3Move(xCord, yCord);
    }
  } // doPhase3

  /**
   * Stores coordinates of apple to be moved, highlights apple selected and potential spots to move to
   *
   * @param xCord - The x coordinate of the space
   * @param yCord - The y coordinate of the space
   */
  protected void phase3Select(int xCord, int yCord) {
    moveFrom3X = xCord;
    moveFrom3Y = yCord;
    highlightPiece(xCord, yCord);
    selectMethod = "phase3MoveTo";
    highlightLegalMovesPhase3();
    return;
  } // phase3Select

  /**
   * Places apple and then handles highlights. Also checks for mills and for a win
   *
   * @param xCord - The x coordinate of the space
   * @param yCord - The y coordinate of the space
   */
  protected void phase3Move(int xCord, int yCord) {
    if (phase3MoveTo(xCord, yCord) == false) {
      return;
    }
    if (checkMill(xCord, yCord) == true) {
      selectMethod = "remove";
    } else {
      selectMethod = "";
    }

    if (checkWin() == true) {
      sendToWinnerActivity();
    }
    clearHighlightedPiece();
    clearHighlightedMoves();
    switchTurn();
  } // phase3Move

  /**
   * Handles movement and error checking for movements in phase 3.
   *
   * @param xCord x coordinate of where piece is getting moved
   * @param yCord y coordinate of where piece is getting moved
   * @return - returns true if piece successfully moved, false otherwise
   */
  protected boolean phase3MoveTo(int xCord, int yCord) {
    //Check if space is empty
    if (checkIfSpaceFull(xCord, yCord)) {
      displaySpaceOccupiedToast();
      return false;
    }
    //Gets ID of players apple
    int appleID;
    if (turn == "1") {
      appleID = player1.getAppleOfPlayer();
    } else {
      appleID = player2.getAppleOfPlayer();
    }
    boardArray[moveFrom3X][moveFrom3Y].setTag("Initialized");
    boardArray[moveFrom3X][moveFrom3Y].setBackgroundColor(Color.TRANSPARENT);
    //Set tag
    if (turn == "1") {
      boardArray[xCord][yCord].setTag("is1");
    } else {
      boardArray[xCord][yCord].setTag("is2");
    }
    boardArray[xCord][yCord].setBackgroundResource(appleID);
    return true;
  } // phase3MoveTo

  /**
   * Displays a short toast, informing the player that the space they've chosen is occupied.
   */
  public void displaySpaceOccupiedToast() {
    CharSequence noSpaceText = "That space is occupied!";
    int durationOfToast = Toast.LENGTH_SHORT;
    Toast spaceOccupiedToast = Toast.makeText(context, noSpaceText, durationOfToast);
    spaceOccupiedToast.show();
  } // displaySpaceOccupiedToast

  /**
   * Removes the given apple from the board
   *
   * @param myButton
   */
  protected void removeApple(ImageButton myButton, int xCord, int yCord) {
    //Wrong apple checks
    if (turn == "1") {
      if (myButton.getTag() == "is2") {
        displayWrongAppleToast();
        return;
      }
    } else {
      if (myButton.getTag() == "is1") {
        displayWrongAppleToast();
        return;
      }
    }
    if (myButton.getTag() == "Initialized") {
      if (phase == 1) {
        highlightLegalMovesPhase1();
      }
      return;
    }
    //Checks if Apple is part of mill
    if (checkMill(xCord, yCord) == true) {
      displayTurn2.setText("This apple is a part of a mill");
      return;
    }
    //Resets select method
    selectMethod = "";
    // removes apple
    myButton.setBackgroundColor(Color.TRANSPARENT);
    myButton.setTag("Initialized");
    //Resets highlights if in phase 1
    if (phase == 1) {
      highlightLegalMovesPhase1();
    }
    //Decreases apples on trees
    if (turn == "1") {
      clearHighlightedMoves();
      player1.setNumOfApplesOnTree(player1.getNumOfApplesOnTree() - 1);
    } else {
      player2.setNumOfApplesOnTree(player2.getNumOfApplesOnTree() - 1);
    }
    // Phase and win checks
    if (phase == 1) {
      checkPhase2();
    } else if (checkWin() == true) {
      sendToWinnerActivity();
      return;
    } else if (phase == 2) {
      checkPhase3();
    }
    displayTurn.setText("Player " + (turn) + "'s Turn");
    return;
  } // removeApple

  /**
   * Displays a short toast, informing the player that the space they've chosen is invalid.
   */
  public void displayInvalidSpaceToast() {
    CharSequence noSpaceText = "That space is invalid!";
    int durationOfToast = Toast.LENGTH_SHORT;
    Toast spaceOccupiedToast = Toast.makeText(context, noSpaceText, durationOfToast);
    spaceOccupiedToast.show();
  } // displayInvalidSpaceToast

  /**
   * Displays a short toast, informing the player that there are no available moves.
   */
  public void displayAppleHasNoMovesToast() {
    CharSequence hasNoMovesText = "There are no available moves, Player " + (turn) + "!";
    int durationOfToast = Toast.LENGTH_SHORT;
    Toast noMovesToast = Toast.makeText(context, hasNoMovesText, durationOfToast);
    noMovesToast.show();
  } // displayAppleHasNoMovesToast

  /**
   * Displays a short toast, informing the player that the apple they tried to select isn't their
   * apple.
   */
  public void displayWrongAppleToast() {
    CharSequence wrongAppleText = "This isn't your apple, Player " + (turn) + "!";
    int durationOfToast = Toast.LENGTH_SHORT;
    Toast wrongAppleToast = Toast.makeText(context, wrongAppleText,
            durationOfToast);
    wrongAppleToast.show();
  } // displayWrongAppleToast

  /**
   * Displays a short toast, informing the player that the apple they tried to select isn't their
   * apple.
   */
  public void displayMadeABranchOfApplesText() {
    CharSequence madeABranchOfApplesText = "Player " + (turn) + " has created a branch of apples!";
    int durationOfToast = Toast.LENGTH_LONG;
    Toast madeBranchOfApples = Toast.makeText(context, madeABranchOfApplesText,
            durationOfToast);
    madeBranchOfApples.show();
  } // displayMadeABranchOfApplesText

  /**
   * Checks whether the game has entered phase 2.
   */
  protected void checkPhase2() {
    if (player1.getNumOfApples() == 0 && player2.getNumOfApples() == 0) {
      phase = 2;
      displayTurn2.setText("Move your apple on the tree!");
      selectMethod = "";
      clearHighlightedMoves();
    }
  } // checkPhase2

  /**
   * Checks whether someone has won the game
   *
   * @return - returns true if someone has won, false otherwise
   */
  protected boolean checkWin() {
    return player1.getNumOfApplesOnTree() == 2 || player2.getNumOfApplesOnTree() == 2;
  } // checkWin

  /**
   * Checks whether the game has entered phase 3.
   */
  protected void checkPhase3() {
    if (player1.getNumOfApplesOnTree() == 3) {
      player1Phase3MoveLock = "Unlocked";
      if (selectMethod != "remove") {
        selectMethod = "";
      }
      displayTurn2.setText("All restrictions on Player " + (turn) + " have been lifted!");
      clearHighlightedMoves();
    } else if (player2.getNumOfApplesOnTree() == 3) {
      player2Phase3MoveLock = "Unlocked";
      if (selectMethod != "remove") {
        selectMethod = "";
      }
      displayTurn2.setText("All restrictions on Player " + (turn) + " have been lifted!");
      clearHighlightedMoves();
    }
  } // checkPhase3

  /**
   * checks if given space is already occupied with an apple
   *
   * @param xCord - x coordinate of space being checked
   * @param yCord - y coordinate of space being checked
   * @return - returns true if space full, false otherwise
   */
  protected boolean checkIfSpaceFull(int xCord, int yCord) {
    return boardArray[xCord][yCord].getTag() == "is1" || boardArray[xCord][yCord].getTag() == "is2";
  } // checkIfSpaceFull

  /**
   * Switches turn.
   */
  protected void switchTurn() {
    if (turn == "1") {
      displayCurrentApple.setImageResource(player2.getIdOfImageViewOfApple());
      turn = "2";
    } else {
      displayCurrentApple.setImageResource(player1.getIdOfImageViewOfApple());
      turn = "1";
    }
    // Display next turn if player not removing piece
    if (selectMethod != "remove") {
      displayTurn.setText("Player " + (turn) + "'s Turn");
    }
  } // switchTurn

  /**
   * Checks to see if last placed piece created a mill.
   *
   * @param xCord - x coordinate of last placed piece
   * @param yCord - y coordinate of last placed piece
   * @return - return true if last placed piece creates a mill
   */
  protected boolean checkMill(int xCord, int yCord) {
    if (checkHorizontal(xCord, yCord) == 3 || checkVertical(xCord, yCord) == 3) {
      // displayTurn2.setText("Player " + (turn) + " has created a branch of apples!");
      displayMadeABranchOfApplesText();
      return true;
    } else {
      return false;
    }
  } // checkMill

  /**
   * Checks horizontally for a mill. Finds the last placed apple, then for each direction will check
   * whether there is space in that direction for one or two apples. Then will count how many apples
   * in that direction. Then adds the left and right number to get a total length. The method
   * first needs to decide which row the the apple is on. This is because the number of array spaces
   * between adjacent button differs based on which row is being looked at.
   *
   * @param xCord - x coordinate of last placed piece
   * @param yCord - y coordinate of last placed piece
   * @return - length of horizontal line of the same apple
   */
  protected int checkHorizontal(int xCord, int yCord) {
    int length = 1;
    switch (yCord) {
      //If the apple is on the row 0 or 6
      case 0:
      case 6: {
        //Check Left
        //If the last placed apple is at x = 3 or greater, the program will first check the
        //adjacent space on the left. If the apples are different, the program moves to the
        //the right side. If the apples are the same, the length of the potential mill increases
        //by one. If the last placed apple is at x = 6, then there are two spaces to the left
        //that need to be checked, so the program checks them
        if (xCord >= 3) {
          if (boardArray[xCord][yCord].getTag() == boardArray[xCord - 3][yCord].getTag()) {
            length++;
            if (xCord == 6) {
              if (boardArray[xCord][yCord].getTag() == boardArray[xCord - 6][yCord].getTag()) {
                length++;
              }
            }
          }
        }
        //Check Right
        if (xCord <= 3) {
          if (boardArray[xCord][yCord].getTag() == boardArray[xCord + 3][yCord].getTag()) {
            length++;
            if (xCord == 0) {
              if (boardArray[xCord][yCord].getTag() == boardArray[xCord + 6][yCord].getTag()) {
                length++;
              }
            }
          }
        }
        break;
      }// Case 0, 6
      case 1:
      case 5: {
        //Check Left
        if (xCord >= 3) {
          if (boardArray[xCord][yCord].getTag() == boardArray[xCord - 2][yCord].getTag()) {
            length++;
            if (xCord == 5) {
              if (boardArray[xCord][yCord].getTag() == boardArray[xCord - 4][yCord].getTag()) {
                length++;
              }
            }
          }
        }
        //Check Right
        if (xCord <= 3) {
          if (boardArray[xCord][yCord].getTag() == boardArray[xCord + 2][yCord].getTag()) {
            length++;
            if (xCord == 1) {
              if (boardArray[xCord][yCord].getTag() == boardArray[xCord + 4][yCord].getTag()) {
                length++;
              }
            }
          }
        }
        break;
      }
      case 2:
      case 4: {
        //Check Left
        if (xCord >= 3) {
          if (boardArray[xCord][yCord].getTag() == boardArray[xCord - 1][yCord].getTag()) {
            length++;
            if (xCord == 4) {
              if (boardArray[xCord][yCord].getTag() == boardArray[xCord - 2][yCord].getTag()) {
                length++;
              }
            }
          }
        }
        //Check Right
        if (xCord <= 3) {
          if (boardArray[xCord][yCord].getTag() == boardArray[xCord + 1][yCord].getTag()) {
            length++;
            if (xCord == 2) {
              if (boardArray[xCord][yCord].getTag() == boardArray[xCord + 2][yCord].getTag()) {
                length++;
              }
            }
          }
        }
        break;
      }
      case 3: {
        //Check Left Row
        if (xCord < 3) {
          //Check Left
          if (xCord >= 1) {
            if (boardArray[xCord][yCord].getTag() == boardArray[xCord - 1][yCord].getTag()) {
              length++;
              if (xCord == 2) {
                if (boardArray[xCord][yCord].getTag() == boardArray[xCord - 2][yCord].getTag()) {
                  length++;
                }
              }
            }
          }
          //Check Right
          if (xCord <= 1) {
            if (boardArray[xCord][yCord].getTag() == boardArray[xCord + 1][yCord].getTag()) {
              length++;
              if (xCord == 0) {
                if (boardArray[xCord][yCord].getTag() == boardArray[xCord + 2][yCord].getTag()) {
                  length++;
                }
              }
            }
          }
        } else if (xCord > 3) {
          //Check Left
          if (xCord >= 5) {
            if (boardArray[xCord][yCord].getTag() == boardArray[xCord - 1][yCord].getTag()) {
              length++;
              if (xCord == 6) {
                if (boardArray[xCord][yCord].getTag() == boardArray[xCord - 2][yCord].getTag()) {
                  length++;
                }
              }
            }
          }
          //Check Right
          if (xCord <= 5) {
            if (boardArray[xCord][yCord].getTag() == boardArray[xCord + 1][yCord].getTag()) {
              length++;
              if (xCord == 4) {
                if (boardArray[xCord][yCord].getTag() == boardArray[xCord + 2][yCord].getTag()) {
                  length++;
                }
              }
            }
          }
        }
      }
      break;
    }
    if (length == 3) {
    }
    return length;
  } // checkHorizontal

  /**
   * Checks vertically for a mill. Finds the last placed apple, then for each direction will check
   * whether there is space in that direction for one or two apples. Then will count how many apples
   * in that direction. Then adds the up and down number to get a total length. The method
   * first needs to decide which column the the apple is on. This is because the number of array spaces
   * between adjacent button differs based on which column is being looked at
   *
   * @param xCord - x coordinate of last placed piece
   * @param yCord - y coordinate of last placed piece
   * @return - length of vertical line of the same apple
   */
  protected int checkVertical(int xCord, int yCord) {
    int length = 1;
    switch (xCord) {
      case 0:
      case 6: {
        // Check Down
        //If the last placed apple is at y = 3 or greater, the program will first check the
        //adjacent space downwards. If the apples are different, the program moves to check upwards.
        // If the apples are the same, the length of the potential mill increases
        //by one. If the last placed apple is at y = 6, then there are two spaces downwards
        //that need to be checked, so the program checks them
        if (yCord >= 3) {
          if (boardArray[xCord][yCord].getTag() == boardArray[xCord][yCord - 3].getTag()) {
            length++;
            if (yCord == 6) {
              if (boardArray[xCord][yCord].getTag() == boardArray[xCord][yCord - 6].getTag()) {
                length++;
              }
            }
          }
        }
        // Check Up
        if (yCord <= 3) {
          if (boardArray[xCord][yCord].getTag() == boardArray[xCord][yCord + 3].getTag()) {
            length++;
            if (yCord == 0) {
              if (boardArray[xCord][yCord].getTag() == boardArray[xCord][yCord + 6].getTag()) {
                length++;
              }
            }
          }
        }
        break;
      }// Case 1, 7
      case 1:
      case 5: {
        // Check Down
        if (yCord >= 3) {
          if (boardArray[xCord][yCord].getTag() == boardArray[xCord][yCord - 2].getTag()) {
            length++;
            if (yCord == 5) {
              if (boardArray[xCord][yCord].getTag() == boardArray[xCord][yCord - 4].getTag()) {
                length++;
              }
            }
          }
        }
        // Check Up
        if (yCord <= 3) {
          if (boardArray[xCord][yCord].getTag() == boardArray[xCord][yCord + 2].getTag()) {
            length++;
            if (yCord == 1) {
              if (boardArray[xCord][yCord].getTag() == boardArray[xCord][yCord + 4].getTag()) {
                length++;
              }
            }
          }
        }
        break;
      }// Case 2, 6
      case 2:
      case 4: {
        // Check Down
        if (yCord >= 3) {
          if (boardArray[xCord][yCord].getTag() == boardArray[xCord][yCord - 1].getTag()) {
            length++;
            if (yCord == 4) {
              if (boardArray[xCord][yCord].getTag() == boardArray[xCord][yCord - 2].getTag()) {
                length++;
              }
            }
          }
        }
        // Check Up
        if (yCord <= 3) {
          if (boardArray[xCord][yCord].getTag() == boardArray[xCord][yCord + 1].getTag()) {
            length++;
            if (yCord == 2) {
              if (boardArray[xCord][yCord].getTag() == boardArray[xCord][yCord + 2].getTag()) {
                length++;
              }
            }
          }
        }
        break;
      }// Case 3, 5
      case 3: {
        // Check Bottom Row
        if (yCord < 3) {
          // Check Down
          if (yCord >= 1) {
            if (boardArray[xCord][yCord].getTag() == boardArray[xCord][yCord - 1].getTag()) {
              length++;
              if (yCord == 2) {
                if (boardArray[xCord][yCord].getTag() == boardArray[xCord][yCord - 2].getTag()) {
                  length++;
                }
              }
            }
          }
          // Check Up
          if (yCord <= 1) {
            if (boardArray[xCord][yCord].getTag() == boardArray[xCord][yCord + 1].getTag()) {
              length++;
              if (yCord == 0) {
                if (boardArray[xCord][yCord].getTag() == boardArray[xCord][yCord + 2].getTag()) {
                  length++;
                }
              }
            }
          }
        } else if (yCord > 3) {
          // Check Down
          if (yCord >= 5) {
            if (boardArray[xCord][yCord].getTag() == boardArray[xCord][yCord - 1].getTag()) {
              length++;
              if (yCord == 6) {
                if (boardArray[xCord][yCord].getTag() == boardArray[xCord][yCord - 2].getTag()) {
                  length++;
                }
              }
            }
          }
          // Check Up
          if (yCord <= 5) {
            if (boardArray[xCord][yCord].getTag() == boardArray[xCord][yCord + 1].getTag()) {
              length++;
              if (yCord == 4) {
                if (boardArray[xCord][yCord].getTag() == boardArray[xCord][yCord + 2].getTag()) {
                  length++;
                }
              }
            }
          }
        }
      }
    }
    if (length == 3) {
    }
    return length;
  } // checkVertical

  /**
   * Places apple of current player down on to board, decreases number of apples in that players
   * inventory
   *
   * @param player   - The player who is placing the apple
   * @param myButton - The button where the apple will be placed
   */
  protected void resolveApplePlacement(Player player, ImageButton myButton) {
    myButton.setBackgroundResource(player.getAppleOfPlayer());
    player.setNumOfApples(player.getNumOfApples() - 1);
    player.setNumOfApplesOnTree(player.getNumOfApplesOnTree() + 1);
    if (turn == "1") {
      applesLeftPlayer1.setText(player.getNumOfApples() + " Apples Left");
      myButton.setTag("is1");
    } else {
      applesLeftPlayer2.setText(player.getNumOfApples() + " Apples Left");
      myButton.setTag("is2");
    }
  } // resolveApplePlacement

  /**
   * This method triggers the remove apple function specifically if there is a mill created
   * on the last turn of phase 1.
   *
   * @param xCord the x coordinate of last placed apple
   * @param yCord the y coordinate of last placed apple
   */
  protected boolean checkLastMillPhase1(int xCord, int yCord) {
    if (player1.getNumOfApples() == 0 && player2.getNumOfApples() == 0 &&
            checkMill(xCord, yCord)) {
      switchTurn();
      selectMethod = "remove";
      return true;
    } else {
      return false;
    }
  } // checkLastMillPhase1

  /**
   * Highlights every legal move for the players in phase 1.
   */
  protected void highlightLegalMovesPhase1() {
    for (int x = 0; x < 7; x++) {
      for (int y = 0; y < 7; y++) {
        try {
          if (boardArray[x][y].getTag() == "Initialized") {
            boardArray[x][y].setBackgroundResource(R.drawable.purpleapplehighlight);
          }
        } catch (Exception e) {
          System.out.println((x) + " " + (y) + "Empty space does not exist");
        }
      }
    }
  } // highlightLegalMovesPhase1

  /**
   * Highlights every legal move for the players in phase 2. Similarly to the checkHorizontal and
   * checkVertical methods, The method first needs to decide which row the the apple is on.
   * This is because the number of array spaces between adjacent button differs based on which
   * row and column is being looked at. If the space that is checked is adjacent to the apple that
   * has been placed, that space will be added to a list. At the end of the method, every
   * space in the list will be highlighted as a legal space to move.
   */
  protected void highlightLegalMovesPhase2() {
    ArrayList<ImageButton> listOfAdjacentSpaces = new ArrayList<ImageButton>();
    //Finding adjacent, empty spots
    //Check Horizontal
    switch (moveFrom2Y) {
      case 0:
      case 6:
        if (moveFrom2X == 0 || moveFrom2X == 6) {
          if (checkIfSpaceFull(3, moveFrom2Y) == false) {
            listOfAdjacentSpaces.add(boardArray[3][moveFrom2Y]);
          }
        } else if (moveFrom2X == 3) {
          if (checkIfSpaceFull(0, moveFrom2Y) == false) {
            listOfAdjacentSpaces.add(boardArray[0][moveFrom2Y]);
          }
          if (checkIfSpaceFull(6, moveFrom2Y) == false) {
            listOfAdjacentSpaces.add(boardArray[6][moveFrom2Y]);
          }
        }
        break;
      case 1:
      case 5:
        if (moveFrom2X == 1 || moveFrom2X == 5) {
          if (checkIfSpaceFull(3, moveFrom2Y) == false) {
            listOfAdjacentSpaces.add(boardArray[3][moveFrom2Y]);
          }
        } else if (moveFrom2X == 3) {
          if (checkIfSpaceFull(1, moveFrom2Y) == false) {
            listOfAdjacentSpaces.add(boardArray[1][moveFrom2Y]);
          }
          if (checkIfSpaceFull(5, moveFrom2Y) == false) {
            listOfAdjacentSpaces.add(boardArray[5][moveFrom2Y]);
          }
        }
        break;
      case 2:
      case 4:
        if (moveFrom2X == 2 || moveFrom2X == 4) {
          if (checkIfSpaceFull(3, moveFrom2Y) == false) {
            listOfAdjacentSpaces.add(boardArray[3][moveFrom2Y]);
          }
        } else if (moveFrom2X == 3) {
          if (checkIfSpaceFull(2, moveFrom2Y) == false) {
            listOfAdjacentSpaces.add(boardArray[2][moveFrom2Y]);
          }
          if (checkIfSpaceFull(4, moveFrom2Y) == false) {
            listOfAdjacentSpaces.add(boardArray[4][moveFrom2Y]);
          }
        }
        break;
      case 3:
        if (moveFrom2X == 0 || moveFrom2X == 4) {
          if (checkIfSpaceFull(moveFrom2X + 1, moveFrom2Y) == false) {
            listOfAdjacentSpaces.add(boardArray[moveFrom2X + 1][moveFrom2Y]);
          }
        } else if (moveFrom2X == 1 || moveFrom2X == 5) {
          if (checkIfSpaceFull(moveFrom2X - 1, moveFrom2Y) == false) {
            listOfAdjacentSpaces.add(boardArray[moveFrom2X - 1][moveFrom2Y]);
          }
          if (checkIfSpaceFull(moveFrom2X + 1, moveFrom2Y) == false) {
            listOfAdjacentSpaces.add(boardArray[moveFrom2X + 1][moveFrom2Y]);
          }
        }
        if (moveFrom2X == 2 || moveFrom2X == 6) {
          if (checkIfSpaceFull(moveFrom2X - 1, moveFrom2Y) == false) {
            listOfAdjacentSpaces.add(boardArray[moveFrom2X - 1][moveFrom2Y]);
          }
        }
        break;
    }
    //Check Vertical
    switch (moveFrom2X) {
      case 0:
      case 6:
        if (moveFrom2Y == 0 || moveFrom2Y == 6) {
          if (checkIfSpaceFull(moveFrom2X, 3) == false) {
            listOfAdjacentSpaces.add(boardArray[moveFrom2X][3]);
          }
        } else if (moveFrom2Y == 3) {
          if (checkIfSpaceFull(moveFrom2X, 0) == false) {
            listOfAdjacentSpaces.add(boardArray[moveFrom2X][0]);
          }
          if (checkIfSpaceFull(moveFrom2X, 6) == false) {
            listOfAdjacentSpaces.add(boardArray[moveFrom2X][6]);
          }
        }
        break;
      case 1:
      case 5:
        if (moveFrom2Y == 1 || moveFrom2Y == 5) {
          if (checkIfSpaceFull(moveFrom2X, 3) == false) {
            listOfAdjacentSpaces.add(boardArray[moveFrom2X][3]);
          }
        } else if (moveFrom2Y == 3) {
          if (checkIfSpaceFull(moveFrom2X, 1) == false) {
            listOfAdjacentSpaces.add(boardArray[moveFrom2X][1]);
          }
          if (checkIfSpaceFull(moveFrom2X, 5) == false) {
            listOfAdjacentSpaces.add(boardArray[moveFrom2X][5]);
          }
        }
        break;
      case 2:
      case 4:
        if (moveFrom2Y == 2 || moveFrom2Y == 4) {
          if (checkIfSpaceFull(moveFrom2X, 3) == false) {
            listOfAdjacentSpaces.add(boardArray[moveFrom2X][3]);
          }
        } else if (moveFrom2Y == 3) {
          if (checkIfSpaceFull(moveFrom2X, 2) == false) {
            listOfAdjacentSpaces.add(boardArray[moveFrom2X][2]);
          }
          if (checkIfSpaceFull(moveFrom2X, 4) == false) {
            listOfAdjacentSpaces.add(boardArray[moveFrom2X][4]);
          }
        }
        break;
      case 3:
        if (moveFrom2Y == 0 || moveFrom2Y == 4) {
          if (checkIfSpaceFull(moveFrom2X, moveFrom2Y + 1) == false) {
            listOfAdjacentSpaces.add(boardArray[moveFrom2X][moveFrom2Y + 1]);
          }
        } else if (moveFrom2Y == 1 || moveFrom2Y == 5) {
          if (checkIfSpaceFull(moveFrom2X, moveFrom2Y - 1) == false) {
            listOfAdjacentSpaces.add(boardArray[moveFrom2X][moveFrom2Y - 1]);
          }
          if (checkIfSpaceFull(moveFrom2X, moveFrom2Y + 1) == false) {
            listOfAdjacentSpaces.add(boardArray[moveFrom2X][moveFrom2Y + 1]);
          }
        }
        if (moveFrom2Y == 2 || moveFrom2Y == 6) {
          if (checkIfSpaceFull(moveFrom2X, moveFrom2Y - 1) == false) {
            listOfAdjacentSpaces.add(boardArray[moveFrom2X][moveFrom2Y - 1]);
          }
        }
        break;
    }
    //Highlighting Available Spaces
    for (int i = 0; i < listOfAdjacentSpaces.size(); i++) {
      listOfAdjacentSpaces.get(i).setBackgroundResource(R.drawable.purpleapplehighlight);
    }
    //Assigning list to variable for use when checking adjacent spaces
    listOfAdjacentSpacesPhase2 = listOfAdjacentSpaces;
  }

  /**
   * Highlights every legal move for the players in phase 3 by adding every empty space to a list
   * and then highlighting every space in that list
   */
  protected void highlightLegalMovesPhase3() {
    ArrayList<ImageButton> listOfAdjacentSpaces = new ArrayList<ImageButton>();
    for (int x = 0; x < 7; x++) {
      for (int y = 0; y < 7; y++) {
        try {
          if (checkIfSpaceFull(x, y) == false) {
            listOfAdjacentSpaces.add(boardArray[x][y]);
          }
        } catch (Exception e) {
        }
      }
    }
    for (int i = 0; i < listOfAdjacentSpaces.size(); i++) {
      listOfAdjacentSpaces.get(i).setBackgroundResource(R.drawable.purpleapplehighlight);
    }
    listOfAdjacentSpacesPhase3 = listOfAdjacentSpaces;
  }

  /**
   * Highlights the last placed piece if the game is in phase 1
   *
   * @param xCord - X coordinate of the last placed piece
   * @param yCord - Y coordinate of the last placed piece
   */
  protected void highlightPiece(int xCord, int yCord) {
    int player1AppleId;
    int player2AppleId;
    // Find player 1's highlighted apple
    if (player1.getAppleOfPlayer() == R.drawable.redapple) {
      player1AppleId = R.drawable.highlightedapplesred;
    } else if (player1.getAppleOfPlayer() == R.drawable.greenapple) {
      player1AppleId = R.drawable.highlightedapplesgreen;
    } else {
      player1AppleId = R.drawable.highlightedapplesyellow;
    }
    // Find player 2's highlighted apple
    if (player2.getAppleOfPlayer() == R.drawable.redapple) {
      player2AppleId = R.drawable.highlightedapplesred;
    } else if (player2.getAppleOfPlayer() == R.drawable.greenapple) {
      player2AppleId = R.drawable.highlightedapplesgreen;
    } else {
      player2AppleId = R.drawable.highlightedapplesyellow;
    }
    //Highlights the apple
    if (turn == "1") {
      boardArray[xCord][yCord].setBackgroundResource(player1AppleId);
    } else {
      boardArray[xCord][yCord].setBackgroundResource(player2AppleId);
    }
    //Set coordinates of placed piece for next turn
    previouslyHighlightedPieceX = xCord;
    previouslyHighlightedPieceY = yCord;
  } // highlightLastMovedPiece

  /**
   * Clears highlighted potential moves on board.
   */
  protected void clearHighlightedMoves() {
    // Clear available highlighted moves
    for (int x = 0; x < 7; x++) {
      for (int y = 0; y < 7; y++) {
        try {
          if (boardArray[x][y].getTag() == "Initialized") {
            boardArray[x][y].setBackgroundResource(0);
          }
        } catch (Exception e) {
        }
      }
    }
  } // clearHighlightedMoves

  /**
   * Clears highlight of last moved piece.
   */
  protected void clearHighlightedPiece() {
    // Removing highlight from previous piece
    if (turn == "1") {
      try {
        boardArray[previouslyHighlightedPieceX][previouslyHighlightedPieceY].setBackgroundResource(player2.getAppleOfPlayer());
      } catch (Exception e) {
      }
    } else {
      boardArray[previouslyHighlightedPieceX][previouslyHighlightedPieceY].setBackgroundResource(player1.getAppleOfPlayer());
    }
  } // clearLastMovedPiece

  /**
   * Sends user to winner activity with the winning players number and image
   */
  protected void sendToWinnerActivity() {
    String winningTurn;
    int appleID;
    // Switch turn to display correct winner
    if (turn == "1") {
      winningTurn = "2";
      appleID = player2.getIdOfImageViewOfApple();
    } else {
      winningTurn = "1";
      appleID = player1.getIdOfImageViewOfApple();
    }
    //Send players to winner activity
    Intent toWinnerActivity = new Intent(context, WinnerActivity.class);
    toWinnerActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    toWinnerActivity.putExtra("Turn", winningTurn);
    toWinnerActivity.putExtra("Winner", appleID);
    context.startActivity(toWinnerActivity);
    return;
  } // sendToWinnerActivity

} // class

