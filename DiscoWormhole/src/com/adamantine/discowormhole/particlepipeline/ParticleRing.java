/* Author: Christopher Grabowski */

package com.adamantine.discowormhole.particlepipeline;

import java.util.Random;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;

public class ParticleRing {
	public static final int NUM_SECTIONS_PER_RING = 16;

	public final Matrix4 transform;

	private static final float ANGLE_ABOUT_RING = 360.0f / (float) NUM_SECTIONS_PER_RING;
	private static final float RADIUS = 12.0f; // 7.3f;

	private final Random random;
	private final RingSectionParticleEffect ringSection;
	private final Matrix4 ringSectionTranslation;
	private final ParticlePipeline flightTranslator;

	private Vector2 radiusTranslation;
	private float rotConst;
	private float rotAngle = 0.0f;
	private float flareAcc;
	private boolean hasFlightTranslator = false;
	private boolean hasOwnModel = false;
	private float[] rands;

	public ParticleRing() {
		this(null, null);
	}

	public ParticleRing(RingSectionParticleEffect ringSection) {
		this(ringSection, null);
	}

	public ParticleRing(RingSectionParticleEffect ringSection,
			ParticlePipeline fTrans) {
		if (fTrans != null) {
			flightTranslator = fTrans;
			hasFlightTranslator = true;
		} else {
			flightTranslator = null;
		}

		if (ringSection != null) {
			this.ringSection = ringSection;
		} else {
			this.ringSection = ringSection;
			hasOwnModel = true;
		}

		random = new Random();
		rotConst = (float) random.nextInt(360);
		ringSectionTranslation = new Matrix4();
		transform = new Matrix4();
		radiusTranslation = new Vector2();

		rands = new float[NUM_SECTIONS_PER_RING];
		for (int i = 0; i < NUM_SECTIONS_PER_RING; i++) {
			rands[i] = (float) random.nextInt(25) / 10.0f;
		}
	}

	public void render(Camera camera, int rank) {
		for (int i = 0; i < NUM_SECTIONS_PER_RING; i++) {
			// reset model
			ringSection.spriteBatch.getTransformMatrix().idt();

			if (hasFlightTranslator) {
				ringSection.spriteBatch.getTransformMatrix()
						.mul(flightTranslator.flightTranslation)
						.mul(transform);
			}
			// place the ringSection on the ring perimeter and add a random z
			// translation to break up aliasing
			radiusTranslation = com.adamantine.util.Math.getRotation(rotAngle
					+ rotConst, RADIUS);
			ringSection.spriteBatch.getTransformMatrix().translate(
					radiusTranslation.x, radiusTranslation.y,
					-rands[i]);

			ringSectionTranslation.idt().setToRotation(0.0f, 0.0f, 1.0f,
					rotAngle + rotConst + 90.0f);
			ringSection.spriteBatch.getTransformMatrix().mul(
					ringSectionTranslation);

			// flares the back end of the wormhole
			// if (rank < 2) {
			// ringSectionTranslation.idt().setToRotation(1.0f, 0.0f, 0.0f,
			// -5.0f);
			// ringSection.getModelMatrix().mul(ringSectionTranslation);
			// flareAcc = (float) (2 - rank) * 8.0f;
			// ringSection.getModelMatrix().translate(0.0f, -flareAcc, 0.0f);
			// }
			rotAngle += ANGLE_ABOUT_RING;

			ringSection.render(camera);
		}
		rotAngle = 0.0f;
	}

	public void dispose() {
		if (hasOwnModel) {
			ringSection.dispose();
		}
	}
}
