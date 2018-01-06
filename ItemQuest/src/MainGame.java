import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;

public class MainGame{

	public static void main(String[] args) {
		
		Algorithm init = Algorithm.GetInstance();
		
        int portNumber = 50000;
        boolean listening = true;
        //Global 
        //List<Thread> connectedClients = new ArrayList<Thread>();
        
        try (ServerSocket serverSocket = new ServerSocket(portNumber)) { 
        	
            while (listening) {
            	
        	   while(init.gameHold) {
        		   //Don't accept new players during crash conditions
            		try {
						Thread.sleep(5000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
        	   }
        	   
            	Thread client = new Thread(new ServerThread(serverSocket.accept()));           	
            	client.start();
	            //connectedClients.add(client);
	            //System.out.println(connectedClients.size());

	        }
	    
        } catch (IOException e) {
            System.err.println("Could not listen on port " + portNumber);
            System.exit(-1);
        }

    }
        

}
