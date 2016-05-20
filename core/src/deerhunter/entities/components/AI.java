package deerhunter.entities.components;

import com.badlogic.ashley.core.Component;

public class AI extends Component {
	public float maxDistance;
	
	public AI(float maxDistance){
		this.maxDistance = maxDistance;
	}
}
