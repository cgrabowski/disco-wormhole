/* Author: Christopher Grabowski */

package com.adamantine.discowormhole.discopipeline;

import java.util.Random;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;

public class DiscoRing {
	private static final int NUM_SECTIONS_PER_RING = 16;
	private static final float ANGLE_ABOUT_RING = 360.0f / (float) NUM_SECTIONS_PER_RING;
	private static final float RADIUS = 11.0f; // 7.3f;

	private final Random random;
	private final RingSection ringSection;
	private final Matrix4 transform;
	private final Matrix4 ringSectionTranslation;
	private final float[][] colors;
	private final FlightTranslator flightTranslator;

	private Vector2 radiusTranslation;
	private float rotConst;
	private float rotAngle = 0.0f;
	private float flareAcc;
	private boolean hasFlightTranslator = false;
	private boolean hasOwnModel = false;

	public DiscoRing() {
		this(null, null);
	}

	public DiscoRing(RingSection ringSection) {
		this(ringSection, null);
	}

	public DiscoRing(RingSection ringSection, FlightTranslator fTrans) {
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
		colors = new float[NUM_SECTIONS_PER_RING][4];

		for (int i = 0; i < NUM_SECTIONS_PER_RING; i++) {
			for (int j = 0; j < 4; j++) {
				colors[i][j] = random.nextFloat();
			}
		}
	}

	public void render(Camera camera) {
		render(camera, -1, null);
	}

	public void render(Camera camera, int rank) {
		render(camera, rank, null);
	}

	public void render(Camera camera, int rank, float[] lightsPos) {
		for (int i = 0; i < NUM_SECTIONS_PER_RING; i++) {
			transformRingSection(rank);
			ringSection.render(camera, lightsPos);
		}
		rotAngle = 0.0f;
	}

	public void render(Camera camera, int rank, float[] lightsPos,
			boolean useColors) {
		for (int i = 0; i < NUM_SECTIONS_PER_RING; i++) {
			transformRingSection(rank);
			ringSection.render(camera, lightsPos, colors[i][0], colors[i][1],
					colors[i][2], colors[i][3]);
		}
		rotAngle = 0.0f;
	}

	// called for each ringSection before that ringSection is rendered
	private void transformRingSection(int rank) {
		// reset model
		ringSection.getModelMatrix().idt();

		if (hasFlightTranslator) {
			ringSection.getModelMatrix()
					.mul(flightTranslator.getFlightTranslation())
					.mul(transform);
		}
		// place the ringSection on the ring perimeter
		radiusTranslation = com.adamantine.util.Math.getRotation(rotAngle
				+ rotConst, RADIUS);
		ringSection.getModelMatrix().translate(radiusTranslation.x,
				radiusTranslation.y, 0.0f);

		ringSectionTranslation.idt().setToRotation(0.0f, 0.0f, 1.0f,
				rotAngle + rotConst + 90.0f);
		ringSection.getModelMatrix().mul(ringSectionTranslation);
		
		// flares the back end of the wormhole
		if (rank < 6) {
			ringSectionTranslation.idt()
					.setToRotation(1.0f, 0.0f, 0.0f, -30.0f);
			ringSection.getModelMatrix().mul(ringSectionTranslation);
			flareAcc = (float) (6 - rank) * 3.0f;
			ringSection.getModelMatrix().translate(0.0f, -flareAcc, 0.0f);
		}
		System.out.println(flareAcc);
		rotAngle += ANGLE_ABOUT_RING;
	}

	public Matrix4 getTransform() {
		return this.transform;
	}

	public void dispose() {
		if (hasOwnModel) {
			ringSection.dispose();
		}
	}
}
