package deerhunter.entities.systems.visual;

import java.util.ArrayList;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import deerhunter.data.Data;
import deerhunter.entities.components.Light;
import deerhunter.entities.components.Position;
import deerhunter.entities.components.Visual;
import deerhunter.entities.components.attack.ProjectileMovment;
import deerhunter.level.Grid;
import deerhunter.shaders.ShadowMapShader;
import deerhunter.shaders.ShadowRenderShader;

public class PointLight {
	private ComponentMapper<Position> positionMapper = ComponentMapper.getFor(Position.class);
	private ComponentMapper<Visual> visualMapper = ComponentMapper.getFor(Visual.class);
	private ComponentMapper<ProjectileMovment> projectileMovmentMapper = ComponentMapper.getFor(ProjectileMovment.class);
	private ComponentMapper<Light> lightMapper = ComponentMapper.getFor(Light.class);
	
	private Grid grid;
	
	private Rectangle lightViewBounds;
	float viewWidth;
	float viewHeight;
	
	private OrthographicCamera camera;
	private FrameBuffer occludersFBO;
	private TextureRegion occluders;
	
	private ShadowMapShader shadowMapShader;
	private FrameBuffer shadowMapFBO;
	private TextureRegion shadowMap;
	private ShadowRenderShader shadowRenderShader;
	
	private FrameBuffer resultFBO;
	private TextureRegion result;
	
	ArrayList<Entity> lights;
	private int lightSize = 256;
	private float upScale = 1.5f;
	
	
	public PointLight(String vertexShader, String shadowMapfragmentShader, String shadowRenderfragmentShader, Grid grid) {
		this.grid = grid;
		lightViewBounds = new Rectangle();
		camera = new OrthographicCamera(Gdx.graphics.getWidth() * Data.UNIT_SCALE, Gdx.graphics.getHeight() * Data.UNIT_SCALE);
		camera.setToOrtho(false);
		
		shadowMapShader = new ShadowMapShader(vertexShader, shadowMapfragmentShader);

		occludersFBO = new FrameBuffer(Format.RGBA8888, lightSize, lightSize, false);
		occluders = new TextureRegion(occludersFBO.getColorBufferTexture());
		occluders.flip(false, true);
		
		// our 1D shadow map, lightSize x 1 pixels, no depth
		shadowMapFBO = new FrameBuffer(Format.RGBA8888, lightSize, 1, false); // TODO moze zmienic size
		Texture shadowMapTex = shadowMapFBO.getColorBufferTexture();
		// use linear filtering and repeat wrap mode when sampling
		shadowMapTex.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		shadowMapTex.setWrap(TextureWrap.Repeat, TextureWrap.Repeat);
		// for debugging only; in order to render the 1D shadow map FBO to
		// screen
		shadowMap = new TextureRegion(shadowMapTex);
		shadowMap.flip(false, true);
		
		shadowRenderShader = new ShadowRenderShader(vertexShader, shadowRenderfragmentShader);
		
		lights = new ArrayList<Entity>();
		
		resultFBO = new FrameBuffer(Format.RGBA8888, 1024, 1024, false);
		result = new TextureRegion(resultFBO.getColorBufferTexture());
		result.flip(false, true);
	}
	
	public TextureRegion update(SpriteBatch batch, ImmutableArray<Entity> lights, final Vector3 startPosition){
		Vector2 position;
		Visual visual;
		ProjectileMovment projectileMovment;
		
		resultFBO.begin();
		Gdx.gl.glClearColor(0f, 0f, 0f, 0f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		resultFBO.end();
		for (int i = 0; i < lights.size(); ++i) {
			Entity light = lights.get(i);
            position = positionMapper.get(light).vector;
            
            // STEP 1. render light region to occluder FBO
            // bind the occluder FBO
            occludersFBO.begin();
            // clear the FBO
            Gdx.gl.glClearColor(0f, 0f, 0f, 0f);
			Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
			// set the orthographic camera to the size of our FBO
			camera.setToOrtho(false, occludersFBO.getWidth() * Data.UNIT_SCALE, occludersFBO.getHeight() * Data.UNIT_SCALE); // TODO moze zmienic size
			// translate camera so that light is in the center
			camera.position.x = position.x;
			camera.position.y = position.y;
			// update camera matrices
			camera.update();
			// set up our batch for the occluder pass
			batch.setProjectionMatrix(camera.combined);
			// use default shader
			batch.setShader(null);
			// ... draw any sprites that will cast shadows here ... //
			batch.begin();
			viewWidth = camera.viewportWidth * camera.zoom;
			viewHeight = camera.viewportHeight * camera.zoom;
			lightViewBounds.set(camera.position.x - viewWidth / 2, camera.position.y - viewHeight / 2, viewWidth, viewHeight);
			for(Entity entity : grid.getEntitiesInRect(lightViewBounds)){
				if(entity.getComponent(Light.class) != null)
					continue;
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
			// unbind the FBO
			occludersFBO.end();
			
			// STEP 2. build a 1D shadow map from occlude FBO
			// bind shadow map
			shadowMapFBO.begin();
			// clear it
			Gdx.gl.glClearColor(0f, 0f, 0f, 0f);
			Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
			// set our shadow map shader
			batch.setShader(shadowMapShader.getShader());
			shadowMapShader.setUniformf(lightSize, upScale);
			batch.begin();
			// reset our projection matrix to the FBO size
			camera.setToOrtho(false, shadowMapFBO.getWidth(), shadowMapFBO.getHeight());
			batch.setProjectionMatrix(camera.combined);
			// draw the occluders texture to our 1D shadow map FBO
			batch.draw(occluders.getTexture(), 0, 0, lightSize, shadowMapFBO.getHeight());
			batch.end();
			// unbind shadow map FBO
			shadowMapFBO.end();
			
			// STEP 3. render the blurred shadows
			resultFBO.begin();
			// reset projection matrix to screen
			//camera.setToOrtho(false);
			camera.setToOrtho(false, Gdx.graphics.getWidth() * Data.UNIT_SCALE, Gdx.graphics.getHeight() * Data.UNIT_SCALE);
			camera.position.x = startPosition.x;
			camera.position.y = startPosition.y;
			camera.update();
			batch.setProjectionMatrix(camera.combined);
			// set the shader which actually draws the light/shadow
			batch.setShader(shadowRenderShader.getShader());
			shadowRenderShader.setUniformf(lightSize, true);
			batch.begin();
			// set color to light
			batch.setColor(lightMapper.get(light).color);
			float finalSize = (lightSize * upScale)* Data.UNIT_SCALE;
			// draw centered on light position
			position = positionMapper.get(light).vector;
			//FIXME zmienic na srodek swiatla a nie pozycje!
			batch.draw(shadowMap.getTexture(), position.x - finalSize / 2f, position.y - finalSize / 2f, finalSize, finalSize);
			// flush the batch before swapping shaders
			batch.end();
			// reset color
			batch.setColor(Color.WHITE);
			resultFBO.end();
		}
		
		return result;
	}
}
