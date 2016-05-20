package deerhunter.items;



public class Resource {
	private final String name;
	
	public Resource(String name) {
		if (name.length() > 6) throw new RuntimeException("Name cannot be longer than six characters!");
		this.name = name;
	}
	
	/*public boolean interactOn(Player player, int x,int y) {
		return false;
	}*/

	public String getName() {
		return name;
	}
}
