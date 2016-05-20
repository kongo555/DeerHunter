package deerhunter.entities.components.attack;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;

public class ProjectileAttack extends Component {
	public Entity shooter;
	public float attackDamage;
	
	public ProjectileAttack(Entity shooter, float attackDamage){
		this.attackDamage = attackDamage;
		this.shooter = shooter;
	}
}
