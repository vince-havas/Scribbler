package randLines;

import java.awt.geom.Point2D;
import java.io.InvalidClassException;
import java.util.ArrayList;
import accessories.Ellipse;
import accessories.Section;
import illustration.Page;

public class Spider extends LinePainting {
	// generates a closed loop of lines and connects every nod with an apex

	private int _nEdges; // number of edges on the loop
	private double _randR; // randomness wrt radius
	private double _randAng; // randomness wrt angle
	private int _diffX; // distance from centre, X
	private int _diffY; // distance from centre, Y

	public Spider() {
		try {
			setUp(getDefaultArgs());
		} catch (InvalidClassException e) {
			e.printStackTrace();
		}
	}

	public Spider(ArrayList<Integer> args_) {
		try {
			setUp(args_);
		} catch (InvalidClassException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void generateLines() {
		Ellipse spine = new Ellipse(Page.getCentre(), Page.getWidth() / 2.5, Page.getHeight() / 2.5, 0.0);
		// the guide line of the closed loop

		Point2D next, previous = spine.getPointInVicinity(0, _randR, _randAng);

		while (!Page.inMargin(previous))
			previous = spine.getPointInVicinity(0, _randR, _randAng);

		for (double ang = Math.PI * 2.0 / _nEdges; ang < Math.PI * 2.0; ang += Math.PI * 2.0 / _nEdges) {
			next = spine.getPointInVicinity(ang, _randR, _randAng);
			Section attempt = new Section(next, previous);
			while (!attempt.inRange()) {
				next = spine.getPointInVicinity(ang, _randR, _randAng);
				attempt = new Section(next, previous);
			}
			_lines.add(attempt);
			previous = next;
		}

		_lines.add(new Section(_lines.get(0).getEnd(), previous));

		ArrayList<Section> connectors = new ArrayList<Section>();
		final Point2D core = new Point2D.Double(Page.getCentre().getX() + _diffX, Page.getCentre().getY() + _diffY);
		for (Section sec : _lines) {
			connectors.add(new Section(core, sec.getStart()));
		}

		_lines.addAll(connectors);
	}

	@Override
	public ArrayList<Integer> getDefaultArgs() throws InvalidClassException {
		ArrayList<Integer> defaultArgs = new ArrayList<>();
		defaultArgs.add(77);
		defaultArgs.add(400);
		defaultArgs.add(2);
		defaultArgs.add(-400);
		defaultArgs.add(-600);

		return defaultArgs;
	}

	@Override
	public void setUp(ArrayList<Integer> args_) throws InvalidClassException, IllegalArgumentException {
		if (args_.size() != 5)
			throw new IllegalArgumentException("Spider constructor called with invalid arguments");

		_centre = Page.getCentre();
		int ii = 0;
		_nEdges = args_.get(ii++);
		_randR = args_.get(ii++) / 1000.0;
		_randAng = args_.get(ii++) / 1000.0;
		_diffX = args_.get(ii++);
		_diffY = args_.get(ii++);

		generateLines();
	}
}
