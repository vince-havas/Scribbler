package accessories;

import java.awt.geom.Point2D;
import java.util.HashMap;

import javax.management.AttributeNotFoundException;

public class NoiseFunction {
	enum Coord {
		X, Y
	}

	private HashMap<GridPoint, GridValue> _grid = new HashMap<GridPoint, GridValue>();
	private HashMap<Coord, Integer> _boundaries = new HashMap<Coord, Integer>();

	public NoiseFunction(int width_, int height_) {
		_boundaries.put(Coord.X, width_);
		_boundaries.put(Coord.Y, height_);

		PRNG ng = PRNG.getInstance();

		for (int ii = 0; ii <= width_; ii++)
			for (int jj = 0; jj <= height_; jj++)
				_grid.put(new GridPoint(ii, jj), new GridValue(ng));
	}

	public GridValue interpolate(Point2D point_) throws AttributeNotFoundException {
		return interpolate(point_.getX(), point_.getY());
	}

	public GridValue interpolate(double x_, double y_) throws AttributeNotFoundException {
		final int leftNeighbourX = (int) x_;
		final int rightNeighbourX = Math.min(leftNeighbourX + 1, this.getBoundary(Coord.X));
		final int lowerNeighbourY = (int) y_;
		final int upperNeighbourY = Math.min(lowerNeighbourY + 1, this.getBoundary(Coord.Y));

		GridValue weightedLower = GridValue.weightGrids(x_ - leftNeighbourX,
				getAtPoint(leftNeighbourX, lowerNeighbourY), getAtPoint(rightNeighbourX, lowerNeighbourY));
		GridValue weightedUpper = GridValue.weightGrids(x_ - leftNeighbourX,
				getAtPoint(leftNeighbourX, upperNeighbourY), getAtPoint(rightNeighbourX, upperNeighbourY));

		GridValue out = GridValue.weightGrids(y_ - lowerNeighbourY, weightedLower, weightedUpper);

		return out;
	}

	public int getBoundary(Coord xy_) {
		if (_boundaries.get(Coord.X) == null || _boundaries.get(Coord.Y) == null) {
			_boundaries.put(Coord.X, -1);
			_boundaries.put(Coord.Y, -1);
			for (GridPoint point : _grid.keySet()) {
				_boundaries.put(Coord.X, Math.max(point._x, _boundaries.get(Coord.X)));
				_boundaries.put(Coord.Y, Math.max(point._y, _boundaries.get(Coord.Y)));
			}
		}

		return _boundaries.get(xy_);
	}

	public GridValue getAtPoint(int x_, int y_) throws AttributeNotFoundException {
		GridValue out_ = null;
		for (GridPoint gp : _grid.keySet())
			if (gp.rightThere(x_, y_))
				out_ = _grid.get(gp);

		if (out_ == null)
			throw new AttributeNotFoundException("No grid value found at {" + x_ + ", " + y_ + "}");

		return out_;
	}

	public static class GridValue {
		public enum SmoothingMethod {
			LINEAR, POWER, TRIGONOMETRIC
		}

		private double _angle;
		private double _length;
		private static final double _maxLength = 50;
		private static final double _minLength = 20;

		public double get_angle() {
			return _angle;
		}

		public double get_length() {
			return _length;
		}

		public GridValue(double angle_, double lenght_) {
			_angle = angle_;
			_length = lenght_;
		}

		public GridValue(final PRNG ng_) {
			this(ng_.nextUnit() * 2 * Math.PI, ng_.nextUnit() * _maxLength + _minLength);
		}

		public static GridValue weightGrids(double localCoord_, GridValue first_, GridValue second_) {
			double weight = smoothing(localCoord_, SmoothingMethod.LINEAR);
			GridValue out = new GridValue(first_._angle * weight + second_._angle * (1 - weight),
					first_._length * weight + second_._length * (1 - weight));
			return out;
		}

		public static double smoothing(double distance_, SmoothingMethod sm_) {
			switch (sm_) {
			case LINEAR:
				return 1 - distance_;
			case POWER:
				return 1 - (3 * distance_ * distance_ - 2 * Math.pow(distance_, 3));
			case TRIGONOMETRIC:
				return 1 - (1 - Math.cos(distance_ * Math.PI)) / 2;
			default:
				throw new IllegalArgumentException("Unsupported smoothing method:" + sm_.toString());
			}
		}

		public String tosString() {
			return "angle: " + _angle + ", length: " + _length;
		}
	}

	static class GridPoint {
		private int _x;
		private int _y;

		public GridPoint(int x_, int y_) {
			_x = x_;
			_y = y_;
		}

		public boolean rightThere(int x_, int y_) {
			if (x_ == this._x && y_ == this._y)
				return true;
			else
				return false;
		}
	}
}
