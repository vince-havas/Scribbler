package randLines;

import java.awt.geom.Point2D;
import java.io.InvalidClassException;
import java.util.ArrayList;

import accessories.PRNG;
import accessories.Section;
import illustration.Page;

public class Buddies extends LinePainting {
	// generates lines that start from a previous line, but don't cross each other
	private int _nLines; // number of lines
	private Point2D _start;
	private Point2D _end;

	public Buddies() {
		try {
			setUp(getDefaultArgs());
		} catch (InvalidClassException | IllegalArgumentException e) {
			e.printStackTrace();
		}
	}

	public Buddies(ArrayList<Integer> args_) {
		try {
			setUp(args_);
		} catch (InvalidClassException | IllegalArgumentException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void generateLines() {
		_lines.add(new Section(_start, _end));
		PRNG ng = PRNG.getInstance();

		while (_lines.size() < _nLines) {
			int randInd = getDrawnPoint(_start);
			_end = new Point2D.Double(Page.getRandomX(), Page.getRandomY());
			Section attempt = new Section(_start, (ng.nextUnitIrwinHall(3) * 2 + 0.5) * Math.PI, ng.nextUnit() * 500);
			if (wellLocatedExcept(attempt, new ArrayList<>(_lines.subList(randInd, randInd + 1))))
				_lines.add(attempt);
		}
	}

	private int getDrawnPoint(Point2D start_) {
		PRNG ng = PRNG.getInstance();
		int baseIndex = ng.nextBelow(_lines.size());
		final Section randSec = _lines.get(baseIndex);
		double param = randSec.getStart().getX()
				+ ng.nextUnit() * (randSec.getEnd().getX() - randSec.getStart().getX());
		start_.setLocation(param, randSec.getValueAt(param));

		return baseIndex;
	}

	public ArrayList<Integer> getDefaultArgs() throws InvalidClassException {
		ArrayList<Integer> defaultArgs = new ArrayList<>();
		defaultArgs.add(1500);
		defaultArgs.add(900);
		defaultArgs.add(2800);
		defaultArgs.add(1100);
		defaultArgs.add(2800);

		return defaultArgs;
	}

	@Override
	public void setUp(ArrayList<Integer> args_) throws InvalidClassException, IllegalArgumentException {
		if (args_.size() != 5)
			throw new IllegalArgumentException("Buddies constructor called with invalid arguments");

		int ii = 0;
		_nLines = args_.get(ii++);
		_start = new Point2D.Double(args_.get(ii++), args_.get(ii++));
		_end = new Point2D.Double(args_.get(ii++), args_.get(ii++));

		generateLines();
	}
}
