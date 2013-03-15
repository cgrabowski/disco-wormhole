package com.adamantine.discowormhole;

import java.io.IOException;
import java.util.Scanner;

public class LogcatReader implements Runnable {
	Scanner sc;
	Thread t;
	String line;
	String[] splits;

	LogcatReader() {
		sc = new Scanner(System.in);
		t = new Thread(this, "scanner thread");
		System.out.println("Logcat Reader Active");
		t.start();
	}

	@Override
	public void run() {
		if (System.in == null) {
			try {
				throw new Exception();
			} catch (Exception e) {
				e.printStackTrace();
				System.exit(-1);
			}
		}
		while (true) {
			line = sc.nextLine();
			System.out.println(line);
			if (line.equals("")) {
				continue;
			}
			splits = line.split(":|,");
			if (splits.length != 3) {
				System.out.println("splits length is not 3 : length is "
						+ splits.length);
				for (String s : splits) {
					System.out.println(s);
				}
				System.out.println("splits outputted");
				continue;
			}
			splits = line.split("[(]");
			if (!splits[0].equals("I/WormholePrefsChange")) {
				System.out.println("wrong tag: " + splits[0]);
				continue;
			}
			splits = line.split(": |, ");
			synchronized (PreferencePasser.getLock()) {
				if (splits[1].equals("color1")) {
					PreferencePasser.color1 = Integer.parseInt(splits[2]);
				} else if (splits[1].equals("color2")) {
					PreferencePasser.color2 = Integer.parseInt(splits[2]);
				} else if (splits[1].equals("color3")) {
					PreferencePasser.color3 = Integer.parseInt(splits[2]);
				} else if (splits[1].equals("flightSpeed")) {
					PreferencePasser.flightSpeed = Integer.parseInt(splits[2]);
				} else if (splits[1].equals("numRings")) {
					PreferencePasser.numRings = Integer.parseInt(splits[2]);
				} else if (splits[1].equals("particleSpeed")) {
					PreferencePasser.particleSpeed = Integer
							.parseInt(splits[2]);
				} else if (splits[1].equals("stretchX")) {
					PreferencePasser.stretchX = Integer.parseInt(splits[2]);
				} else if (splits[1].equals("stretchY")) {
					PreferencePasser.stretchY = Integer.parseInt(splits[2]);
				} else {
					try {
						throw new IOException();
					} catch (IOException e) {
						System.out.println("no match found for: " + splits[1]);
					}
				}
				PreferencePasser.prefsChanged = true;
			}

		}
	}
}
