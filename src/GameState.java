//*******************************************************************
//
// File       : GameState.java
// Assignment : Project3
// Author     : David Pitoniak dhp6397@rit.edu
// Date       : 04/06/2021
//
//*******************************************************************

import java.util.ArrayList;
import java.util.Arrays;

/**
 * State of the game. Used by NimModel to change the state of
 * the game based on players actions.
 */
public class GameState {

    // Piles being used in the game.
    private ArrayList<Integer> piles;

    public final ArrayList<Integer> INITIAL_PILES;

    /**
     * Default Constructor to initialize the game.
     */
    public GameState() {

        this.piles = new ArrayList<>();

        // Initialize Default game.
        piles.add(3);
        piles.add(4);
        piles.add(5);

        // Set the initial piles to the starting piles.
        INITIAL_PILES = new ArrayList<>(piles);

    } // end GameState.

    /**
     * Constructor to initialize the game with the
     * number of piles passed in.
     *
     * @param piles piles to start the game with.
     */
    public GameState(ArrayList<Integer> piles) {

        this.piles = piles;

        // Set the initial piles to the starting piles.
        INITIAL_PILES = new ArrayList<>(piles);

    } // end GameState.

    /**
     * clear all piles on the board.
     */
    public void clearPiles() {

        this.piles.clear();

    } // end clearPiles.

    /**
     * Set the piles for a game.
     *
     * @param piles piles to use.
     */
    public void setPiles(ArrayList<Integer> piles) {

        this.piles = piles;

    } // end setPiles.

    /**
     * Update the piles based on the move passed in.
     *
     * @param i pile index ( 0 based ).
     * @param j start pin index ( 0 based ).
     * @param val number of pins.
     */
    public void updatePiles(int i, int j, int val) {

        updatePile(this.piles.get(i), i, j, val);

    } // end updatePile.

    /**
     * Called by update piles to update a pile according to move.
     *
     * @param num number of pins.
     * @param pileIndex index of pile being updated.
     * @param i pile index ( 0 based ).
     * @param j start pin index ( 0 based ).
     */
    private void updatePile(int num, int pileIndex, int i, int j) {
        int[] pins = new int[num];

        Arrays.fill(pins, 1);

        for (; i < j; i++) {
            pins[i] = 0;
        }

        int pinCount = 0;
        int count = 0;

        for (int pin : pins) {
            if (pin == 1) {
                pinCount++;
            } else {
                this.piles.set(pileIndex + count, pinCount);
                count++;
                pinCount = 0;
            }
        }

    } // end updatePiles.

    /**
     * Returns if the piles is empty.
     *
     * @return if piles is empty.
     */
    public boolean arePilesEmpty() {

        return this.piles.isEmpty();

    } // end arePilesEmpty.

    /**
     * Creates a String representation of GameState.
     *
     * @return string representation of GameState.
     */
    public String toString() {

        StringBuilder sb = new StringBuilder();

        for (Integer i : piles) {
            sb.append(" ");
            sb.append(i);
        }

        // Remove the last space from string.
        //sb.replace(sb.length() - 1, sb.length(), "");

        return sb.toString();
    }

} // end GameState.