package deerhunter.entities.components;

import com.badlogic.ashley.core.Component;

public class Health extends Component {
	public float maxHP;
	public float currentHP;
	
	public Health(float maxHP){
		this.maxHP = maxHP;
		currentHP = maxHP;
	}
	
	public void injure(float amount) {
		currentHP -= amount;
	}
}
