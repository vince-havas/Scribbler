package illustration;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.imageio.ImageIO;

import accessories.PRNG;
import accessories.Section;
import randLines.LinePainting;

public class Page {
	static public double _pageRatio = 1.414;
	static public int _width = 2100, _margin = 50;
	static private final PRNG _ng = PRNG.getInstance();
	static private final Path _outputPath = Paths.get(".").toAbsolutePath().getParent().resolve("output");

	static public void saveDrawing(LinePainting painting_, String name_) {
		int height = (int) (_width * _pageRatio);

		BufferedImage bi = new BufferedImage(_width, height, BufferedImage.TYPE_INT_ARGB);

		Graphics2D g2 = bi.createGraphics();

		g2.setPaint(Color.white);
		g2.fillRect(0, 0, _width, height);
		g2.setPaint(Color.black);
		g2.setStroke(new BasicStroke(4));

		for (Section line : painting_.getLines()) {
			g2.draw(line.toLine());
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
		int height = (int) (_width * _pageRatio);
		int size = 5;

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

	static public int getMaxX() {
		return _width - _margin;
	}

	static public int getMaxY() {
		return (int) (_width * _pageRatio - _margin);
	}

	static public double getRandomX() {
		return _ng.nextInRange(_margin, getMaxX());
	}

	static public double getRandomY() {
		return _ng.nextInRange(_margin, getMaxY());
	}
}
