package deerhunter.entities.components;

import java.util.ArrayList;

import com.badlogic.ashley.core.Component;

import deerhunter.items.Item;

public class Loot extends Component{
	public ArrayList<Item> items;
	
	public Loot(Item item){
		items = new ArrayList<Item>();
		items.add(item);
	}
	
	public Loot(ArrayList<Item> items){
		this.items = items;
	}
}
