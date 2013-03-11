package com.adamantine.discowormhole.particlepipeline;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Matrix4;

public interface ParticleRingSection {
	
	//void setNumSections(int numSections);
	Matrix4 getModelMatrix();
	void render(Camera camera);
	void dispose();
}
