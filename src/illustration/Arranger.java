package illustration;

import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.Supplier;

public class Arranger<T extends Drawing> extends Drawing {
	HashMap<Point2D, ArrayList<T>> _drawings;

	public Arranger(Supplier<T> cls_) {
		// TODO to be tested
		_drawings = new HashMap<Point2D, ArrayList<T>>();
		ArrayList<T> list = new ArrayList<T>();
		list.add(cls_.get());
		_drawings.put(Page.getCentre(), list);
	}

	@Override
	public void drawMe(Graphics2D g2_) {
		for (Point2D thisCentre : _drawings.keySet()) {
			for (Drawing thisDraw : _drawings.get(thisCentre)) {
				thisDraw.drawMe(g2_);
			}
		}
	}
}
