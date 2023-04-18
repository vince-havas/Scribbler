package randLines;

import java.awt.geom.Point2D;

import accessories.Section;
import illustration.Page;

public class Krixkrax extends LinePainting {
	// generates straight lines that don't intersect each other

	public Krixkrax() {
		generateLines();
	}

	@Override
	public void generateLines() {
		while (_lines.size() < 1000) {
			Section attempt = new Section(new Point2D.Double(Page.getRandomX(), Page.getRandomY()),
					new Point2D.Double(Page.getRandomX(), Page.getRandomY()));
			if (wellLocated(attempt))
				_lines.add(attempt);
		}
	}
}
