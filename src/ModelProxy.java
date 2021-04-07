//*******************************************************************
//
// File       : ModelProxy.java
// Assignment : Project3
// Author     : David Pitoniak dhp6397@rit.edu
// Date       : 04/06/2021
//
//*******************************************************************

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

/**
 *
 *
 * @author David Pitoniak dhp6397@rit.edu
 */
public class ModelProxy implements ViewListener {

    // Socket from the client that will be communicating with View Proxy.
    private Socket socket;

    // Output and input streams for socket.
    private DataOutputStream out;
    private DataInputStream in;

    // Listener to handle input from the socket and notify clients.
    private ModelListener listener;

    /**
     *
     * @param socket client socket.
     */
    public ModelProxy(Socket socket) {

        try {

            this.socket = socket;

            socket.setTcpNoDelay(true);

            out = new DataOutputStream(socket.getOutputStream());
            in = new DataInputStream(socket.getInputStream());

        } catch (IOException e) {
            handleErrorMessage(e.getMessage());
        }

    } // end ModelProxy Constructor.

    public void setListener(ModelListener listener) {

        // Set the listener to update on messages.
        this.listener = listener;

        // Start the reader thread to read messages from ViewProxy.
        new ReaderThread().start();

    } // end setListener.

    @Override
    public void join(ModelListener view, String username) {

        try {

            // Write out the header Byte of 'J' for join.
            out.writeByte('J');

            // Write out the username of user joining.
            out.writeUTF(username);

            // Flush the output.
            out.flush();

        } catch (IOException e) {
            handleErrorMessage(e.getMessage());
        }

    } // end join.

    @Override
    public void moveRequest(ModelListener view, int pn, int sp, int np) {

        try {
            // Write out the header Byte of 'R' for Request.
            out.writeByte('R');

            // Write out move details.
            out.writeByte(pn);
            out.writeByte(sp);
            out.writeByte(np);

            // Flush the output.
            out.flush();

        } catch (IOException e) {
            handleErrorMessage(e.getMessage());
        }
    }

    @Override
    public void newGame(ModelListener view, ArrayList<Integer> piles) {

        try {
            // Write out the header Byte of 'N' for New Game.
            out.writeByte('N');

            // Write out all piles.
            for(Integer i : piles) {
                out.writeByte(i);
            }

            // Signal that the pile is finished.
            out.writeByte(-1);

            // Flush the output.
            out.flush();

        } catch (IOException e) {
            handleErrorMessage(e.getMessage());
        }

    } // end newGame.

    @Override
    public void quit(ModelListener view) {

        try {
            // Write out the header Byte of 'Q' for quit.
            out.writeByte('Q');

            // Flush the output.
            out.flush();

        } catch (IOException e) {
            handleErrorMessage(e.getMessage());
        }

    } // end quit.

    @Override
    public void help(ModelListener view) {

    } // end help.

    /**
     * Prints msg to System.err and exits with code 1.
     *
     * @param msg message to print to err.
     */
    private void handleErrorMessage(String msg) {

        System.err.println(msg);
        System.exit(1);

    } // end handleErrorMessage.

/* ######################### Helper Class ######################## */

    /**
     *
     */
    private class ReaderThread extends Thread {
        public void run() {
            int header, pn, sp, np;
            String name;

            try {
                while(true) {
                    header = in.readByte();
                    switch (header) {
                        case 'N':
                            // Create storage variables.
                            ArrayList<Integer> piles = new ArrayList<>();
                            int val = in.readByte();

                            // Get input while not -1 Byte.
                            while(val != -1) {
                                piles.add(val);
                                val = in.readByte();
                            }

                            // Send piles to newGame.
                            listener.newGame(piles);
                        case 'R': listener.updatePile(in.readByte(), in.readByte(), in.readByte());
                        case 'W': listener.waitingForOpponent();
                        case 'Y': listener.yourTurn();
                        case 'O': listener.otherTurn();
                        case 'U': listener.youWin();
                        case 'L': listener.otherWin();
                        case 'Q': listener.quit();
                        default:
                            System.err.println("Bad Message.");
                            System.exit(1);
                    }
                }
            } catch (IOException e) {
                handleErrorMessage(e.getMessage());
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {
                    handleErrorMessage(e.getMessage());
                }
            }

        } // end run.

    } // end ReaderThread.

} // end ModelProxy.

