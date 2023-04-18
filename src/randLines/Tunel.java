package randLines;

import java.awt.geom.Point2D;

import accessories.Section;
import illustration.Page;

public class Tunel extends LinePainting {
	private double _radius;
	final private double _ratio = 0.90;
	private int _nEdges; // number of edges
	static private double _t;
	static private boolean _isHypnotic;
	// if true, circles are drawn along trigonometric or polynomial curves with
	// oscillating radii,
	// otherwise rotating polygons are drawn around a fixed centre

	public Tunel(boolean periodicRadius_) {
		_isHypnotic = periodicRadius_;
		generateLines();
	}

	@Override
	public void generateLines() {
		double boundary = 500;
		_t = 0;
		_radius = Page._width;
		_nEdges = _isHypnotic ? 700 : 7;
		double edgeLength = Double.NaN;
		double angleShift = _isHypnotic ? 0 : Math.PI / 8;
		boolean enough = false;
		do {
			final double angleStep = 2 * Math.PI / _nEdges;
			if (_isHypnotic)
				_radius = Math.sin(0.1 * _t) * Math.sin(0.1 * _t) * 40 + 10;
			else {
				edgeLength = Double.NaN;
			}

			final Point2D centre = getCentre();
			for (double angle = 0; angle < 2 * Math.PI - angleStep / 2; angle += angleStep) {
				double shiftedAngle = angleShift + angle;
				Section attempt = new Section(
						new Point2D.Double(_radius * Math.cos(shiftedAngle), _radius * Math.sin(shiftedAngle)),
						new Point2D.Double(_radius * Math.cos(shiftedAngle + angleStep),
								_radius * Math.sin(shiftedAngle + angleStep)));
				attempt.shift(centre);
				_lines.add(attempt);
				if (!_isHypnotic && Double.isNaN(edgeLength))
					edgeLength = attempt.getLenght();
			}
			_radius *= _ratio;
			angleShift *= 1.1;
			enough = _isHypnotic ? _t < boundary : edgeLength > 5;
		} while (enough);
	}

	private Point2D getCentre() {
		double x, y;

		if (_isHypnotic) {
			// circle
//			double x = Math.cos(_t) * 500 + 600;
//			double y = Math.sin(_t) * 500 + Page.getCenter().getY();

			// polynomial, _t in [-75,100]
//			double x = (0.1 * _t - 25) * (0.1 * _t - 20) * (-0.3 * _t + 10) * (-0.2 * _t + 19)
//			* (0.2 * _t + 15) * 0.0003 - 350;
//			double y = (0.1 * _t - 15) * (0.1 * _t - 10) * (-0.3 * _t - 20) * (-0.2 * _t - 1)
//			* (0.2 * _t - 5) * 0.0048;

			// trigonometric, _t in [-320,320]
//			x = 235 * (3 * Math.sin(0.01 * _t) + Math.sin(0.08 * _t));
			x = 250 * (Math.sin(0.01 * _t) + 2 * Math.sin(0.05 * _t) + Math.sin(0.2 * _t));
			y = -1000 * (Math.sin(0.05 * _t) + Math.sin(0.05 * _t) * Math.sin(0.05 * _t)) + 800;

			_t += 0.5;
		} else {
			// constant
			x = 200;
			y = -400;
		}
		return new Point2D.Double(x + Page.getCentre().getX(), y + Page.getCentre().getY());
	}
}
