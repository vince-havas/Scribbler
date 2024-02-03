package accessories;

import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;

public abstract class CurvedLine implements PaintStrokes {
	final static int _nStep = 100;

	public abstract Point2D getPoint(double t_);

	public abstract double getLimit(); // running parameter is defined in [0, upperLimit]

	public GeneralPath drawMe() {
		double t = 0.0;
		Point2D point = getPoint(t);
		GeneralPath gp = new GeneralPath();
		gp.moveTo(point.getX(), point.getY());

		double limit = getLimit();
		for (; t < limit; t += limit / _nStep) {
			point = getPoint(t);
			gp.lineTo(point.getX(), point.getY());
		}

		gp.closePath();
		return gp;
	}
}
