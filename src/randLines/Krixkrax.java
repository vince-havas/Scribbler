package randLines;

import java.awt.geom.Point2D;
import java.io.InvalidClassException;
import java.util.ArrayList;

import accessories.Section;
import illustration.Page;

public class Krixkrax extends LinePainting {
	// generates straight lines that don't intersect each other

	private int _nLines;

	public Krixkrax() {
		try {
			setUp(getDefaultArgs());
		} catch (InvalidClassException e) {
			e.printStackTrace();
		}
	}

	public Krixkrax(ArrayList<Integer> args_) {
		try {
			setUp(args_);
		} catch (InvalidClassException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void generateLines() {
		while (_lines.size() < _nLines) {
			Section attempt = new Section(new Point2D.Double(Page.getRandomX(), Page.getRandomY()),
					new Point2D.Double(Page.getRandomX(), Page.getRandomY()));
			if (wellLocated(attempt))
				_lines.add(attempt);
		}
	}

	@Override
	public ArrayList<Integer> getDefaultArgs() throws InvalidClassException {
		ArrayList<Integer> defaultArgs = new ArrayList<>();
		defaultArgs.add(1000);

		return defaultArgs;
	}

	@Override
	public void setUp(ArrayList<Integer> args_) throws InvalidClassException, IllegalArgumentException {
		if (args_.size() != 1)
			throw new IllegalArgumentException("Krixkrax constructor called with invalid arguments");

		int ii = 0;
		_nLines = args_.get(ii++);

		generateLines();
	}
}
