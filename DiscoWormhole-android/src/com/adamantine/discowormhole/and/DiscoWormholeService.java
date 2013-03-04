package com.adamantine.discowormhole.and;

import java.util.Map;

import android.preference.PreferenceManager;
import android.service.wallpaper.WallpaperService.Engine;

import com.adamantine.discowormhole.DiscoConfig;
import com.adamantine.discowormhole.DiscoWormhole;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.badlogic.gdx.backends.android.AndroidLiveWallpaperService;
import com.badlogic.gdx.backends.android.AndroidLiveWallpaperService.AndroidWallpaperEngine;

public class DiscoWormholeService extends AndroidLiveWallpaperService {
	public static final String SHARED_PREFS_NAME = "DiscoWormhole";

	private DiscoWormhole discoWormhole;

	@Override
	public ApplicationListener createListener(boolean arg0) {
		discoWormhole = new DiscoWormhole();
		Map<String, ?> prefMap = PreferenceManager.getDefaultSharedPreferences(
				this).getAll();

		synchronized (DiscoConfig.getInstance()) {
			DiscoConfig.flightSpeed = (Integer) prefMap.get("1");
			DiscoConfig.numRings = (Integer) prefMap.get("2");
			DiscoConfig.useColors = (Boolean) prefMap.get("4");
			DiscoConfig.useSpaceDust = (Boolean) prefMap.get("8");
			DiscoConfig.prefsChanged = true;
		}

		discoWormhole.setPreferences(prefMap);
		return discoWormhole;
	}

	@Override
	public Engine onCreateEngine() {
		PreferenceManager.setDefaultValues(this, R.xml.setting_xml, false);
		return new DiscoWormholeEngine(this);
	}

	@Override
	public AndroidApplicationConfiguration createConfig() {
		AndroidApplicationConfiguration cfg = new AndroidApplicationConfiguration();
		cfg.useGL20 = true;
		return cfg;
	}

	@Override
	public void offsetChange(ApplicationListener listener, float xOffset,
			float yOffset, float xOffsetStep, float yOffsetStep,
			int xPixelOffset, int yPixelOffset) {
		Gdx.app.log("LiveWallpaper", "offset changed: " + xOffset + ", "
				+ yOffset);
	}

	public class DiscoWormholeEngine extends
			AndroidLiveWallpaperService.AndroidWallpaperEngine {
		private DiscoWormholeService service;

		DiscoWormholeEngine(DiscoWormholeService service) {
			super();
			this.service = service;
		}

		@Override
		public void onResume() {
			Map<String, ?> prefMap = PreferenceManager
					.getDefaultSharedPreferences(service).getAll();
			service.discoWormhole.setPreferences(prefMap);
			super.onResume();
		}
	}

}