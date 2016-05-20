package deerhunter.entities.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.Color;

public class Light extends Component {
	public Color color;
	public Light(Color color){
		this.color = color;
	}
}
