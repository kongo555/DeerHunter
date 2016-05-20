package deerhunter.level;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import deerhunter.data.Data;
import deerhunter.data.MapData;
import deerhunter.entities.Player;
import deerhunter.entities.systems.AISystem;
import deerhunter.entities.systems.AnimationSystem;
import deerhunter.entities.systems.CollisionSystem;
import deerhunter.entities.systems.DirectionSystem;
import deerhunter.entities.systems.HealthSystem;
import deerhunter.entities.systems.MovementSystem;
import deerhunter.entities.systems.SwimSystem;
import deerhunter.entities.systems.attack.MeleeAttackSystem;
import deerhunter.entities.systems.attack.ProjectileAttackSystem;
import deerhunter.entities.systems.attack.ProjectileMovementSystem;
import deerhunter.entities.systems.attack.RangedAttackSystem;
import deerhunter.entities.systems.visual.RenderSystem;
import deerhunter.managers.AssetsManager;
import deerhunter.utilities.AStar;

public class Level {
	private OrthographicCamera camera;
	private Vector3 mosuePosition;
	private final AssetsManager assetsManager;
	private CameraContoller cameraContoller;
	private SpriteBatch batch;
	private final Engine engine;
	private EntityFactor entityFactor;
	
	private Map map;
	private Grid grid;
	private AStar astar;

	private Player player;
	
	public Level(AssetsManager assetsManager, MapData mapData) {
		mosuePosition = new Vector3();
		this.assetsManager = assetsManager;
		camera = new OrthographicCamera();
		camera.setToOrtho(false, Gdx.graphics.getWidth() * Data.UNIT_SCALE, Gdx.graphics.getHeight() * Data.UNIT_SCALE);
		camera.update();
		cameraContoller = new CameraContoller(camera);
		batch = new SpriteBatch();
		
		map = new Map(mapData, assetsManager);
		grid = new Grid(map.getWidth(), map.getHeight());
		astar = new AStar(map.getWidth(), map.getWidth(), grid);

		engine = new Engine();
		entityFactor = new EntityFactor(engine, grid, assetsManager);
		
		engine.addSystem(new RenderSystem(camera, batch, map, grid, cameraContoller.getViewBounds()));
		//engine.addSystem(new DebugRenderSystem(camera, grid, cameraContoller.getViewBounds()));
		
		player = new Player(entityFactor.addPlayer("player", new Vector2(20,20), 1, 1, new Rectangle(20,20,1,1), 5), this, entityFactor, grid);
		
		engine.addSystem(new AISystem(player.getPosition(), new AStar(map.getWidth(),map.getHeight(),grid)));
		engine.addSystem(new CollisionSystem(map, grid));
		engine.addSystem(new MovementSystem(grid));
		engine.addSystem(new DirectionSystem());
		engine.addSystem(new MeleeAttackSystem(grid));
		engine.addSystem(new ProjectileAttackSystem(entityFactor, grid));
		engine.addSystem(new RangedAttackSystem(entityFactor));
		engine.addSystem(new HealthSystem(entityFactor));
		engine.addSystem(new ProjectileMovementSystem());
		engine.addSystem(new SwimSystem(map));
		engine.addSystem(new AnimationSystem());
		
		camera.position.set(player.getPosition().x,player.getPosition().y,0);
		cameraContoller.update(player.getPosition());
		
		entityFactor.addMob("player", new Vector2(26,24), 1, 1, new Rectangle(26,24,1,1));
		entityFactor.addTree("tree", new Vector2(23,25), 1, 1, new Rectangle(23,25,1,1));
		entityFactor.addLight("light", new Vector2(21,22), 1, 1, new Rectangle(21,22,1,1));
		entityFactor.addLight("light", new Vector2(24,18), 1, 1, new Rectangle(24,18,1,1));
	}

	public void update(float delta) {
		mosuePosition.set(Gdx.input.getX(), Gdx.input.getY(), 0);
		camera.unproject(mosuePosition);
		
		player.processInput(delta);
		engine.update(delta);
		
		cameraContoller.update(player.getPosition());
	}
	
	public Player getPlayer(){
		return player;
	}
	
	public Vector3 getMousePosition() {
		return mosuePosition;
	}
	
	public AssetsManager getAssetsManager(){
		return assetsManager;
	}
	
	
	public void dispose() {
	}
}
