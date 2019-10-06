
/* A Java program for a Server */
import java.net.*;
import java.io.*;

public class chat_server
{
/* initialize socket and input stream */
private Socket[] socket = new Socket[100];
private ServerSocket server = null;
private DataInputStream in = null;
private DataOutputStream out = null;
private String[] clients = new String[100];
//free = 0, busy = 1
private Boolean[] states = new Boolean[100];
private int numClients = 0;
private int numFree = 0;

/* constructor with port */
public chat_server(int port)
{
	/* starts server and waits for a connection */
	try {
		server = new ServerSocket(port);
	} catch(Exception i) {
		System.out.println("Error in port");
		System.exit(0);
	}
	System.out.println("Server started");

  for (int i = 0; i < 100; i++) {
    socket[i] = null;
    clients[i] = "";
    states[i] = false;
  }

  int counter = 0;
  try {
    while (counter < 100) {

    	System.out.println("Waiting for a client ...");
    	try {
    		socket[counter] = server.accept();
    		System.out.println("Client accepted");
        /* takes input from the client socket */
        numClients++;
        numFree ++;

        in = new DataInputStream(
            new BufferedInputStream(socket[counter].getInputStream()));
        out = new DataOutputStream(
            new BufferedOutputStream(socket[counter].getOutputStream()));

        clients[counter] = in.readUTF();

        System.out.println("List of clients and states");
        int j = 0;
        for (int i = 0; i < 100; i++) {
          if (socket[i] != null) {
            line = clients[i] + "          ";
            if (states[i]) {
              line = line + "busy";
            } else {
              line = line + "free";
            }
            System.out.println(line);
            out.writeUTF(line);
            j++;
          }
          if (j >= numClients) {
            break;
          }
        }

        if (numFree >= 2) {
          connect();
        }

        counter++;
      } catch(Exception i) {
    	    System.out.println(i);
    	}

    }
    System.out.println("Closing connection");

    /* close connection */
    socket[counter].close();
    in.close();
    out.close();
	} catch(EOFException i) {
	    System.out.println(i);
	}
	catch(Exception i) {
	    System.out.println(i);
	}
}

public static void main(String args[])
{
	if (args.length < 1) {
		System.out.println("Server usage: java chat_server #port_number");
	}
	else {
		try {
			chat_server server = new chat_server(Integer.parseInt(args[0]));
		} catch(Exception i) {
			System.out.println("Error in port");
		}
	}
}

private static void connect(){

}


}
