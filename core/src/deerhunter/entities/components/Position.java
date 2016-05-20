package deerhunter.entities.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;

public class Position extends Component {
	public Vector2 vector;
    
	public Position(Vector2 position){
		this.vector = position;
	}
}