
public class MainGame {

	public static void main(String[] args) {
		
		Items itemManager = Items.GetInstance();


		Player player1 = new Player("Lauren");
		
		String[] playerInventory = player1.GetInventory();
		
		
		System.out.println(player1.Name + "'s Inventory:");
		
		for (String item : playerInventory) {
			System.out.println(item);
		}
		
/*		for(int i = 0; i <= 10; i++) {
			System.out.println(itemManager.DropItem());		
		}*/
		
/*		if(itemManager.itemCheck("21, Silver Ring")) {
			System.out.println("Yes");
		}
		else {
			System.out.println("No");
		}*/
	
		//System.out.println("Item 11 = " + itemManager.ItemLookup(11));
	
	}

	
}
