package base;

import accessories.PRNG;
import accessories.PRNG.Distribution;
import illustration.Arranger;
import illustration.Page;
import randCurves.Crossroads;
import randCurves.NoisyCircles;
import randCurves.PearlString;
import randDots.DotDrawing;
import randDots.Rain;
import randDots.Salty;
import randLines.Buddies;
import randLines.Caterpillars;
import randLines.FantomGrid;
import randLines.Krixkrax;
import randLines.LinePainting;
import randLines.Spider;
import randLines.Tunel;
import tester.Tester;

public class Main {

	public static void main(String[] args) {
//		PRNG.stepSeed();
		/*-*/
		Page.hangyafoci("test_hangyafoci");

		Page.saveDrawing(new Tunel(), "test_tunel");

		Page.saveDrawing(new Crossroads(), "test_crossroads");

		Page.saveDrawing(new PearlString(), "test_pearls");

		Page.saveDrawing(new Krixkrax(), "test_krixkrax");
		Arranger<Krixkrax> arr4 = new Arranger<>(Krixkrax::new);
		Page.saveDrawing(arr4, "test_krixkrax_arr");

		Page.saveDrawing(new Caterpillars(Distribution.UNIFORM), "test_caterpillars_uniform");
		LinePainting lp = new Caterpillars(Distribution.IRWIN_HALL, Distribution.KUMARASWAMY);
		Page.saveDrawing(lp, "test_caterpillars_2");
		Arranger<Caterpillars> arr5 = new Arranger<>(Caterpillars::new);
		Page.saveDrawing(arr5, "test_caterpillars_arr");

		Arranger<Buddies> arr6 = new Arranger<>(Buddies::new);
		Page.saveDrawing(arr6, "test_buddies_arr");

		Page.saveDrawing(new Rain(Rain.RainType.DRIZZLE), "test_rain");
		Page.saveDrawing(new Arranger<>(Rain::new), "test_rain_arr");

		DotDrawing dd = new Salty(PRNG.Distribution.UNIFORM, PRNG.Distribution.POWER, DotDrawing.FadingType.CORNER);
		Page.saveDrawing(dd, "test_salty");
		Page.saveDrawing(new Arranger<>(Salty::new), "test_salty_arr");

		Page.saveDrawing(new Arranger<>(Spider::new), "test_spider");

		Page.saveDrawing(new Arranger<>(FantomGrid::new), "test_fantomGrid");

		Page.saveDrawing(new FantomGrid(), "test_fantomGrid2");

		Page.saveDrawing(new Tester(), "test_tester");

		Arranger<NoisyCircles> arr12 = new Arranger<>(NoisyCircles::new, Arranger.ArrangementType.PETRI_DISH, 151);
		Page.saveDrawing(arr12, "test_noisyCircles");

		System.out.println("\nDone");
	}
}
