package com.adamantine.discowormhole;

import com.badlogic.gdx.graphics.PerspectiveCamera;

public class DiscoWormholeCamera extends PerspectiveCamera{
	public static float aspectRatio;
	
	public DiscoWormholeCamera(int fieldOfView, int width, int height) {
		super(fieldOfView, width, height);
		aspectRatio = viewportHeight / viewportWidth;
		up.set(0.0f, 1.0f, 0.0f);
		position.set(0.0f, 0.0f, -1.0f);
		far = 300.0f;
		update();
	}
	
	public void resize(int width, int height) {
		viewportHeight = height;
		viewportWidth = width;
		aspectRatio = viewportHeight / viewportWidth;
		update();
	}
}
