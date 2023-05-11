package randCurves;

import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import accessories.CurvedLine;
import accessories.PaintStrokes;
import illustration.Drawing;

public abstract class CurveGraphic extends Drawing {
	public CurveGraphic(Point2D centre_) {
		super();
		setCentre(centre_);
	}

	public ArrayList<CurvedLine> _curves = new ArrayList<CurvedLine>();

	public abstract void generateCurves();

	public ArrayList<PaintStrokes> getPaintStrokes() {
		ArrayList<PaintStrokes> out = new ArrayList<PaintStrokes>();
		for (CurvedLine c : _curves)
			out.add(c);
		return out;
	}

	@Override
	public void drawMe(Graphics2D g2_) {
		for (CurvedLine curve : _curves) {
			g2_.draw(curve.drawMe());
		}
	}
}
