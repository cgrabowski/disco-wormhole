package com.adamantine.discowormhole.and;

import android.annotation.TargetApi;
import android.content.SharedPreferences;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Window;

import com.adamantine.discowormhole.PreferencePasser;
import com.adamantine.discowormhole.and.ColorPickerDialog.OnColorChangedListener;

public class DiscoSettings extends PreferenceActivity implements
		SharedPreferences.OnSharedPreferenceChangeListener,
		OnColorChangedListener {
	public static final int DEFAULT_COLOR_1 = 0xff7d9fff;
	public static final int DEFAULT_COLOR_2 = 0xffff4b31;
	public static final int DEFAULT_COLOR_3 = 0xff64ff46;

	private static PreferenceActivity context;
	private boolean honeycombOrGreater;
	private Preference color1, color2, color3;

	public static boolean commonOnSharedPreferencesChanged(
			SharedPreferences sharedPrefs, String key) {
		//Log.e("DiscoSettings", "commonOnSharedPreferencesChanged called");

		synchronized (PreferencePasser.getLock()) {
			if (key.equals("flight_speed")) {
				PreferencePasser.flightSpeed = sharedPrefs.getInt(key, 50);
			} else if (key.equals("num_rings")) {
				PreferencePasser.numRings = sharedPrefs.getInt(key, 30);
			} else if (key.equals("particle_speed")) {
				PreferencePasser.particleSpeed = sharedPrefs.getInt(key, 50);
			} else if (key.equals("use_space_dust")) {
				PreferencePasser.useSpaceDust = sharedPrefs.getBoolean(key,
						true);
			}
			PreferencePasser.prefsChanged = true;
		}

		return true;
	}

	private static void commonSetOnClickListeners(
			final OnColorChangedListener colorListener, Preference... prefs) {
		for (final Preference p : prefs) {
			p.setOnPreferenceClickListener(new OnPreferenceClickListener() {
				@Override
				public boolean onPreferenceClick(Preference preference) {
					Log.e(p.getKey(), "onPreferenceClick");
					new ColorPickerDialog(context, colorListener, p.getKey(),
							0xff0000ff, 0x00ff00ff).show();
					return true;
				}
			});
		}
	}

	private static void commonColorChanged(String key, int color) {
		synchronized (PreferencePasser.getLock()) {
			// if (key.equals("background_color")) {
			// PreferencePasser.backgroundColor = color;
			if (key.equals("color_one")) {
				PreferencePasser.color1 = color;
			} else if (key.equals("color_two")) {
				PreferencePasser.color2 = color;
			} else if (key.equals("color_three")) {
				PreferencePasser.color3 = color;
			}
			PreferencePasser.prefsChanged = true;
		}
	}

	@Override
	public void onCreate(Bundle sis) {
		DiscoWormholeService.engine.onResume();
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(sis);
		context = this;
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			honeycombOrGreater = true;
			createHoneycombOrGreater();
		} else {
			honeycombOrGreater = false;
			createLessThanHoneycomb();
		}
	}

	/*
	 * The following PreferenceFragment and methods are used if API is honeycomb
	 * or greater.
	 */

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void createHoneycombOrGreater() {
		getFragmentManager().beginTransaction()
				.replace(android.R.id.content, new DiscoPreferenceFragment())
				.commit();
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public static class DiscoPreferenceFragment extends PreferenceFragment
			implements SharedPreferences.OnSharedPreferenceChangeListener,
			OnColorChangedListener {
		private Preference color1, color2, color3;
		private SharedPreferences prefs;
		private SharedPreferences.Editor editor;

		@Override
		public void onCreate(Bundle sis) {
			super.onCreate(sis);
			PreferenceManager.setDefaultValues(getActivity(),
					R.xml.preferences, false);
			addPreferencesFromResource(R.xml.preferences);
			prefs = getPreferenceScreen().getSharedPreferences();
			editor = prefs.edit();
			prefs.registerOnSharedPreferenceChangeListener(this);

			//backgroundColor = (Preference) findPreference("background_color");
			color1 = (Preference) findPreference("color_one");
			color2 = (Preference) findPreference("color_two");
			color3 = (Preference) findPreference("color_three");

			commonSetOnClickListeners(this, color1, color2,
					color3);

		}

		@Override
		public void onResume() {
			super.onResume();

			//GradientDrawable tmp = (GradientDrawable) getResources()
			//		.getDrawable(R.drawable.background_color_icon);
			//tmp.mutate();
			//tmp.setColor(prefs.getInt("background_color", 0xff000000));
			//backgroundColor.setIcon(tmp);
			//Log.e("background color", prefs.getInt("background_color", -2) + "");

			GradientDrawable tmp = (GradientDrawable) getResources().getDrawable(
					R.drawable.color_one_icon);
			tmp.mutate();
			tmp.setColor(prefs.getInt("color_one", DEFAULT_COLOR_1));
			color1.setIcon(tmp);

			tmp = (GradientDrawable) getResources().getDrawable(
					R.drawable.color_two_icon);
			tmp.mutate();
			tmp.setColor(prefs.getInt("color_two", DEFAULT_COLOR_2));
			color2.setIcon(tmp);

			tmp = (GradientDrawable) getResources().getDrawable(
					R.drawable.color_three_icon);
			tmp.mutate();
			tmp.setColor(prefs.getInt("color_three", DEFAULT_COLOR_3));
			color3.setIcon(tmp);
		}

		@Override
		public void onSharedPreferenceChanged(SharedPreferences sharedPrefs,
				String key) {
			commonOnSharedPreferencesChanged(sharedPrefs, key);
		}

		@Override
		public void colorChanged(String key, int color) {
			Log.e("colorChanged", "color changed. key: " + key + ", color: "
					+ color);
			setIconColor(key, color);
			editor.putInt(key, color);
			editor.commit();
			commonColorChanged(key, color);
		}

		private void setIconColor(String key, int color) {
			GradientDrawable shape = (GradientDrawable) findPreference(key)
					.getIcon();
			shape.mutate();
			shape.setColor(color);
			findPreference(key).setIcon(shape);
		}
	}

	/*
	 * The following methods are used if API is less than honeycomb.
	 */

	@SuppressWarnings("deprecation")
	private void createLessThanHoneycomb() {
		color1 = (Preference) findPreference("color_one");
		color2 = (Preference) findPreference("color_two");
		color3 = (Preference) findPreference("color_three");
		commonSetOnClickListeners(this, color1, color2, color3);
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onResume() {
		super.onResume();

		if (!honeycombOrGreater) {
			getPreferenceScreen().getSharedPreferences()
					.registerOnSharedPreferenceChangeListener(this);
		}
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onPause() {
		super.onPause();

		if (!honeycombOrGreater) {
			getPreferenceScreen().getSharedPreferences()
					.unregisterOnSharedPreferenceChangeListener(this);
		}
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPrefs,
			String key) {
		commonOnSharedPreferencesChanged(sharedPrefs, key);
	}

	@Override
	public void colorChanged(String key, int color) {
		commonColorChanged(key, color);
	}
}
