/* Author: Christopher Grabowski */

package com.adamantine.discowormhole.discopipeline;

import java.util.Random;

import com.adamantine.util.Math;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class DiscoPipeline implements FlightTranslator {
	public static final int SPEED_RANGE = 1000;
	public static final int MAX_SPEED = (SPEED_RANGE - 200);

	private static final boolean USE_LIGHTS = false;
	private static final int NUM_LIGHTS = 2;
	private static final float RING_INTERVAL = 7.0f;
	// lower values create more bends
	private static final int BEND_FREQUENCY = 5;
	// higher values make bends sharper
	private static final float BEND_SHARPNESS = 5.0f;
	private static final int NUM_RINGS_IN_BEND = 10;
	// MAX_SPEED must be at least 200 less than SPEED_RANGE
	private static final int MAX_RINGS = 40;
	private static final int DEFAULT_NUM_RINGS = 40; // 25
	private static final boolean DEFAULT_USE_COLORS = true;

	public float speedCoef = 0.9f;
	public int speed = (int) (MAX_SPEED * speedCoef);
	public static int numRings = DEFAULT_NUM_RINGS;
	public boolean useColors = DEFAULT_USE_COLORS;

	private final Random random;
	private final DiscoRing[] discoRings;
	private final float[] lightsPos;
	private final float[] bendAngles;
	private final Matrix4 flightTranslation;
	private final Matrix4 translation;
	private final Quaternion rotation;
	private final Quaternion slerpRot;
	private final RingSection ringSection;

	private Vector2 vRot;
	private float bend = -1.0f;
	private int pointer = 0;
	private long time = System.currentTimeMillis();
	private float oldPace = 0.0f;
	private float pace;
	private boolean isBending = false;
	private int bendCount = 0;
	private float bendAngle = -1.0f;

	public DiscoPipeline(RingSection ringSection) {
		random = new Random();
		discoRings = new DiscoRing[numRings];
		lightsPos = new float[NUM_LIGHTS * 3];
		bendAngles = new float[numRings];
		flightTranslation = new Matrix4();
		translation = new Matrix4()
				.setToTranslation(0.0f, 0.0f, -RING_INTERVAL);
		rotation = new Quaternion();
		slerpRot = new Quaternion(0.0f, 0.0f, 0.0f, 0.0f);
		this.ringSection = ringSection;

		for (int i = 0; i < bendAngles.length; i++) {
			bendAngles[i] = -1.0f;
		}

		for (int j = 0; j < numRings; j++) {
			discoRings[j] = new DiscoRing(ringSection, this);
		}
	}

	// Every ringSection ring and every ringSection are drawn every time this
	// method is called
	public void render(Camera camera) {
		time = System.currentTimeMillis();
		// Pace resets the pipeline to its original position
		// after moving the pipeline one ringSection_INTERVAL
		// The new ringSection ring will actually be drawn last, because pointer
		// is incremented at the end of the method

		// - speed + max speed
		pace = (time % (SPEED_RANGE - speed))
				/ ((SPEED_RANGE - speed) / RING_INTERVAL);

		if (oldPace > pace) {
			// discoRings[pointer] = new DiscoRing();
			// increment the pointer
			pointer++;
			// if the pointer is at the end of the ringSection ring array, move
			// it to
			// the beginning
			if (pointer == numRings) {
				pointer -= numRings;
			}
		}

		// this translates each ring by pace
		// to create the illusion of continuous movement
		flightTranslation.idt().translate(0.0f, 0.0f, pace);
		// method that creates bends
		createBends();

		// this lays out each ring
		for (int i = 0; i < numRings; i++) {
			// start drawing at pointer. Once the loop reaches the end of the
			// array, it starts drawing from the beginning
			if (i + pointer < numRings) {
				layout(discoRings[i + pointer], bendAngles, pointer, i
						+ pointer);
			} else {
				layout(discoRings[i + pointer - numRings], bendAngles, pointer,
						i + pointer);
			}
		}

		// the method that lays out each light
		if (USE_LIGHTS) {
			layoutLights(lightsPos, pointer);
		}

		// this draws each ring
		int rank = 0;		
		for (int i = 0; i > -numRings; i--) {				
			// start drawing at pointer. Once the loop reaches the end of the
			// array, it starts drawing from the beginning
			if (pointer - 1 + i > - 1) {
				discoRings[pointer - 1 + i].render(camera, rank, lightsPos);
			} else {
				discoRings[pointer - 1 + i + numRings].render(camera, rank,
						lightsPos);
			}
			rank++;
		}
		// oldPace is compared with pace to determine if the pipeline position
		// has reset
		oldPace = pace;
	}

	void createBends() {
		// the state of bending is chosen at random
		if (isBending == false && oldPace > pace
				&& random.nextInt(BEND_FREQUENCY) == 0) {
			isBending = true;
			// the direction of the bend is randomly chosen
			// and remains unchanged for the duration of the bend
			// bend = 360.0f / 16.0f * (float) random.nextInt(16);
			bend = (float) random.nextInt(360);
		} else if (isBending == true && bendCount == NUM_RINGS_IN_BEND) {
			isBending = false;
			// When the bend is finished, the bend direction is reset to -1.0f
			bend = -1.0f;
			bendCount = 0;
		}

		// bendAngle[pointer] is set every time the pipeline position is reset
		// to its original position (if !isBending, bend = 0.0f)
		if (oldPace > pace) {
			if (pointer == 0) {
				bendAngles[bendAngles.length - 1] = bend;
			} else {
				bendAngles[pointer - 1] = bend;
			}
		}

		// if the pipeline position is reset and isBending, increment bendCount
		if (oldPace > pace && isBending == true) {
			bendCount++;
		}
	}

	// Called for each ring before that ring is rendered
	DiscoRing layout(DiscoRing discoRing, float[] bendAngles, int pointer,
			int rank) {
		discoRing.getTransform().idt();

		for (int i = 0; i < rank - pointer; i++) {
			if (i + pointer < bendAngles.length) {
				bendAngle = bendAngles[i + pointer];
			} else {
				bendAngle = bendAngles[i + pointer - bendAngles.length];
			}

			if (bendAngle == -1.0f) {
				rotation.idt();
				translation.setToTranslation(0.0f, 0.0f, -RING_INTERVAL);
			} else {
				vRot = Math.getRotation(bendAngle, BEND_SHARPNESS);
				rotation.setEulerAngles(vRot.x, vRot.y, 0.0f);
				translation
						.setToTranslation(0.0f, 0.0f, (float) -RING_INTERVAL);
			}

			if (i == 0) {
				slerpRot.idt();
				rotation.slerp(slerpRot, time % (SPEED_RANGE - speed)
						/ (float) (SPEED_RANGE - speed));
				discoRing.getTransform().rotate(rotation).mul(translation);
			} else {
				discoRing.getTransform().rotate(rotation).mul(translation);
			}
		}
		return discoRing;
	}

	void layoutLights(float[] lightsPos, int pointer) {
		final Vector3 tmp = new Vector3();
		if (pointer + 4 < numRings) {
			tmp.set(0.0f, 0.0f, 0.0f).mul(
					discoRings[pointer + 4].getTransform());
		} else {
			tmp.set(0.0f, 0.0f, 0.0f).mul(
					discoRings[pointer + 4 - numRings].getTransform());
		}

		lightsPos[0] = tmp.x;
		lightsPos[1] = tmp.y;
		lightsPos[2] = tmp.z;

		if (pointer + 15 < numRings) {
			tmp.set(0.0f, 0.0f, 0.0f).mul(
					discoRings[pointer + 15].getTransform());
		} else {
			tmp.set(0.0f, 0.0f, 0.0f).mul(
					discoRings[pointer + 15 - numRings].getTransform());
		}

		lightsPos[3] = tmp.x;
		lightsPos[4] = tmp.y;
		lightsPos[5] = tmp.z;
	}

	@Override
	public Matrix4 getFlightTranslation() {
		return flightTranslation;
	}

	public void dispose() {
		ringSection.dispose();
	}
}
