package com.adamantine.discowormhole.stardust;

import com.adamantine.particle.RenderableParticleEffect;
import com.badlogic.gdx.graphics.Camera;

public class StardustBox extends RenderableParticleEffect {
	
	public StardustBox() {
		super("particle/stardust1.p", "particle");
	}

	public void render(Camera camera) {
		super.prepareRender(camera);
		
		spriteBatch.getTransformMatrix().translate(0.0f, 0.0f, 50.0f)
				.scale(0.1f, 0.1f, 0.1f);
		spriteBatch.begin();
		draw(spriteBatch, delta);
		spriteBatch.end();

		spriteBatch.setTransformMatrix(camera.view);
		spriteBatch.getTransformMatrix().rotate(0.0f, 1.0f, 0.0f, 90)
				.translate(0.0f, 0.0f, 50.0f).scale(0.1f, 0.1f, 0.1f);
		spriteBatch.begin();
		draw(spriteBatch, delta);
		spriteBatch.end();

		spriteBatch.setTransformMatrix(camera.view);
		spriteBatch.getTransformMatrix().rotate(0.0f, 1.0f, 0.0f, -90)
				.translate(0.0f, 0.0f, 50.0f).scale(0.1f, 0.1f, 0.1f);
		spriteBatch.begin();
		draw(spriteBatch, delta);
		spriteBatch.end();

		spriteBatch.setTransformMatrix(camera.view);
		spriteBatch.getTransformMatrix().rotate(1.0f, 0.0f, 0.0f, 90)
				.translate(0.0f, 0.0f, 50.0f).scale(0.1f, 0.1f, 0.1f);
		spriteBatch.begin();
		draw(spriteBatch, delta);
		spriteBatch.end();

		spriteBatch.setTransformMatrix(camera.view);
		spriteBatch.getTransformMatrix().rotate(1.0f, 0.0f, 0.0f, -90)
				.translate(0.0f, 0.0f, 50.0f).scale(0.1f, 0.1f, 0.1f);
		spriteBatch.begin();
		draw(spriteBatch, delta);
		spriteBatch.end();

		spriteBatch.setTransformMatrix(camera.view);
		spriteBatch.getTransformMatrix().rotate(0.0f, 1.0f, 0.0f, 180)
				.translate(0.0f, 0.0f, 50.0f).scale(0.1f, 0.1f, 0.1f);
		spriteBatch.begin();
		draw(spriteBatch, delta);
		spriteBatch.end();

		spriteBatch.getTransformMatrix().idt();
	}
	
	@Override
	public void dispose() {
		super.dispose();
	}
}
