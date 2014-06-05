package evaluation;

import jcolibri.cbrcore.CBRCase;
import jcolibri.cbrcore.CBRCaseBase;
import colibreek.caserepresentation.CaseDescription;
import colibreek.reasoner.cbrreasoner.StatementExtractor;

public abstract class LeaveOneOutActivator extends LeaveOneOutTest {
	@Override
	protected double runSingleCase(CBRCaseBase casebase, int i, CBRCase queryCase) {
		System.out.println(i);
		return activator.activate(casebase, new StatementExtractor().getInitialStatements(), (CaseDescription) queryCase.getDescription()).getCases().size();
	}
}
