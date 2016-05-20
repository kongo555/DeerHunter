package deerhunter.entities.components;

import java.util.ArrayList;

import com.badlogic.ashley.core.Component;

import deerhunter.items.Item;
import deerhunter.items.Resource;
import deerhunter.items.ResourceItem;

public class Inventory extends Component{
	public ArrayList<Item> items;
	
	public Inventory(){
		items = new ArrayList<Item>();	
	}
	
	public void add(Item item) {
		add(items.size(), item);
	}
	
	public void add(int slot, Item item) {
		if (item instanceof  ResourceItem) {
			ResourceItem itemToTake = (ResourceItem) item;
			ResourceItem itemInInventory = findResource(itemToTake.getResource());
			if (itemInInventory == null) {
				items.add(slot, itemToTake);
			} else {
				itemInInventory.setQuantity(itemToTake.getQuantity());
			}
		} else {
			items.add(slot, item);
		}
		
		System.out.println(items.size());
	}
	
	private ResourceItem findResource(Resource resource) {
		for (int i = 0; i < items.size(); i++) {
			if (items.get(i) instanceof ResourceItem) {
				ResourceItem itemInInventory = (ResourceItem) items.get(i);
				if (itemInInventory.getResource() == resource) return itemInInventory;
			}
		}
		return null;
	}

}
