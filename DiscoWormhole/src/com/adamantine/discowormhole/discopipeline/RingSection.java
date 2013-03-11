package com.adamantine.discowormhole.discopipeline;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Matrix4;

public interface RingSection {
	
	Matrix4 getModelMatrix();
	void render(Camera camera);
	void render(Camera camera, float[] lightPos);
	void render(Camera camera, float[] lightPos, float red, float green, float blue, float alpha);
	void dispose();
}
