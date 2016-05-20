package deerhunter.entities.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;

public class Velocity extends Component {
    public Vector2 vector;
    public float maxVelocity;
    
    public Velocity(float maxVelocity){
    	vector = new Vector2(0,0);
    	this.maxVelocity = maxVelocity;
    }
    public Velocity(Vector2 vector, float maxVelocity){
    	this.vector = vector;
    	this.maxVelocity = maxVelocity;
    }
}
