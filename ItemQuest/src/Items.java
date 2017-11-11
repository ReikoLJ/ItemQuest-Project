import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Items {

	private static Items instance = null;	
	
    private List<String> itemList = new ArrayList<String>();
    private List<String> itemsDropped = new ArrayList<String>();
    
    private Items() {
			LoadItems();
    }
    
    //Items class is singleton(only one instance will exist). This gets the current instance
    public static Items GetInstance() {
    	
    	if(instance == null) {
    		instance = new Items();
    	}
    	return instance;
    }
    
    //Loads items from text file into memory
	public void LoadItems(){
	     
		try {
			  BufferedReader reader = new BufferedReader(new FileReader("ItemList.txt"));
		      String line = reader.readLine();
		      while (line != null) {
		    	  itemList.add(line);
		          line = reader.readLine();
		      }
		      reader.close();
		}
		catch(Exception e) {
			System.out.println(e);
		}
	}
	
	//Returns random item from list
	public String DropItem() {
		
		Random rand = new Random();
	
		String randomItem = itemList.get(rand.nextInt(50));
		itemsDropped.add(randomItem);
		
		return randomItem;
	}
	
	//Check if item has been dropped
	public boolean ItemCheck(String item) {
		
		if(itemsDropped.contains(item)) {
			return true;
		}
		else {
			return false;
		}
	}
	
	//Get full item desc from number
	public String ItemLookup(int itemNumber) {
		
		String fullItem = itemList.get(itemNumber-1);
		
		return fullItem;
	}
	
	//Clear dropped items list (server crash)
	public void ClearItems() {
		itemsDropped.clear();
	}
	
}
