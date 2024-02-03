package accessories;

import java.awt.geom.Point2D;

import illustration.Page.Hull;
import illustration.Page.Hull.HullType;

public class Ellipse extends CurvedLine {
	private Point2D _centre;
	private double _xRadius;
	private double _yRadius;
	private double _inclination;

	public Ellipse(Point2D centre_, double xRadius_, double yRadius_, double inclination_) {
		_centre = centre_;
		_xRadius = xRadius_;
		_yRadius = yRadius_;
		_inclination = inclination_;
	}

	@Override
	public double getLimit() {
		return 2 * Math.PI;
	}

	@Override
	public Point2D getPoint(double t_) {
		return getPointInVicinity(t_, 0.0, 0.0);
	}

	public Point2D getPointInVicinity(double t_, double randR_, double randAng_) {
		// parameters rand* represent variances of radius and angle respectively
		PRNG rg = PRNG.getInstance();
		double randR = rg.nextInRange(1.0 - randR_, 1.0 + randR_);
		double randAng = rg.nextNormal(1.0, randAng_);
		Point2D out = new Point2D.Double(_xRadius * randR * Math.cos(t_ * randAng),
				_yRadius * randR * Math.sin(t_ * randAng));
		ColouredDots.rotateAroundOrigin(out, _inclination);
		ColouredDots.shiftPoint(out, _centre);
		return out;
	}

	@Override
	public Hull getHull() {
		Hull hk = new Hull();
		hk.setType(HullType.CIRCULAR);
		hk.setCentreX((int) _centre.getX());
		hk.setCentreY((int) _centre.getY());
		hk.setRadius((int) Math.max(_xRadius, _yRadius));

		/*-
		 * limit of the ellipse in Cartesian coordinates for RECTANGLE type husk
		double tgInc = Math.tan(_inclination);
		double t = Math.atan(_yRadius / _xRadius / tgInc);
		hk.setTop((int) getPoint(t).getY());
		hk.setBottom((int) getPoint(t + Math.PI).getY());
		
		t = Math.atan(-_yRadius / _xRadius * tgInc);
		hk.setLeft((int) getPoint(t + Math.PI).getX());
		hk.setRight((int) getPoint(t).getX());
		hk.sortHusk();*/

		return hk;
	}
}
