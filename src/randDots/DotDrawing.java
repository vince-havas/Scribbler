package randDots;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashMap;

import accessories.ColouredDots;
import accessories.PaintStrokes;
import illustration.Drawing;

public abstract class DotDrawing extends Drawing {
	public enum FadingType {
		CONCENTRIC(0), CORNER(1), WAVE(2);

		private int _index;
		private static HashMap<Integer, FadingType> _map = new HashMap<>();

		FadingType(final int index_) {
			this._index = index_;
		}

		static {
			for (FadingType ft : FadingType.values()) {
				_map.put(ft._index, ft);
			}
		}

		public static FadingType valueOf(int ft_) {
			return (FadingType) _map.get(ft_);
		}

		public int getIndex() {
			return _index;
		}
	}

	final public double rootSquareSum(double a_, double b_) {
		return Math.sqrt(a_ * a_ + b_ * b_);
	}

	protected ArrayList<ColouredDots> _dots = new ArrayList<ColouredDots>();

	public abstract void generateDots();

	protected void updateCurves() {
		_curves = new ArrayList<PaintStrokes>();
		for (ColouredDots d : _dots)
			_curves.add(d);
	}

	@Override
	public void drawMe(Graphics2D g2_) {
		final int size = 6;
		for (ColouredDots dot : _dots) {
			int[] rgb = dot.getRGB();
			Point2D pos = dot.getPosition();
			g2_.setColor(new Color(rgb[0], rgb[1], rgb[2]));
			g2_.fillRect((int) pos.getX(), (int) pos.getY(), size, size);
		}
	}
}
