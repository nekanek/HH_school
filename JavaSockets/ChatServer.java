/* 
 * Implements simple multithreaded chat server on port 8189. 
 * Test with telnet as "telnet localhost 8189"
 * Assumpitons: 
 *   - no user id's are preserved, no authentication or other means for client to know message author
 *   - message from one client is delivered to evry connected client (including itself)
*/

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class ChatServer {
    public static void main(String[] args ) {  
        try {
            ArrayList<Socket> connections = new ArrayList<>(1);
            int i = 0;
            ServerSocket s = new ServerSocket(8189);

            while (true) {  
                Socket incoming = s.accept();
                System.out.println("User #" + i + " connected");
                connections.add(incoming);
                Runnable r = new ThreadedEchoHandler(incoming, connections);
                Thread t = new Thread(r);
                t.start();
                i++;
            }
        }
        catch (IOException e) {  
            e.printStackTrace();
        }
    }
}

class ThreadedEchoHandler implements Runnable {
    
    private final Socket incoming;
    public ArrayList<Socket> connections;

    public ThreadedEchoHandler(Socket s, ArrayList<Socket> c) { 
        incoming = s;
        this.connections = c;
    }

    @Override
    public void run() {  
        try {  
            try {
                InputStream inStream = incoming.getInputStream();
                OutputStream outStream = incoming.getOutputStream();
                Scanner in = new Scanner(inStream);         
                PrintWriter out = new PrintWriter(outStream, true); // true for autoFlush
            
                out.println( "Welcome to chatserver! Enter BYE to exit." );
            
                boolean done = false;
                while (!done && in.hasNextLine()) {  
                    String line = in.nextLine();
                    for (Socket s : connections) {
                        OutputStream sStream = s.getOutputStream();
                        PrintWriter sOut = new PrintWriter(sStream, true);
                        sOut.println("> " + line);
                    }
                    System.out.println("Echoed (" + line + ") from " + incoming);
                    if (line.trim().equals("BYE"))
                        done = true;
                }
            }
            finally {
                connections.remove(incoming);
                incoming.close();
            }
        }
        catch (IOException e) {  
            e.printStackTrace();
        }
    }
}
