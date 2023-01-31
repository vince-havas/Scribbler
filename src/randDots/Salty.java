package randDots;

import java.awt.geom.Point2D;

import accessories.ColouredDots;
import accessories.PRNG;
import illustration.Page;

public class Salty extends DotDrawing {
	// generates dots with positions and darkness following the given distributions

	private PRNG.Distribution _distX;
	private PRNG.Distribution _distY;
	private FadingType _fading;

	public Salty(PRNG.Distribution distX_, PRNG.Distribution distY_, FadingType fading_) {
		_distX = distX_;
		_distY = distY_;
		_fading = fading_;

		generateDots();
	}

	@Override
	public void generateDots() {
		for (int ii = 0; ii < 200000; ii++) {
			double coordX = Page.getRandomX(_distX);
			double coordY = Page.getRandomY(_distY);
			final Point2D p = new Point2D.Double(coordX, coordY);
			_dots.add(new ColouredDots(getFading(coordX, coordY), p));
		}
	}

	private int[] getFading(double coordX, double coordY) {
		double weight = Double.NaN;

		switch (_fading) {
		case CONCENTRIC:
			weight = Page.getCenter().distance(coordX, coordY) / (Page._width * Page._pageRatio / 2 - Page._margin);
			break;
		case CORNER:
			weight = rootSquareSum(coordX, coordY) / rootSquareSum(Page.getMaxX(), Page.getMaxX());
			break;
		case WAVE:
			// ratio of radii
			weight = Page.getCenter().distance(coordX, coordY) / (Page._width * Page._pageRatio / 2 - Page._margin);
			final double order = 10;
			weight = 1 - Math.exp(-(order * weight - order / 2) * (order * weight - order / 2));
			break;
		default:
			throw new IllegalArgumentException("Unhandled fading type: " + _fading);
		}

		int greyShade = (int) (weight * 255);
		PRNG ng = PRNG.getInstance();
		greyShade += (int) ng.nextNormal(0, 10);
		greyShade = Math.min(255, Math.max(0, greyShade));
		return new int[] { greyShade, greyShade, greyShade };
	}

}
