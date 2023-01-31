package base;

import accessories.PRNG;
import illustration.Page;
import randDots.DotDrawing;
import randDots.Rain;
import randDots.Salty;
import randLines.Buddies;
import randLines.Caterpillars;
import randLines.Krixkrax;
import randLines.LinePainting;

public class Main {

	public static void main(String[] args) {
		Page.hangyafoci("test_hangyafoci");

		LinePainting cp = new Buddies();
		cp.generateLines();
//		cp.print();
		Page.saveDrawing(cp, "test_buddies");

		cp = new Krixkrax();
		cp.generateLines();
//		cp.print();
		Page.saveDrawing(cp, "test_krixkrax");

		cp = new Caterpillars(PRNG.Distribution.UNIFORM);
		cp.generateLines();
//		cp.print();
		Page.saveDrawing(cp, "test_caterpillars");

		PRNG.stepSeed();
		DotDrawing dd = new Rain(Rain.RainType.DRIZZLE);
		Page.saveDrawing(dd, "test_rain_all");

		dd = new Rain(Rain.RainType.CENTRAL);
		Page.saveDrawing(dd, "test_rain_single");

		dd = new Rain(Rain.RainType.PAIR);
		Page.saveDrawing(dd, "test_rain_pair");

		dd = new Salty(PRNG.Distribution.IRWIN_HALL, PRNG.Distribution.POWER, DotDrawing.FadingType.WAVE);
		Page.saveDrawing(dd, "test_salty");

		System.out.println("\nDone");
	}
}
