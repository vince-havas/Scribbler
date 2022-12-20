package randLines;

import java.awt.geom.Point2D;

import javax.management.AttributeNotFoundException;

import accessories.NoiseFunction;
import accessories.PRNG;
import accessories.Section;
import illustration.Page;

public class Caterpillars extends LinePainting {
	// generates short lines in given horizontal and vertical distributions with
	// direction and length following a noise function

	private NoiseFunction _nfn;
	private final int _gridStep = 400;
	private PRNG.Distribution _distributionX;
	private PRNG.Distribution _distributionY;

	public Caterpillars(PRNG.Distribution distribution_) {
		this(distribution_, distribution_);
	}

	public Caterpillars(PRNG.Distribution distributionX_, PRNG.Distribution distributionY_) {
		_distributionX = distributionX_;
		_distributionY = distributionY_;
	}

	@Override
	public void generateLines() {
		PRNG ng = PRNG.getInstance();
		_nfn = new NoiseFunction(Page._width / _gridStep + 1, (int) (Page._width * Page._pageRatio / _gridStep + 1));

		for (int ii = 0; ii < 4000; ii++) {
			Point2D base = new Point2D.Double(ng.nextInRange(_distributionX, Page._margin, Page._width - Page._margin),
					ng.nextInRange(_distributionY, Page._margin, Page._width * Page._pageRatio - Page._margin));
			try {
				NoiseFunction.GridValue gv = _nfn.interpolate(base.getX() / _gridStep, base.getY() / _gridStep);
				Section newLine = new Section(base, gv.get_angle(), gv.get_length());

				if (wellLocated(newLine))
					_lines.add(newLine);

			} catch (AttributeNotFoundException e) {
				System.out.println("\nCouldn't generate line section in Caterpillars.\n");
				e.printStackTrace();
			}
		}
	}
}
