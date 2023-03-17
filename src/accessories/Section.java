package accessories;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

public class Section implements PaintStrokes {
	private Point2D _start;
	private Point2D _end;

	public Section(final Point2D start_, double angle_, double length_) {
		this(start_, generateRelativePoint(start_, angle_, length_));
	}

	public Section(final Point2D start_, final Point2D end_) {
		_start = (Point2D) start_.clone();
		_end = (Point2D) end_.clone();
	}

	public static Point2D generateRelativePoint(final Point2D start_, double angle_, double length_) {
		return new Point2D.Double(start_.getX() + length_ * Math.cos(angle_),
				start_.getY() + length_ * Math.sin(angle_));
	}

	public void shift(Point2D p_) {
		this.set_start(new Point2D.Double(this.get_start().getX() + p_.getX(), this.get_start().getY() + p_.getY()));
		this.set_end(new Point2D.Double(this.get_end().getX() + p_.getX(), this.get_end().getY() + p_.getY()));
	}

	public boolean inRange(double width_, double height_) {
		return this.inRange(0, 0, width_, height_);
	}

	public boolean inRange(double baseX_, double baseY_, double width_, double height_) {
		if (this._start.getX() < baseX_ || this._end.getX() < baseX_ || this._start.getX() > width_
				|| this._end.getX() > width_ || this._start.getY() < baseY_ || this._end.getY() < baseY_
				|| this._start.getY() > height_ || this._end.getY() > height_)
			return false;
		else
			return true;
	}

	public double getLenght() {
		final double dx = _end.getX() - _start.getX();
		final double dy = _end.getY() - _start.getY();
		return Math.sqrt(dx * dx + dy * dy);
	}

	public double getAngle() {
		return Math.atan2(_end.getY() - _start.getY(), _end.getX() - _start.getX());
	}

	public double getShift() {
		// line value at x = 0
		return _start.getY() - Math.tan(getAngle()) * _start.getX();
	}

	public double getValueAt(double parameter_) {
		return Math.tan(getAngle()) * parameter_ + getShift();
	}

	public boolean isAbove(Point2D point_) {
		// divides the plane by the line of this section, checks if the point is in the
		// upper half plane
		double projection = getValueAt(point_.getX());
		return projection < point_.getY();
	}

	public boolean onOneSide(Section other_) {
		// checks if the defining points of the other section are on the same side of
		// this section's line
		return isAbove(other_._end) == isAbove(other_._start);
	}

	public boolean isCrossing(Section other_) {
		// sections are crossing each other when mutually cross each others extended
		// lines
		return !onOneSide(other_) && !other_.onOneSide(this);
	}

	public String toString() {
		return "start: " + _start.toString() + ", end: " + _end.toString();
	}

	public Line2D toLine() {
		return new Line2D.Double(_start.getX(), _start.getY(), _end.getX(), _end.getY());
	}

	public Point2D get_start() {
		return _start;
	}

	public void set_start(Point2D _start) {
		this._start = (Point2D) _start.clone();
	}

	public Point2D get_end() {
		return _end;
	}

	public void set_end(Point2D _end) {
		this._end = (Point2D) _end.clone();
	}
}
