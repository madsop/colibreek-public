package colibreek.reasoner;

import jcolibri.cbrcore.CBRCase;
import colibreek.caserepresentation.CaseDescription;

public interface ColibreekReasoner {
	CBRCase reason(CaseDescription newCase);
}
