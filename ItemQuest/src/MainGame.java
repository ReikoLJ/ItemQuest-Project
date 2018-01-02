import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class MainGame {

	public static void main(String[] args) {
		
		Items itemManager = Items.GetInstance();
		String moveCheck;
		String foundItem;
        
       int portNumber = 50000;

       try ( 
           ServerSocket serverSocket = new ServerSocket(portNumber);
           Socket clientSocket = serverSocket.accept();
           PrintWriter out =
               new PrintWriter(clientSocket.getOutputStream(), true);
           BufferedReader in = new BufferedReader(
               new InputStreamReader(clientSocket.getInputStream()));
       ) {
        
           String inputLine, outputLine;
            
           // Initiate conversation with client
           outputLine = "Welcome to Item Quest. What is your name?";
           out.println(outputLine);

           inputLine = in.readLine();
           
           Player player1 = new Player(inputLine);
   		
   		   String[] playerInventory = player1.GetInventory();
   		
           outputLine = "Hello " + player1.Name + ". Your starting inventory is:";
           out.println(outputLine);
			
	   		for (String item : playerInventory) {
	   			out.println(item);
	   		}

	   		while(true) {
	   			
	   		   outputLine = "Your current location is: " + player1.playerLocation() + ". Which direction would you like to go?";
	           out.println(outputLine);
	
	           inputLine = in.readLine();
           
	           moveCheck = player1.movement(inputLine);
	           
	           if(moveCheck.equals("ITEM")) {
	        	
		   		   outputLine = "ITEM";
		           out.println(outputLine);
	        	   
	        	   foundItem = itemManager.DropItem();
		   		   outputLine = "You find item " + foundItem;
		           out.println(outputLine);
		           

		           
		   		   outputLine = "Your current inventory is:";
		           out.println(outputLine);
		           
			   		for (String item : playerInventory) {
			   			out.println(item);
			   		}
			           
		   		   outputLine = "Type a slot to replace item with found one (1,2,3) or any other key to drop it:";
		           out.println(outputLine);
		           inputLine = in.readLine();
		           
		           if(inputLine.equals("1") || inputLine.equals("2") || inputLine.equals("3")) {
		        	   
		        	   int swapSlot = Integer.parseInt(inputLine);
		        	   
		        	   player1.ItemSwap(swapSlot, foundItem, playerInventory[swapSlot-1]);
		        	   //System.out.println("New Inventory");
		        	   playerInventory = player1.GetInventory();
//				   		for (String item : playerInventory) {
//				   			System.out.println(item);
//				   		}
		           }
		           else {
		        	   //If they chose not to take it then remove from dropped list to maintain validity
		        	   itemManager.itemsDropped.remove(foundItem);
		           }
		           
		           System.out.println("Current dropped list");
			   		for (String item : itemManager.itemsDropped) {
			   			System.out.println(item);
			   		}
	           }
	   		}


       } catch (IOException e) {
           System.out.println("Exception caught when trying to listen on port "
               + portNumber + " or listening for a connection");
           System.out.println(e.getMessage());
       }
	}

	
}
