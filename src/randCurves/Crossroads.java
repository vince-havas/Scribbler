package randCurves;

import java.awt.geom.Point2D;

import accessories.Ellipse;
import accessories.PRNG;
import illustration.Page;

public class Crossroads extends CurveGraphic {
	public Crossroads() {
		this(Page.getCentre());
	}

	public Crossroads(Point2D centre_) {
		super(centre_);
		generateCurves();
	}

	@Override
	public void generateCurves() {
		final PRNG ng = PRNG.getInstance();
		for (int ii = 0; _curves.size() < 900; ii++) {
			double longRad = ng.nextInRange(100, 200);
			Ellipse attempt = new Ellipse(Page.getRandomPoint(), longRad, longRad * 0.04,
					Math.PI * (0.1 + ii % 2 * 0.2));
			if (Page.inMargin(attempt))
				_curves.add(attempt);
		}
	}
}
