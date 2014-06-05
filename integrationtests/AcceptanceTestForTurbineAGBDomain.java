package integrationtests;


import jcolibri.cbrcore.CaseComponent;
import jcolibri.cbrcore.Connector;
import jcolibri.exception.InitializingException;
import jcolibri.exception.OntologyAccessException;
import colibreek.ExplanationVisualiser;
import colibreek.Main;
import colibreek.domain.turbineagbcase.TurbineAGBCaseConfigurations;
import colibreek.domain.turbineagbcase.TurbineAGBCaseDescription;
import colibreek.domain.turbineagbcase.TurbineAGBCaseSolution;
import colibreek.domain.turbineagbcase.TurbineAGBDomainExecuter;
import colibreek.domain.turbineagbcase.TurbineAGBTestHelper;
import colibreek.getInput.InputRetriever;
import jcolibri.casebase.IDIndexedLinearCaseBase;
import jcolibri.cbrcore.CBRCaseBase;
import jcolibri.connector.OntologyConnector;
import jcolibri.datatypes.Instance;
import jcolibri.exception.ExecutionException;
import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import colibreek.reasoner.cbrreasoner.ColibreekCBRReasoner;
import colibreek.reasoner.cbrreasoner.StatementExtractor;
import colibreek.reasoner.cbrreasoner.steps.activate.ColibreekCBRActivator;
import colibreek.reasoner.cbrreasoner.steps.activate.ActivationSpreader;
import colibreek.reasoner.cbrreasoner.steps.activate.ThresholdMatcher;
import colibreek.reasoner.cbrreasoner.steps.explain.ColibreekCBRExplainer;
import colibreek.reasoner.cbrreasoner.steps.explain.DateTimeSimilarityComputer;
import colibreek.reasoner.cbrreasoner.steps.explain.NumberSimilarityComputer;
import colibreek.reasoner.cbrreasoner.steps.explain.PathHandlerFactory;
import colibreek.reasoner.cbrreasoner.steps.explain.CaseSimilarityComputerFactory;
import colibreek.reasoner.cbrreasoner.steps.focus.ColibreekCBRFocuser;
import static org.mockito.Mockito.mock;

public class AcceptanceTestForTurbineAGBDomain {
	private String configURL = "config/turbineAGBConfig.xml";
	private TurbineAGBCaseConfigurations turbineAGBCaseConfigurations;
	private ColibreekCBRActivator activator;
	private ColibreekCBRExplainer explainer;
	private ColibreekCBRFocuser focuser;
	private ExplanationVisualiser visualiser;
	private StatementExtractor statementExtractor;

	@Before
	public void setUp() {
		turbineAGBCaseConfigurations = new TurbineAGBCaseConfigurations();
		activator = new ColibreekCBRActivator(new ThresholdMatcher(turbineAGBCaseConfigurations), new ActivationSpreader(turbineAGBCaseConfigurations), turbineAGBCaseConfigurations);
		explainer = new ColibreekCBRExplainer(new CaseSimilarityComputerFactory(new PathHandlerFactory(turbineAGBCaseConfigurations), new DateTimeSimilarityComputer(turbineAGBCaseConfigurations), new NumberSimilarityComputer()));
		focuser = new ColibreekCBRFocuser();
		visualiser = new ExplanationVisualiser();
		statementExtractor = new StatementExtractor();
	}

	@Test
	public void testThatTheReasonerGivesTheCorrectSolution() throws InitializingException, OntologyAccessException {
		Connector connector = new OntologyConnector();
		connector.initFromXMLfile(jcolibri.util.FileIO.findFile(configURL));
		CBRCaseBase casebase = new IDIndexedLinearCaseBase();
		casebase.init(connector);

		ColibreekCBRReasoner reasoner = new ColibreekCBRReasoner(casebase, activator, explainer, focuser, visualiser, statementExtractor);
		TurbineAGBCaseSolution solution = (TurbineAGBCaseSolution) reasoner.reason(TurbineAGBTestHelper.setUpCase0Query()).getSolution();

		Assert.assertTrue(solution.getMainConcept().toString().equals("Case0"));
		Assert.assertTrue(solution.getIsAlarmingSituation().toString().equals("NotAlarmingSituation"));
	}


	@Test
	public void testThatTheReasonerGivesTheCorrectSolutionAndTheSurroundingsFromMain() throws OntologyAccessException, ExecutionException {
		String[] args = new String[0];
		Main main = new Main(args);
		CBRCaseBase casebase = main.setUp(new TurbineAGBDomainExecuter(mock(InputRetriever.class), turbineAGBCaseConfigurations));

		ColibreekCBRReasoner reasoner = new ColibreekCBRReasoner(casebase, activator, explainer, focuser, visualiser, statementExtractor);
		CaseComponent solutionAsCaseComponent = reasoner.reason(TurbineAGBTestHelper.setUpCase0Query()).getSolution();

		main.postCycle();
		
		Assert.assertTrue(solutionAsCaseComponent instanceof TurbineAGBCaseSolution);
		TurbineAGBCaseSolution solution = (TurbineAGBCaseSolution) solutionAsCaseComponent;
		Assert.assertTrue(solution.getMainConcept().toString().equals("Case0"));
		Assert.assertTrue(solution.getIsAlarmingSituation().toString().equals("NotAlarmingSituation"));
	}
	
	@Test
	public void testThatAlarmingSituationGetsDetected() throws OntologyAccessException, ExecutionException {
		String[] args = new String[0];
		Main main = new Main(args);
		CBRCaseBase casebase = main.setUp(new TurbineAGBDomainExecuter(mock(InputRetriever.class), turbineAGBCaseConfigurations));

		ColibreekCBRReasoner reasoner = new ColibreekCBRReasoner(casebase, activator, explainer, focuser, visualiser, statementExtractor);
		TurbineAGBCaseDescription queryDesc = TurbineAGBTestHelper.setUpAlarmingQuery();	
		CaseComponent solutionAsCaseComponent = reasoner.reason(queryDesc).getSolution();

		main.postCycle();
		
		Assert.assertTrue(solutionAsCaseComponent instanceof TurbineAGBCaseSolution);
		TurbineAGBCaseSolution solution = (TurbineAGBCaseSolution) solutionAsCaseComponent;
		Assert.assertTrue(solution.getIsAlarmingSituation().toString().equals("ActuallyAlarmingSituation"));
	}
	

	@Test
	public void testWithEdgyAlarmingSituation() throws OntologyAccessException, ExecutionException {
		String[] args = new String[0];
		Main main = new Main(args);
		CBRCaseBase casebase = main.setUp(new TurbineAGBDomainExecuter(mock(InputRetriever.class), turbineAGBCaseConfigurations));

		ColibreekCBRReasoner reasoner = new ColibreekCBRReasoner(casebase, activator, explainer, focuser, visualiser, statementExtractor);
		TurbineAGBCaseDescription queryDesc = TurbineAGBTestHelper.setUpAlarmingQuery();	
		queryDesc.setAGBBearingSumpTemperature(new Instance("94.3"));
		CaseComponent solutionAsCaseComponent = reasoner.reason(queryDesc).getSolution();

		main.postCycle();
		
		Assert.assertTrue(solutionAsCaseComponent instanceof TurbineAGBCaseSolution);
		TurbineAGBCaseSolution solution = (TurbineAGBCaseSolution) solutionAsCaseComponent;
		Assert.assertTrue(solution.getIsAlarmingSituation().toString().equals("NotAlarmingSituation"));
	}
}