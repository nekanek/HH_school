/* 
 * Implements simple multithreaded chat server on port 8189. 
 * Test with telnet as "telnet localhost 8189"
 * Assumpitons: 
 *   - no user id's are preserved, no authentication or other means for client to know message author
 *   - message from one client is delivered to every connected client (including itself)
*/

import java.io.IOException;
import java.net.ServerSocket;
import java.net.InetSocketAddress; 
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.channels.ServerSocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Set;


public class ChatServer {
    private final Selector selector;
    private final int PORT = 8189;
    private final String HELLO = "Welcome to chatserver! Enter BYE to exit.\n";
    byte[] B_HELLO = HELLO.getBytes("UTF-8");
    byte[] FORMAT = "> ".getBytes("UTF-8");
    private final ByteBuffer echoBuffer = ByteBuffer.allocate(64);

    public ChatServer() throws IOException {
        Selector selector = Selector.open();
        this.selector = selector;
        start();
    }

    public static void main(String[] args ) throws IOException {  
        new ChatServer();
    }
    
    private void start() {
        Runnable connections = new ConnectionsHandler();
        Thread tConnections = new Thread(connections);
        tConnections.start();
        Runnable messages = new MessageSender();
        Thread tMessages = new Thread(messages);
        tMessages.start();
        System.out.println("All started");        
    }

    class ConnectionsHandler implements Runnable {
        @Override
        public void run() {  
            try {
                ServerSocketChannel ssc = ServerSocketChannel.open();
                ssc.configureBlocking(false);
                ServerSocket ss = ssc.socket();
                ss.bind(new InetSocketAddress(PORT));

                while (true) {  
                    SocketChannel sc = ssc.accept();
                    if (sc != null) {
                        sc.configureBlocking(false);
                        System.out.println("User " + sc + " connected");
                        sc.register( selector, SelectionKey.OP_READ );
                        ByteBuffer helloBuffer = ByteBuffer.allocate(50);
                        helloBuffer.put(B_HELLO);
                        helloBuffer.flip();
                        sc.write(helloBuffer);
                    }
                }
            }
            catch (IOException e) {}        
        }

    }

    class MessageSender implements Runnable {
        @Override
        public void run() {  
            try {  
                while(true) {
                    int readyChannels = selector.select(100);
                    if(readyChannels != 0) {
                        Set<SelectionKey> selectedKeys = selector.selectedKeys();

                        Iterator<SelectionKey> keyIterator = selectedKeys.iterator();

                        while(keyIterator.hasNext()) {
                            SelectionKey key = (SelectionKey) keyIterator.next();
                            if ((key.readyOps() & SelectionKey.OP_READ) == SelectionKey.OP_READ) {
                                SocketChannel sc = (SocketChannel)key.channel();
                                while (true) {
                                    echoBuffer.clear();
                                    int r = sc.read(echoBuffer);
                                    if (r<=0)
                                        break;
                                    echoBuffer.flip();
                                    String message = new String(echoBuffer.array(), Charset.forName("UTF-8")).trim();
                                    System.out.println(message);
                                    if (message.startsWith("BYE")) {
                                        System.out.println("connection with " + sc + " closed.\n");
                                        sc.close();
                                        break;
                                    }
                                    else {
                                        Iterator<SelectionKey> allKeyIterator = selector.keys().iterator();
                                        while(allKeyIterator.hasNext()) {
                                            echoBuffer.rewind();
                                            SocketChannel serverChannel = (SocketChannel) allKeyIterator.next().channel();
                                            serverChannel.write(echoBuffer);
                                        }
                                    }
                               }
                               keyIterator.remove();
                           }
                       }
                   }
               }            
           }
           catch (IOException e) {}
        }
    }
}