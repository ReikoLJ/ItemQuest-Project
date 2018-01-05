import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.io.*;

public class ServerThread extends Thread {
    
	Items itemManager = Items.GetInstance();
	String moveCheck;
	String foundItem;
	List<Player> playerList = new ArrayList<Player>();
	int loopCounter = 0;
	Algorithm checker = new Algorithm();
	boolean gameEnd = false;
	
	private Socket clientSocket = null;

    public ServerThread(Socket socket) {
        super("ServerThread");
        this.clientSocket = socket;
    }
    
    public void run() {

        try (
        		
        	PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));	
        		
        ) {
        
        	////////////////////// Game Setup ///////////////////////////////
 	   
        String inputLine, outputLine;           
        
        // Initiate conversation with client
        outputLine = "Welcome to Item Quest. What is your name?";
        out.println(outputLine);
        
        // Receive response
        inputLine = in.readLine();
        
        // Create new player object with given name
        Player currentPlayer = new Player(inputLine);
        playerList.add(currentPlayer);
		
        // Setup initial inventory
		   String[] playerInventory = currentPlayer.GetInventory();
		   
		   // Send inventory to client (4 lines total - no response req)
        outputLine = "Hello " + currentPlayer._name + ". Your starting inventory is:";
        out.println(outputLine);
			
	   		for (String item : playerInventory) {
	   			out.println(item);
	   		}
	   		
   			////////////////////// Main Game Loop ///////////////////////////////
	   		
	   		while(!gameEnd) {
	   			
	   		   // Send movement request to client 
	   		   outputLine = "Your current location is: " + currentPlayer.playerLocation() + ". Which direction would you like to go?";
	           out.println(outputLine);
	           
	           // Receive response
	           inputLine = in.readLine();
	           
	           if (!inputLine.equals("exit")) {
	        	// See if player has moved 5 times. If so then an item drops.
		           moveCheck = currentPlayer.movement(inputLine);
		           
		           if(moveCheck.equals("ITEM")) {
		        	   
		        	   // Send item drop message to client processor (No response req)
			   		   outputLine = moveCheck;
			           out.println(outputLine);
		        	   
			           // Send message for client user and current inventory (6 lines total including above)
		        	   foundItem = itemManager.DropItem();
			   		   outputLine = "You find item " + foundItem;
			           out.println(outputLine);
			           		           
			   		   outputLine = "Your current inventory is:";
			           out.println(outputLine);
			           
				   		for (String item : playerInventory) {
				   			out.println(item);
				   		}
				       
				   	   // Send inventory replace option to client, 
			   		   outputLine = "Type a slot to replace item with found one (1,2,3) or any other key to drop it:";
			           out.println(outputLine);
			           
			           // Receive response
			           inputLine = in.readLine();
			           
			           // If they chose to swap then replace their inventory slot and remove to 
			           if(inputLine.equals("1") || inputLine.equals("2") || inputLine.equals("3")) {
			        	   
			        	   int swapSlot = Integer.parseInt(inputLine);
			        	   
			        	   currentPlayer.ItemSwap(swapSlot, foundItem, playerInventory[swapSlot-1]);
			        	   playerInventory = currentPlayer.GetInventory();

			           }
			           else {
			        	   //If they chose not to take it then remove from dropped list to maintain validity
			        	   itemManager.itemsDropped.remove(foundItem);
			           }
			           
			           // Below for testing purposes on server side
			           System.out.println("Current dropped list");
				   		for (String item : itemManager.itemsDropped) {
				   			System.out.println(item);
				   		}
		           }
		           
		           loopCounter++;
		           if(loopCounter == 7) {
			   		    outputLine = "CHECK";
			            out.println(outputLine);
			            
		        	   // Send check message to client (4 lines)
			   		    outputLine = "Inventory check. Last record:";
			            out.println(outputLine);
			           
				   		for (String item : playerInventory) {
				   			out.println(item);
				   		}
				   	
				   		int[] claimedInventory = new int[3];
				   		
				   		for (int i = 0; i < 3; i++) {
				   		   outputLine = "What item number is in slot :" + (i+1) + "?";
				           out.println(outputLine);
				           inputLine = in.readLine();
				           claimedInventory[i] = Integer.parseInt(inputLine);
				   		}
				   		
				   		checker.TrustTest(currentPlayer, claimedInventory);
				   		loopCounter = 0;
				   		System.out.println("Player score:" + currentPlayer._playerScore);
		           }
	           }
	           else {
	        	   //skip game text and exit while loop
	        	   gameEnd = true;
	           }
        
	   		}
	   		
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}