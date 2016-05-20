package deerhunter.entities.systems.visual;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import deerhunter.data.Data;
import deerhunter.entities.components.ItemComponent;
import deerhunter.entities.components.Light;
import deerhunter.entities.components.Position;
import deerhunter.entities.components.Visual;
import deerhunter.entities.components.attack.ProjectileMovment;
import deerhunter.level.Grid;
import deerhunter.level.Map;
import deerhunter.level.MapRenderer;
import deerhunter.shaders.NightShader;

public class RenderSystem extends EntitySystem {
	private ComponentMapper<Position> positionMapper = ComponentMapper.getFor(Position.class);
	private ComponentMapper<Visual> visualMapper = ComponentMapper.getFor(Visual.class);
	private ComponentMapper<ProjectileMovment> projectileMovmentMapper = ComponentMapper.getFor(ProjectileMovment.class);

	private OrthographicCamera camera;
	private Rectangle viewBounds;
	private SpriteBatch batch;
	private MapRenderer mapRenderer;
	private Grid grid;
	private ArrayList<Entity> entityToRender;

	private NightShader nightShader;

	private SunLight sunLight;

	private PointLight pointLight;
	private ImmutableArray<Entity> lights;

	private TextureRegion lightMap;

	public RenderSystem(OrthographicCamera camera, SpriteBatch batch, Map map, Grid grid, Rectangle viewBounds) {
		this.camera = camera;
		this.batch = batch;
		this.mapRenderer = new MapRenderer(map, Data.UNIT_SCALE, viewBounds);
		this.grid = grid;
		this.viewBounds = viewBounds;

		// TODO wczytywac inaczej shadery
		String vertexShader = Gdx.files.internal("shaders/standard.vert").readString();
		String fragmentShader = Gdx.files.internal("shaders/night.frag").readString();
		nightShader = new NightShader(vertexShader, fragmentShader);

		fragmentShader = Gdx.files.internal("shaders/sunShadow.frag").readString();
		sunLight = new SunLight(vertexShader, fragmentShader);

		fragmentShader = Gdx.files.internal("shaders/shadowMap.frag").readString();
		String fragmentShader2 = Gdx.files.internal("shaders/shadowRender.frag").readString();
		pointLight = new PointLight(vertexShader, fragmentShader, fragmentShader2, grid);

	}

	@Override
	public void addedToEngine(Engine engine) {
		lights = engine.getEntitiesFor(Family.getFor(Light.class));
	}

	@Override
	public void removedFromEngine(Engine engine) {
	}

	@Override
	public void update(float deltaTime) {
		Vector2 position;
		Visual visual;
		ProjectileMovment projectileMovment;

		batch.setProjectionMatrix(camera.combined);
		entityToRender = grid.getEntitiesInRect(viewBounds);
		Collections.sort(entityToRender, spriteSorter);

		// make sunShadowMap
		sunLight.update(deltaTime, batch, entityToRender);
		// make light/shadowMap
		if (sunLight.isNight())
			lightMap = pointLight.update(batch, lights, camera.position);

		batch.setProjectionMatrix(camera.combined);

		// set day/night shader
		nightShader.setUniformf(sunLight.getTimeToNight());
		batch.setShader(nightShader.getShader());

		// render map
		batch.begin();
		mapRenderer.render(batch);
		batch.end();

		// set day/night shader
		batch.setShader(nightShader.getShader());
		batch.begin();
		// draw sunShadowMap
		if (!sunLight.isNight())
			batch.draw(sunLight.getShadowMap(), viewBounds.x, viewBounds.y, viewBounds.width, viewBounds.height);
		for (Entity entity : entityToRender) {
			position = positionMapper.get(entity).vector;
			visual = visualMapper.get(entity);

			if (entity.getComponent(ProjectileMovment.class) != null) {
				projectileMovment = projectileMovmentMapper.get(entity);
				batch.draw(visual.textureRegion, position.x, position.y + projectileMovment.positionZ * 0.5f * Data.UNIT_SCALE,
						visual.width / 2, visual.height / 2, visual.width, visual.height, 1, 1, projectileMovment.rotation);
			} else {
				batch.draw(visual.textureRegion, position.x, position.y, visual.width / 2, visual.height / 2, visual.width, visual.height,
						visual.flip, 1, 0);
			}
		}
		batch.end();

		// render light/shadowMap
		if (sunLight.isNight()) {
			batch.setShader(null);
			batch.begin();
			batch.draw(lightMap, viewBounds.x, viewBounds.y, viewBounds.width, viewBounds.height);
			batch.end();
		}
	}

	private Comparator<Entity> spriteSorter = new Comparator<Entity>() {
		public int compare(Entity e0, Entity e1) {
			Vector2 position0 = positionMapper.get(e0).vector;
			Vector2 position1 = positionMapper.get(e1).vector;

			if (e1.getComponent(ItemComponent.class) != null)
				return +1;
			if (position1.y > position0.y)
				return +1;
			if (position1.y < position0.y)
				return -1;
			return 0;
		}
	};
}