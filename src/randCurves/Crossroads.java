package randCurves;

import java.io.InvalidClassException;
import java.util.ArrayList;

import accessories.Ellipse;
import accessories.PRNG;
import illustration.Page;

public class Crossroads extends CurveGraphic {
	private int _nCurves;
	private int _longRadSize;
	private double _inclination;

	public Crossroads() {
		try {
			setUp(getDefaultArgs());
		} catch (InvalidClassException | IllegalArgumentException e) {
			e.printStackTrace();
		}
	}

	public Crossroads(ArrayList<Integer> args_) {
		try {
			setUp(args_);
		} catch (InvalidClassException | IllegalArgumentException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void generateCurves() {
		final PRNG ng = PRNG.getInstance();
		for (int ii = 0; _curvedLines.size() < _nCurves; ii++) {
			double longRad = ng.nextInRange(_longRadSize, 2 * _longRadSize);
			Ellipse attempt = new Ellipse(Page.getRandomPoint(), longRad, longRad * 0.04,
					Math.PI * (_inclination + ii % 2 * 0.2));
			if (Page.inMargin(attempt))
				_curvedLines.add(attempt);
		}
	}

	@Override
	public ArrayList<Integer> getDefaultArgs() throws InvalidClassException {
		ArrayList<Integer> defaultArgs = new ArrayList<>();
		defaultArgs.add(900);
		defaultArgs.add(100);
		defaultArgs.add(1);

		return defaultArgs;
	}

	@Override
	public void setUp(ArrayList<Integer> args_) throws InvalidClassException {
		if (args_.size() != 3)
			throw new IllegalArgumentException("Crossroads constructor called with invalid arguments");

		int ii = 0;
		_nCurves = args_.get(ii++);
		_longRadSize = args_.get(ii++);
		_inclination = args_.get(ii++) / 10.0;

		generateCurves();
	}
}
