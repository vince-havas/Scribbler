package randLines;

import java.awt.geom.Point2D;
import java.util.ArrayList;

import accessories.PRNG;
import accessories.Section;
import illustration.Page;

public class Buddies extends LinePainting {
	// generates lines that start from a previous line, but don't cross each other
	// otherwise

	@Override
	public void generateLines() {
		Point2D start = new Point2D.Double(900, 2800);
		Point2D end = new Point2D.Double(1100, 2800);
		_lines.add(new Section(start, end));
		PRNG ng = PRNG.getInstance();

		while (_lines.size() < 1500) {
			int randInd = getDrawnPoint(start);
			end = new Point2D.Double(Page.getRandomX(), Page.getRandomY());
			Section attempt = new Section(start, (ng.nextUnitIrwinHall(3) * 2 + 0.5) * Math.PI, ng.nextUnit() * 500);
			if (wellLocatedExcept(attempt, new ArrayList<>(_lines.subList(randInd, randInd + 1))))
				_lines.add(attempt);
		}
	}

	private int getDrawnPoint(Point2D start_) {
		PRNG ng = PRNG.getInstance();
		int baseIndex = ng.nextBelow(_lines.size());
		final Section randSec = _lines.get(baseIndex);
		double param = randSec.get_start().getX()
				+ ng.nextUnit() * (randSec.get_end().getX() - randSec.get_start().getX());
		start_.setLocation(param, randSec.getValueAt(param));

		return baseIndex;
	}

}
