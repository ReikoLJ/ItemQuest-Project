import java.util.ArrayList;
import java.util.List;

public class Algorithm {
		
		private static Algorithm instance = null;	
	
		int heldItems = 3;         																				//total number of held items in the game
		int triggerCount = 0;
		public boolean gameHold = false;
		String userReturn1, userReturn2, userReturn3;															//set up item slots to store user returned values
		String playerInventorySlot1, playerInventorySlot2, playerInventorySlot3;								//set up item slots to store items in playerInventorySlots
		volatile List<Player> playerList = new ArrayList<Player>();
		
		Items itemManager = Items.GetInstance();
		
		private Algorithm () {
			//Default constructor
		}
		
	    //Algorithm class is singleton(only one instance will exist). This gets the current instance
	    public static Algorithm GetInstance() {
	    	
	    	if(instance == null) {
	    		instance = new Algorithm();
	    	}
	    	return instance;
	    }
		
		
		//test to see if item is in inventory
		public void TrustTest(Player player, int[] claimedInventory) {
			
			String[] playerInventory = player.GetInventory();
			
			playerInventorySlot1 = playerInventory[0];															//set the player's inventory slots
			playerInventorySlot2 = playerInventory[1];
			playerInventorySlot3 = playerInventory[2];
			
			
			for (int i = 1; i <= heldItems; i++) {
				switch (i) {
							
				case (1):	{
					userReturn1 = itemManager.ItemLookup(claimedInventory[0]);
					if (userReturn1 == playerInventorySlot1 || (userReturn1.equals("00") && playerInventorySlot1.equals("00, EMPTY"))) {
						player._playerScore++;
					}
					else {
						player._playerScore--;
					}
					break;
				}
				case (2):	{
					userReturn2 = itemManager.ItemLookup(claimedInventory[1]);
					if (userReturn2 == playerInventorySlot2 || (userReturn2.equals("00") && playerInventorySlot2.equals("00, EMPTY"))) {
						player._playerScore++;
					}
					else {
						player._playerScore--;
					}
					break;
				}
				case (3):	{
					userReturn3 = itemManager.ItemLookup(claimedInventory[2]);
					if (userReturn3 == playerInventorySlot3 || (userReturn3.equals("00") && playerInventorySlot3.equals("00, EMPTY"))) {
						player._playerScore++;
					}	
					else {
						player._playerScore--;
					}
					break;
				}
				}
			}
			
			// After test, sort player's by score to keep records up to date
			orderUsers();
		}	
		
		//Bubble sort the players by their playerScore
		public void orderUsers() {		
			
			//convert playerList to array for easy sorting
			Player[] sortList = playerList.toArray(new Player[playerList.size()]);
			
			if(playerList.size() > 1) {
				int length = sortList.length;
				Player temp = null;
				boolean flag = true;
				
				
				while(flag) {
					flag = false;
					
					for(int i = 0; i < length - 1; i++) {
						
						for(int j = 0; j < (length - 1); j++) { 
							
							if(sortList[j]._playerScore < sortList[j+1]._playerScore) {
								
								temp = sortList[j];
								sortList[j] = sortList[j+1];
								sortList[j+1] = temp;
								flag = true;
							}
						}
					}
				}
			}
			
			playerList.clear();
			
	   		for (Player s_play : sortList) {
	   			playerList.add(s_play);	
	   		}
	   		
	   		System.out.println("Current player trust scores:");
	   		for (Player player : playerList) {
	   			System.out.println(player._name + ": " + player._playerScore);
	   		}
		}
		
		public void playerInventoryAssign(Player player, String[] newInventory) {
			//Take all returned answers and assign items.
			//Once item has been claimed it cannot be claimed again and any subsequent user who attempts to claim it will end up with an empty inventory slot.
			//Focus on writing about potential improvements - full comparison against what other players have/weight on getting items at all with a low score? Ramifications
			
			for(int i = 0; i < 3; i++){
				
				//If not already on dropped list, add to inventory and list
				if (!itemManager.ItemCheck(newInventory[i])){
					player.ItemSet(i, newInventory[i]);
					itemManager.itemsDropped.add(newInventory[i]);
				}
				else {
					player.ItemSet(i, "00, EMPTY");
				}
			}
		}
		
		public void crashTest() {
			//Wait for all currently connected players to hit this point then assign inventory
			//Players with highest trust score get priority picks

			gameHold = true;
			triggerCount++;
			
			//Don't action until all players have checked their inventory in
			//Will drop through and players will be stuck in while loops until all in
			if(triggerCount == playerList.size()) {
				
				//Clear itemlist (crash simulation)
				itemManager.ClearItems();

				//playerList is in trust score order with most trusted first
				//So they are sent to inventory assignment in that order
				for(Player player : playerList) {
					
					String[] claimedInv = player.GetClaimedInventory();
					
					for(int i = 0; i < 3; i++) {
						
						System.out.println(claimedInv[i]);
					}
					
					
					playerInventoryAssign(player, claimedInv);
				}
				
				//Reset count now test has been completed
				triggerCount = 0;
				gameHold = false;
				
			}
		}
}
