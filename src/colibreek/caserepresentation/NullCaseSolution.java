package colibreek.caserepresentation;

import jcolibri.cbrcore.Attribute;
import jcolibri.datatypes.Instance;

public class NullCaseSolution extends CaseSolution {
	@Override
	public Instance getMainConcept() {
		return null;
	}

	@Override
	public Attribute getIdAttribute() {
		return new Attribute("caseID", this.getClass());
	}

	@Override
	public String toString() {
		return "No solution was found";
	}
}