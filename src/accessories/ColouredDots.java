package accessories;

import java.awt.geom.Point2D;

import illustration.Page.Hull;

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

	static public void shiftPoint(Point2D base_, Point2D diff_) {
		base_.setLocation(base_.getX() + diff_.getX(), base_.getY() + diff_.getY());
	}

	static public void rotateAroundOrigin(Point2D p_, double angle_) {
		double x = p_.getX() * Math.cos(angle_) - p_.getY() * Math.sin(angle_);
		double y = p_.getX() * Math.sin(angle_) + p_.getY() * Math.cos(angle_);
		p_.setLocation(x, y);
	}

	public Point2D getPosition() {
		return _position;
	}

	public int[] getRGB() {
		return _RGB;
	}

	@Override
	public Hull getHull() {
		Hull hk = new Hull(this.getPosition(), this.getPosition());
		return hk;
	}
}
