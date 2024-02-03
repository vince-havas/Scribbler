package randCurves;

import java.awt.geom.Point2D;
import java.io.InvalidClassException;
import java.util.ArrayList;

import accessories.Ellipse;
import illustration.Page;

public class PearlString extends CurveGraphic {
	static private double _t;
	private double _boundary;
	private double _frequency;
	private double _rad1;
	private double _rad2;

	public PearlString() {
		try {
			setUp(getDefaultArgs());
		} catch (InvalidClassException | IllegalArgumentException e) {
			e.printStackTrace();
		}
	}

	public PearlString(ArrayList<Integer> args_) {
		try {
			setUp(args_);
		} catch (InvalidClassException | IllegalArgumentException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void generateCurves() {
		_t = 0;
		do {
			final double radius = Math.sin(_frequency * _t) * Math.sin(_frequency * _t) * _rad1 + _rad2;
			Ellipse attempt = new Ellipse(getMiddle(), radius, radius, 0.0);
			_curvedLines.add(attempt);
		} while (_t < _boundary);
	}

	private Point2D getMiddle() {
		double x, y;

		// circle
//		double x = Math.cos(_t) * 500 + 600;
//		double y = Math.sin(_t) * 500 + Page.getCenter().getY();

		// polynomial, _t in [-75,100]
//		double x = (0.1 * _t - 25) * (0.1 * _t - 20) * (-0.3 * _t + 10) * (-0.2 * _t + 19) * (0.2 * _t + 15) * 0.0003
//				- 350;
//		double y = (0.1 * _t - 15) * (0.1 * _t - 10) * (-0.3 * _t - 20) * (-0.2 * _t - 1) * (0.2 * _t - 5) * 0.0048;

		// trigonometric, _t in [-320,320]
//		x = 235 * (3 * Math.sin(0.01 * _t) + Math.sin(0.08 * _t));
		x = 250 * (Math.sin(0.01 * _t) + 2 * Math.sin(0.05 * _t) + Math.sin(0.2 * _t));
		y = -1000 * (Math.sin(0.05 * _t) + Math.sin(0.05 * _t) * Math.sin(0.05 * _t)) + 800;

		_t += 0.5;
		return new Point2D.Double(x + Page.getCentre().getX(), y + Page.getCentre().getY());
	}

	@Override
	public ArrayList<Integer> getDefaultArgs() throws InvalidClassException {
		ArrayList<Integer> defaultArgs = new ArrayList<>();
		defaultArgs.add(500);
		defaultArgs.add(1);
		defaultArgs.add(40);
		defaultArgs.add(10);

		return defaultArgs;
	}

	@Override
	public void setUp(ArrayList<Integer> args_) throws InvalidClassException {
		if (args_.size() != 4)
			throw new IllegalArgumentException("PearlString constructor called with invalid arguments");

		int ii = 0;
		_boundary = args_.get(ii++);
		_frequency = args_.get(ii++) / 10.0;
		_rad1 = args_.get(ii++);
		_rad2 = args_.get(ii++);

		generateCurves();
	}
}
