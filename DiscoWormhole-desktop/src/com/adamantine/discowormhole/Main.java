package com.adamantine.discowormhole;

import com.adamantine.discowormhole.DiscoWormhole;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class Main {
	
	public static void main(String[] args) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "Disco Wormhole";
		cfg.useGL20 = true;
		cfg.width = 800;
		cfg.height = 480;
		new LwjglApplication(new DiscoWormhole(), cfg);
	}
} 
