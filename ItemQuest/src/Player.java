
public class Player {

	public String Name;
	private String mInventory[] = new String[3];
	Items mItemManager = Items.GetInstance();
		
	
	public Player(String name) {
		
		Name = name;
		StarterInventory();
	}
	
	public String[] GetInventory() {
		
		return mInventory;
	}
	
	private void StarterInventory(){
		for(int i = 0; i < 3; i++){
			mInventory[i] = mItemManager.DropItem();
		}
	}
}
