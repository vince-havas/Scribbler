package randLines;

import java.awt.Graphics2D;
import java.util.ArrayList;

import accessories.PaintStrokes;
import accessories.Section;
import illustration.Drawing;

public abstract class LinePainting extends Drawing {
	public ArrayList<Section> _lines = new ArrayList<Section>();

	public abstract void generateLines();

	public void print() {
		int ii = 0;
		for (Section s : _lines)
			System.out.println("section " + ii++ + ": " + s.toString());
	}

	public ArrayList<PaintStrokes> getPaintStrokes() {
		ArrayList<PaintStrokes> out = new ArrayList<PaintStrokes>();
		for (Section l : _lines)
			out.add(l);
		return out;
	}

	public boolean wellLocatedExcept(Section attempt_, ArrayList<Section> excludedIndices_) {
		// checks if the section is within the boundaries and not crossing the previous
		// lines not listed in excludedIndices_
		if (!attempt_.inRange())
			return false;

		for (Section old : _lines)
			if (excludedIndices_ != null && excludedIndices_.contains(old))
				continue;
			else if (old.isCrossing(attempt_))
				return false;

		return true;
	}

	public boolean wellLocated(Section attempt_) {
		return wellLocatedExcept(attempt_, null);
	}

	@Override
	public void drawMe(Graphics2D g2_) {
		for (Section line : _lines)
			g2_.draw(line.toLine());
	}
}
