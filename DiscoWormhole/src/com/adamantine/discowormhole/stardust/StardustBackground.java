package com.adamantine.discowormhole.stardust;

import com.adamantine.particle.RenderableParticleEffect;
import com.badlogic.gdx.graphics.Camera;

public class StardustBackground extends RenderableParticleEffect {

	public StardustBackground() {
		super("particle/stardust1.p", "particle");
	}

	public void render(Camera camera) {
		super.prepareRender(camera);

		spriteBatch.setTransformMatrix(camera.view);
		spriteBatch.getTransformMatrix().translate(0.0f, 0.0f, -camera.far + 0.1f)
				.scale(camera.far / 450.0f, camera.far / 450.0f, camera.far / 450.0f);
		spriteBatch.begin();
		draw(spriteBatch, delta);
		spriteBatch.end();
	}

	@Override
	public void dispose() {
		super.dispose();
	}
}
