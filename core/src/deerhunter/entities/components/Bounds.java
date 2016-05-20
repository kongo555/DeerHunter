package deerhunter.entities.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Rectangle;

public class Bounds extends Component {
	public Rectangle rect;

	public Bounds(Rectangle rect) {
		this.rect = rect;
	}
}