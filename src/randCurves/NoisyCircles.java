package randCurves;

import java.awt.geom.Point2D;
import java.io.InvalidClassException;
import java.util.ArrayList;

import accessories.CurvedLine;
import accessories.NoiseFunction;
import accessories.PRNG;
import illustration.Page;
import illustration.Page.Hull;
import illustration.Page.Hull.HullType;

public class NoisyCircles extends CurveGraphic {
	double[] _noise;
	double _baseRad;
	int _nCurves;
	int _nSegm;
	double _radStep;

	public NoisyCircles() {
	}

	public NoisyCircles(ArrayList<Integer> args_) {
		try {
			setUp(args_);
		} catch (InvalidClassException | IllegalArgumentException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void generateCurves() {
		final PRNG ng = PRNG.getInstance();
		_noise = new double[_nSegm];
		for (int jj = 0; jj < _nSegm; jj++) {
			_noise[jj] = 1.0 + ng.nextInRange(-0.25, 0.0);
		}

		for (int ii = 0; _curvedLines.size() < _nCurves; ii++) {
			NoisyCircle attempt = new NoisyCircle(_baseRad + ii * _radStep, ng.nextUnit() * 0.3);

			if (ng.nextBelow(9) < 8) // making a few circles disappear
				_curvedLines.add(attempt);
		}
		setCentre(Page.getCentre());
	}

	class NoisyCircle extends CurvedLine {
		double _rad;
		double _inclination;
		double _angleStep;

		NoisyCircle(double rad_, double inclination_) {
			_rad = rad_;
			_inclination = inclination_;
			_angleStep = getLimit() / _nSegm;
		}

		@Override
		public Hull getHull() {
			Hull out = new Hull();
			out.setType(HullType.CIRCULAR);
			out.setCentreX((int) _centre.getX());
			out.setCentreY((int) _centre.getY());
			out.setRadius((int) _rad);
			return out;
		}

		@Override
		public Point2D getPoint(double t_) {
			int segment = (int) (t_ / _angleStep);
			double localCoord = (t_ - _angleStep * segment) / _angleStep;
			double weight = NoiseFunction.GridValue.smoothing(localCoord,
					NoiseFunction.GridValue.SmoothingMethod.TRIGONOMETRIC);
			double radian = _rad * weight * _noise[segment] + _rad * (1 - weight) * _noise[(segment + 1) % _nSegm];
			Point2D out = new Point2D.Double(radian * Math.cos(t_ + _inclination) + _centre.getX(),
					radian * Math.sin(t_ + _inclination) + _centre.getY());
			return out;
		}

		@Override
		public double getLimit() {
			return 2 * Math.PI;
		}
	}

	@Override
	public int getStrokeWidth() {
		return 7;
	}

	@Override
	public ArrayList<Integer> getDefaultArgs() throws InvalidClassException {
		ArrayList<Integer> defaultArgs = new ArrayList<>();

		defaultArgs.add(20);
		defaultArgs.add(4);
		defaultArgs.add(11);
		defaultArgs.add(20);

		return defaultArgs;
	}

	@Override
	public void setUp(ArrayList<Integer> args_) throws InvalidClassException, IllegalArgumentException {
		if (args_.size() != 4)
			throw new IllegalArgumentException("NoisyCircles constructor called with invalid arguments");

		int ii = 0;
		_baseRad = args_.get(ii++);
		_nCurves = args_.get(ii++);
		_nSegm = args_.get(ii++);
		_radStep = args_.get(ii++);

		generateCurves();
	}

	@Override
	public void setUpSmall() {
		PRNG ng = PRNG.getInstance();
		_baseRad = ng.nextBelow(60);
		_nCurves = (int) ng.nextInRange(2, 7);
		_nSegm = (int) ng.nextInRange(6, 15);
		_radStep = ng.nextInRange(10, 30);

		generateCurves();
	}

	@Override
	public boolean implementsSmallSetup() {
		return true;
	}
}
