package accessories;

import java.awt.geom.Point2D;

import illustration.Page.Hull;

public class Ellipse extends CurvedLine {
	private Point2D _centre;
	private double _xRadius;
	private double _yRadius;
	private double _inclination;
	private double _tgInc; // tangent of the inclination angle

	public Ellipse(Point2D centre_, double xRadius_, double yRadius_, double inclination_) {
		_centre = centre_;
		_xRadius = xRadius_;
		_yRadius = yRadius_;
		_inclination = inclination_;
		_tgInc = Math.tan(_inclination);
	}

	@Override
	public double getLimit() {
		return 2 * Math.PI;
	}

	@Override
	public Point2D getPoint(double t_) {
		Point2D out = new Point2D.Double(_xRadius * Math.cos(t_), _yRadius * Math.sin(t_));
		ColouredDots.rotateAroundOrigin(out, _inclination);
		ColouredDots.shiftPoint(out, _centre);
		return out;
	}

	@Override
	public Hull getHull() {
		Hull hk = new Hull();
		double t = Math.atan(_yRadius / _xRadius / _tgInc);
		hk.setTop(getPoint(t).getY());
		hk.setBottom(getPoint(t + Math.PI).getY());

		t = Math.atan(-_yRadius / _xRadius * _tgInc);
		hk.setLeft(getPoint(t + Math.PI).getX());
		hk.setRight(getPoint(t).getX());
		hk.sortHusk();
		return hk;
	}
}
