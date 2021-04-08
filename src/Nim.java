//*******************************************************************
//
// File       : Nim.java
// Assignment : Project3
// Author     : David Pitoniak dhp6397@rit.edu
// Date       : 04/06/2021
//
//*******************************************************************

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * Client end of Nim networked game to interact with server.
 *
 * @author David Pitoniak dhp6397@rit.edu
 */
public class Nim {

    /**
     * Main method to connect the client to the server.
     *
     * @param args command line arguments.
     */
    public static void main(String[]args) {

        if(args.length < 3) { usage(); }
        else {

            // Hostname of server.
            String hostname;

            // Port of server.
            int port;

            // Username to play game with.
            String username;

            // Get host and port from commandline arguments.
            hostname = args[0];
            port = Integer.parseInt(args[1]);

            // Get username from arguments.`
            username = args[2];

            try {
                // Create new Socket.
                Socket socket = new Socket();

                // Connect socket to server of hostname and port.
                socket.connect(new InetSocketAddress(hostname, port));

                NimView view = new NimView();
                ModelProxy proxy = new ModelProxy(socket);

                view.setListener(proxy);
                proxy.setListener(view);

                proxy.join(view, username);

            } catch (IOException ex) {
                System.err.println(ex.getMessage());
            }
        }

    } // end main.

    /**
     * Usage message if the user enters incorrect arguments.
     */
    private static void usage() {
        System.err.println("java Nim <host-name> <port-number> <username>");
        System.exit(1);

    } // end usage.

} // end Nim.
