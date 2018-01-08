import java.net.*;
import java.io.*;

public class ServerThread extends Thread {
 
	int trustTest = 3;				//change to trigger trust test at different rate
	int crashTest = 4;				//change to trigger crash test at different rate
	int loopCounter = 0;			//used for inventory check trigger
	int loopCounter2 = 0;
	int[] claimedInventory = new int[3];
	String moveCheck;
	String foundItem;
	boolean gameEnd = false;
	
	Items itemManager = Items.GetInstance();
	Algorithm checker = Algorithm.GetInstance();
	
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
        checker.playerList.add(currentPlayer);
		
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
        	   // See if player has moved n times. If so then an item drops.
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
		           
		           // If they chose to swap then replace their inventory slot with found item
		           if(inputLine.equals("1") || inputLine.equals("2") || inputLine.equals("3")) {
		        	   
		        	   int swapSlot = Integer.parseInt(inputLine);
		        	   
		        	   currentPlayer.ItemSwap(swapSlot-1, foundItem, playerInventory[swapSlot-1]);
		        	   playerInventory = currentPlayer.GetInventory();

		           }
		           else {
		        	   //If they chose not to take it then remove from dropped list to maintain validity
		        	   itemManager.itemsDropped.remove(foundItem);
		           }
		           
		           // Below for testing purposes on server side
//			           System.out.println("Current dropped list");
//				   		for (String item : itemManager.itemsDropped) {
//				   			System.out.println(item);
//				   		}
	           }
	           
	           //Increment loop counters 
	           loopCounter++;
	           loopCounter2++;
	           
	           ////////////////////// Crash Test ///////////////////////////////
	           //Section for testing crash conditions. Comment out for normal operation
	           if(loopCounter2 == crashTest) {
		   		    outputLine = "CRASH";
		            out.println(outputLine);
	        	   
	        	   // Send crash message to client (1 line no response)
		   		    outputLine = "----Server Crash---- Enter Inventory:";
		            out.println(outputLine);
		            
		           //Get inventory from client (send and receive x 3) 
			   		for (int i = 0; i < 3; i++) {
				   		   outputLine = "What item number is in slot : " + (i+1) + "?";
				           out.println(outputLine);
				           inputLine = in.readLine();
				           claimedInventory[i] = Integer.parseInt(inputLine);
				   		}
			   		currentPlayer.InventoryClaim(claimedInventory);
	
	        	    checker.crashTest();
	        	   
	        	   // Send wait message to client (1 line no response)
		   		    outputLine = "----Please Wait----";
		            out.println(outputLine);
	        	  
		            
	        	   while(checker.gameHold) {
	        		   //Hold until all inventory checks are in and assignment complete
   	            		try {
								Thread.sleep(5000);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
	        	   }
	        	   
	        	   // Send post-crash message to client (4 lines no response)
		   		    outputLine = "----Game resuming---- Current Inventory:";
		            out.println(outputLine);
			           
			   		for (String item : playerInventory) {
			   			out.println(item);
			   		}
		            
		            
	        	   //Reset both counters as check not needed after a crash
	        	   loopCounter = 0;
	        	   loopCounter2 = 0;
	           }
	           	//////////////////////Trust Test ///////////////////////////////
	           else if(loopCounter == trustTest) {
		   		    outputLine = "CHECK";
		            out.println(outputLine);
		            
	        	   // Send check message to client (4 lines)
		   		    outputLine = "----Inventory check---- Last record:";
		            out.println(outputLine);
		           
			   		for (String item : playerInventory) {
			   			out.println(item);
			   		}
			   	
			   		
			   		// Get each slot of inventory from client
			   		for (int i = 0; i < 3; i++) {
			   		   outputLine = "What item number is in slot : " + (i+1) + "?";
			           out.println(outputLine);
			           inputLine = in.readLine();
			           claimedInventory[i] = Integer.parseInt(inputLine);
			   		}
			   		
			   		checker.TrustTest(currentPlayer, claimedInventory);
			   		
			   		//Reset counter 
			   		loopCounter = 0;
	           }
           }
           else {
        	   
        	   //If here then 'exit' has been typed by client
        	   
        	   //Clean up player records from game
        	   currentPlayer.clearInventory();
        	   checker.playerList.remove(currentPlayer);
        	   
        	   //Skip game text and exit while loop
        	   gameEnd = true;
           }
    
   		}
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}