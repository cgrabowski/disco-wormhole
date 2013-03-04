package com.adamantine.discowormhole;

import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;

public class PointLight {

	public final Vector3 pos;
	public final float[] color;
	public final Matrix4 transform;

	public PointLight() {
		pos = new Vector3();
		color = new float[4];
		transform = new Matrix4();
	}

	public PointLight(float[] color) {
		this();
		for (int i = 0; i < this.color.length; i++) {
			this.color[i] = color[i];
		}
	}

	public PointLight(float red, float green, float blue, float alpha) {
		this();
		color[0] = red;
		color[1] = green;
		color[2] = blue;
		color[3] = alpha;
	}

	public Matrix4 getTransform() {
		// TODO Auto-generated method stub
		return transform;
	}
	
	public float[] getColor() {
		return color;
	}
	
	public Vector3 getPos() {
		return pos;
	}
}
