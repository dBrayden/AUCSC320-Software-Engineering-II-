package com.example.ninemensmorrisapp;

import java.io.Serializable;

/**
 * Creates a Player of 9 Apples Morris, with methods to check whether the Player has
 * three or two apples left on the tree, along with setters and getters to change and
 * obtain the private instance variables.
 */
public class Player implements Serializable {

    private int numOfApples;
    private int numOfApplesOnTree;
    private int idOfImageViewOfApple;


    /**
     * Creates a Player of 9 Apples Morris, setting the apple of the player to the image ID of the
     * apple.
     * @param idOfAppleImage id of the apple that is red, green or yellow
     */

    public Player(int idOfAppleImage) {
        numOfApples = 9;
        numOfApplesOnTree = 0;
        idOfImageViewOfApple = idOfAppleImage;
    } // Player

    /**
     * Default constructor that creates the Player with the red apple set as the apple of that
     * Player.
     */
    public Player() {
        this(R.drawable.redapple);
    } // Player

    /**
     * Gets the amount of apples the player has.
     * @return - # of apples of a player
     */
    public int getNumOfApples() {
      return numOfApples;
    } // getNumOfApples

    /**
     * Sets the amount of apples the player has to the new value.
     * @param valueOfApples - the new value of the # of apples the player has
     */
    public void setNumOfApples(int valueOfApples) {
        numOfApples = valueOfApples;
    } // setNumOfApples

    /**
     * Gets the amount of apples the player has on the tree.
     * @return # of apples of a player on the tree
     */
    public int getNumOfApplesOnTree() { return numOfApplesOnTree; } // getNumOfApplesOnTree

    /**
     * Sets the amount of apples the player has on the tree to the new value.
     * @param valueOfApplesOnTree - the new value of the # of apples the player has on the tree
     */
    public void setNumOfApplesOnTree(int valueOfApplesOnTree) { numOfApplesOnTree = valueOfApplesOnTree; } // setNumOfApplesOnTree

    /**
     * Gets the apple of the player.
     * @return apple image ID of the player
     */
    public int getIdOfImageViewOfApple() {
        return idOfImageViewOfApple;
    } // getIdOfImageViewOfApple

    public int getAppleOfPlayer() { return idOfImageViewOfApple; }

    /**
     * Sets the ID of the apple of the player.
     * @param idOfImageViewOfApple - new apple ID
     */
    public void setIdOfImageViewOfApple(int idOfImageViewOfApple) {
        this.idOfImageViewOfApple = idOfImageViewOfApple;
    } // setIdOfImageViewOfApple
    public void setAppleOfPlayer(int newImage) { idOfImageViewOfApple = newImage; }

    /**
     * Checks if all of the apples have been put on the tree.
     * @return true if all apples are on the tree, false otherwise
     */
    public boolean areNoApplesLeft() {
        if (numOfApples == 0) {
            return true;
        } else {
            return false;
        }
    } // areNoApplesLeft
    /**
     * If the player has 3 apples remaining on the tree, the method returns true and the player will
     * have all restrictions lifted, meaning they are able to move their apples to any spot in the
     * apple tree. Otherwise, the method returns false and the player continues playing the game.
     * @return true if # of apples is 3, false otherwise
     */
    public boolean areThreeApplesLeftOnTheTree() {
        if (numOfApplesOnTree == 3) {
            return true;
        } else {
            return false;
        }
    } // areThreeApplesLeftOnTheTree

    /**
     * If the player has 2 or less apples remaining on the tree, the method returns true and the
     * player loses the game. Otherwise, the method returns false and the player continues playing
     * the game.
     * @return true if # of apples is less than or equal to 2, false otherwise
     */
    public boolean areTwoApplesLeftOnTheTree() {
        if (numOfApplesOnTree <= 2) {
            return true;
        } else {
            return false;
        }
    } // areTwoApplesLeftOnTheTree
} // class
