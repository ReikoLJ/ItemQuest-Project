import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class MainGameConnect {

	public static void main(String[] args) throws IOException {
	
	       	//change to current PC
	       	//String hostName = "192.168.0.10";
			String hostName = "10.30.8.162";
			int portNumber = 50000;

	        try (
	            Socket kkSocket = new Socket(hostName, portNumber);
	            PrintWriter out = new PrintWriter(kkSocket.getOutputStream(), true);
	            BufferedReader in = new BufferedReader(
	                new InputStreamReader(kkSocket.getInputStream()));
	        ) {
	            BufferedReader stdIn =
	                new BufferedReader(new InputStreamReader(System.in));
	            String fromServer;
	            String fromUser;
	            
	            //Game setup
	            fromServer = in.readLine();
	            System.out.println(fromServer);
	            
	            fromUser = stdIn.readLine();
	            out.println(fromUser);
	            
	            //Getting initial inventory statement and
	            //First request for movement input
	            for(int i = 0; i <= 4; i++) {
	            	fromServer = in.readLine();
	            	System.out.println(fromServer);
	            }
	            //First movement send
	            fromUser = stdIn.readLine();
	            out.println(fromUser);

               //Main game loop
	            while ((fromServer = in.readLine()) != null) {

	            	if (fromServer.equals("ITEM")) {
	    	            for(int i = 0; i <= 5; i++) {
	    	            	fromServer = in.readLine();
	    	            	System.out.println(fromServer);
	    	            }
	    	            //Send item replacement response
	    	            fromUser = stdIn.readLine();
	    	            out.println(fromUser);
	            	}
	            	else {
		            	System.out.println(fromServer);
		                fromUser = stdIn.readLine();
		                if (fromUser != null) {
		                    out.println(fromUser);
		                }
	            	}

	            }
	        } catch (UnknownHostException e) {
	            System.err.println("Don't know about host " + hostName);
	            System.exit(1);
	        } catch (IOException e) {
	            System.err.println("Couldn't get I/O for the connection to " +
	                hostName);
	            System.exit(1);
	        }
	}
	
}
