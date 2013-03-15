/*
 * Copyright (C) 2007 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.adamantine.discowormhole.and;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;

public class ColorPickerDialog extends Dialog {

	public interface OnColorChangedListener {
		void colorChanged(String key, int color);
	}

	private static int centerX;
	private static int centerY;
	private static int centerRadius;
	private final Display display;

	private OnColorChangedListener mListener;
	private int mInitialColor;
	private String key;

	public ColorPickerDialog(PreferenceActivity activity,
			OnColorChangedListener listener, int initialColor) {
		super(activity);

		display = activity.getWindowManager().getDefaultDisplay();
		centerX = display.getWidth() / 2;
		centerY = display.getHeight() / 2;
		centerRadius = Math.round((float) display.getWidth() * 0.1f);

		mListener = listener;
		mInitialColor = initialColor;
	}

	public ColorPickerDialog(PreferenceActivity activity,
			OnColorChangedListener listener, int initialColor, String key) {
		this(activity, listener, initialColor);
		this.key = key;
		if (key.equals("color_one") && initialColor == 0x00000000) {
			mInitialColor = DiscoSettings.DEFAULT_COLOR_1;
		} else if (key.equals("color_two") && initialColor == 0x00000000) {
			mInitialColor = DiscoSettings.DEFAULT_COLOR_2;
		} else if (key.equals("color_three") && initialColor == 0x00000000) {
			mInitialColor = DiscoSettings.DEFAULT_COLOR_3;
		} else {
			mInitialColor = initialColor;
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		OnColorChangedListener l = new OnColorChangedListener() {
			public void colorChanged(String key, int color) {
				mListener.colorChanged(key, color);
				dismiss();
			}
		};

		setContentView(new ColorPickerView(getContext(), l, mInitialColor));
		setTitle("Select a color, then push the middle.");
	}

	private class ColorPickerView extends View {
		private Paint mPaint;
		private Paint mCenterPaint;
		private final int[] mColors;
		private OnColorChangedListener mListener;

		ColorPickerView(Context c, OnColorChangedListener l, int color) {
			super(c);
			mListener = l;
			mColors = new int[] { 0xFFFF0000, 0xFFFF00FF, 0xFF0000FF,
					0xFF00FFFF, 0xFF00FF00, 0xFFFFFF00, 0xFFFF0000 };
			Shader s = new SweepGradient(0, 0, mColors, null);

			mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
			mPaint.setShader(s);
			mPaint.setStyle(Paint.Style.STROKE);
			mPaint.setStrokeWidth(32);

			mCenterPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
			mCenterPaint.setColor(color);
			mCenterPaint.setStrokeWidth(5);
		}

		private boolean mTrackingCenter;
		private boolean mHighlightCenter;

		@SuppressLint("DrawAllocation")
		@Override
		protected void onDraw(Canvas canvas) {
			float r = ((float) centerX * 0.7f) - mPaint.getStrokeWidth() * 0.5f;

			canvas.translate((float) getMeasuredWidth() / 2.0f,
					(float) getMeasuredHeight() / 2.0f);
			canvas.drawOval(new RectF(-r, -r, r, r), mPaint);
			canvas.drawCircle(0, 0, centerRadius, mCenterPaint);

			if (mTrackingCenter) {
				int c = mCenterPaint.getColor();
				mCenterPaint.setStyle(Paint.Style.STROKE);

				if (mHighlightCenter) {
					mCenterPaint.setAlpha(0xFF);
				} else {
					mCenterPaint.setAlpha(0x80);
				}
				canvas.drawCircle(0, 0,
						centerRadius + mCenterPaint.getStrokeWidth(),
						mCenterPaint);

				mCenterPaint.setStyle(Paint.Style.FILL);
				mCenterPaint.setColor(c);
			}
		}

		@Override
		protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
			setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
		}

		private int floatToByte(float x) {
			int n = java.lang.Math.round(x);
			return n;
		}

		private int pinToByte(int n) {
			if (n < 0) {
				n = 0;
			} else if (n > 255) {
				n = 255;
			}
			return n;
		}

		private int ave(int s, int d, float p) {
			return s + java.lang.Math.round(p * (d - s));
		}

		private int interpColor(int colors[], float unit) {
			if (unit <= 0) {
				return colors[0];
			}
			if (unit >= 1) {
				return colors[colors.length - 1];
			}

			float p = unit * (colors.length - 1);
			int i = (int) p;
			p -= i;

			// now p is just the fractional part [0...1) and i is the index
			int c0 = colors[i];
			int c1 = colors[i + 1];
			int a = ave(Color.alpha(c0), Color.alpha(c1), p);
			int r = ave(Color.red(c0), Color.red(c1), p);
			int g = ave(Color.green(c0), Color.green(c1), p);
			int b = ave(Color.blue(c0), Color.blue(c1), p);

			return Color.argb(a, r, g, b);
		}

		private int rotateColor(int color, float rad) {
			float deg = rad * 180 / 3.1415927f;
			int r = Color.red(color);
			int g = Color.green(color);
			int b = Color.blue(color);

			ColorMatrix cm = new ColorMatrix();
			ColorMatrix tmp = new ColorMatrix();

			cm.setRGB2YUV();
			tmp.setRotate(0, deg);
			cm.postConcat(tmp);
			tmp.setYUV2RGB();
			cm.postConcat(tmp);

			final float[] a = cm.getArray();

			int ir = floatToByte(a[0] * r + a[1] * g + a[2] * b);
			int ig = floatToByte(a[5] * r + a[6] * g + a[7] * b);
			int ib = floatToByte(a[10] * r + a[11] * g + a[12] * b);

			return Color.argb(Color.alpha(color), pinToByte(ir), pinToByte(ig),
					pinToByte(ib));
		}

		private static final float PI = 3.1415926f;

		@Override
		public boolean onTouchEvent(MotionEvent event) {
			float x = event.getX() - centerX;
			float y = event.getY() - centerY;
			boolean inCenter = java.lang.Math.sqrt(x * x + y * y) <= centerRadius * 2.0f;

			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				mTrackingCenter = inCenter;
				if (inCenter) {
					mHighlightCenter = true;
					invalidate();
					break;
				}
			case MotionEvent.ACTION_MOVE:
				if (mTrackingCenter) {
					if (mHighlightCenter != inCenter) {
						mHighlightCenter = inCenter;
						invalidate();
					}
				} else {
					float angle = (float) java.lang.Math.atan2(y, x);
					// need to turn angle [-PI ... PI] into unit [0....1]
					float unit = angle / (2 * PI);
					if (unit < 0) {
						unit += 1;
					}
					mCenterPaint.setColor(interpColor(mColors, unit));
					invalidate();
				}
				break;
			case MotionEvent.ACTION_UP:
				if (mTrackingCenter) {
					if (inCenter) {
						mListener.colorChanged(key, mCenterPaint.getColor());
					}
					mTrackingCenter = false; // so we draw w/o halo
					invalidate();
				}
				break;
			}
			return true;
		}
	}

}
