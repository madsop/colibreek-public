package evaluation;

import colibreek.caserepresentation.CaseDescription;
import colibreek.reasoner.cbrreasoner.StatementExtractor;
import colibreek.reasoner.cbrreasoner.steps.activate.ActivatedAreaAndCases;
import jcolibri.cbrcore.CBRCase;
import jcolibri.cbrcore.CBRCaseBase;

public abstract class LeaveOneOutExplainer extends LeaveOneOutActivator {
	@Override
	protected double runSingleCase(CBRCaseBase casebase, int i, CBRCase queryCase) {
		ActivatedAreaAndCases activatedAreaAndCases = activator.activate(casebase, new StatementExtractor().getInitialStatements(), (CaseDescription) queryCase.getDescription());
		return explainer.explain(activatedAreaAndCases, (CaseDescription) queryCase.getDescription()).getExplanations().size();
	}

}
