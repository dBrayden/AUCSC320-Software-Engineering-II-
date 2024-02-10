
package com.example.ninemensmorrisapp;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

/**
 * currently only handles some containment of variables for the computer. because of time
 * constraints most of the cpu logic is ran in the gameActivity.java file. Future development
 * would have it switched over to the computer.class
 */
public class Computer extends AppCompatActivity{

    String difficulty;
    Boolean computerEnabled = false;

    Player cpu = new Player(R.drawable.greenapple);
    ArrayList<ImageButton> currentPieces = new ArrayList<>();
    ArrayList<Integer> xCoords = new ArrayList<>();
    ArrayList<Integer> yCoords = new ArrayList<>();
    int yCoord = 0;
    int xCoord = 0;
    int xStart;
    int yStart;
    int yMove;
    int xMove;
    int cpuMovePhase;
    String turn;
    ImageButton boardArray[][];
    Board cpuGameBoard;
    int removeX;
    int removeY;


    public void setDifficultyEasy(){ difficulty = "easy";}

    public void setDifficultyHard(){ difficulty = "hard";}

    /**
     * Method controlls how the computer places thier apples
     */
    /**
     * Method controlls how the computer places thier apples
     */
    public void placePiece() {
        if(turn != "2"){
            return;
        }
        int x = 0;
        int y = 0;
        Random random = new Random();
        int tempX = random.nextInt(7);
        int tempY = random.nextInt(7);

        ImageButton buttonToPress = checkIfEmpty(tempX, tempY);

        if (buttonToPress != null) {
            buttonToPress.performClick();

        } else {
            placePiece();
        }//else

        TextView playerInfo = findViewById(R.id.placeAppleOnTreeText);
        if (playerInfo.getText() == "That space is occupied") {
            placePiece();
        }

    }//placePiece


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
            }//if
        }
        return placeHere;
    }

    /**
     * seperates the steps for moving a piece
     */
    public void cpuMovePress() {

        if(cpuMovePhase == 1) {
            cpuPick();
        }
        if(cpuMovePhase == 2){
            TextView error = findViewById(R.id.error);
            error.setText("press: " +xMove +","+yMove);
            boardArray[xMove][yMove].performClick();
            cpuMovePhase=1;
        }

    }

    public void cpuPick(){
        TextView error = findViewById(R.id.error);
        Random random = new Random();
        int x = random.nextInt(7);
        int y = random.nextInt(7);
        if(boardArray[x][y] != null){
            if(boardArray[x][y].getTag() == "is2"){

                error.setText(x+","+y);
                if(cpuCanMove(x,y)){
                    cpuMovePhase = 2;
                    error.setText(x+","+y+" can move");
                    boardArray[x][y].performClick();

                }

            }
            else{
                cpuPick();
            }
        }
        else{
            cpuPick();
        }

    }

    public Boolean cpuCanMove(int x, int y){
        //================================== outerSquare ==============================//
        //topLeftCorner
        if(x == 0 && y == 6){
            if(boardArray[3][6].getTag() != "is1"
                    && boardArray[3][6].getTag() != "is2"){
                xMove = 3;
                yMove = 6;
                return true;
            }
            if(boardArray[0][3].getTag() != "is1"
                    && boardArray[0][3].getTag() != "is2"){
                xMove = 0;
                yMove = 3;
                return true;
            }
        }
        //TopMiddle
        if(x == 3 && y == 6){
            //Left
            if(boardArray[0][6].getTag() != "is1"
                    && boardArray[0][6].getTag() != "is2"){
                xMove = 0;
                yMove = 6;
                return true;
            }
            //Down
            if(boardArray[3][5].getTag() != "is1"
                    && boardArray[3][5].getTag() != "is2"){
                xMove = 3;
                yMove = 5;
                return true;
            }
            //Right
            if(boardArray[6][6].getTag() != "is1"
                    && boardArray[6][6].getTag() != "is2"){
                xMove = 6;
                yMove = 6;
                return true;
            }
        }
        //TopRightCorner
        if(x == 6 && y == 6){
            //Left
            if(boardArray[3][6].getTag() != "is1"
                    && boardArray[3][6].getTag() != "is2"){
                xMove = 3;
                yMove = 6;
                return true;
            }//Down
            if(boardArray[6][3].getTag() != "is1"
                    && boardArray[6][3].getTag() != "is2"){
                xMove = 6;
                yMove = 3;
                return true;
            }
        }
        //RightMiddle
        if(x == 6 && y == 3){
            //Up
            if(boardArray[6][6].getTag() != "is1"
                    && boardArray[6][6].getTag() != "is2"){
                xMove = 6;
                yMove = 6;
                return true;
            }
            //Left
            if(boardArray[5][3].getTag() != "is1"
                    && boardArray[5][3].getTag() != "is2"){
                xMove = 5;
                yMove = 3;
                return true;
            }
            //Down
            if(boardArray[6][0].getTag() != "is1"
                    && boardArray[6][0].getTag() != "is2"){
                xMove = 6;
                yMove = 0;
                return true;
            }
        }
        //BottomRightCorner
        if(x == 6 && y == 0){
            //Up
            if(boardArray[6][3].getTag() != "is1"
                    && boardArray[6][3].getTag() != "is2"){
                xMove = 6;
                yMove = 3;
                return true;
            }//Left
            if(boardArray[3][0].getTag() != "is1"
                    && boardArray[3][0].getTag() != "is2"){
                xMove = 3;
                yMove = 0;
                return true;
            }
        }
        //BottomMiddle
        if(x == 3 && y == 0){
            //Right
            if(boardArray[6][0].getTag() != "is1"
                    && boardArray[6][0].getTag() != "is2"){
                xMove = 6;
                yMove = 0;
                return true;
            }
            //Up
            if(boardArray[3][1].getTag() != "is1"
                    && boardArray[3][1].getTag() != "is2"){
                xMove = 3;
                yMove = 1;
                return true;
            }
            //Left
            if(boardArray[0][0].getTag() != "is1"
                    && boardArray[0][0].getTag() != "is2"){
                xMove = 0;
                yMove = 0;
                return true;
            }
        }
        //BottomLeftCorner
        if(x == 0 && y == 0){
            //Up
            if(boardArray[0][3].getTag() != "is1"
                    && boardArray[0][3].getTag() != "is2"){
                xMove = 0;
                yMove = 3;
                return true;
            }//Right
            if(boardArray[3][0].getTag() != "is1"
                    && boardArray[3][0].getTag() != "is2"){
                xMove = 3;
                yMove = 0;
                return true;
            }
        }
        //LeftMiddle
        if(x == 0 && y == 3){
            //Up
            if(boardArray[0][6].getTag() != "is1"
                    && boardArray[0][6].getTag() != "is2"){
                xMove = 0;
                yMove = 6;
                return true;
            }
            //Right
            if(boardArray[1][3].getTag() != "is1"
                    && boardArray[1][3].getTag() != "is2"){
                xMove = 1;
                yMove = 3;
                return true;
            }
            //Down
            if(boardArray[0][0].getTag() != "is1"
                    && boardArray[0][0].getTag() != "is2"){
                xMove = 0;
                yMove = 0;
                return true;
            }
        }
        //================================= MiddleSquare ==============================//
        //topLeftCorner
        if(x == 1 && y == 5){
            //Right
            if(boardArray[3][5].getTag() != "is1"
                    && boardArray[3][5].getTag() != "is2"){
                xMove = 3;
                yMove = 5;
                return true;
            }
            //Down
            if(boardArray[1][3].getTag() != "is1"
                    && boardArray[1][3].getTag() != "is2"){
                xMove = 1;
                yMove = 3;
                return true;
            }
        }
        //TopMiddle
        if(x == 3 && y == 5){
            //Left
            if(boardArray[1][5].getTag() != "is1"
                    && boardArray[1][5].getTag() != "is2"){
                xMove = 1;
                yMove = 5;
                return true;
            }
            //Down
            if(boardArray[3][4].getTag() != "is1"
                    && boardArray[3][4].getTag() != "is2"){
                xMove = 3;
                yMove = 4;
                return true;
            }
            //Right
            if(boardArray[5][5].getTag() != "is1"
                    && boardArray[5][5].getTag() != "is2"){
                xMove = 5;
                yMove = 5;
                return true;
            }
            //Up
            if(boardArray[3][6].getTag() != "is1"
                    && boardArray[3][6].getTag() != "is2"){
                xMove = 3;
                yMove = 6;
                return true;
            }
        }
        //TopRightCorner
        if(x == 5 && y == 5){
            //Left
            if(boardArray[3][5].getTag() != "is1"
                    && boardArray[3][5].getTag() != "is2"){
                xMove = 3;
                yMove = 5;
                return true;
            }//Down
            if(boardArray[5][3].getTag() != "is1"
                    && boardArray[5][3].getTag() != "is2"){
                xMove = 5;
                yMove = 3;
                return true;
            }
        }
        //RightMiddle
        if(x == 5 && y == 3){
            //Up
            if(boardArray[5][5].getTag() != "is1"
                    && boardArray[5][5].getTag() != "is2"){
                xMove = 5;
                yMove = 5;
                return true;
            }
            //Left
            if(boardArray[4][3].getTag() != "is1"
                    && boardArray[4][3].getTag() != "is2"){
                xMove = 4;
                yMove = 3;
                return true;
            }
            //Down
            if(boardArray[5][1].getTag() != "is1"
                    && boardArray[5][1].getTag() != "is2"){
                xMove = 5;
                yMove = 1;
                return true;
            }
            //Right
            if(boardArray[6][3].getTag() != "is1"
                    && boardArray[6][3].getTag() != "is2"){
                xMove = 6;
                yMove = 3;
                return true;
            }
        }
        //BottomRightCorner
        if(x == 5 && y == 1){
            //Up
            if(boardArray[5][3].getTag() != "is1"
                    && boardArray[5][3].getTag() != "is2"){
                xMove = 5;
                yMove = 3;
                return true;
            }//Left
            if(boardArray[3][1].getTag() != "is1"
                    && boardArray[3][1].getTag() != "is2"){
                xMove = 3;
                yMove = 1;
                return true;
            }
        }
        //BottomMiddle
        if(x == 3 && y == 1){
            //Right
            if(boardArray[5][1].getTag() != "is1"
                    && boardArray[5][1].getTag() != "is2"){
                xMove = 5;
                yMove = 1;
                return true;
            }
            //Up
            if(boardArray[3][2].getTag() != "is1"
                    && boardArray[3][2].getTag() != "is2"){
                xMove = 3;
                yMove = 2;
                return true;
            }
            //Left
            if(boardArray[1][1].getTag() != "is1"
                    && boardArray[1][1].getTag() != "is2"){
                xMove = 1;
                yMove = 1;
                return true;
            }
            //Down
            if(boardArray[3][0].getTag() != "is1"
                    && boardArray[3][0].getTag() != "is2"){
                xMove = 3;
                yMove = 0;
                return true;
            }
        }
        //BottomLeftCorner
        if(x == 1 && y == 1){
            //Up
            if(boardArray[1][3].getTag() != "is1"
                    && boardArray[1][3].getTag() != "is2"){
                xMove = 1;
                yMove = 3;
                return true;
            }//Right
            if(boardArray[3][1].getTag() != "is1"
                    && boardArray[3][1].getTag() != "is2"){
                xMove = 3;
                yMove = 1;
                return true;
            }
        }
        //LeftMiddle
        if(x == 1 && y == 3){
            //Up
            if(boardArray[1][5].getTag() != "is1"
                    && boardArray[1][5].getTag() != "is2"){
                xMove = 1;
                yMove = 5;
                return true;
            }
            //Right
            if(boardArray[2][3].getTag() != "is1"
                    && boardArray[2][3].getTag() != "is2"){
                xMove = 2;
                yMove = 3;
                return true;
            }
            //Down
            if(boardArray[1][1].getTag() != "is1"
                    && boardArray[1][1].getTag() != "is2"){
                xMove = 1;
                yMove = 1;
                return true;
            }
            //Left
            if(boardArray[0][3].getTag() != "is1"
                    && boardArray[0][3].getTag() != "is2"){
                xMove = 0;
                yMove = 3;
                return true;
            }
        }
        //================================== InnerSquare ==============================//
        //topLeftCorner
        if(x == 2 && y == 4){
            //Right
            if(boardArray[3][4].getTag() != "is1"
                    && boardArray[3][4].getTag() != "is2"){
                xMove = 3;
                yMove = 4;
                return true;
            }//Down
            if(boardArray[2][3].getTag() != "is1"
                    && boardArray[2][3].getTag() != "is2"){
                xMove = 2;
                yMove = 3;
                return true;
            }
        }
        //TopMiddle
        if(x == 3 && y == 4){
            //Left
            if(boardArray[2][4].getTag() != "is1"
                    && boardArray[2][4].getTag() != "is2"){
                xMove = 2;
                yMove = 4;
                return true;
            }
            //Up
            if(boardArray[3][5].getTag() != "is1"
                    && boardArray[3][5].getTag() != "is2"){
                xMove = 3;
                yMove = 5;
                return true;
            }
            //Right
            if(boardArray[4][4].getTag() != "is1"
                    && boardArray[4][4].getTag() != "is2"){
                xMove = 4;
                yMove = 4;
                return true;
            }
        }
        //TopRightCorner
        if(x == 4 && y == 4){
            //Left
            if(boardArray[3][4].getTag() != "is1"
                    && boardArray[3][4].getTag() != "is2"){
                xMove = 3;
                yMove = 4;
                return true;
            }//Down
            if(boardArray[3][4].getTag() != "is1"
                    && boardArray[3][4].getTag() != "is2"){
                xMove = 3;
                yMove = 4;
                return true;
            }
        }
        //RightMiddle
        if(x == 4 && y == 3){
            //Up
            if(boardArray[4][4].getTag() != "is1"
                    && boardArray[4][4].getTag() != "is2"){
                xMove = 4;
                yMove = 4;
                return true;
            }
            //Right
            if(boardArray[5][3].getTag() != "is1"
                    && boardArray[5][3].getTag() != "is2"){
                xMove = 5;
                yMove = 3;
                return true;
            }
            //Down
            if(boardArray[4][2].getTag() != "is1"
                    && boardArray[4][2].getTag() != "is2"){
                xMove = 4;
                yMove = 2;
                return true;
            }
        }
        //BottomRightCorner
        if(x == 4 && y == 2){
            //Up
            if(boardArray[4][3].getTag() != "is1"
                    && boardArray[4][3].getTag() != "is2"){
                xMove = 4;
                yMove = 3;
                return true;
            }//Left
            if(boardArray[3][2].getTag() != "is1"
                    && boardArray[3][2].getTag() != "is2"){
                xMove = 3;
                yMove = 2;
                return true;
            }
        }
        //BottomMiddle
        if(x == 3 && y == 2){
            //Right
            if(boardArray[4][2].getTag() != "is1"
                    && boardArray[4][2].getTag() != "is2"){
                xMove = 4;
                yMove = 2;
                return true;
            }
            //Down
            if(boardArray[3][1].getTag() != "is1"
                    && boardArray[3][1].getTag() != "is2"){
                xMove = 3;
                yMove = 1;
                return true;
            }
            //Left
            if(boardArray[2][2].getTag() != "is1"
                    && boardArray[2][2].getTag() != "is2"){
                xMove = 2;
                yMove = 2;
                return true;
            }
        }
        //BottomLeftCorner
        if(x == 2 && y == 2){
            //Up
            if(boardArray[2][3].getTag() != "is1"
                    && boardArray[2][3].getTag() != "is2"){
                xMove = 2;
                yMove = 3;
                return true;
            }//Right
            if(boardArray[3][2].getTag() != "is1"
                    && boardArray[3][2].getTag() != "is2"){
                xMove = 3;
                yMove = 2;
                return true;
            }
        }
        //LeftMiddle
        if(x == 2 && y == 3){
            //Up
            if(boardArray[2][4].getTag() != "is1"
                    && boardArray[2][4].getTag() != "is2"){
                xMove = 2;
                yMove = 4;
                return true;
            }
            //Left
            if(boardArray[1][3].getTag() != "is1"
                    && boardArray[1][3].getTag() != "is2"){
                xMove = 1;
                yMove = 3;
                return true;
            }
            //Down
            if(boardArray[2][2].getTag() != "is1"
                    && boardArray[2][2].getTag() != "is2"){
                xMove = 2;
                yMove = 2;
                return true;
            }
        }

        return false;
    }


    public void cpuHardPlace(){

        ImageButton pieceToMove = null;
        TextView error = findViewById(R.id.error);
        int button1ID;
        int button2ID;
        String button1Name;
        String button2Name;

        String tagToCompare;
//        if(millCheckPhase == 1) {
//            tagToCompare = "is1";
//        }
//        else{
//            tagToCompare = "is2";
//        }
        tagToCompare = "is1";

        //============================== Outer Square =============================//
        //------------------------------ bottomRow
        //RightCorner Empty
        checkIfPlaceable(6,0,3,0,0,0,"is1");

        //MiddleEmpty
        checkIfPlaceable(3,0,0,6,0,0,"is1");

        //LeftCornerEmpty
        checkIfPlaceable(0,0,3,6,0,0,"is1");

        //-----------------------------------leftColumn

        //TopCornerEmpty
        checkIfPlaceable(0,6,0,0,0,3,"is1");

        //MiddleEmpty
        checkIfPlaceable(0,3,0,0,0,6,"is1");

        //BottomCornerEmpty
        checkIfPlaceable(0,0,0,0,3,6,"is1");

        //------------------------------------TopRow
        //RightCornerEmpty
        checkIfPlaceable(6,6,0,3,6,6,"is1");

        //MiddleEmpty
        checkIfPlaceable(3,6,0,6,6,6,"is1");

        //LeftCornerEmpty
        checkIfPlaceable(0,6,3,6,6,6,"is1");

        //------------------------------------ RightColumn -------------------------------//
        //TopCornerEmpty
        checkIfPlaceable(6,6,6,6,3,0,"is1");

        //MiddleEmpty
        checkIfPlaceable(6,3,6,6,6,0,"is1");

        //BottomColumn
        checkIfPlaceable(6,0,6,6,6,3,"is1");

        //==================================== Middle Square =============================//

        //------------------------------------ TopRow      -------------------------------//
        //LeftCorner
        checkIfPlaceable(1,5,3,5,5,5,"is1");

        //MiddleEmpty
        checkIfPlaceable(3,5,1,5,5,5,"is1");

        //RightCorner
        checkIfPlaceable(5,5,1,3,5,5,"is1");

        //------------------------------------ RightColumn -------------------------------//
        //TopCorner
        checkIfPlaceable(5,5,5,5,3,1,"is1");

        //MiddleEmpty
        checkIfPlaceable(5,3,5,5,5,1,"is1");

        //BottomCorner
        checkIfPlaceable(5,1,5,5,5,3,"is1");

        //------------------------------------ BottomRow   -------------------------------//
        //RightCorner
        checkIfPlaceable(5,1,3,1,1,1,"is1");

        //MiddleEmpty
        checkIfPlaceable(3,1,5,1,1,1,"is1");

        //LeftCorner
        checkIfPlaceable(1,1,5,3,1,1,"is1");

        //------------------------------------ leftColumn  -------------------------------//
        //BottomCorner
        checkIfPlaceable(1,1,1,1,3,5,"is1");

        //MiddleEmpty
        checkIfPlaceable(1,3,1,1,1,5,"is1");

        //TopCorner
        checkIfPlaceable(1,5,1,1,3,3,"is1");

        //==================================== inner Square =============================//

        //------------------------------------ TopRow      -------------------------------//
        //LeftCorner
        checkIfPlaceable(2,4,3,4,4,4,"is1");

        //MiddleEmpty
        checkIfPlaceable(3,4,2,4,4,4,"is1");

        //RightCorner
        checkIfPlaceable(4,4,2,3,4,4,"is1");

        //------------------------------------ RightColumn -------------------------------//
        //TopCorner
        checkIfPlaceable(4,4,4,4,3,2,"is1");

        //MiddleEmpty
        checkIfPlaceable(4,3,4,4,2,4,"is1");

        //BottomCorner
        checkIfPlaceable(4,2,4,4,3,4,"is1");

        //------------------------------------ BottomRow   -------------------------------//
        //RightCorner
        checkIfPlaceable(4,2,3,2,2,2,"is1");

        //MiddleEmpty
        checkIfPlaceable(3,2,4,2,2,2,"is1");

        //LeftCorner
        checkIfPlaceable(2,2,4,3,2,2,"is1");

        //------------------------------------ leftColumn  -------------------------------//
        //BottomCorner
        checkIfPlaceable(2,2,2,2,3,4,"is1");

        //MiddleEmpty
        checkIfPlaceable(2,3,2,2,2,4,"is1");

        //TopCorner
        checkIfPlaceable(2,4,2,2,2,3,"is1");

        //==================================== SquareConnectors  =========================//

        //------------------------------------ North  -------------------------------//

        //Top
        checkIfPlaceable(3,6,3,3,5,4,"is1");

        //Middle
        checkIfPlaceable(3,5,3,3,6,4,"is1");

        //Bottom
        checkIfPlaceable(3,4,3,3,6,5,"is1");

        //------------------------------------ East  -------------------------------//

        //Left
        checkIfPlaceable(4,3,5,6,3,3,"is1");

        //Middle
        checkIfPlaceable(5,3,4,6,3,3,"is1");

        //Right
        checkIfPlaceable(6,3,4,5,3,3,"is1");

        //------------------------------------ South  -------------------------------//

        //Top
        checkIfPlaceable(3,2,3,3,1,0,"is1");

        //Middle
        checkIfPlaceable(3,1,3,3,2,0,"is1");

        //Bottom
        checkIfPlaceable(3,0,3,3,2,1,"is1");

        //------------------------------------ West  -------------------------------//

        //Right
        checkIfPlaceable(2,3,1,0,3,3,"is1");

        //Middle
        checkIfPlaceable(1,3,2,0,3,3,"is1");

        //Left
        checkIfPlaceable(0,3,2,1,3,3,"is1");

        placePiece();
    }

    public void checkIfPlaceable(int xEmpty,int yEmpty, int xOneFull, int xTwoFull, int yOneFull,
                                 int yTwoFull, String tag){

        if(cpuGameBoard.boardArray[xOneFull][yOneFull].getTag() == tag
                && cpuGameBoard.boardArray[xTwoFull][yTwoFull].getTag() == tag){
            if(cpuGameBoard.boardArray[xEmpty][yEmpty].getTag() != "is1"
                    && cpuGameBoard.boardArray[xEmpty][yEmpty].getTag() != "is2"){
                if(cpuMovePhase == 1) {
                    cpuGameBoard.boardArray[xEmpty][yEmpty].performClick();
                }
            }
        }


    }

    /**
     * Chooses the piece that the computer will move;
     */
    public void pickPiece(){
        TextView error = findViewById(R.id.error);
        Random random = new Random();
        int x = random.nextInt(7);
        int y = random.nextInt(7);
        xStart = x;
        yStart = y;
        ImageButton press = boardArray[x][y];
        if (press != null) {
            if (cpuMovePhase == 1) {
                if (press.getTag() == "is2") {
                    if (true) {
                        error.setText("coords:" + x + y);
                        cpuMovePhase = 2;
                        press.performClick();
                    }
                } else {
                    pickPiece();
                }
            }
        }
        else {
            pickPiece();
        }

    }

    public void findPieceToRemove(){
        Random random = new Random();
        removeX = random.nextInt(7);
        removeY = random.nextInt(7);
    }

    /**
     * handles the movement of the computer in phase 2.
     * @param xOrigin The piece to move x coord
     * @param yOrigin the piece to move y coord
     */








}//computer
