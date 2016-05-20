package deerhunter.entities.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Visual extends Component {
	public TextureRegion textureRegion;
	public float width, height;
	public int flip = 1;

	public Visual(TextureRegion textureRegion, float width, float height) {
		this.textureRegion = textureRegion;
		this.width = width;
		this.height = height;
	}
	
}	