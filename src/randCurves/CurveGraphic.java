package randCurves;

import java.awt.Graphics2D;
import java.util.ArrayList;

import accessories.CurvedLine;
import accessories.PaintStrokes;
import illustration.Drawing;

public abstract class CurveGraphic extends Drawing {
	public ArrayList<CurvedLine> _curvedLines = new ArrayList<CurvedLine>();

	public abstract void generateCurves();

	public void updateCurves() {
		_curves = new ArrayList<PaintStrokes>();
		for (CurvedLine c : _curvedLines)
			_curves.add(c);
	}

	@Override
	public void drawMe(Graphics2D g2_) {
		for (CurvedLine curve : _curvedLines) {
			g2_.draw(curve.drawMe());
		}
	}
}
