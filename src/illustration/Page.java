package illustration;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.imageio.ImageIO;

import accessories.ColouredDots;
import accessories.PRNG;
import accessories.PaintStrokes;
import accessories.Painting;
import accessories.Section;
import randDots.DotDrawing;
import randLines.LinePainting;

public class Page {
	static public double _pageRatio = 1.414;
	static public int _width = 2100, _margin = 50;
	static private final PRNG _ng = PRNG.getInstance();
	static private final Path _outputPath = Paths.get(".").toAbsolutePath().getParent().resolve("output");

	static public void saveDrawing(Painting painting_, String name_) {
		int height = (int) (_width * _pageRatio);

		BufferedImage bi = new BufferedImage(_width, height, BufferedImage.TYPE_INT_ARGB);

		Graphics2D g2 = bi.createGraphics();

		g2.setPaint(Color.white);
		g2.fillRect(0, 0, _width, height);
		g2.setPaint(Color.black);
		g2.setStroke(new BasicStroke(4));

		final int size = 5;

		if (painting_ instanceof LinePainting) {
			for (PaintStrokes line : painting_.getPaintStrokes())
				g2.draw(((Section) line).toLine());

		} else if (painting_ instanceof DotDrawing) {
			for (PaintStrokes dot : painting_.getPaintStrokes()) {
				ColouredDots cd = (ColouredDots) dot;
				int[] rgb = cd.get_RGB();
				Point2D pos = cd.get_position();
				g2.setColor(new Color(rgb[0], rgb[1], rgb[2]));
				g2.fillRect((int) pos.getX(), (int) pos.getY(), size, size);
			}
		}

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

	static public Point2D getCenter() {
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
}
