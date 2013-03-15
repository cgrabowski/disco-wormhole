package com.adamantine.discowormhole.and;

import java.util.Map;

import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.SurfaceHolder;

import com.adamantine.discowormhole.DiscoWormhole;
import com.adamantine.discowormhole.PreferencePasser;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.badlogic.gdx.backends.android.AndroidLiveWallpaperService;

public class DiscoWormholeService extends AndroidLiveWallpaperService {
	public static final String SHARED_PREFS_NAME = "DiscoWormhole";
	public static DiscoWormholeEngine engine;

	@Override
	public ApplicationListener createListener(boolean arg0) {
		return new DiscoWormhole();
	}

	@Override
	public Engine onCreateEngine() {
		PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
		engine = new DiscoWormholeEngine();
		return engine;
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
		// Gdx.app.log("LiveWallpaper", "offset changed: " + xOffset + ", "
		// + yOffset);
	}

	public class DiscoWormholeEngine extends
			AndroidLiveWallpaperService.AndroidWallpaperEngine {

		@Override
		public void onCreate(SurfaceHolder surface) {
			super.onCreate(surface);
			
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
				PreferenceManager.setDefaultValues(getApplicationContext(),
						R.xml.preferences, false);
			} else {
				PreferenceManager.setDefaultValues(getApplicationContext(),
						R.xml.preferences_lt_11, false);
			}
			Map<String, ?> prefMap = PreferenceManager
					.getDefaultSharedPreferences(getApplicationContext())
					.getAll();
			// Log.e("prefMap", prefMap.toString());

			synchronized (PreferencePasser.getLock()) {
				PreferencePasser.flightSpeed = (Integer) prefMap
						.get("flight_speed");
				PreferencePasser.particleSpeed = (Integer) prefMap
						.get("particle_speed");
				PreferencePasser.numRings = (Integer) prefMap.get("num_rings");
				PreferencePasser.useSpaceDust = (Boolean) prefMap
						.get("use_space_dust");
				// PreferencePasser.backgroundColor =
				// (prefMap.get("background_color") != null) ? (Integer) prefMap
				// .get("background_color") : 0xff000000;
				PreferencePasser.color1 = (prefMap.get("color_one") != null) ? (Integer) prefMap
						.get("color_one") : DiscoSettings.DEFAULT_COLOR_1;
				PreferencePasser.color2 = (prefMap.get("color_two") != null) ? (Integer) prefMap
						.get("color_two") : DiscoSettings.DEFAULT_COLOR_2;
				PreferencePasser.color3 = (prefMap.get("color_three") != null) ? (Integer) prefMap
						.get("color_three") : DiscoSettings.DEFAULT_COLOR_3;
				PreferencePasser.prefsChanged = true;
			}
		}
	}

}