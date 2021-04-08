//*******************************************************************
//
// File       : NimServer.java
// Assignment : Project3
// Author     : David Pitoniak dhp6397@rit.edu
// Date       : 04/06/2021
//
//*******************************************************************

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * NimServer accepts clients and pairs them off into Nim games.
 *
 * @author David Pitoniak dhp6397@rit.edu
 */
public class NimServer {

    // If the verbose option was specified.
    static boolean verbose;

    /**
     * Accept clients connections, and start games when two clients connect.
     *
     * @param args command line arguments.
     */
    public static void main(String[] args) {

        // If incorrect number of arguments are passed.
        if(args.length < 2) {
            System.err.println("Error: Too few arguments.");
            usage(); }
        else {

            // Piles to start the game with if they are specified in args.
            ArrayList<Integer> piles;

            // Set the default of verbose to off.
            boolean verbose = false;

            try {

                // Create new ServerSocket.
                ServerSocket serverSocket = new ServerSocket();

                // Get host and port from commandline arguments.
                String host = args[0];
                int port = Integer.parseInt(args[1]);

                // Bind serverSocket to host and port from arguments.
                serverSocket.bind(new InetSocketAddress(host, port));

                // Check if the verbose option was specified.
                // initialize accordingly.
                piles = processArgs(args);

                // Display the starting values of piles when the game starts.
                displayPiles(piles);

                NimModel model = null;

                while (true) {

                    // Accept incoming clients.
                    Socket socket = serverSocket.accept();

                    // Create a proxy for the client.
                    ViewProxy proxy = new ViewProxy(socket);

                    // If the model hasn't been initialized.
                    // Due to only one player being in the game.
                    if(model == null) {
                        //Create a new model.
                        model = new NimModel(piles, verbose);
                        proxy.setListener(model);

                    } else {
                        // Use the model that was created by
                        // the first player.
                        proxy.setListener(model);
                        model = null;
                    }
                }

            } catch (IOException | NumberFormatException ex) {
                System.err.println("Error Initial: " + ex.getMessage());
                usage();
            }
        }
    }

    /**
     * Display the piles in p.
     *
     * @param p piles to display.
     */
    private static void displayPiles(ArrayList<Integer> p) {

        System.out.print("piles:");

        for(Integer i : p) {
            System.out.print(" " + i);
        }

        System.out.println();

    } // end displayPiles

    /**
     * Process the command line arguments to get if verbose option
     * was specified or if starting piles were specified.
     *
     * @param args command line arguments.
     *
     * @throws NumberFormatException propagating up.
     *
     * @return ArrayList of piles.
     */
    public static ArrayList<Integer> processArgs(String[] args)
            throws NumberFormatException {

        if(args.length != 2) {
            if(args[2].equals("true")){

                // Verbose option is set.
                verbose = true;
                return initPiles(3, args);

            } else {

                // Start at position after port.
                return initPiles(2, args);
            }
        }

        return initPiles(args.length, args);

    } // end processArgs.

    /**
     * Get the initial values of the piles from the command line arguments.
     * If the values are not specified by command line arguments they are
     * set to 3, 4, 5.
     *
     * @param startIndex where the piles start in the argument array.
     * @param args command line arguments.
     *
     * @throws NumberFormatException parsing integers.
     *
     * @return ArrayList of piles.
     */
    public static ArrayList<Integer> initPiles(int startIndex, String[] args)
            throws NumberFormatException {
        ArrayList<Integer> p = new ArrayList<>();

        for(int i = startIndex; i < args.length; i++) {
            p.add(Integer.parseInt(args[i]));
        }

        if (p.size() == 0) {
            p.add(3);
            p.add(4);
            p.add(5);
        }

        return p;

    } // end initPiles.

    /**
     * Usage message if the user enters incorrect arguments.
     */
    public static void usage() {

        System.err.println("java NimServer <host-name> <port-number> " +
                "[true] [pile-info...]");
        System.exit(1);

    } // end usage.

} // end NimServer.
