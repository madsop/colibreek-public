package colibreek.caserepresentation;

import jcolibri.cbrcore.CBRCase;

public class NullCBRCase extends CBRCase {
	public static final String NULL_CASE_ID = "NullCase";
	
	@Override
	public Object getID() {
		return NULL_CASE_ID;
	}
	
	public CaseSolution getSolution() {
		return new NullCaseSolution();
	}
}
