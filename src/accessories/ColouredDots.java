package accessories;

import java.awt.geom.Point2D;

public class ColouredDots implements PaintStrokes {
	private int[] _RGB;
	private Point2D _position;

	public ColouredDots(int[] RGB_, Point2D pos_) {
		if (RGB_.length != 3)
			throw new IllegalArgumentException("Invalid number of components for RGB: " + RGB_);

		_RGB = new int[3];
		for (int ii = 0; ii < 3; ii++)
			_RGB[ii] = RGB_[ii];

		_position = (Point2D) pos_.clone();
	}

	static public Point2D shiftPoint(Point2D base_, Point2D diff_) {
		Point2D out = new Point2D.Double(base_.getX() + diff_.getX(), base_.getY() + diff_.getY());
		return out;
	}

	public Point2D get_position() {
		return _position;
	}

	public int[] get_RGB() {
		return _RGB;
	}
}
