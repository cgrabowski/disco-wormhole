package com.adamantine.discowormhole.and;

import com.adamantine.discowormhole.DiscoConfig;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceActivity;

public class DiscoSettings extends PreferenceActivity implements
		SharedPreferences.OnSharedPreferenceChangeListener {
	private int flag = 0;

	@Override
	protected void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		addPreferencesFromResource(R.xml.setting_xml);
	}

	@Override
	protected void onResume() {
		super.onResume();
		flag = 0;
		getPreferenceScreen().getSharedPreferences()
				.registerOnSharedPreferenceChangeListener(this);
	}

	@Override
	protected void onPause() {
		super.onPause();

		getPreferenceScreen().getSharedPreferences()
				.unregisterOnSharedPreferenceChangeListener(this);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPrefs,
			String key) {
		int tmp = Integer.parseInt(key);
		// System.out.println("parsed key: " + tmp);

		synchronized (DiscoConfig.getInstance()) {
			switch (tmp) {
			case (1):
				DiscoConfig.flightSpeed = sharedPrefs.getInt(key, 50);
				break;
			case (2):
				DiscoConfig.numRings = sharedPrefs.getInt(key, 50);
				break;
			case (4):
				DiscoConfig.useColors = sharedPrefs.getBoolean(key, true);
				break;
			case (8):
				DiscoConfig.useSpaceDust = sharedPrefs.getBoolean(key, true);
				break;
			}
			// System.out.println("packed flag:" + flag);
			DiscoConfig.prefsChanged = true;
		}
	}
}
