
/* A Java program for a Server */
import java.net.*;
import java.io.*;

public class chat_server implements Runnable
{
/* initialize socket and input stream */
private Socket[]      socket = new Socket[100];
private ServerSocket  server = null;
private DataInputStream   in = null;
private DataOutputStream out = null;
private Thread        thread = null;
private String[]     clients = new String[100];
//free = 0, busy = 1
private Boolean[]     states = new Boolean[100];
private int       numClients = 0;
private int          numFree = 100;
private int    clientCounter = 0;

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

  connect();
}

public void run(){
  while(thread != null)
    try {

      	System.out.println("Waiting for a client ...");
      	try {
      		socket[clientCounter] = server.accept();
          System.out.println(clientCounter);
          System.out.println(socket[clientCounter]);
      		System.out.println("Client accepted");
          /* takes input from the client socket */
          numClients++;
          numFree ++;

          in = new DataInputStream(
              new BufferedInputStream(socket[clientCounter].getInputStream()));
          out = new DataOutputStream(socket[clientCounter].getOutputStream()));
          System.out.println(out);

          clients[clientCounter] = in.readUTF();
          

          out.writeUTF("List of clients and states");
          out.flush();
          System.out.println("List of clients and states");


          String line;

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
              System.out.println("Try " + i);
              out.writeUTF(line);
              j++;
            }
            if (j >= numClients) {
              System.out.println("break: " + i);
              break;
            }
          }

          clientCounter++;
        } catch(Exception i) {
      	    System.out.println(i);
      	}



  	} catch(Exception i) {
  	    System.out.println(i);
  	}
  }
}

public void connect(){
  if (thread == null) {
    thread = new Thread(this);
    thread.start();
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



}
