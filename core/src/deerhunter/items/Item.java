package deerhunter.items;


public class Item {
	String name;
	public Item(String name){
		this.name = name;
	}
	
	/*public boolean interactOn(Player player, int x,int y) {
		return false;
	}*/
	
	public String getName(){
		return name;
	}
}
