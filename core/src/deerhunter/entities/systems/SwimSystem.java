package deerhunter.entities.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Rectangle;

import deerhunter.entities.components.Bounds;
import deerhunter.entities.components.Swim;
import deerhunter.level.Map;

public class SwimSystem extends IteratingSystem {
	private ComponentMapper<Swim> swimMapper = ComponentMapper.getFor(Swim.class);
	private ComponentMapper<Bounds> boundsMapper = ComponentMapper.getFor(Bounds.class);
	
	private Map map;

	public SwimSystem(Map map) {
		super(Family.getFor(Swim.class, Bounds.class));
		this.map = map;
	}

	@Override
	public void processEntity(Entity entity, float delta) {
		Swim swim = swimMapper.get(entity);
		Rectangle rect = boundsMapper.get(entity).rect;
		
		for(int i =(int)(rect.x +1 ); i <=(int)(rect.x + rect.width);i++){
			if( map.getTile(i, (int) rect.y).isLiquid()){
				swim.swimming = true;
				System.out.println("plyne na albatrosie");
			}
			//if(tile instanceof WaterTile)
			//	return true;
		}
		swim.swimming = false;
	}
}