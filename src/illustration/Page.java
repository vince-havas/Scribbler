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

public class Page {
	static public double _pageRatio = 1.414;
	static public int _width = 2100, _margin = 100;
	static private final PRNG _ng = PRNG.getInstance();
	static private final Path _outputPath = Paths.get(".").toAbsolutePath().getParent().resolve("output/temp");

	public static void saveDrawing(Drawing painting_, String name_) {
		int height = (int) (_width * _pageRatio);

		BufferedImage bi = new BufferedImage(_width, height, BufferedImage.TYPE_INT_ARGB);

		Graphics2D g2 = bi.createGraphics();

		g2.setPaint(Color.white);
		g2.fillRect(0, 0, _width, height);
		g2.setPaint(Color.black);
		g2.setStroke(new BasicStroke(4));

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
		// if repeating patterns appear in the result the PRNG has small period
		final int height = (int) (_width * _pageRatio);
		final int size = 5;

		BufferedImage bi = new BufferedImage(_width, height, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2 = bi.createGraphics();
		PRNG ng = PRNG.getInstance();

		g2.setPaint(Color.white);
		g2.fillRect(0, 0, _width, height);

		for (int xx = _margin; xx < _width - _margin; xx += size) {
			for (int yy = _margin; yy < height - _margin; yy += size) {
				int colour = ng.nextLCG();
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

	public static boolean inMargin(Point2D p) {
		return p.getX() > _margin && p.getX() < _width - _margin && p.getY() > _margin
				&& p.getY() < _width * _pageRatio - _margin;
	}

	public static boolean inMargin(Hull husk_) {
		return husk_.getTop() > _margin && husk_.getLeft() > _margin
				&& husk_.getBottom() < _width * _pageRatio - _margin && husk_.getRight() < _width - _margin;
	}

	public static <T extends PaintStrokes> boolean inMargin(T curve) {
		return inMargin(curve.getHull());
	}

	static public Point2D getCentre() {
		return new Point2D.Double(_width / 2, _width * _pageRatio / 2);
	}

	static public int getMaxX() {
		return _width - _margin;
	}

	static public int getMaxY() {
		return (int) (_width * _pageRatio - _margin);
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
			TOP, LEFT, BOTTOM, RIGHT;
		}

		private HashMap<Side, Double> _hull;

		public Hull() {
			_hull = new HashMap<Side, Double>();
		}

		public Hull(Hull other_) {
			this.setTop(other_.getTop());
			this.setLeft(other_.getLeft());
			this.setBottom(other_.getBottom());
			this.setRight(other_.getRight());
			sortHusk();
		}

		public Hull(double top_, double left_, double bottom_, double right_) {
			this();
			this.setTop(top_);
			this.setLeft(left_);
			this.setBottom(bottom_);
			this.setRight(right_);
			sortHusk();
		}

		public Hull(Point2D p1_, Point2D p2_) {
			this.setTop(Math.min(p1_.getY(), p2_.getY())); // vertical axis is turned around in printing
			this.setLeft(Math.min(p1_.getX(), p2_.getX()));
			this.setBottom(Math.max(p1_.getY(), p2_.getY()));
			this.setRight(Math.max(p1_.getX(), p2_.getX()));
		}

		public void sortHusk() {
			if (getTop() > getBottom()) { // vertical axis is turned around in printing
				double temp = getBottom();
				setBottom(getTop());
				setTop(temp);
			}
			if (getLeft() > getRight()) {
				double temp = getLeft();
				setLeft(getRight());
				setRight(temp);
			}
		}

		public void annex(Hull other_) {
			if (this.getTop() > other_.getTop())
				setTop(other_.getTop());
			if (this.getLeft() > other_.getLeft())
				setLeft(other_.getLeft());
			if (this.getBottom() < other_.getBottom())
				setBottom(other_.getBottom());
			if (this.getRight() < other_.getRight())
				setRight(other_.getRight());
		}

		public double getTop() {
			return _hull.get(Side.TOP);
		}

		public void setTop(double limes_) {
			_hull.put(Side.TOP, limes_);
		}

		public double getLeft() {
			return _hull.get(Side.LEFT);
		}

		public void setLeft(double limes_) {
			_hull.put(Side.LEFT, limes_);
		}

		public double getBottom() {
			return _hull.get(Side.BOTTOM);
		}

		public void setBottom(double limes_) {
			_hull.put(Side.BOTTOM, limes_);
		}

		public double getRight() {
			return _hull.get(Side.RIGHT);
		}

		public void setRight(double limes_) {
			_hull.put(Side.RIGHT, limes_);
		}
	}
}
