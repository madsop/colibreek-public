package colibreek.reasoner.cbrreasoner;

import colibreek.ExplanationVisualiser;
import colibreek.caserepresentation.CaseDescription;
import colibreek.caserepresentation.NullCBRCase;
import colibreek.reasoner.ColibreekReasoner;
import colibreek.reasoner.cbrreasoner.steps.activate.ActivatedAreaAndCases;
import colibreek.reasoner.cbrreasoner.steps.activate.ColibreekCBRActivator;
import colibreek.reasoner.cbrreasoner.steps.explain.ColibreekCBRExplainer;
import colibreek.reasoner.cbrreasoner.steps.explain.datastructure.ColibreekCBRExplanation;
import colibreek.reasoner.cbrreasoner.steps.explain.datastructure.ExplanationForOneCase;
import colibreek.reasoner.cbrreasoner.steps.focus.ColibreekCBRFocuser;
import jcolibri.cbrcore.CBRCase;
import jcolibri.cbrcore.CBRCaseBase;

import java.util.Optional;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

public class ColibreekCBRReasoner implements ColibreekReasoner {
	private CBRCaseBase casebase;
	private ColibreekCBRActivator activator;
	private ColibreekCBRExplainer explainer;
	private ColibreekCBRFocuser focuser;
	private ExplanationVisualiser explanationVisualiser;
	private StatementExtractor statementExtractor;

	@Inject
	public ColibreekCBRReasoner(@Assisted CBRCaseBase casebase, ColibreekCBRActivator activator, ColibreekCBRExplainer explainer, ColibreekCBRFocuser focuser, ExplanationVisualiser explanationVisualiser, StatementExtractor statementExtractor) {
		this.casebase = casebase;
		this.activator = activator;
		this.explainer = explainer;
		this.focuser = focuser;
		this.explanationVisualiser = explanationVisualiser;
		this.statementExtractor = statementExtractor;		
	}

	public CBRCase reason(CaseDescription query) {
		return retrieve(query);  
	}

	private CBRCase retrieve(CaseDescription newCaseDescription) {
		CBRCase caseAlreadySolved = findTheExistingSolutionIfTheCaseIsAlreadySolved(newCaseDescription);
		if (!caseAlreadySolved.getID().equals(NullCBRCase.NULL_CASE_ID)) { return caseAlreadySolved; }
		ExplanationForOneCase solutionToReturn = activateExplainFocus(newCaseDescription);
		return solutionToReturn.getCase();
	}

	private CBRCase findTheExistingSolutionIfTheCaseIsAlreadySolved(CaseDescription query) {
		if (query.getCaseId() == null) { return new NullCBRCase(); }
		Optional<CBRCase> caseInCaseBase = casebase.getCases().stream().filter(cbrcase -> cbrcase.getID().toString().equals(query.getCaseId().toString())).findAny();
		if (!caseInCaseBase.isPresent()) { return new NullCBRCase(); }
		return caseInCaseBase.get();
	}

	private ExplanationForOneCase activateExplainFocus(CaseDescription newCaseDescription) {
		ActivatedAreaAndCases activatedArea = activator.activate(casebase, statementExtractor.getInitialStatements(), newCaseDescription);
		ColibreekCBRExplanation explanation = explainer.explain(activatedArea, newCaseDescription);
		ExplanationForOneCase solutionToReturn = focuser.focus(explanation);
		
		explanationVisualiser.visualise(newCaseDescription, explanation, solutionToReturn);
		return solutionToReturn;
	}
}