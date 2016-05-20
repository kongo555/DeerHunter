package deerhunter.entities.components.attack;

import com.badlogic.ashley.core.Component;

public class MeleeAttack extends Component {
	public boolean attack = false;
	public float damage;
	public float range;
	public float lenght;
	public float range0 = 0.1f;
	public float lenght0 = 0.1f;
	
	public MeleeAttack(float damage, float range, float lenght){
		this.damage = damage;
		this.range = range;
		this.lenght = lenght + (2* lenght0);
	}
}
