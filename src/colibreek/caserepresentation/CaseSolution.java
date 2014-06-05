package colibreek.caserepresentation;

import jcolibri.cbrcore.CaseComponent;
import jcolibri.datatypes.Instance;

public abstract class CaseSolution implements CaseComponent {
	public abstract Instance getMainConcept();
	public abstract String toString();
}