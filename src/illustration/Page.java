package illustration;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;

import javax.imageio.ImageIO;

import accessories.PRNG;
import accessories.PaintStrokes;
import illustration.Page.Hull.HullType;

public class Page {
	static private final double _pageRatio = 1.414;
	static private final int _width = 2100, _margin = 100, _height;
	static private final PRNG _ng = PRNG.getInstance();
	static private final Path _outputPath = Paths.get(".").toAbsolutePath().getParent().resolve("output/temp");

	static {
		_height = (int) (_width * _pageRatio);
	}

	public static void saveDrawing(Drawing painting_, String name_) {
		BufferedImage bi = new BufferedImage(_width, _height, BufferedImage.TYPE_INT_ARGB);

		Graphics2D g2 = bi.createGraphics();

		g2.setPaint(Color.white);
		g2.fillRect(0, 0, _width, _height);
		g2.setPaint(Color.black);
		g2.setStroke(new BasicStroke(painting_.getStrokeWidth()));

		painting_.drawMe(g2);

		try {
			ImageIO.write(bi, "PNG", _outputPath.resolve(name_ + ".PNG").toFile());
			System.out.println(name_ + ".PNG saved");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	static public void hangyafoci(String name_) {
		// colours every pixel with a random shade of grey
		// if repeating patterns appear in the result,
		// the PRNG's period isn't long enough
		final int size = 5;

		BufferedImage bi = new BufferedImage(_width, _height, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2 = bi.createGraphics();

		g2.setPaint(Color.white);
		g2.fillRect(0, 0, _width, _height);

		for (int xx = _margin; xx < _width - _margin; xx += size) {
			for (int yy = _margin; yy < _height - _margin; yy += size) {
				int colour = _ng.nextLCG();
				g2.setColor(new Color(colour, colour, colour));
				g2.fillRect(xx, yy, size, size);
			}
		}

		try {
			ImageIO.write(bi, "PNG", _outputPath.resolve(name_ + ".PNG").toFile());
			System.out.println(name_ + ".PNG saved");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static boolean inMargin(Point2D p_) {
		return p_.getX() > _margin && p_.getX() < _width - _margin && p_.getY() > _margin
				&& p_.getY() < _height - _margin;
	}

	public static boolean inMargin(Hull husk_) {
		if (husk_._type == HullType.CIRCULAR) {
			boolean out = husk_.getCentreX() - husk_.getRadius() > _margin;
			out &= husk_.getCentreX() + husk_.getRadius() < _width - _margin;
			out &= husk_.getCentreY() - husk_.getRadius() > _margin;
			out &= husk_.getCentreY() + husk_.getRadius() < _height - _margin;
			return out;
		} else
			return husk_.getTop() > _margin && husk_.getLeft() > _margin && husk_.getBottom() < _height - _margin
					&& husk_.getRight() < _width - _margin;
	}

	public static boolean inPetriDish(Hull husk_) {
		if (husk_._type != HullType.CIRCULAR)
			throw new Error("Only circular objects are investigated in Petri dishes");

		double dx = husk_.getCentreX() - getCentre().getX();
		double dy = husk_.getCentreY() - getCentre().getY();
		double rad = Math.sqrt(dx * dx + dy * dy) + husk_.getRadius();

		return rad < _width / 2.0 - _margin;
	}

	public static <T extends PaintStrokes> boolean inMargin(T curve) {
		return inMargin(curve.getHull());
	}

	public static int getWidth() {
		return _width;
	}

	public static int getHeight() {
		return _height;
	}

	public static double getPageRatio() {
		return _pageRatio;
	}

	public static int getMargin() {
		return _margin;
	}

	static public Point2D getCentre() {
		return new Point2D.Double(_width / 2, _height / 2);
	}

	static public int getMaxX() {
		return _width - _margin;
	}

	static public int getMaxY() {
		return _height - _margin;
	}

	static public double getRandomX() {
		return getRandomX(PRNG.Distribution.UNIFORM);
	}

	static public double getRandomX(PRNG.Distribution dist_) {
		return _ng.nextInRange(dist_, _margin, getMaxX());
	}

	static public double getRandomY() {
		return getRandomY(PRNG.Distribution.UNIFORM);
	}

	static public double getRandomY(PRNG.Distribution dist_) {
		return _ng.nextInRange(dist_, _margin, getMaxY());
	}

	static public Point2D getRandomPoint() {
		return getRandomPoint(PRNG.Distribution.UNIFORM, PRNG.Distribution.UNIFORM);
	}

	static public Point2D getRandomPoint(PRNG.Distribution distX_, PRNG.Distribution distY_) {
		return new Point2D.Double(getRandomX(distX_), getRandomY(distY_));
	}

	public static class Hull {
		private enum Side {
			TOP, LEFT, BOTTOM, RIGHT, CENTRE_X, CENTRE_Y, RADIUS;
		}

		public enum HullType {
			RECTANGULAR, CIRCULAR;
		}

		private HashMap<Side, Integer> _hull;
		private HullType _type;

		public Hull() {
			_type = HullType.RECTANGULAR;
			_hull = new HashMap<Side, Integer>();
			for (Side s : Side.values())
				_hull.put(s, null);
		}

		public Hull(Hull other_) {
			this();
			_type = other_.getType();
			for (Side s : Side.values())
				_hull.put(s, other_._hull.get(s));

			sortHusk();
		}

		public Hull(int top_, int left_, int bottom_, int right_) {
			this();
			_type = HullType.RECTANGULAR;
			setTop(top_);
			setLeft(left_);
			setBottom(bottom_);
			setRight(right_);
			sortHusk();
		}

		public Hull(Point2D p1_, Point2D p2_) {
			this();
			_type = HullType.RECTANGULAR;
			setTop((int) Math.min(p1_.getY(), p2_.getY())); // vertical axis is turned over in printing
			setLeft((int) Math.min(p1_.getX(), p2_.getX()));
			setBottom((int) Math.max(p1_.getY(), p2_.getY()));
			setRight((int) Math.max(p1_.getX(), p2_.getX()));
		}

		public void sortHusk() {
			if (_type == HullType.RECTANGULAR) {
				if (getTop() > getBottom()) { // vertical axis is turned over in printing
					int temp = getBottom();
					setBottom(getTop());
					setTop(temp);
				}
				if (getLeft() > getRight()) {
					int temp = getLeft();
					setLeft(getRight());
					setRight(temp);
				}
			}
		}

		public boolean touches(Hull other_) {
			if (_type == HullType.CIRCULAR && other_._type == HullType.CIRCULAR) {
				final double dx = getCentreX() - other_.getCentreX();
				final double dy = getCentreY() - other_.getCentreY();
				final double r = getRadius() + other_.getRadius();
				return dx * dx + dy * dy < r * r;
			}

			throw new Error("To be implemented");
		}

		public void annex(Hull other_) {
			if (_type == HullType.RECTANGULAR && other_._type == HullType.RECTANGULAR) {
				if (getTop() > other_.getTop())
					setTop(other_.getTop());
				if (getLeft() > other_.getLeft())
					setLeft(other_.getLeft());
				if (getBottom() < other_.getBottom())
					setBottom(other_.getBottom());
				if (getRight() < other_.getRight())
					setRight(other_.getRight());
			} else if (_type != other_._type) {
				throw new Error("to be implemented");
			} else {

				int dx = other_.getCentreX() - getCentreX();
				int dy = other_.getCentreY() - getCentreY();
				int centerDist = (int) Math.sqrt(dx * dx + dy * dy);
				if (centerDist * centerDist < 3) {
					// curve type hulls have common center, just use the larger radius
					setRadius(Math.max(getRadius(), other_.getRadius()));
				} else {
					// - the new center is in the middle of a section with length
					// - radius1 + centerDistance + radius2
					// - the new radius half of the above length
					double ratio = (other_.getRadius() + centerDist - getRadius()) / 2.0 / centerDist;
					setCentreX((int) (getCentreX() + ratio * dx));
					setCentreY((int) (getCentreY() + ratio * dy));
					setRadius((getRadius() + other_.getRadius() + centerDist) / 2);
				}
			}
		}

		public void convertType(HullType newType_) {
			if (_type == HullType.CIRCULAR) {
				_type = HullType.RECTANGULAR;
				throw new Error("to be implemented");
			}
		}

		public int getTop() {
			return _type == HullType.RECTANGULAR ? _hull.get(Side.TOP)
					: _hull.get(Side.CENTRE_Y) - _hull.get(Side.RADIUS);
		}

		public void setTop(int limes_) {
			_hull.put(Side.TOP, limes_);
		}

		public int getLeft() {
			return _type == HullType.RECTANGULAR ? _hull.get(Side.LEFT)
					: _hull.get(Side.CENTRE_X) - _hull.get(Side.RADIUS);
		}

		public void setLeft(int limes_) {
			_hull.put(Side.LEFT, limes_);
		}

		public int getBottom() {
			return _type == HullType.RECTANGULAR ? _hull.get(Side.BOTTOM)
					: _hull.get(Side.CENTRE_Y) + _hull.get(Side.RADIUS);
		}

		public void setBottom(int limes_) {
			_hull.put(Side.BOTTOM, limes_);
		}

		public int getRight() {
			return _type == HullType.RECTANGULAR ? _hull.get(Side.RIGHT)
					: _hull.get(Side.CENTRE_X) + _hull.get(Side.RADIUS);
		}

		public void setRight(int limes_) {
			_hull.put(Side.RIGHT, limes_);
		}

		public int getCentreX() {
			return _hull.get(Side.CENTRE_X);
		}

		public void setCentreX(int x_) {
			_hull.put(Side.CENTRE_X, x_);
		}

		public int getCentreY() {
			return _hull.get(Side.CENTRE_Y);
		}

		public void setCentreY(int y_) {
			_hull.put(Side.CENTRE_Y, y_);
		}

		public int getRadius() {
			return _hull.get(Side.RADIUS);
		}

		public void setRadius(int rad_) {
			_hull.put(Side.RADIUS, rad_);
		}

		public HullType getType() {
			return _type;
		}

		public void setType(HullType type_) {
			_type = type_;
		}
	}
}
