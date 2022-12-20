package randLines;

import java.util.ArrayList;

import accessories.Section;
import illustration.Page;

public abstract class LinePainting {
	public ArrayList<Section> _lines = new ArrayList<Section>();

	public abstract void generateLines();

	public void print() {
		int ii = 0;
		for (Section s : _lines)
			System.out.println("section " + ii++ + ": " + s.toString());
	}

	public ArrayList<Section> getLines() {
		return _lines;
	}

	public boolean wellLocatedExcept(Section attempt_, ArrayList<Section> excludedIndices_) {
		// checks if the section is within the boundaries and not crossing the previous
		// lines not listed in excludedIndices_
		if (!attempt_.inRange(Page._margin, Page._margin, Page._width - Page._margin,
				Page._pageRatio * Page._width - Page._margin))
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
}
