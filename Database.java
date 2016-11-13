import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author Bryan Charlie
 * 10/14/2016
 * A port of a javascript webstorage API interface 
 * can be seen at https://github.com/g0rush/jsDBWrapper/blob/master/webstorage.js
 * 
 * THIS CLASS IS A SINGLETON!!!
 */

public class IndexedDB {
	private boolean initialized = false;
	private HashMap<String, ArrayList<Item>> localStorage;
	private ArrayList<String> initializedObjectStores;
	private static int idCounter = 0;
	private static IndexedDB instance = null; //Singular instance of this database class.

	/*
	 * singular private constructor to ensure a single existing db instance.
	 * can only by invoked via getDatabaseInstance();
	 */
	private IndexedDB(){
		localStorage = new HashMap<String, ArrayList<Item>>();
		initializedObjectStores = new ArrayList<String>();
		initialized = true;
	}

	public static IndexedDB getDatabaseInstance(){
		if(instance == null){
			instance = new IndexedDB();
		}

		return instance;
	}
	//Inspect item type. Log its status in the initializedObjectStores object.
	public boolean initOjectStores(String type) {
		if (!initialized) {
			return false;
		}
		else if(localStorage.get(type) != null)
			return true;
		else if (localStorage.get(type) == null) {
			ArrayList<Item> itemList = new ArrayList<Item>();
			localStorage.put(type, itemList);
		}
		initializedObjectStores.add(type);
		return true;
	}
	
	public boolean save(String type, Item obj) {  //attempt to store the obj in local storage
		if (!initialized) {
			return false;
		} else if (!initializedObjectStores.contains(type)) {
			return false;
		}	
		if (!obj.hasId()) { 
			obj.setId(idCounter++); 
		} 
		ArrayList<Item> storageItem = localStorage.get(type); 
		storageItem.add(obj);
		localStorage.put(type, storageItem);

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


	public boolean delete(String type, int id) { 
		if (!initialized) 
			return false;
		else if (!initializedObjectStores.contains(type)) 
			return true;

		ArrayList<Item> storageItem = localStorage.get(type); 
		for (int i = 0; i < storageItem.size(); i++) { //iterate through the associated list to find the object via id.
			if(storageItem.get(i).getId() == id){
				storageItem.remove(i); //remove item if found
				localStorage.put(type, storageItem); //re-associate type with the modified list
				return true;
			}
		}
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
