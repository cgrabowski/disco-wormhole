package com.adamantine.discowormhole.and;

import android.app.Activity;
import android.content.Context;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.ads.AdRequest;
import com.google.ads.AdSize;
import com.google.ads.AdView;

public class AdPreference extends Preference {

	private View view;
	private AdView adView;

	public AdPreference(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public AdPreference(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public AdPreference(Context context) {
		super(context);
	}

	@Override
	protected View onCreateView(ViewGroup parent) {
		// Log.i("AdPreference", "onCreateView called");
		if (view == null) {
			view = super.onCreateView(parent);
			((LinearLayout) view).addView(adView);
		} else {
			super.onCreateView(parent);
		}
		return view;
	}

	@Override
	protected void onBindView(View view) {
		super.onBindView(view);
		// Log.i("AdPreference", "onBindView called");
	}

	@Override
	protected void onAttachedToActivity() {
		super.onAttachedToActivity();
		// Log.i("AdPreference", "onAttachedToActivity called");

		// Create the adView
		adView = new AdView((Activity) getContext(), AdSize.BANNER,
				"a151390e12917b5");

		// Initiate a generic request to load it with an ad
		AdRequest request = new AdRequest();
		request.addTestDevice("23392C83B8B55DE893A18286CB92DDA2");
		request.addTestDevice("E1BAA0317138AEE05268B2E4F76B2D3F");
		adView.loadAd(request);
	}

	@Override
	protected void onAttachedToHierarchy(PreferenceManager preferenceManager) {
		super.onAttachedToHierarchy(preferenceManager);
		// Log.i("AdPreference", "onAttachedToHierarchy called");
	}

	@Override
	protected void onPrepareForRemoval() {
		// Log.i("AdPreference", "onPrepareForRemoval called");
		super.onPrepareForRemoval();
	}

}
