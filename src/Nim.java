import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

public class Nim {

    public static void main(String[]args) {

        if(args.length < 3) { usage(); }
        else {

            String hostname;
            String username;
            int port;

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

                /*
                    ### figure out how to get piles to the view. ###
                */
                NimView view = new NimView(username);

                ModelProxy proxy = new ModelProxy(socket);

                view.setListener(proxy);
                proxy.setListener(view);

                proxy.join(view, username);

            } catch (IOException ex) {
                System.err.println(ex.getMessage());
            }

        }

    } // end main.

    private static void usage() {
        System.err.println("java Nim <host-name> <port-number> <username>");
        System.exit(1);

    } // end usage.

} // end Nim.
