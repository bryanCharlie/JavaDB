import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.Semaphore;

public class TxDatabase {
	private boolean initialized = false;
	private HashMap<String, ArrayList<Item>> localStorage;
	private ArrayList<String> initializedObjectStores;
	private static int idCounter = 0;
	private static TxDatabase instance = null; //Singular instance of this database class.
	private final static Semaphore mutex = new Semaphore(1);

	/*
	 * singular private constructor to ensure a single existing db instance.
	 * can only by invoked via getDatabaseInstance();
	 */
	private TxDatabase(){
		localStorage = new HashMap<String, ArrayList<Item>>();
		initializedObjectStores = new ArrayList<String>();
		initialized = true;
	}

	public static TxDatabase getDatabaseInstance() throws InterruptedException{
		mutex.acquire();
		if(instance == null){
			instance = new TxDatabase();
		}
		mutex.release();

		return instance;
	}
	//Inspect item type. Log its status in the initializedObjectStores object.
	public boolean initOjectStores(String type) throws InterruptedException {
		mutex.acquire();
		if (!initialized) {
			mutex.release();
			return false;
		}
		else if(localStorage.get(type) != null){
			mutex.release();
			return true;
		}
		else if (localStorage.get(type) == null) {
			ArrayList<Item> itemList = new ArrayList<Item>();
			localStorage.put(type, itemList);
		}
		initializedObjectStores.add(type);
		mutex.release();
		
		return true;
	}
	
	public boolean save(String type, Item obj) throws InterruptedException {  //attempt to store the obj in local storage
		mutex.acquire();
		if (!initialized) {
			mutex.release();
			return false;
		} else if (!initializedObjectStores.contains(type)) {
			mutex.release();
			return false;
		}	
		if (!obj.hasId()) { 
			obj.setId(idCounter++); 
		} 
		ArrayList<Item> storageItem = localStorage.get(type); 
		storageItem.add(obj);
		localStorage.put(type, storageItem);
		mutex.release();

		return true;
	}
	
	public ArrayList<Item> findAll(String type) { //find all of a related type
		if (!initialized) {
			return null;
		} else if (!initializedObjectStores.contains(type)) {
			return null;
		}
		ArrayList<Item> storageItem = localStorage.get(type);
		return storageItem; //return a list of items.
	}


	public boolean delete(String type, int id) throws InterruptedException { 
		mutex.acquire();
		if (!initialized){ 
			mutex.release();
			return false;
		}
		else if (!initializedObjectStores.contains(type)){
			mutex.release();
			return true;
		}

		ArrayList<Item> storageItem = localStorage.get(type); 
		for (int i = 0; i < storageItem.size(); i++) { //iterate through the associated list to find the object via id.
			if(storageItem.get(i).getId() == id){
				storageItem.remove(i); //remove item if found
				localStorage.put(type, storageItem); //re-associate type with the modified list
				mutex.release();
				return true;
			}
		}
		mutex.release();
		return false; //if item not found, return false.
	}
	public Item findById(String type, int id)	{
		Item item = null;
		if (!initialized) {
			return null;
		} else if (!initializedObjectStores.contains(type)) {
			return null;
		}	
		ArrayList<Item> storageItem = localStorage.get(type); //get item of associated type from storage
		for(Item i : storageItem){ //if item found, return the item
			if(i.getId() == id){
				item = i;
				return item;
			}
		}
		return null; //return null if no item found
	}
	
	public ArrayList<String> getInitializedObjectStores(){
		return initializedObjectStores;
	}
}
