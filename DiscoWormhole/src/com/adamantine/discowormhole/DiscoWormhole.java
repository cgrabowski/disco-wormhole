/* Author: Christopher Grabowski */

package com.adamantine.discowormhole;

import java.util.Map;

import com.adamantine.discowormhole.discopipeline.DiscoPipeline;
import com.adamantine.discowormhole.discopipeline.DiscoRing;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;

public class DiscoWormhole implements ApplicationListener {
	static final boolean DEFAULT_USE_SPACE_DUST = true;
	
	private static final DiscoConfig config = DiscoConfig.getInstance(); 

	DiscoWormholeCamera camera;

	volatile private Map<String, ?> preferences;
	private DiscoPipeline discoPipeline;
	private boolean isSetup = false;
	private boolean useSpaceDust = DEFAULT_USE_SPACE_DUST;

	@Override
	public void create() {
		camera = new DiscoWormholeCamera(67, Gdx.graphics.getWidth(),
				Gdx.graphics.getHeight());
		discoPipeline = new DiscoPipeline();
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
		// Gdx.graphics.getGL20().glViewport(0, 0, Gdx.graphics.getWidth(),
		// Gdx.graphics.getHeight());
		camera.resize(width, height);
	}

	@Override
	public void render() {

		synchronized (config) {
			if (DiscoConfig.prefsChanged == true) {
				configureFromPreferences();
				DiscoConfig.prefsChanged = false;
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
				GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT
						| GL20.GL_STENCIL_BUFFER_BIT);
		discoPipeline.render(camera);
	}

	private void configureFromPreferences() {
		System.out.println("flight speed: " + DiscoConfig.flightSpeed);
		System.out.println("num rings: " + DiscoConfig.numRings);
		System.out.println("use colors: " + DiscoConfig.useColors);
		System.out.println("use spacedust: " + DiscoConfig.useSpaceDust);
		
		discoPipeline.speed = (int) (DiscoPipeline.MAX_SPEED * (Math.sqrt((float) DiscoConfig.flightSpeed / 100.0f)));
		System.out.println("pipe speed: " + discoPipeline.speed);
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
		synchronized(DiscoConfig.getInstance()) {
			configureFromPreferences();
		}
	}

	public void setPreferences(Map<String, ?> preferences) {
		this.preferences = preferences;
		System.out.println(this.preferences.toString());
	}

	@Override
	public void dispose() {
		discoPipeline.dispose();
		System.out.println("DiscoWormhole.dispose() called");
	}

}
