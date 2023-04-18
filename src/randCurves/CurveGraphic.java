package randCurves;

import java.util.ArrayList;

import accessories.CurvedLine;
import accessories.PaintStrokes;

public abstract class CurveGraphic {
	public ArrayList<CurvedLine> _curves = new ArrayList<CurvedLine>();

	public abstract void generateCurves();

	public ArrayList<PaintStrokes> getPaintStrokes() {
		ArrayList<PaintStrokes> out = new ArrayList<PaintStrokes>();
		for (CurvedLine c : _curves)
			out.add(c);
		return out;
	}
}
