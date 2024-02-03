package randLines;

import java.awt.geom.Point2D;
import java.io.InvalidClassException;
import java.util.ArrayList;

import accessories.Section;
import illustration.Page;

public class Tunel extends LinePainting {
	private int _angVel; // reciprocal of angular velocity
	private double _angAcc; // angular acceleration
	private int _nEdges; // number of edges
	private int _nShapes; // number of polygons. if negative, draw until smallest edge is invisible
	private double _radius;
	private double _ratio;
	private int _xStep; // velocity of centre
	private int _yStep; // velocity of centre
	private Point2D _centre;

	public Tunel() {
		try {
			setUp(getDefaultArgs());
		} catch (InvalidClassException | IllegalArgumentException e) {
			e.printStackTrace();
		}
	}

	public Tunel(ArrayList<Integer> args_) {
		try {
			setUp(args_);
		} catch (InvalidClassException | IllegalArgumentException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void generateLines() {
		double edgeLength = Double.NaN;
		double angleShift = 2 * Math.PI / _angVel;
		boolean notDone = true;
		int n = 0;
		do {
			final double angleStep = 2 * Math.PI / _nEdges;
			edgeLength = Double.NaN;

			for (double angle = 0; angle < 2 * Math.PI - angleStep / 2; angle += angleStep) {
				double shiftedAngle = angleShift + angle;
				Section attempt = new Section(
						new Point2D.Double(_radius * Math.cos(shiftedAngle), _radius * Math.sin(shiftedAngle)),
						new Point2D.Double(_radius * Math.cos(shiftedAngle + angleStep),
								_radius * Math.sin(shiftedAngle + angleStep)));
				attempt.shift(_centre);
				_lines.add(attempt);
				if (Double.isNaN(edgeLength))
					edgeLength = attempt.getLenght();
			}
			_radius *= _ratio;
			angleShift *= _angAcc;
			_centre.setLocation(_centre.getX() + _xStep, _centre.getY() + _yStep);
			n++;
			notDone = _nShapes <= 0 ? edgeLength > 5 : n < _nShapes;
		} while (notDone);
	}

	@Override
	public ArrayList<Integer> getDefaultArgs() {
		ArrayList<Integer> defaultArgs = new ArrayList<>();
		defaultArgs.add(16);
		defaultArgs.add(11);
		defaultArgs.add(7);
		defaultArgs.add(-1);
		defaultArgs.add(Page.getWidth()); // TODO width private?
		defaultArgs.add(9);
		defaultArgs.add(0);
		defaultArgs.add(0);
		defaultArgs.add((int) (Page.getCentre().getX() + 200));
		defaultArgs.add((int) (Page.getCentre().getY() - 400));

		return defaultArgs;
	}

	@Override
	public void setUp(ArrayList<Integer> args_) throws InvalidClassException, IllegalArgumentException {
		if (args_.size() != 8 && args_.size() != 10)
			throw new IllegalArgumentException("Tunel constructor called with invalid arguments");

		int ii = 0;
		_angVel = args_.get(ii++);
		_angAcc = args_.get(ii++) / 10.0;
		_nEdges = args_.get(ii++);
		_nShapes = args_.get(ii++);
		_radius = args_.get(ii++);
		_ratio = args_.get(ii++) / 10.0;
		_xStep = args_.get(ii++);
		_yStep = args_.get(ii++);

		if (args_.size() == 10)
			_centre = new Point2D.Double(args_.get(ii++), args_.get(ii++));
		else
			_centre = Page.getCentre();

		generateLines();
	}
}
