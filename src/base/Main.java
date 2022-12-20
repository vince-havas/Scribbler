package base;

import accessories.PRNG;
import illustration.Page;
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

		System.out.println("\n\nDone");
	}
}
