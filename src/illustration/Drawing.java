package illustration;

import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import accessories.PaintStrokes;
import illustration.Page.Hull;

public abstract class Drawing {
	private Point2D _centre;
	public ArrayList<PaintStrokes> _curves;

	public abstract void drawMe(Graphics2D g2_);

	public Drawing() {
	}

	public Drawing(Point2D centre_) {
		setCentre(centre_);
	}

	public Point2D getCentre() {
		return _centre;
	}

	public void setCentre(Point2D centre_) {
		_centre = (Point2D) centre_.clone();
	}

	public Page.Hull getHull() {
		Page.Hull ret = new Hull(_curves.get(0).getHull());
		for (PaintStrokes stroke : _curves)
			ret.annex(stroke.getHull());

		return ret;
	}
}
