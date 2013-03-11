package com.adamantine.discowormhole;

/*
 * Access to this class's fields must be synchronized
 */

public class PreferencePasser {
	public static boolean prefsChanged = false;
	public static int flightSpeed;
	public static int numRings;
	public static int particleSpeed;
	public static boolean useSpaceDust;
	public static int backgroundColor;
	public static int color1;
	public static int color2;
	public static int color3;
	public static PreferencePasser singleton;

	public static PreferencePasser getLock() {
		if (singleton == null) {
			singleton = new PreferencePasser();
		}

		return singleton;
	}

	private PreferencePasser() {
	}

}
