package accessories;

import java.util.HashMap;

public class PRNG {
	// pseudo random number generator
	public enum Distribution {
		UNIFORM(0), IRWIN_HALL(1), POWER(2), KUMARASWAMY(3);

		private int _index;
		private static HashMap<Integer, Distribution> _map = new HashMap<>();

		Distribution(final int index_) {
			this._index = index_;
		}

		static {
			for (Distribution dist : Distribution.values()) {
				_map.put(dist._index, dist);
			}
		}

		public static Distribution valueOf(int dist_) {
			return (Distribution) _map.get(dist_);
		}

		public int getIndex() {
			return _index;
		}
	}

	private long _seed = 2087362089;
	private double _min = 0;
	private double _max = 255;
	private static PRNG _instance;

	private PRNG() {
		super();
	}

	public static PRNG getInstance() {
		if (_instance == null)
			_instance = new PRNG();

		return _instance;
	}

	public static PRNG getInstance(double min_, double max_) {
		if (_instance == null)
			_instance = new PRNG();

		_instance._min = min_;
		_instance._max = max_;
		return _instance;
	}

	public int nextBelow(int upperLimit_) {
		// random number between zero and upper limit - 1
		return (int) (hash() % upperLimit_);
	}

	public int nextBinary() {
		return (int) nextBelow(2);
	}

	public double nextUnitKumaraswamy() {
		// with inverse CDF method
		final double a = 0.001;
		final double b = 0.25;

		return Math.pow(1 - Math.pow(1 - nextUnit(), 1 / b), 1 / a);
	}

	public double nextUnitSquare() {
		final double power = 0.5;
		return Math.pow(nextUnit(), power);
	}

	public double nextIrwinHall() {
		return nextUnitIrwinHall(12) - 6;
	}

	public double nextUnitIrwinHall(int order_) {
		double out = 0;
		for (int ii = 0; ii < order_; ii++)
			out += nextUnit();

		return out / order_;
	}

	public double nextUnit() {
		return (double) hash() / Long.MAX_VALUE;
	}

	public double nextInDefaultRange() {
		return nextUnit() * (_max - _min) + _min;
	}

	public double nextInRange(double min_, double max_) {
		return nextInRange(Distribution.UNIFORM, min_, max_);
	}

	public double nextInRange(Distribution d_, double min_, double max_) {
		double out = 0;
		switch (d_) {
		case IRWIN_HALL:
			out = nextUnitIrwinHall(5);
			break;
		case POWER:
			out = nextUnitSquare();
			break;
		case KUMARASWAMY:
			out = nextUnitKumaraswamy();
			break;
		case UNIFORM:
			out = nextUnit();
			break;
		default:
			throw new IllegalArgumentException("Unsupported smoothing method:" + d_.toString());
		}

		return out * (max_ - min_) + min_;
	}

	public long hash() {
		long base = (long) 2747636419L;
		_seed ^= base;
		_seed *= base;
		_seed ^= _seed >> 16;
		_seed *= base;
		_seed ^= _seed >> 16;
		_seed *= base;
		return Math.abs(_seed);
	}

	public int nextLCG() {
		// Linear congruential generator
		// returns an integer in [0, 255] for RGB illustration
		// good parameter set
		final long modulus = 922337198929L;
		final long multiplier = 9237204283L;
		final long increment = 9237204469L;

		// weak parameter set
//		final long modulus = 515231;
//		final long multiplier = 9202;
//		final long increment = 9231;
		_seed = (_seed * multiplier + increment) % modulus;
		return (int) (((double) Math.abs(_seed) / modulus) * 255);
	}

	public double nextNormal(double mean_, double variance_) {
		if (variance_ < 0)
			throw new IllegalArgumentException("negative variance in PRNG");

		return nextStandardNormal() * Math.sqrt(variance_) + mean_;
	}

	public double nextStandardNormal() {
		// pseudo random numbers with normal distribution with Box–Muller method
		return Math.sqrt(-2 * Math.log(nextUnit())) * Math.cos(2 * Math.PI * nextUnit());
	}

	public static void stepSeed() {
		getInstance()._seed++;
	}
}
