package deerhunter.level;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import deerhunter.data.Data;
import deerhunter.entities.components.AI;
import deerhunter.entities.components.AnimationComponent;
import deerhunter.entities.components.Bounds;
import deerhunter.entities.components.Collision;
import deerhunter.entities.components.Direction;
import deerhunter.entities.components.Health;
import deerhunter.entities.components.Inventory;
import deerhunter.entities.components.ItemComponent;
import deerhunter.entities.components.LifeTime;
import deerhunter.entities.components.Light;
import deerhunter.entities.components.Loot;
import deerhunter.entities.components.Position;
import deerhunter.entities.components.Swim;
import deerhunter.entities.components.Velocity;
import deerhunter.entities.components.Visual;
import deerhunter.entities.components.attack.MeleeAttack;
import deerhunter.entities.components.attack.ProjectileAttack;
import deerhunter.entities.components.attack.ProjectileMovment;
import deerhunter.entities.components.attack.RangedAttack;
import deerhunter.entities.systems.State;
import deerhunter.items.Item;
import deerhunter.items.ResourceData;
import deerhunter.items.ResourceItem;
import deerhunter.managers.AssetsManager;
import deerhunter.utilities.Utility;

public class EntityFactor {
	private Engine engine; 
	private Grid grid;
	private AssetsManager assetsManager;
	
	public EntityFactor(Engine engine, Grid grid, AssetsManager assetsManager){
		this.engine = engine;
		this.grid = grid;
		this.assetsManager = assetsManager;
	}
	
	// zobaczyc czy nie szybysze by bylo wszystko po kropce bez entity za kazdym razem
	public Entity addPlayer(String type, Vector2 position, float width, float height, Rectangle rect, float rangedAttackDamage){
		Entity player = addEntity(type, position, width, height);
		player.add(new Velocity(5));
		player.add(new Bounds(rect));
		player.add(new Direction());
		player.add(new Health(10f));
		player.add(new MeleeAttack(5f, 0.5f, 1f));
		player.add(new RangedAttack("arrow", 1, 1, rangedAttackDamage));
		player.add(new Collision());
		player.add(new Inventory());
		player.add(new Swim());
		
		AnimationComponent animation = new AnimationComponent();
		Texture idle = assetsManager.getTexture("playerIdle");
		Animation idleAnimation = new Animation(0.2f, new TextureRegion(idle, 0, 0, 12, 16), new TextureRegion(idle, 12, 0, 12, 16), new TextureRegion(idle, 24, 0, 12, 16), new TextureRegion(idle, 36, 0, 12, 16));
		idleAnimation.setPlayMode(PlayMode.LOOP);
		Texture walk = assetsManager.getTexture("playerWalking");
		Animation walkAnimation = new Animation(0.2f, new TextureRegion(walk, 0, 0, 12, 16), new TextureRegion(walk, 12, 0, 12, 16), new TextureRegion(walk, 24, 0, 12, 16), new TextureRegion(walk, 36, 0, 12, 16), new TextureRegion(walk, 48, 0, 12, 16), new TextureRegion(walk, 60, 0, 12, 16), new TextureRegion(walk, 72, 0, 12, 16), new TextureRegion(walk, 84, 0, 12, 16));
		walkAnimation.setPlayMode(PlayMode.LOOP);
		
		animation.animations.put(State.STATE_IDLE, idleAnimation);
		animation.animations.put(State.STATE_WALKING, walkAnimation);
		player.add(animation);
		player.add(new State());

		return player;
	}
	
	public Entity addMob(String type, Vector2 position, float width, float height, Rectangle rect){
		Entity mob = addEntity(type, position, width, height);
		mob.add(new Velocity(5));
		mob.add(new AI(5));
		mob.add(new Bounds(rect));
		mob.add(new Health(10f));
		mob.add(new Collision());
		mob.add(new Loot(new ResourceItem(ResourceData.rock, 1)));

		return mob;
	}
	
	public Entity addTree(String type, Vector2 position, float width, float height, Rectangle rect){
		Entity tree = addEntity(type, position, width, height);
		tree.add(new Bounds(rect));
		//mob.add(new Health(10f));
		tree.add(new Collision());
		//tree.add(new Velocity(0));
		tree.add(new Loot(new ResourceItem(ResourceData.rock, 1)));

		return tree;
	}
	
	public Entity addLight(String type, Vector2 position, float width, float height, Rectangle rect){
		Entity light = addEntity(type, position, width, height);
		light.add(new Bounds(rect));
		light.add(new Light(new Color(1f, 0.8f, 0.6f, 1f)));

		return light;
	}
	
	public Entity addItem(String type, Vector2 position, float width, float height, Rectangle rect, Item item){
		Entity itemEntity = addEntity(type, position, width, height);
		//item.add(new Velocity(5));
		itemEntity.add(new Bounds(rect));
		itemEntity.add(new LifeTime(10));
		itemEntity.add(new ItemComponent(item));

		return itemEntity;
	}
	
	public Entity addProjectile(String type, Vector2 position, float width, float height, Rectangle rect, Vector2 direction, float tension, Entity shooter, float attackDamage){
		Entity projectile = new Entity();
		engine.addEntity(projectile);
		
		projectile.add(new Position(position));
		projectile.add(new Velocity(new Vector2 (Utility.lengthdir_x(5*1+1,direction.angleRad())/ 50, Utility.lengthdir_y(5*1+1,direction.angleRad())/ 50), 50));
		projectile.add(new ProjectileMovment( 20, 2*tension+0.5f));
		projectile.add(new Visual(assetsManager.getTextureRegion(type), assetsManager.getTextureRegion(type).getRegionWidth() * Data.UNIT_SCALE * 4, assetsManager.getTextureRegion(type).getRegionHeight() * Data.UNIT_SCALE * 4));
		projectile.add(new Bounds(rect));
		projectile.add(new ProjectileAttack(shooter, attackDamage));
		
		grid.insertEntity(projectile, (int)(position.x/grid.cellSize), (int)(position.y/grid.cellSize));
		
		return projectile;
	}
	
	public Entity addEntity(String type, Vector2 position, float width, float height) {
		Entity entity = new Entity();
		engine.addEntity(entity);

		entity.add(new Position(position));
		entity.add(new Visual(assetsManager.getTextureRegion(type), width, height));
		grid.insertEntity(entity, (int)(position.x/grid.cellSize), (int)(position.y/grid.cellSize));
		
		return entity;
	}
	
	public void removeEntity(Entity entity, Vector2 position){
		grid.removeEntity(entity,(int) (position.x/ grid.cellSize),(int) (position.y/ grid.cellSize));
		engine.removeEntity(entity);
	}

}
