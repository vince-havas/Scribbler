package randDots;

import java.awt.geom.Point2D;
import java.io.InvalidClassException;
import java.util.ArrayList;

import accessories.ColouredDots;
import accessories.PRNG;
import illustration.Page;

public class Salty extends DotDrawing {
	// generates dots with positions and darkness following the given distributions

	private PRNG.Distribution _distX;
	private PRNG.Distribution _distY;
	private FadingType _fading;
	private int _nDots = 200000;

	public Salty() {
		try {
			setUp(getDefaultArgs());
		} catch (InvalidClassException | IllegalArgumentException e) {
			e.printStackTrace();
		}
	}

	public Salty(ArrayList<Integer> args_) {
		try {
			setUp(args_);
		} catch (InvalidClassException | IllegalArgumentException e) {
			e.printStackTrace();
		}
	}

	public Salty(PRNG.Distribution distX_, PRNG.Distribution distY_, FadingType fading_) {
		_distX = distX_;
		_distY = distY_;
		_fading = fading_;

		generateDots();
	}

	@Override
	public void generateDots() {
		for (int ii = 0; ii < _nDots; ii++) {
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
			weight = Page.getCentre().distance(coordX, coordY) / (Page.getHeight() / 2 - Page.getMargin());
			break;
		case CORNER:
			weight = rootSquareSum(coordX, coordY) / rootSquareSum(Page.getMaxX(), Page.getMaxX());
			break;
		case WAVE:
			// ratio of radii
			weight = Page.getCentre().distance(coordX, coordY) / (Page.getHeight() / 2 - Page.getMargin());
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

	@Override
	public ArrayList<Integer> getDefaultArgs() throws InvalidClassException {
		ArrayList<Integer> defaultArgs = new ArrayList<>();
		defaultArgs.add(1);
		defaultArgs.add(2);
		defaultArgs.add(2);
		defaultArgs.add(20000);

		return defaultArgs;
	}

	@Override
	public void setUp(ArrayList<Integer> args_) throws InvalidClassException, IllegalArgumentException {
		if (args_.size() != 4)
			throw new IllegalArgumentException("Salty constructor called with invalid arguments");

		int ii = 0;
		_distX = PRNG.Distribution.valueOf(args_.get(ii++));
		_distY = PRNG.Distribution.valueOf(args_.get(ii++));
		_fading = FadingType.valueOf(args_.get(ii++));
		_nDots = args_.get(ii++);

		generateDots();
	}
}
