package deerhunter.entities.components.attack;

import com.badlogic.ashley.core.Component;

public class ProjectileMovment extends Component {
	public float positionZ;
    public float velocityZ;
    public boolean landed;
    public float direction;
    public float rotation;
    
    public ProjectileMovment(float positionZ, float zspd){
    	this.positionZ = positionZ;
    	velocityZ = zspd;
        landed = false;
    }
}
