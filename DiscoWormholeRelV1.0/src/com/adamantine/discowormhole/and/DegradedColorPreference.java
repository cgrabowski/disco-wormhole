package com.adamantine.discowormhole.and;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.GradientDrawable;
import android.preference.Preference;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

public class DegradedColorPreference extends Preference {
	public LinearLayout layout;
	
	private SharedPreferences prefs;
	

	public DegradedColorPreference(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public DegradedColorPreference(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	protected View onCreateView(ViewGroup parent) {
		LinearLayout layout = null;

		try {
			LayoutInflater lif = (LayoutInflater) getContext()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			layout = (LinearLayout) lif.inflate(R.layout.degraded_color_preference, parent,
					false);
		} catch (Exception e) {
			Log.e("ColorPreference", " Error creating color preference");
		}
		return layout;
	}

	@Override
	public void onBindView(View view) {
		super.onBindView(view);

		layout = (LinearLayout) view;
		TextView tv = (TextView) layout.getChildAt(0);
		GradientDrawable tmp = (GradientDrawable) tv.getBackground();
		tmp.mutate();
		String title = null;
		if (getKey().equals("color_one")) {
			tmp.setColor(getPersistedInt(DiscoSettings.DEFAULT_COLOR_1));
			title = "Color One";
		} else if (getKey().equals("color_two")) {
			tmp.setColor(getPersistedInt(DiscoSettings.DEFAULT_COLOR_2));
			title = "Color Two";
		} else if (getKey().equals("color_three")) {
			tmp.setColor(getPersistedInt(DiscoSettings.DEFAULT_COLOR_3));
			title = "Color Three";
		}
		tv = (TextView) layout.getChildAt(1);
		tv.setText(title);
	}
	
	public void setBackgroundColor() {
		
	}
}
