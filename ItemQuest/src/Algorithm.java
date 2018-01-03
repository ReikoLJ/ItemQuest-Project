public class Algorithm {
																							
		int heldItems = 3;         																				//total number of held items in the game
		String userReturn1, userReturn2, userReturn3;															//set up item slots to store user returned values
		String playerInventorySlot1, playerInventorySlot2, playerInventorySlot3;								//set up item slots to store items in playerInventorySlots
		
		Items itemManager = Items.GetInstance();
		
		public Algorithm () {
			//Default constructor
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
					if (userReturn1 == playerInventorySlot1 || (userReturn1.equals("EMPTY") && playerInventorySlot1 == null)) {
						player.playerScore++;
					}
					else {
						player.playerScore--;
					}
					break;
				}
				case (2):	{
					userReturn2 = itemManager.ItemLookup(claimedInventory[1]);
					if (userReturn2 == playerInventorySlot2 || (userReturn2.equals("EMPTY") && playerInventorySlot2 == null)) {
						player.playerScore++;
					}
					else {
						player.playerScore--;
					}
					break;
				}
				case (3):	{
					userReturn3 = itemManager.ItemLookup(claimedInventory[2]);
					if (userReturn3 == playerInventorySlot3 || (userReturn3.equals("EMPTY") && playerInventorySlot3 == null)) {
						player.playerScore++;
					}	
					else {
						player.playerScore--;
					}
					break;
				}
				}
			}
		}	
		
		//Bubble sort the players by their playerScore
		public Player[] orderUsers(Player[] playerList) {			
			int length = playerList.length;
			Player temp = null;
			boolean flag = true;
			
			while(flag) {
				flag = false;
				for(int i = 0; i < length; i++) {
					for(int j = 0; j < (length - i); j++) {
						if(playerList[j].playerScore < playerList[j+1].playerScore) {
							temp = playerList[j];
							playerList[j].playerScore = playerList[j+1].playerScore;
							playerList[j+1] = temp;
							flag = true;
						}
					}
				}
			}
			return playerList;
		}
		
		public void playerInventoryAssign(Player player, int[] newInventory) {
			//TODO: Take all returned answers and assign items.
			//Once item has been claimed it cannot be claimed again and any subsequent user who attempts to claim it will end up with and empty inventory slot.
			// Focus on writing about potential improvements - full comparison
			
			for(int i = 0; i < 3; i++){
				String itemString = itemManager.ItemLookup(newInventory[i]);
				
				if (!itemManager.ItemCheck(itemString)){
					player.ItemSwap(i, itemString, null);
				}
			}
		}
}
