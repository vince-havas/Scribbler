package randLines;

import java.awt.geom.Point2D;
import java.io.InvalidClassException;
import java.util.ArrayList;

import javax.management.AttributeNotFoundException;

import accessories.NoiseFunction;
import accessories.PRNG;
import accessories.Section;
import illustration.Page;

public class Caterpillars extends LinePainting {
	// generates short lines in given horizontal and vertical distributions with
	// direction and length following a noise function

	private NoiseFunction _nfn;
	private int _gridStep;
	private int _nLines;
	private PRNG.Distribution _distributionX;
	private PRNG.Distribution _distributionY;

	public Caterpillars() {
		try {
			setUp(getDefaultArgs());
		} catch (InvalidClassException | IllegalArgumentException e) {
			e.printStackTrace();
		}
	}

	public Caterpillars(ArrayList<Integer> args_) {
		try {
			setUp(args_);
		} catch (InvalidClassException | IllegalArgumentException e) {
			e.printStackTrace();
		}
	}

	public Caterpillars(PRNG.Distribution distribution_) {
		this(distribution_, distribution_);
	}

	public Caterpillars(PRNG.Distribution distributionX_, PRNG.Distribution distributionY_) {
		this();
		_distributionX = distributionX_;
		_distributionY = distributionY_;
		generateLines();
	}

	@Override
	public void generateLines() {
		PRNG ng = PRNG.getInstance();
		_nfn = new NoiseFunction(Page.getWidth() / _gridStep + 1, Page.getHeight() / _gridStep + 1);

		_lines.clear();
		while (_lines.size() < _nLines) {
			Point2D base = new Point2D.Double(
					ng.nextInRange(_distributionX, Page.getMargin(), Page.getWidth() - Page.getMargin()),
					ng.nextInRange(_distributionY, Page.getMargin(), Page.getHeight() - Page.getMargin()));
			try {
				NoiseFunction.GridValue gv = _nfn.interpolate(base.getX() / _gridStep, base.getY() / _gridStep);
				Section newLine = new Section(base, gv.get_angle(), gv.get_length());

				if (wellLocated(newLine))
					_lines.add(newLine);

			} catch (AttributeNotFoundException e) {
				System.out.println("\nCouldn't generate line section in Caterpillars.\n");
				e.printStackTrace();
			}
		}
	}

	@Override
	public ArrayList<Integer> getDefaultArgs() throws InvalidClassException {
		ArrayList<Integer> defaultArgs = new ArrayList<>();
		defaultArgs.add(3000);
		defaultArgs.add(400);
		defaultArgs.add(2);
		defaultArgs.add(1);

		return defaultArgs;
	}

	@Override
	public void setUp(ArrayList<Integer> args_) throws InvalidClassException {
		if (args_.size() != 4)
			throw new IllegalArgumentException("Caterpillars constructor called with invalid arguments");

		int ii = 0;
		_nLines = args_.get(ii++);
		_gridStep = args_.get(ii++);
		_distributionX = PRNG.Distribution.valueOf(args_.get(ii++));
		_distributionY = PRNG.Distribution.valueOf(args_.get(ii++));

		generateLines();
	}
}
