package integrationtests;

import jcolibri.casebase.IDIndexedLinearCaseBase;
import jcolibri.cbrcore.CBRCaseBase;
import jcolibri.cbrcore.Connector;
import jcolibri.connector.OntologyConnector;
import jcolibri.datatypes.Instance;
import jcolibri.exception.ExecutionException;
import jcolibri.exception.InitializingException;
import jcolibri.exception.OntologyAccessException;
import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import colibreek.ExplanationVisualiser;
import colibreek.Main;
import colibreek.caserepresentation.CaseDescription;
import colibreek.domain.travelrecommender.StaticTravelDomainTestHelper;
import colibreek.domain.travelrecommender.TravelCaseConfigurations;
import colibreek.domain.travelrecommender.TravelCaseDescription;
import colibreek.domain.travelrecommender.TravelCaseSolution;
import colibreek.domain.travelrecommender.TravelDomainExecuter;
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

public class AcceptanceTestForTravelDomain {
	private TravelCaseConfigurations travelCaseConfigurations;
	private ColibreekCBRActivator activator;
	private ColibreekCBRExplainer explainer;
	private ColibreekCBRFocuser focuser;
	private ExplanationVisualiser visualiser;
	private StatementExtractor statementExtractor;
	
	@Before
	public void setUp() {
		travelCaseConfigurations = new TravelCaseConfigurations();
		activator = new ColibreekCBRActivator(new ThresholdMatcher(travelCaseConfigurations), new ActivationSpreader(travelCaseConfigurations), travelCaseConfigurations);
		explainer = new ColibreekCBRExplainer(new CaseSimilarityComputerFactory(new PathHandlerFactory(travelCaseConfigurations), new DateTimeSimilarityComputer(travelCaseConfigurations), new NumberSimilarityComputer()));
		focuser = new ColibreekCBRFocuser();
		visualiser = new ExplanationVisualiser();
		statementExtractor = new StatementExtractor();
	}

	@Test
	public void testThatTheReasonerGivesTheCorrectSolution() throws InitializingException, OntologyAccessException {
		
		Connector connector = new OntologyConnector();
		connector.initFromXMLfile(jcolibri.util.FileIO.findFile(StaticTravelDomainTestHelper.LOCATION_OF_XML_CONFIG_FILE));
		CBRCaseBase casebase = new IDIndexedLinearCaseBase();
		casebase.init(connector);
		
		ColibreekCBRReasoner reasoner = new ColibreekCBRReasoner(casebase, activator, explainer, focuser, visualiser, statementExtractor);
		TravelCaseSolution solution = (TravelCaseSolution) reasoner.reason(setUpQuery()).getSolution();
		
		Assert.assertTrue(solution.getMainConcept().toString().equals("I415"));
		Assert.assertTrue(solution.getPrice().toString().equals("I439.00"));
	}

	@Test
	public void testThatTheReasonerGivesTheCorrectSolutionAndTheSurroundingsFromMain() throws OntologyAccessException, ExecutionException {
		String[] args = new String[0];
		Main main = new Main(args);
		
		CBRCaseBase casebase = main.setUp(new TravelDomainExecuter());
				
		ColibreekCBRReasoner reasoner = new ColibreekCBRReasoner(casebase, activator, explainer, focuser, visualiser, statementExtractor);
		TravelCaseSolution solution = (TravelCaseSolution) reasoner.reason(setUpQuery()).getSolution();
		
		main.postCycle();
		
		Assert.assertTrue(solution.getMainConcept().toString().equals("I415"));
		Assert.assertTrue(solution.getPrice().toString().equals("I439.00"));
	}


	private CaseDescription setUpQuery() throws OntologyAccessException {
		TravelCaseDescription queryDesc = new TravelCaseDescription();
		queryDesc.setAccommodation(new Instance("HOLIDAYFLAT"));
		queryDesc.setDuration(new Instance("I7"));
		queryDesc.setHolidayType(new Instance("WANDERING"));
		queryDesc.setNumberOfPersons(new Instance("I4"));
		queryDesc.setSeason(new Instance("July"));
		queryDesc.setDestination(new Instance("TYROL"));
		queryDesc.setTransportation(new Instance("CAR"));
		return queryDesc;
	}
}
