public class Algorithm {
																							
		int heldItems = 3;         																				//total number of held items in the game
		String userReturn1, userReturn2, userReturn3;															//set up item slots to store user returned values
		String playerInventorySlot1, playerInventorySlot2, playerInventorySlot3;								//set up item slots to store items in playerInventorySlots
		
		Items itemManager = Items.GetInstance();
		
		
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
					if (userReturn1 == playerInventorySlot1 || userReturn1 == playerInventorySlot2 || userReturn1 == playerInventorySlot3) {
						player.playerScore++;
					}
					else {
						player.playerScore--;
					}
					break;
				}
				case (2):	{
					userReturn2 = itemManager.ItemLookup(claimedInventory[1]);
					if (userReturn2 == playerInventorySlot1 || userReturn2 == playerInventorySlot2 || userReturn2 == playerInventorySlot3) {
						player.playerScore++;
					}
					else {
						player.playerScore--;
					}
					break;
				}
				case (3):	{
					userReturn3 = itemManager.ItemLookup(claimedInventory[2]);
					if (userReturn3 == playerInventorySlot1 || userReturn3 == playerInventorySlot2 || userReturn3 == playerInventorySlot3) {
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
		
		public void orderUsers() {
			//TODO: Order users by their playerScore.
			
			
			
			//TODO: Ask players for their inventory in descending order.
		}
		
		public void playerInventoryAssign() {
			//TODO: Take all returned answers and assign items.
			//Once item has been claimed it cannot be claimed again and any subsequent user who attempts to claim it will end up with and empty inventory slot.
			// Focus on writing about potential improvements - full comparison
		}
}
