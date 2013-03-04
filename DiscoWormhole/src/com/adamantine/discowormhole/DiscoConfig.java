package com.adamantine.discowormhole;

/*
 * All access to this class's fields must be synchronized
 */
public class DiscoConfig {
	public static boolean prefsChanged = false;
	public static int flightSpeed;
	public static int numRings;
	public static boolean useColors;
	public static boolean useSpaceDust;
	public static DiscoConfig singleton;

	public static DiscoConfig getInstance() {
		if (singleton == null) {
			singleton = new DiscoConfig();
		}
		return singleton;
	}

	private DiscoConfig() {

	}
	
}
