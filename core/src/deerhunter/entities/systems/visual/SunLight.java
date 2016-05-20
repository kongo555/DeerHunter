package deerhunter.entities.systems.visual;

import java.util.ArrayList;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.math.Vector2;

import deerhunter.entities.components.Position;
import deerhunter.entities.components.Visual;
import deerhunter.entities.components.attack.ProjectileMovment;
import deerhunter.shaders.SunShadowShader;

public class SunLight {
	private ComponentMapper<Position> positionMapper = ComponentMapper.getFor(Position.class);
	private ComponentMapper<Visual> visualMapper = ComponentMapper.getFor(Visual.class);
	private ComponentMapper<ProjectileMovment> projectileMovmentMapper = ComponentMapper.getFor(ProjectileMovment.class);

	private SunShadowShader shadowShader;
	private FrameBuffer shadowFBO;
	private TextureRegion shadowMap;

	private float lightIntensity = 0.5f;
	private float angleXY = 0;
	private float angleZ = 0.5f;
	
	private float timeToNight = 0;
	private boolean night = false;

	public SunLight(String vertexShader, String fragmentShader) {
		shadowShader = new SunShadowShader(vertexShader, fragmentShader);

		int FBOSIZE = 1024;
		shadowFBO = new FrameBuffer(Format.RGBA8888, FBOSIZE, FBOSIZE, true);
		Texture shadowMapTex = shadowFBO.getColorBufferTexture();
		// use linear filtering and repeat wrap mode when sampling
		shadowMapTex.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		shadowMapTex.setWrap(TextureWrap.Repeat, TextureWrap.Repeat);
		// for debugging only; in order to render the 1D shadow map FBO to
		// screen
		shadowMap = new TextureRegion(shadowMapTex);
		shadowMap.flip(false, true);
	}

	public void update(float deltaTime, SpriteBatch batch, ArrayList<Entity> entityToRender) {
		angleXY += deltaTime * 10;
		if (angleXY > 360)
			angleXY = 0;
		//TODO wlaczyc dzien/noc
		if (angleXY > 160 && angleXY < 340) {

			timeToNight -= deltaTime;
			if (timeToNight < 0) {
				timeToNight = 0;
			}
			night = false;
		} else {
			timeToNight += deltaTime;
			if (timeToNight > 0.6f) {
				timeToNight = 0.6f;
				night = true;
			}
		}

		Vector2 position;
		Visual visual;
		ProjectileMovment projectileMovment;

		shadowFBO.begin();
		Gdx.gl.glClearColor(0f, 0f, 0f, 0f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		shadowShader.setUniformf(lightIntensity);
		batch.setShader(shadowShader.getShader());
		batch.begin();
		for (Entity entity : entityToRender) {
			position = positionMapper.get(entity).vector;
			visual = visualMapper.get(entity);

			if (entity.getComponent(ProjectileMovment.class) != null) {
				projectileMovment = projectileMovmentMapper.get(entity);
				if (!projectileMovment.landed)
					batch.draw(visual.textureRegion, position.x, position.y, visual.width / 2, visual.height / 2, visual.width,
							visual.height, 1, 1 + angleZ, projectileMovment.direction);
			} else {
				batch.draw(visual.textureRegion, position.x, position.y + 0.1f, visual.width / 2, 0, visual.width, visual.height,
						visual.flip * -1, 1 + angleZ, angleXY - 90);
			}
		}
		batch.end();
		shadowFBO.end();
	}

	public TextureRegion getShadowMap() {
		return shadowMap;
	}
	
	public float getTimeToNight(){
		return timeToNight;
	}
	
	public boolean isNight(){
		return night;
	}
}
