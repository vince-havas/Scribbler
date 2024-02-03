package illustration;

import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.io.InvalidClassException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.Supplier;

public class Arranger<T extends Drawing> extends Drawing {
	public enum ArrangementType {
		SINGLE, PETRI_DISH
	}

	HashMap<Point2D, T> _drawings;

	public Arranger(Supplier<T> cls_) {
		this(cls_, ArrangementType.SINGLE, 0);
	}

	public Arranger(Supplier<T> cls_, ArrangementType arTy_, int param_) {
		_drawings = new HashMap<Point2D, T>();
		try {
			switch (arTy_) {
			case SINGLE: {
				T item = cls_.get();
				_drawings.put(Page.getCentre(), item);
			}
				break;
			case PETRI_DISH:
				T item = cls_.get();
				if (!item.implementsSmallSetup())
					throw new IllegalArgumentException("Improper ArrangementType");

				while (_drawings.size() < param_) {
					Point2D p = Page.getRandomPoint();
					item = cls_.get();
					item.setUpSmall();
					item.setCentre(p);

					Page.Hull thisHull = item.getHull();
					if (!Page.inPetriDish(thisHull))
						continue;
					boolean hasSpace = true;
					for (Point2D key : _drawings.keySet())
						if (thisHull.touches(_drawings.get(key).getHull())) {
							hasSpace = false;
							break;
						}
					if (hasSpace)
						_drawings.put(p, item);
				}
				break;
			}
		} catch (InvalidClassException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void drawMe(Graphics2D g2_) {
		for (Point2D thisCentre : _drawings.keySet()) {
			_drawings.get(thisCentre).drawMe(g2_);
		}
	}

	@Override
	public ArrayList<Integer> getDefaultArgs() throws InvalidClassException {
		throw new InvalidClassException("Arranger class doesn't have default arguments.");
	}

	@Override
	public void setUp(ArrayList<Integer> args_) throws InvalidClassException {
		throw new InvalidClassException("Arranger class can't be set up.");
	}

	@Override
	public void setUpSmall() throws InvalidClassException {
		throw new InvalidClassException("Arranger class can't be set up.");
	}

	protected void updateCurves() {
		return;
	}

	@Override
	public boolean implementsSmallSetup() {
		return false;
	}

	@Override
	public int getStrokeWidth() {
		Point2D key = (Point2D) _drawings.keySet().toArray()[0];
		return _drawings.get(key).getStrokeWidth();
	}
}
