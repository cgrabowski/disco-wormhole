/* Author: Christopher Grabowski */

package com.adamantine.discowormhole.discopipeline;

import java.util.Random;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;

public class DiscoRing{
	public static final int NUM_TILES_PER_RING = 16;
	public static final float ANGLE_ABOUT_RING = 360.0f / (float) NUM_TILES_PER_RING;
	public static final float RADIUS = 11.0f; // 7.3f;

	private final Random random;
	private final DiscoTile tile;
	private final Matrix4 transform;
	private final Matrix4 tileRotation;
	private final float[][] colors;
	private final FlightTranslator flightTranslator;
	
	private Vector2 radiusTranslation;
	private float rotAngle = 0.0f;
	private boolean hasFlightTranslator = false;
	private boolean hasOwnModel = false;

	public DiscoRing() {
		this(null, null);
	}

	public DiscoRing(DiscoTile tile) {
		this(tile, null);
	}

	public DiscoRing(DiscoTile tile, FlightTranslator fTrans) {
		if (fTrans != null) {
			flightTranslator = fTrans;
			hasFlightTranslator = true;
		} else {
			flightTranslator = null;
		}

		if (tile != null) {
			this.tile = tile;			
		} else {
			this.tile = new DiscoTile();
			hasOwnModel = true;
		}

		random = new Random();
		tileRotation = new Matrix4();
		transform = new Matrix4();
		radiusTranslation = new Vector2();
		colors = new float[NUM_TILES_PER_RING][4];

		for (int i = 0; i < NUM_TILES_PER_RING; i++) {
			for (int j = 0; j < 4; j++) {
				colors[i][j] = random.nextFloat();
			}
		}
	}
	
	public void render(Camera camera) {
		render(camera, null);
	}

	public void render(Camera camera, float[] lightsPos) {
		for (int i = 0; i < NUM_TILES_PER_RING; i++) {
			transformTile();
			tile.render(camera, lightsPos);
		}
		rotAngle = 0.0f;
	}

	public void render(Camera camera, float[] lightsPos, boolean useColors) {
		for (int i = 0; i < NUM_TILES_PER_RING; i++) {
			transformTile();
			tile.render(camera, lightsPos, colors[i][0], colors[i][1],
					colors[i][2], colors[i][3]);
		}
		rotAngle = 0.0f;
	}

	// called for each tile before that tile is rendered
	private void transformTile() {
		// reset model.
		tile.model.idt();
		// rotation and bendRot are rotations for the entire ring
		// used when the ring is on a bend
		if (hasFlightTranslator) {
			tile.model.mul(flightTranslator.getFlightTranslation()).mul(
					transform);
		}
		// place the tile on the ring perimeter
		// tile.model.mul(tileRotation).mul(radiusTranslation);
		radiusTranslation = com.adamantine.util.Math.getRotation(rotAngle,
				RADIUS);
		tile.model.translate(radiusTranslation.x, radiusTranslation.y, 0.0f);
		// rotate the tile itself so it is flying longways
		// tileRotation set to rotate 360/NUM_RINGS
		tileRotation.idt().setToRotation(0.0f, 0.0f, 1.0f, rotAngle + 90.0f);
		tile.model.mul(tileRotation);
		tileRotation.setToRotation(0.0f, 1.0f, 0.0f, 90.0f);
		tile.model.mul(tileRotation);
		rotAngle += ANGLE_ABOUT_RING;
	}

	public Matrix4 getTransform() {
		return this.transform;
	}

	public void dispose() {
		if (hasOwnModel) {
			tile.dispose();
		}
	}
}
