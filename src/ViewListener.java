//*******************************************************************
//
// File       : ViewListener.java
// Assignment : Project3
// Author     : David Pitoniak dhp6397@rit.edu
// Date       : 04/06/2021
//
//*******************************************************************

/**
 * Interface to be implemented by an object that takes reports from
 * the View in a Nim game.
 *
 * @author David Pitoniak dhp6397@rit.edu
 */
public interface ViewListener {

    /**
     * Report that a player has joined.
     *
     * @param view Calling view object.
     * @param username Name of player.
     */
    void join(ModelListener view, String username);

    /**
     * Report that a move has been made.
     *
     * @param view Calling view object.
     * @param pn pile index ( 0 based ).
     * @param sp start pin index ( 0 based ).
     * @param np number of pins to take.
     */
    void moveRequest(ModelListener view, int pn, int sp, int np);

    /**
     * Report that a new game is requested.
     *
     * @param view Calling view object.
     */
    void newGame(ModelListener view);

    /**
     * Report that a player has quit.
     *
     * @param view Calling view object.
     */
    void quit(ModelListener view);

    /**
     * Report a player asked for help.
     *
     * @param view Calling view object.
     */
    void help(ModelListener view);

 } // end ViewListener.