/* Author: Christopher Grabowski */

package com.adamantine.discowormhole;

import com.adamantine.discowormhole.discopipeline.DiscoPipeline;
import com.adamantine.discowormhole.particlepipeline.ParticlePipeline;
import com.adamantine.discowormhole.particlepipeline.ParticleRingSection;
import com.adamantine.discowormhole.particlepipeline.RingSectionParticleEffect;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;

public class DiscoWormhole implements ApplicationListener {

	DiscoWormholeCamera camera;

	private ParticlePipeline pipeline;
	private RingSectionParticleEffect particleEffect;
	//private float[] backgroundColor;
	private boolean isSetup = false;

	@Override
	public void create() {
		camera = new DiscoWormholeCamera(67, Gdx.graphics.getWidth(),
				Gdx.graphics.getHeight());
		camera.update();
		particleEffect = new RingSectionParticleEffect(camera,
				"particle/glitter9.p", "particle");
		pipeline = new ParticlePipeline(particleEffect);
		// discoPipeline = new DiscoPipeline(new DiscoTile());
		//backgroundColor = new float[] { 0.0f, 0.0f, 0.0f, 1.0f };
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
		Gdx.graphics.getGL20().glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
	}

	@Override
	public void resize(int width, int height) {
		// Gdx.graphics.getGL20().glViewport(0, 0, Gdx.graphics.getWidth(),
		// Gdx.graphics.getHeight());
		camera.resize(width, height);
	}

	@Override
	public void render() {

		synchronized (PreferencePasser.getLock()) {
			if (PreferencePasser.prefsChanged == true) {
				configureFromPreferences();
				PreferencePasser.prefsChanged = false;
			}
		}

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
				GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		
		pipeline.render(camera);
	}

	// must be called from a synchronized block
	private void configureFromPreferences() {
		// System.out.println("flight speed: " + DiscoConfig.flightSpeed);
		// System.out.println("num rings: " + DiscoConfig.numRings);
		// System.out.println("use colors: " + DiscoConfig.useColors);
		// System.out.println("use spacedust: " + DiscoConfig.useSpaceDust);

		pipeline.speed = (int) (DiscoPipeline.MAX_SPEED * (Math
				.sqrt((float) PreferencePasser.flightSpeed / 100.0f)));
		particleEffect.setParticleSpeed(PreferencePasser.particleSpeed);
		if(PreferencePasser.numRings != pipeline.numRings) {
			pipeline = new ParticlePipeline(particleEffect, PreferencePasser.numRings);
		}
		//Color color = new Color();
		//Color.rgb888ToColor(color, (PreferencePasser.backgroundColor & 0xffffff));
		//backgroundColor[0] = color.r;
		//backgroundColor[1] = color.g;
		//backgroundColor[2] = color.b;
		particleEffect.setColors(PreferencePasser.color1,
				PreferencePasser.color2, PreferencePasser.color3);
		System.out.println("pipe speed: " + pipeline.speed);
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
		configureFromPreferences();
	}

	@Override
	public void dispose() {
		pipeline.dispose();
		System.out.println("DiscoWormhole.dispose() called");
	}

}
