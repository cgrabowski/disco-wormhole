package com.adamantine.discowormhole.particlepipeline;

import com.adamantine.particle.RenderableParticleEffect;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class RingSectionParticleEffect extends RenderableParticleEffect {

	private float pSpeedFactor = 225.0f;
	private float stretchX = 0.05f, stretchY = 0.05f;

	public RingSectionParticleEffect(Camera camera, SpriteBatch spriteBatch,
			Sprite sprite, String effectFile, String imagesDirectory) {
		super(spriteBatch, effectFile, imagesDirectory);
		for (ParticleEmitter e : getEmitters()) {
			e.setSprite(sprite);
		}
		spriteBatch.setTransformMatrix(camera.view);
	}

	// @Override
	// public void setNumSections(int numSections) {
	// this.numSections = numSections;
	// timeFactors = new float[numSections];
	// for (int i = 0; i < numSections; i++) {
	// timeFactors[i] = (float) random.nextInt(25000) / 10000.0f;
	// }
	// }

	@Override
	public void render(Camera camera) {
		try {
			spriteBatch.getTransformMatrix().scale(stretchX, 1.0f, stretchY);
			spriteBatch.getTransformMatrix().rotate(1.0f, 0.0f, 0.0f, -90.0f);
			// spriteBatch.getTransformMatrix().rotate(0.0f, 0.0f, 0.0f,
			// 180.0f);

			// counter = (counter < numSections) ? counter++ : 0;

			delta = (Gdx.graphics.getDeltaTime()) / pSpeedFactor;
			spriteBatch.setProjectionMatrix(camera.projection);

			spriteBatch.begin();
			draw(spriteBatch, delta);
			spriteBatch.end();
		} catch (IllegalStateException s) {
			// s.printStackTrace();

			try {
				spriteBatch.end();
			} catch (Exception n) {
				// n.printStackTrace();
			}

		} catch (Exception e) {
			// e.printStackTrace();
		}
	}

	public void setColors(int color1, int color2, int color3) {
		final Color tmp = new Color();

		Color.rgb888ToColor(tmp, (color1 & 0xffffff));
		this.getEmitters().get(0).getTint()
				.setColors(new float[] { tmp.r, tmp.g, tmp.b });
		// System.out.println(tmp.toString());
		// System.out.println(tmp.r + ", " + tmp.g + ", " + tmp.b);

		Color.rgb888ToColor(tmp, (color2 & 0xffffff));
		this.getEmitters().get(1).getTint()
				.setColors(new float[] { tmp.r, tmp.g, tmp.b });
		// System.out.println(tmp.toString());
		// System.out.println(tmp.r + ", " + tmp.g + ", " + tmp.b);

		Color.rgb888ToColor(tmp, (color3 & 0xffffff));
		this.getEmitters().get(2).getTint()
				.setColors(new float[] { tmp.r, tmp.g, tmp.b });
		// System.out.println(tmp.toString());
		// System.out.println(tmp.r + ", " + tmp.g + ", " + tmp.b);
	}

	public void setParticleSpeed(int ps) {
		pSpeedFactor = ((float) (100 - (ps - 1)) * 4.5f) + 50f;
		// System.out.println("pSpeedFactor: " + pSpeedFactor);
	}

	public void setStretch(int x, int y) {

		stretchX = ((float) x / 100.0f) / 2.0f + 0.04f;
		stretchY = ((float) y / 100.0f) / 2.0f + 0.15f;
	}

}
