package deerhunter.entities.components;

import com.badlogic.ashley.core.Component;

import deerhunter.items.Item;

public class ItemComponent extends Component {
	public Item item;
	
	public ItemComponent(Item item){
		this.item = item;
	}

}
