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
 *
 * @author David Pitoniak dhp6397@rit.edu
 */
public class GameState {

    // Piles being used in the game.
    private ArrayList<Integer> piles;

    // Initial piles for new games.
    public ArrayList<Integer> INITIAL_PILES;

    /**
     * Constructor to initialize the game with the
     * number of piles passed in.
     *
     * @param piles piles to start the game with.
     */
    public GameState(ArrayList<Integer> piles) {

        // Set start game state.
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
     * Initialize game states.
     */
    public void setInitialPiles(ArrayList<Integer> piles) {

        this.piles = piles;
        this.INITIAL_PILES = new ArrayList<>(piles);

    } // end setInitialPiles

    /**
     * Update the piles based on the move passed in.
     *
     * @param i pile index ( 0 based ).
     * @param j start pin index ( 0 based ).
     * @param val number of pins.
     */
    public void updatePiles(int i, int j, int val) {

        // Update the pile at specified pile index.
        updatePile(i, this.piles.get(i), j, val);

    } // end updatePile.

    /**
     * Called by update piles to update a pile according to move.
     *
     * @param num number of pins.
     * @param i start pin index ( 0 based ).
     * @param j number to remove ( 0 based ).
     */
    private void updatePile(int pileIndex, int num, int i, int j) {

        // Array to represent the pins in a pile.
        int[] pins = new int[num];

        // Fill the array with "pins".
        Arrays.fill(pins, 1);

        // Remove the specified "pins".
        for(int index = i; index < j+i; index++) {
            pins[index]=0;
        }

        int pinCount = 0;
        int count = 0;

        // Remove the original pile from the piles.
        this.piles.remove(pileIndex);

        // Update the new piles.
        for (int pin : pins) {
            if (pin == 1) {
                pinCount++;
            } else {
                this.piles.add(pileIndex + count, pinCount);
                count++;
                pinCount = 0;
            }
        }

        // Add last pile into the piles.
        this.piles.add(pileIndex + count, pinCount);

        // Remove the empty piles.
        piles.removeIf(filter -> filter == 0);

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

        for (Integer pinNum : piles) {
            sb.append(" ");
            sb.append(pinNum);
        }

        return sb.toString();

    } // end toString.

} // end GameState.