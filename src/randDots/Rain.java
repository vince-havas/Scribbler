package randDots;

import java.awt.geom.Point2D;
import java.util.ArrayList;

import accessories.ColouredDots;
import accessories.PRNG;
import illustration.Page;

public class Rain extends DotDrawing {
	// generates sets of central rings of dots

	public enum RainType {
		CENTRAL, PAIR, DRIZZLE
	}

	private ArrayList<Droplet> _dropps;
	public RainType _type;

	public Rain(RainType type_) {
		_type = type_;
		_dropps = new ArrayList<Droplet>();
		regenerateDroplets();
		generateDots();
	}

	public void regenerateDroplets() {
		_dropps.clear();
		switch (_type) {
		case CENTRAL:
			// one fading drop symmetrically in the middle
			// rings are fading from centre towards the side
			_dropps.add(generateCentralDroplet());
			break;
		case DRIZZLE:
			// based on 'step' the canvas is distributed to cells
			// one drop is generated randomly within every cell
			// only a few rings are visible
			PRNG ng = PRNG.getInstance();
			final int step = 200;
			for (int ii = Page._margin; ii < Page.getMaxX(); ii += step)
				for (int jj = Page._margin; jj < Page.getMaxY(); jj += step) {
					Point2D p = new Point2D.Double(ii + ng.nextBelow(step), jj + ng.nextBelow(step));
					_dropps.add(new SmallDroplet(p));
				}
			break;
		case PAIR:
			// two drops "collide"
			// rings are fading from centre towards the side
			_dropps.add(generateBigDroplet());
			_dropps.add(generateBigDroplet());
			break;
		default:
			throw new IllegalArgumentException("Unhandled rain type: " + _type);
		}
	}

	public Droplet generateCentralDroplet() {
		return generateBigDroplet(Page.getCentre());
	}

	public Droplet generateBigDroplet() {
		Point2D p = new Point2D.Double(Page.getRandomX(), Page.getRandomY());
		return generateBigDroplet(p);
	}

	public Droplet generateBigDroplet(Point2D pos_) {
		int halfSize = (int) (Page._width * Page._pageRatio / 2 - 2 * Page._margin);
		Droplet out = new BigDroplet(pos_, halfSize / 15);

		return out;
	}

	@Override
	public void generateDots() {
		for (Droplet drop : _dropps)
			drop.generateDots();
	}

	class Droplet {
		protected Point2D _center;
		protected int _nRings;
		protected double _radiusStep;
		protected double _angleFreq;
		protected int _firstRingRadius;
		protected double _uncertainty;

		private void generateDots() {
			PRNG ng = PRNG.getInstance();
			for (int ring = 0; ring < _nRings; ring++) {
				int rgb = -9999; // represents the shade of gray
				switch (_type) {
				case CENTRAL:
				case PAIR:
					rgb = Math.max(0, 255 * ring / _nRings - 50);
//					rgb = 0;
					break;
				case DRIZZLE:
					final double order = 10;
					double weight = (_firstRingRadius + ring * _radiusStep)
							/ (_firstRingRadius + _radiusStep * _nRings / 2);
					weight = 1 - Math.exp(-order * (weight - 1) * (weight - 1));
					rgb = (int) (150 * weight + 105);
					break;
				}

				for (double angle = 0; angle < Math.PI * 2; angle += Math.PI / _angleFreq) {
					double uncertainR = (_firstRingRadius + ring * _radiusStep);
					uncertainR *= (1 + uncertainR * _uncertainty * ng.nextStandardNormal() / 1000);
					double uncertainAngle = angle * (1 + _uncertainty * ng.nextStandardNormal());
					Point2D p = new Point2D.Double(uncertainR * Math.cos(uncertainAngle) + _center.getX(),
							uncertainR * Math.sin(uncertainAngle) + _center.getY());

					int rgb_temp;
					do {
						rgb_temp = (int) (rgb + ng.nextNormal(0, 5));
					} while (rgb_temp < 0 || rgb_temp > 250);

					if (Page.inMargin(p))
						_dots.add(new ColouredDots(new int[] { rgb_temp, rgb_temp, rgb_temp }, p));
				}
			}
		}
	}

	class BigDroplet extends Droplet {
		public BigDroplet(Point2D pos_, double step_) {
			_center = (Point2D) pos_.clone();
			_nRings = 20;
			_radiusStep = step_;
			_angleFreq = 1400;
			_firstRingRadius = 5;
			_uncertainty = 0.03;
		}
	}

	class SmallDroplet extends Droplet {
		public SmallDroplet(Point2D pos_) {
			_center = (Point2D) pos_.clone();
			PRNG ng = PRNG.getInstance();
			_nRings = ng.nextBelow(2) + 2;
			_firstRingRadius = ng.nextBelow(50);
			_radiusStep = ng.nextBelow(30) + 20;
			_angleFreq = 800;
			_uncertainty = 0.03;
		}
	}
}
