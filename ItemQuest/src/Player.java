
public class Player {

	public String _name;
	private String _inventory[] = new String[3];
	private String _claimedInventory[] = new String[3];
	private int _movementCounter = 0;
	public int _currentX = 0;
	public int _currentY = 0;
	public String moveResponse;
	int itemDrop = 1;							//change to trigger drops at different rate
	public int _playerScore = 10;
	
	Items itemManager = Items.GetInstance();
	
	public Player() {
		StarterInventory();
	}
	
	public Player(String name) {
		
		_name = name;
		StarterInventory();
	}
	
	public String[] GetInventory() {
		
		return _inventory;
	}
	
	public String[] GetClaimedInventory() {
		
		return _claimedInventory;
	}
	
	private void StarterInventory(){
		
		for(int i = 0; i < 3; i++){
			_inventory[i] = "00, EMPTY";
		}
		
	}
	
	public void ItemSwap(int invIndex, String newItem, String oldItem) {
		
		_inventory[invIndex] = newItem;
		
		if (!oldItem.equals("00, EMPTY")) {
			itemManager.itemsDropped.remove(oldItem);
		}
	}
	
	public void ItemSet(int invIndex, String newItem) {
		
		_inventory[invIndex] = newItem;
	}
	
	public void InventoryClaim(int[] inventory) {
		
		for (int i = 0; i < 3; i++) {

			_claimedInventory[i] = itemManager.ItemLookup(inventory[i]);
		}		
	}
	
	public String movement(String direction) {
		
		Direction moveDirection = Direction.valueOf(direction.toUpperCase());
		
		switch(moveDirection) {
			
		case NORTH:
		case 	 N:
			_currentY++;
			break;
		case  EAST:
		case     E:
			_currentX++;
			break;		
		case SOUTH:
		case 	 S:
			_currentY--;
			break;
		case  WEST:
		case 	 W:
			_currentX--;
			break;	
		}
		
		_movementCounter++;
		
		//when player has moved enough times, an item will drop
		if(_movementCounter == itemDrop) {
			moveResponse = "ITEM";
			_movementCounter = 0;
		}
		else {
			moveResponse =  "NOTHING"; 			
		}

		
		return moveResponse;
	}
	
	public String playerLocation() {
		return _currentX + ", " + _currentY;
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

	//Used when user leaves game so items are released from dropped list
	public void clearInventory() {
		
		for(int i = 0; i < 3; i++){
			
			if(!_inventory[i].equals("00, EMPTY")) {
				itemManager.itemsDropped.remove(_inventory[i]);
			}
			
		}
		
	}
}
