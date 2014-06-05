package integrationtests;

import jcolibri.cbrcore.CaseComponent;
import jcolibri.cbrcore.Connector;
import jcolibri.exception.InitializingException;
import jcolibri.exception.OntologyAccessException;
import colibreek.ExplanationVisualiser;
import colibreek.Main;
import colibreek.domain.mechanicalcase.MechanicalCaseConfigurations;
import colibreek.domain.mechanicalcase.MechanicalCaseDescription;
import colibreek.domain.mechanicalcase.MechanicalCaseSolution;
import colibreek.domain.mechanicalcase.MechanicalDomainExecuter;
import colibreek.domain.mechanicalcase.MechanicalDomainTestHelper;
import colibreek.domain.mechanicalcase.MechanicalInputRetriever;
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

public class AcceptanceTestForMechanicalDomain {
	private String configURL = "config/mechanicalCaseConfig.xml";
	private MechanicalCaseConfigurations mechanicalCaseConfigurations;
	private ColibreekCBRActivator activator;
	private ColibreekCBRExplainer explainer;
	private ColibreekCBRFocuser focuser;
	private ExplanationVisualiser visualiser;
	private StatementExtractor statementExtractor;
	
	@Before
	public void setUp() {
		mechanicalCaseConfigurations = new MechanicalCaseConfigurations();
		activator = new ColibreekCBRActivator(new ThresholdMatcher(mechanicalCaseConfigurations), new ActivationSpreader(mechanicalCaseConfigurations), mechanicalCaseConfigurations);
		explainer = new ColibreekCBRExplainer(new CaseSimilarityComputerFactory(new PathHandlerFactory(mechanicalCaseConfigurations), new DateTimeSimilarityComputer(mechanicalCaseConfigurations), new NumberSimilarityComputer()));
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
		MechanicalCaseSolution solution = (MechanicalCaseSolution) reasoner.reason(MechanicalDomainTestHelper.setUpQuery()).getSolution();

		Assert.assertTrue(solution.getMainConcept().toString().equals("Case1"));
		Assert.assertTrue(solution.getIsAlarmingSituation().toString().equals("NotAlarmingSituation"));
	}

	@Test
	public void testThatTheReasonerGivesTheCorrectSolutionAndTheSurroundingsFromMain() throws OntologyAccessException, ExecutionException {
		String[] args = new String[0];
		Main main = new Main(args);
		CBRCaseBase casebase = main.setUp(new MechanicalDomainExecuter(mock(MechanicalInputRetriever.class), mechanicalCaseConfigurations));

		ColibreekCBRReasoner reasoner = new ColibreekCBRReasoner(casebase, activator, explainer, focuser, visualiser, statementExtractor);
		CaseComponent solutionAsCaseComponent = reasoner.reason(MechanicalDomainTestHelper.setUpQuery()).getSolution();

		main.postCycle();
		
		Assert.assertTrue(solutionAsCaseComponent instanceof MechanicalCaseSolution);
		MechanicalCaseSolution solution = (MechanicalCaseSolution) solutionAsCaseComponent;
		Assert.assertTrue(solution.getMainConcept().toString().equals("Case1"));
		Assert.assertTrue(solution.getIsAlarmingSituation().toString().equals("NotAlarmingSituation"));
	}
	
	@Test
	public void testThatAlarmingSituationGetsDetected() throws OntologyAccessException, ExecutionException {
		String[] args = new String[0];
		Main main = new Main(args);
		CBRCaseBase casebase = main.setUp(new MechanicalDomainExecuter(mock(MechanicalInputRetriever.class), mechanicalCaseConfigurations));

		ColibreekCBRReasoner reasoner = new ColibreekCBRReasoner(casebase, activator, explainer, focuser, visualiser, statementExtractor);
		MechanicalCaseDescription queryDesc = new MechanicalCaseDescription();
		queryDesc.setGeneratorPower(new Instance("3.6"));
		queryDesc.setGeneratorID(new Instance("EE-43-E660"));
		queryDesc.setExciterFieldVoltage(new Instance("0"));
		queryDesc.setTimestamp(new Instance("2014-04-13T00:00:00"));
		queryDesc.setFrequency(new Instance("60.1"));
		queryDesc.setExciterFieldCurrent(new Instance("1.8"));
		queryDesc.setReactiveLoad(new Instance("1.7"));		
		CaseComponent solutionAsCaseComponent = reasoner.reason(queryDesc).getSolution();

		main.postCycle();
		
		Assert.assertTrue(solutionAsCaseComponent instanceof MechanicalCaseSolution);
		MechanicalCaseSolution solution = (MechanicalCaseSolution) solutionAsCaseComponent;
		Assert.assertTrue(solution.getIsAlarmingSituation().toString().equals("ActuallyAlarmingSituation"));
	}
}