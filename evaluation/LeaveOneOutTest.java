package evaluation;

import java.util.ArrayList;
import java.util.Arrays;

import jcolibri.casebase.CachedLinearCaseBase;
import jcolibri.cbrcore.CBRCase;
import jcolibri.cbrcore.CBRCaseBase;
import jcolibri.connector.OntologyConnector;
import jcolibri.exception.InitializingException;
import colibreek.ExplanationVisualiser;
import colibreek.caserepresentation.CaseDescription;
import colibreek.caserepresentation.CaseSolution;
import colibreek.config.DomainDependentConfigurations;
import colibreek.reasoner.cbrreasoner.ColibreekCBRReasoner;
import colibreek.reasoner.cbrreasoner.StatementExtractor;
import colibreek.reasoner.cbrreasoner.steps.activate.ActivationSpreader;
import colibreek.reasoner.cbrreasoner.steps.activate.ColibreekCBRActivator;
import colibreek.reasoner.cbrreasoner.steps.activate.ThresholdMatcher;
import colibreek.reasoner.cbrreasoner.steps.explain.CaseSimilarityComputerFactory;
import colibreek.reasoner.cbrreasoner.steps.explain.ColibreekCBRExplainer;
import colibreek.reasoner.cbrreasoner.steps.explain.DateTimeSimilarityComputer;
import colibreek.reasoner.cbrreasoner.steps.explain.NumberSimilarityComputer;
import colibreek.reasoner.cbrreasoner.steps.explain.PathHandlerFactory;
import colibreek.reasoner.cbrreasoner.steps.focus.ColibreekCBRFocuser;

public abstract class LeaveOneOutTest {

	protected ColibreekCBRActivator activator;
	protected ColibreekCBRExplainer explainer;
	private ColibreekCBRFocuser focuser;
	private ExplanationVisualiser explanationVisualiser;
	private StatementExtractor statementExtractor;

	protected abstract DomainDependentConfigurations getDomainDependentConfigurations();

	private void setUp(DomainDependentConfigurations domainDependentConfigurations) {
		ActivationSpreader activationSpreader = new ActivationSpreader(domainDependentConfigurations);
		ThresholdMatcher thresholdMatcher = new ThresholdMatcher(domainDependentConfigurations);

		activator = new ColibreekCBRActivator(thresholdMatcher, activationSpreader, domainDependentConfigurations);

		PathHandlerFactory pathHandlerFactory = new PathHandlerFactory(domainDependentConfigurations);
		DateTimeSimilarityComputer dateTimeSimilarityComputer = new DateTimeSimilarityComputer(domainDependentConfigurations);
		NumberSimilarityComputer numberSimilarityComputer = new NumberSimilarityComputer();
		CaseSimilarityComputerFactory similarityComputerFactory = new CaseSimilarityComputerFactory(pathHandlerFactory, dateTimeSimilarityComputer,	numberSimilarityComputer);
		explainer = new ColibreekCBRExplainer(similarityComputerFactory);

		focuser = new ColibreekCBRFocuser();

		explanationVisualiser = new ExplanationVisualiser();

		statementExtractor = new StatementExtractor();
	}

	private ColibreekCBRReasoner createReasoner(CBRCaseBase casebase){
		ColibreekCBRReasoner reasoner = new ColibreekCBRReasoner(casebase, activator, explainer, focuser, explanationVisualiser, statementExtractor);
		return reasoner;
	}

	protected void leaveOneOutTest() throws InitializingException {
		DomainDependentConfigurations domainDependentConfigurations = getDomainDependentConfigurations();
		setUp(domainDependentConfigurations);
		CachedLinearCaseBase casebase = setUpCaseBase(domainDependentConfigurations.getLocationOfXMLConfigFile());

		ArrayList<CBRCase> cases = new ArrayList<>(casebase.getCases());

		int numberOfSuccesses = 0;

		for (int i = 0; i < cases.size(); i++) {
			CBRCase queryCase = cases.get(i);
			casebase.forgetCases(Arrays.asList(queryCase));
			
			numberOfSuccesses += runSingleCase(casebase, i, queryCase);

			casebase.learnCases(Arrays.asList(queryCase));
		}

		printResults(cases, numberOfSuccesses);
	}

	protected double runSingleCase(CBRCaseBase casebase, int i, CBRCase queryCase) {
		CaseSolution solution = runQuery(casebase, queryCase);

		if (checkIsSuccess(queryCase, solution)) {
			System.out.println(i);
			System.out.println("WIN!");
		}
		else { System.out.println("FAIL"); }
		return checkIsSuccess(queryCase, solution) ? 1.0 : 0.0;
	}

	protected abstract boolean checkIsSuccess(CBRCase queryCase, CaseSolution solution);

	private CaseSolution runQuery(CBRCaseBase casebase, CBRCase queryCase) {
		ColibreekCBRReasoner reasoner = createReasoner(casebase);

		CaseDescription query = (CaseDescription) queryCase.getDescription();
		CBRCase solutionCase = reasoner.reason(query);
		return (CaseSolution) solutionCase.getSolution();
	}

	private CachedLinearCaseBase setUpCaseBase(String caseconfigpath) throws InitializingException {
		CachedLinearCaseBase casebase = new CachedLinearCaseBase();
		OntologyConnector connector = new OntologyConnector();
		connector.initFromXMLfile(jcolibri.util.FileIO.findFile(caseconfigpath));
		casebase.init(connector);
		return casebase;
	}

	private void printResults(ArrayList<CBRCase> cases, int numberOfSuccesses) {
		System.out.println(numberOfSuccesses + " successes");
		System.out.println("Out of total " +cases.size());
		System.out.println("Leaving " + (cases.size() - numberOfSuccesses) + " errors");
		System.out.println("Success degree: " + (numberOfSuccesses + 0.0)/(cases.size() + 0.0));
	}
}
