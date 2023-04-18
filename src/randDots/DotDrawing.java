package randDots;

import java.util.ArrayList;

import accessories.ColouredDots;
import accessories.PaintStrokes;

public abstract class DotDrawing {
	public enum FadingType {
		CONCENTRIC, CORNER, WAVE
	}

	final public double rootSquareSum(double a_, double b_) {
		return Math.sqrt(a_ * a_ + b_ * b_);
	}

	protected ArrayList<ColouredDots> _dots = new ArrayList<ColouredDots>();

	public abstract void generateDots();

	public ArrayList<PaintStrokes> getPaintStrokes() {
		ArrayList<PaintStrokes> out = new ArrayList<PaintStrokes>();
		for (ColouredDots d : _dots)
			out.add(d);
		return out;
	}
}
