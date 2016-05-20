package deerhunter.entities.components;

import com.badlogic.ashley.core.Component;

public class LifeTime extends Component {
	public float maxTime;
	public float acctualTime;
	public boolean alive;
	
	public LifeTime(float maxTime){
		this.maxTime = maxTime;
		acctualTime = maxTime;
		alive = true;
	}
}
