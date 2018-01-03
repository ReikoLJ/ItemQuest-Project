
public class Player {

	public String Name;
	private String mInventory[] = new String[3];
	private int movementCounter = 0;
	private int itemCounter = 0;
	public int currentX = 0;
	public int currentY = 0;
	public String moveResponse;
	public int playerScore = 10;
	
	Items mItemManager = Items.GetInstance();
	
	public Player(String name) {
		
		Name = name;
		StarterInventory();
	}
	
	public String[] GetInventory() {
		
		return mInventory;
	}
	
	private void StarterInventory(){
		
//		for(int i = 0; i < 3; i++){
//			mInventory[i] = mItemManager.DropItem();
//		}
		for(int i = 0; i < 3; i++){
			mInventory[i] = null;
		}
		
	}
	
	public void ItemSwap(int invIndex, String newItem, String oldItem) {
		mInventory[invIndex-1] = newItem;
		mItemManager.itemsDropped.remove(oldItem);
	}
	

	public String movement(String direction) {
		
		Direction moveDirection = Direction.valueOf(direction.toUpperCase());
		
		switch(moveDirection) {
			
		case NORTH:
		case 	 N:
			currentY++;
			break;
		case  EAST:
		case     E:
			currentX++;
			break;		
		case SOUTH:
		case 	 S:
			currentY--;
			break;
		case  WEST:
		case 	 W:
			currentX--;
			break;	
		}
		
		movementCounter++;
		
		//when player has moved 5 times, an item will drop
		if(movementCounter == 5) {
			moveResponse = "ITEM";
			movementCounter = 0;
			itemCounter++;
		}
//		else if (itemCounter == 5) {
//			moveResponse = "CHECK";
//			
//		}
		else {
			moveResponse =  "NOTHING"; 			
		}

		
		return moveResponse;
	}
	
	public String playerLocation() {
		return currentX + ", " + currentY;
	}
	
	public enum Direction {
		NORTH,
		EAST,
		SOUTH,
		WEST,
		N,
		E,
		S,
		W
	}
}
