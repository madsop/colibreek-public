package colibreek.reasoner.cbrreasoner;

import jcolibri.cbrcore.CBRCaseBase;

public interface CBRReasonerFactory {
	ColibreekCBRReasoner createReasoner(CBRCaseBase casebase);
}