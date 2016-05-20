package deerhunter.entities.systems;

import com.badlogic.ashley.core.Component;

public class State extends Component {
	public static final int STATE_IDLE = 0;
	public static final int STATE_WALKING = 1;
	public static final int STATE_SHOOTING = 2;
	
	private int state = 0;
	public float time = 0.0f;
	
	public int get() {
		return state;
	}
	
	public void set(int newState) {
		if (newState != state){
			state = newState;
			time = 0.0f;
		}
	}
}
