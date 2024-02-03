package illustration;

import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.io.InvalidClassException;
import java.util.ArrayList;

import accessories.PaintStrokes;
import illustration.Page.Hull;

public abstract class Drawing {
	protected Point2D _centre;
	public ArrayList<PaintStrokes> _curves;

	public abstract void drawMe(Graphics2D g2_);

	public abstract ArrayList<Integer> getDefaultArgs() throws InvalidClassException;

	public abstract void setUp(ArrayList<Integer> args_) throws InvalidClassException, IllegalArgumentException;

	public void setUpSmall() throws InvalidClassException {
		throw new InvalidClassException("setUpSmall is unimplemented for this class");
	}

	public boolean implementsSmallSetup() {
		return false;
	}

	protected abstract void updateCurves();

	public Drawing() {
	}

	public Drawing(ArrayList<Integer> args_) {
	}

	public Drawing(Point2D centre_) {
		setCentre(centre_);
	}

	public int getStrokeWidth() {
		return 3;
	}

	public Point2D getCentre() {
		return _centre;
	}

	public void setCentre(Point2D centre_) {
		_centre = (Point2D) centre_.clone();
	}

	public Page.Hull getHull() {
		updateCurves();
		Hull hh = _curves.get(0).getHull();
		Page.Hull ret = new Hull(hh);
		for (PaintStrokes stroke : _curves) {
			Hull h = stroke.getHull();
			ret.annex(h);
		}

		return ret;
	}
}
