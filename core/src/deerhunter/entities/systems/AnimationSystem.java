package deerhunter.entities.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.g2d.Animation;

import deerhunter.entities.components.AnimationComponent;
import deerhunter.entities.components.Direction;
import deerhunter.entities.components.Direction.Dir;
import deerhunter.entities.components.Velocity;
import deerhunter.entities.components.Visual;

public class AnimationSystem extends IteratingSystem {
	private ComponentMapper<Visual> visualMapper;
	private ComponentMapper<AnimationComponent> animationMapper;
	private ComponentMapper<State> stateMapper;

	public AnimationSystem() {
		super(Family.getFor(AnimationComponent.class, Visual.class, State.class));

		visualMapper = ComponentMapper.getFor(Visual.class);
		animationMapper = ComponentMapper.getFor(AnimationComponent.class);
		stateMapper = ComponentMapper.getFor(State.class);
	}

	@Override
	public void processEntity(Entity entity, float deltaTime) {
		Visual visual = visualMapper.get(entity);
		AnimationComponent anim = animationMapper.get(entity);
		State state = stateMapper.get(entity);

		if(entity.getComponent(Velocity.class) != null){
			if(entity.getComponent(Velocity.class).vector.x != 0 || entity.getComponent(Velocity.class).vector.y != 0){
				state.set(State.STATE_WALKING);
				if(entity.getComponent(Direction.class).direction == Dir.east)
					visual.flip = 1;
				else if(entity.getComponent(Direction.class).direction == Dir.west)
					visual.flip = -1;
			}
			else
				state.set(State.STATE_IDLE);
		}
			
		
		Animation animation = anim.animations.get(state.get());

		if (animation != null) {
			visual.textureRegion = animation.getKeyFrame(state.time);
		}

		state.time += deltaTime;
		
	}
}
