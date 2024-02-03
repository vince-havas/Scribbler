package randLines;

import java.awt.geom.Point2D;
import java.io.InvalidClassException;
import java.util.ArrayList;
import java.util.HashMap;

import accessories.PRNG;
import accessories.Section;
import illustration.Page;

public class FantomGrid extends LinePainting {
	private enum Movement {
		VERTICAL, HORIZONTAL, DIAGONAL, RETURNING;

		private static boolean _increasing;
	}

	private enum CoverageType {
		RANDOM(0), WHOLE_COVERAGE(1), WALK_THROUGH(2);

		private int _index;
		private static HashMap<Integer, CoverageType> _map = new HashMap<>();

		CoverageType(final int index_) {
			this._index = index_;
		}

		static {
			for (CoverageType type : CoverageType.values()) {
				_map.put(type._index, type);
			}
		}

		public static CoverageType valueOf(int type_) {
			return (CoverageType) _map.get(type_);
		}
	}

	static private int _iMax; // number of points minus one horizontally
	static private int _jMax; // number of points minus one vertically
	static private int _gridStep;
	static private int _nLines;
	static private int _nLayers;
	static private Movement _direction;
	static private CoverageType _coverage;

	public FantomGrid() {
		try {
			setUp(getDefaultArgs());
		} catch (InvalidClassException e) {
			e.printStackTrace();
		}
	}

