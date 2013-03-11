package com.adamantine.discowormhole;

import com.adamantine.discowormhole.particlepipeline.ParticleRingSection;
import com.adamantine.discowormhole.particlepipeline.RingSectionParticleEffect;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;

public class RingSectionTest implements ApplicationListener {
	private DiscoWormholeCamera camera;
	private static final float RADIUS = 11.0f; // 7.3f;
	private Vector2 radiusTranslation;
	private Matrix4 ringSectionRotation;

	// private DiscoPipeline pipe;
	public ParticleRingSection section;

	private boolean isSetup = false;

	@Override
	public void create() {
		camera = new DiscoWormholeCamera(67, Gdx.graphics.getWidth(),
				Gdx.graphics.getHeight());
		radiusTranslation = new Vector2();
		ringSectionRotation = new Matrix4();
		// pipe = new DiscoPipeline(new DiscoTile());
		section = new RingSectionParticleEffect(camera, "particle/glitter5.p",
				"particle");
		// section = new DiscoTile();
		 section.getModelMatrix().translate(0.0f, 0.0f, -30.0f);
	}

	private void setup() {
		// Enable backface culling
		// Gdx.graphics.getGL20().glEnable(GL20.GL_CULL_FACE);
		// Gdx.graphics.getGL20().glCullFace(GL20.GL_BACK);

		Gdx.graphics.getGL20().glViewport(0, 0, Gdx.graphics.getWidth(),
				Gdx.graphics.getHeight());
		Gdx.graphics.getGL20().glEnable(GL20.GL_DEPTH_TEST);
		Gdx.graphics.getGL20().glDepthFunc(GL20.GL_LESS);
		Gdx.graphics.getGL20().glDepthMask(true);
		Gdx.graphics.getGL20().glClearDepthf(1.0f);
		Gdx.graphics.getGL20().glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
	}

	@Override
	public void resize(int width, int height) {
		camera.resize(width, height);
	}

	private void transformRingSection() {
		// reset model
		section.getModelMatrix().idt();

		section.getModelMatrix().translate(0.0f, 0.0f, -5.0f)
				.scale(0.01f, 0.01f, 0.01f);

		// place the ringSection on the ring perimeter
		radiusTranslation = com.adamantine.util.Math.getRotation(90, RADIUS);
		System.out.println(radiusTranslation.x + ", " + radiusTranslation.y);
		// section.getModelMatrix().translate(radiusTranslation.x,
		// radiusTranslation.y, 0.0f);

		ringSectionRotation.idt().setToRotation(0.0f, 0.0f, 1.0f, 0.0f + 90.0f);
		// section.getModelMatrix().mul(ringSectionRotation);
	}

	@Override
	public void render() {
		if (!isSetup) {
			if (Gdx.gl.glGetString(GL20.GL_VERSION) == null) {
				// if (Gdx.app.getType().equals(ApplicationType.Android)) {
				// Log.e("render: ", "GL Context not fully initialized yet!");
				// } else {
				Gdx.app.log("render: ", "GL Context not fully initialized yet!");
				// }
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
				}
				return;
			}

			setup();
			isSetup = true;
		}

		Gdx.graphics.getGL20().glClear(
				GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT
						| GL20.GL_STENCIL_BUFFER_BIT);

		transformRingSection();
		// section.getModelMatrix().rotate(0.0f, 0.0f, 0.0f, 90.0f);
		section.render(camera);
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void dispose() {
		section.dispose();
	}
}
