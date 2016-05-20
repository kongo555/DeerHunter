package deerhunter.entities.components.attack;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;

public class RangedAttack extends Component {
	public String name;
	public float width, height;
	public boolean shoot = false;
	public Vector2 targetPosition;
	public float tension;
	public float attackDamage;
	
	public RangedAttack(String name, float width, float height, float attackDamage){
		this.name = name;
		this.width = width;
		this.height = height;
		targetPosition = new Vector2();
		this.attackDamage = attackDamage;
	}
	
	public void set(Vector2 targetPosition){
		shoot = true;
		this.targetPosition = targetPosition;
		if (tension > 2)
			tension = 2;
	}
	
	public void reset(){
		shoot = false;
		tension = 0;
	}
}
