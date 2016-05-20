package deerhunter.entities.components;

import com.badlogic.ashley.core.Component;

public class Direction extends Component{
	public static enum Dir {
		north, west, east, south
	}
	
	public Dir direction = Dir.south;
}
