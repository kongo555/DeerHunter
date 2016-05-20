package deerhunter.items;


public class ResourceItem extends Item {
	private Resource resource;
	private int quantity;

	public ResourceItem(Resource resource, int quantity){
		super(resource.getName());
		this.resource = resource;
		this.quantity = quantity;
	}
	
	/*public boolean interactOn(Player player, int x,int y) {
		if (resource.interactOn(player, x, y)) {
			quantity--;
			return true;
		}
		return false;
	}*/
	
	public int getQuantity(){
		return quantity;
	}
	
	public void setQuantity(int value){
		quantity += value;
	}
	
	public boolean toRemove() {
		return quantity <= 0;
	}
	
	public Resource getResource(){
		return resource;
	}
}