	public FantomGrid(ArrayList<Integer> args_) {
		try {
			setUp(args_);
		} catch (InvalidClassException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void generateLines() {
		for (int xCoord = 0; xCoord <= _iMax; xCoord++)
			for (int yCoord = 0; yCoord <= _jMax; yCoord++) {
				PointKey.getKey(xCoord, yCoord);
			}

		PointKey key = PointKey.getKey(0, 0);
		Point2D previousPoint = getPointInVicinity(key);
		switch (_coverage) {
		case RANDOM:
			for (int ii = 0; ii < _nLines; ii++) {
				key = getRandomNeighbour(key);
				Point2D nextPoint = getPointInVicinity(key);
				_lines.add(new Section(previousPoint, nextPoint));
				previousPoint = nextPoint;
			}
			break;
		case WALK_THROUGH:
			for (int ii = 0; ii < _nLines; ii++) {
				key = getNext(key);
				Point2D nextPoint = getPointInVicinity(key);
				_lines.add(new Section(previousPoint, nextPoint));
				previousPoint = nextPoint;
			}
			break;
		case WHOLE_COVERAGE:
			for (int ii = 0; ii < _nLayers; ii++) {
				_lines.addAll(getLayer());
			}
			break;
		default:
			throw new IllegalArgumentException("Uncovered coverage type in FantomGrid");
		}
	}

	private ArrayList<Section> getLayer() {
		ArrayList<Section> out = new ArrayList<Section>();
		HashMap<PointKey, Point2D> body = generateFullCoverage();
		for (int ii = 0; ii <= _iMax; ii += 2) {
			for (int jj = 0; jj <= _jMax; jj++) {
				PointKey base = PointKey.getKey(ii, jj);
				ArrayList<PointKey> allNeighbours = getAllNeighbours(PointKey.getKey(ii, jj));
				for (PointKey pk : allNeighbours) {
					if (pk._i == base._i && pk._j == base._j - 1)
						// edge with the previous point is already covered
						continue;

					out.add(new Section(body.get(base), body.get(pk)));
				}
			}
			if (ii < _iMax) {
				for (int jj = 1; jj <= _jMax; jj += 2) {
					out.add(new Section(body.get(PointKey.getKey(ii + 1, jj)),
							body.get(PointKey.getKey(ii + 1, jj - 1))));
					if (jj < _jMax)
						out.add(new Section(body.get(PointKey.getKey(ii + 1, jj)),
								body.get(PointKey.getKey(ii + 1, jj + 1))));
				}
			}
		}
		return out;
	}

	private HashMap<PointKey, Point2D> generateFullCoverage() {
		HashMap<PointKey, Point2D> out = new HashMap<PointKey, Point2D>();
		for (PointKey pk : PointKey.getAll()) {
			out.put(pk, getPointInVicinity(pk));
		}
		return out;
	}

	private Point2D getPointInVicinity(PointKey pk_) {
		PRNG ng = PRNG.getInstance();
		double x = pk_._i * _gridStep + ng.nextNormal(0.0, pk_.calcVariance(true));
		x += _centre.getX() - _iMax / 2.0 * _gridStep + pk_._iShift;
		double y = pk_._j * _gridStep + ng.nextNormal(0.0, pk_.calcVariance(false));
		y += _centre.getY() - _jMax / 2.0 * _gridStep + pk_._jShift;

		return new Point2D.Double(x, y);
	}

	private PointKey getNext(PointKey base_) {
		int i = base_._i, j = base_._j;

		switch (_direction) {
		case HORIZONTAL:
			if (j == _jMax) {
				if (i == _iMax && Movement._increasing) {
					j--;
					Movement._increasing = false;
					_direction = Movement.VERTICAL;
					break;
				} else if (i == 0 && !Movement._increasing) {
					i++;
					Movement._increasing = true;
					break;
				}
			}

			if (Movement._increasing)
				if (i == _iMax) {
					j++;
					Movement._increasing = false;
				} else
					i++;
			else {
				if (i == 0) {
					j++;
					Movement._increasing = true;
				} else
					i--;
			}
			break;
		case DIAGONAL:
			if (i == 0 && j == _jMax) {
				_direction = Movement.RETURNING;
				j--;
				break;
			}
			if (Movement._increasing) {
				j -= (int) Math.signum(i % 2 - 0.5);
				if (i == _iMax) {
					Movement._increasing = false;
				} else {
					i++;
				}
			} else {
				if (i == 0) {
					Movement._increasing = true;
					i++;
					j++;
				} else {
					j += (int) Math.signum(i % 2 - 0.5);
					i--;
				}
			}
			break;
		case RETURNING:
			if (j == 0) {
				Movement._increasing = true;
				_direction = Movement.HORIZONTAL;
				i++;
			} else
				j--;
			break;
		case VERTICAL:
			if (i == 0) {
				if (j == _jMax && Movement._increasing) {
					Movement._increasing = false;
					j--;
					break;
				} else if (j == 0 && !Movement._increasing) {
					Movement._increasing = true;
					_direction = Movement.DIAGONAL;
					i++;
					j++;
					break;
				}
			}
			if (Movement._increasing)
				if (j == _jMax) {
					i--;
					Movement._increasing = false;
				} else
					j++;
			else {
				if (j == 0) {
					i--;
					Movement._increasing = true;
				} else
					j--;
			}
			break;
		default:
			break;
		}

		return PointKey.getKey(i, j);
	}

	private ArrayList<PointKey> getAllNeighbours(PointKey base_) {
		ArrayList<PointKey> out = new ArrayList<PointKey>();
		for (int kk = 0; kk < 9; kk++) {
			if (kk == 4)
				// points aren't neighbours of themselves
				continue;

			int x = base_._i + kk % 3 - 1;
			int y = base_._j + kk / 3 - 1;
			if (x >= 0 && x <= _iMax && y >= 0 && y <= _jMax)
				out.add(PointKey.getKey(x, y));
		}

		return out;
	}

	private PointKey getRandomNeighbour(PointKey base_) {
		int i, j;
		PRNG ng = PRNG.getInstance();

		if (base_._i == 0 || base_._i == _iMax) {
			i = base_._i + ng.nextBinary() * (int) Math.signum(1.0 - base_._i);
		} else
			i = base_._i + ng.nextBelow(3) - 1;

		if (base_._j == 0 || base_._j == _jMax) {
			j = base_._j + ng.nextBinary() * (int) Math.signum(1.0 - base_._j);
		} else
			j = base_._j + ng.nextBelow(3) - 1;

		if (i == base_._i && j == base_._j) {
			if (ng.nextBinary() < 0.5)
				i += (int) Math.signum(_iMax / 2 - i);
			else
				j += (int) Math.signum(_jMax / 2 - j);
		}

		return PointKey.getKey(i, j);
	}

	@Override
	public int getStrokeWidth() {
		return 1;
	}

	@Override
	public ArrayList<Integer> getDefaultArgs() throws InvalidClassException {
		ArrayList<Integer> defaultArgs = new ArrayList<>();
		defaultArgs.add(9);
		defaultArgs.add(130);
		defaultArgs.add(1);

		return defaultArgs;
	}

	@Override
	public void setUp(ArrayList<Integer> args_) throws InvalidClassException, IllegalArgumentException {
		if (args_.size() != 3)
			throw new IllegalArgumentException("FantomGrid constructor called with invalid arguments");

		PointKey.clearAll();
		int ii = 0;
		_nLayers = args_.get(ii++);
		_gridStep = args_.get(ii++);
		_coverage = CoverageType.valueOf(args_.get(ii++));

		_iMax = (Page.getWidth() - 5 * Page.getMargin()) / _gridStep;
		_jMax = (Page.getHeight() - 5 * Page.getMargin()) / _gridStep;
		_centre = Page.getCentre();
		_direction = Movement.HORIZONTAL;
		Movement._increasing = true;

		switch (_coverage) {
		case RANDOM:
			_nLines = _nLayers * 1000;
			break;
		case WALK_THROUGH:
			_nLines = _nLayers * // for each layer
					(int) ((_iMax + 2) * _jMax // approximation for horizontal lines including connectors at the ends
							+ (_jMax + 2) * _iMax) // approximation for vertical lines including connectors at the ends
					+ 2 * _iMax * _jMax + 2 * _iMax; // approximation for diagonal lines including connectors at the
														// ends
			break;
		case WHOLE_COVERAGE:
			_nLines = -1; // logic is based on number of layers
			break;
		default:
			throw new IllegalArgumentException("Uncovered coverage type in FantomGrid");
		}

		generateLines();
	}

	static class PointKey {
		static private ArrayList<PointKey> _all;
		private int _i;
		private int _j;
		private int _iShift;
		private int _jShift;

		private PointKey(int i_, int j_) {
			_i = i_;
			_j = j_;

			PRNG ng = PRNG.getInstance();
			final int limit = 50;
			_iShift = ng.nextBelow(limit) - limit / 2;
			_jShift = ng.nextBelow(limit) - limit / 2;
		}

		public static ArrayList<PointKey> getAll() {
			return _all;
		}

		public static void clearAll() {
			_all = new ArrayList<PointKey>();
		}

		public ArrayList<PointKey> getAllNeighbours() {
			ArrayList<PointKey> out = new ArrayList<PointKey>();
			for (int ind = 0; ind < 9; ind++) {
				int i = _i + ind % 3 - 1;
				int j = _j + ind / 3 - 1;
				if (i > -1 && j > -1 && i <= _iMax && j <= _jMax && (i != 0 || j != 0))
					out.add(getKey(i, j));
			}
			return out;
		}

		static public PointKey getKey(int i_, int j_) {
			if (_all != null)
				for (PointKey key : _all)
					if (key._i == i_ && key._j == j_)
						return key;

			if (_all == null)
				_all = new ArrayList<PointKey>();

			PointKey newKey = new PointKey(i_, j_);
			_all.add(newKey);
			return newKey;
		}

		public double calcVariance(boolean forX_) {
			if (forX_)
				return Math.abs(Math.pow(_iMax / 2.0 - _i, 3.0)) * 4 + 20;
			else
				return Math.abs(Math.pow(_jMax / 2.0 - _j, 3.0)) + 20;
		}

		public String toString() {
			return _i + " - " + _j;
		}
	}
}
