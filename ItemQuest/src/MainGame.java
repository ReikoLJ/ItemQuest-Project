import java.io.IOException;
import java.net.ServerSocket;

public class MainGame{

	public static void main(String[] args) {
		
        int portNumber = 50000;
        boolean listening = true;

        try (ServerSocket serverSocket = new ServerSocket(portNumber)) { 
            while (listening) {
	            new ServerThread(serverSocket.accept()).start();
	        }
	    } catch (IOException e) {
            System.err.println("Could not listen on port " + portNumber);
            System.exit(-1);
        }

    }
        
//    	// Reference code 
//    	Player[] sentList = playerList.toArray(new Player[playerList.size]);
}
