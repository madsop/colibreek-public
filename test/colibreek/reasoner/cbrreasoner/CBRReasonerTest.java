package colibreek.reasoner.cbrreasoner;

import jcolibri.casebase.IDIndexedLinearCaseBase;
import jcolibri.cbrcore.CBRCase;
import jcolibri.cbrcore.CBRCaseBase;
import jcolibri.connector.OntologyConnector;
import jcolibri.datatypes.Instance;
import jcolibri.exception.InitializingException;
import jcolibri.exception.OntologyAccessException;
import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import colibreek.ExplanationVisualiser;
import colibreek.caserepresentation.CaseDescription;
import colibreek.caserepresentation.NullCBRCase;
import colibreek.caserepresentation.NullCaseSolution;
import colibreek.domain.travelrecommender.StaticTravelDomainTestHelper;
import colibreek.domain.travelrecommender.TravelCaseConfigurations;
import colibreek.domain.travelrecommender.TravelCaseDescription;
import colibreek.domain.travelrecommender.TravelCaseSolution;
import colibreek.reasoner.ColibreekReasoner;
import colibreek.reasoner.cbrreasoner.steps.activate.ActivatedAreaAndCases;
import colibreek.reasoner.cbrreasoner.steps.activate.ColibreekCBRActivator;
import colibreek.reasoner.cbrreasoner.steps.activate.ActivationSpreader;
import colibreek.reasoner.cbrreasoner.steps.activate.ThresholdMatcher;
import colibreek.reasoner.cbrreasoner.steps.explain.ColibreekCBRExplainer;
import colibreek.reasoner.cbrreasoner.steps.explain.PathHandlerFactory;
import colibreek.reasoner.cbrreasoner.steps.explain.CaseSimilarityComputerFactory;
import colibreek.reasoner.cbrreasoner.steps.focus.ColibreekCBRFocuser;

import com.hp.hpl.jena.rdf.model.Statement;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyListOf;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CBRReasonerTest {
	private IDIndexedLinearCaseBase casebase;
	
	@Before
	public void setUp() throws InitializingException {
		casebase = StaticTravelDomainTestHelper.establishCasebase();
	}
	
	@Test
	public void testThatReasonerReturnsSameCaseWhenCaseIsTestedAgainstItself() throws OntologyAccessException {
		TravelCaseDescription caseDescription = StaticTravelDomainTestHelper.createCaseDescription("HOLIDAYFLAT", "FANO", "I5", "RECREATION", "CAR", "May", "I7", "I50");
		
		ColibreekReasoner reasoner = setUpReasoner();
		TravelCaseSolution returnedSolution = (TravelCaseSolution) reasoner.reason(caseDescription).getSolution(); 
		Assert.assertEquals("I789.00", returnedSolution.getPrice().toString());
		Assert.assertEquals("I50", returnedSolution.getMainConcept().toString());
	}
	
	@Test
	public void testThatEmptyCasebaseIsNoProblem() throws OntologyAccessException {
		TravelCaseDescription newCase = StaticTravelDomainTestHelper.createExampleCase("May");
		newCase.setMainConcept(new Instance("I4"));
		casebase = mock(IDIndexedLinearCaseBase.class);
		ColibreekReasoner reasoner = setUpReasoner();
		
		reasoner.reason(newCase);
	}

	@Test
	public void testIfRightSolutionIsReturnedWhenCasebaseContainsThisCaseAndOneMoreCase() throws OntologyAccessException {
		ColibreekReasoner reasoner = setUpReasoner();

		CaseDescription newCaseDescription1 = StaticTravelDomainTestHelper.createCaseDescription("HOLIDAYFLAT", "FANO", "I5", "RECREATION", "CAR", "May", "I7", "I974");		
		TravelCaseSolution solution1 = (TravelCaseSolution) reasoner.reason(newCaseDescription1).getSolution();

		CaseDescription newCaseDescription2 = StaticTravelDomainTestHelper.createCaseDescription("HOLIDAYFLAT", "FANO", "I5", "RECREATION", "CAR", "May", "I7", "I50");
		TravelCaseSolution solution2 = (TravelCaseSolution) reasoner.reason(newCaseDescription2).getSolution();

		Assert.assertEquals("I454.00", solution1.getPrice().toString());
		Assert.assertEquals("I974", solution1.getMainConcept().toString());

		Assert.assertEquals("I789.00", solution2.getPrice().toString());
		Assert.assertEquals("I50", solution2.getMainConcept().toString());
	}

	@Test(expected = AssertionError.class)
	public void testThatActivatorGetsCalledOnReasoningEvenIfEmptyCasebase() throws InitializingException {
		ColibreekCBRActivator activator = mock(ColibreekCBRActivator.class);
		ColibreekCBRExplainer explainer = mock(ColibreekCBRExplainer.class);
		OntologyConnector connector = mock(OntologyConnector.class);
		CBRCaseBase casebase = new IDIndexedLinearCaseBase();
		casebase.init(connector);
		
		Assert.assertEquals(0, casebase.getCases().size());
		verify(activator, times(1)).activate(any(CBRCaseBase.class), anyListOf(Statement.class), any(CaseDescription.class));
		
		ColibreekCBRReasoner reasoner = new ColibreekCBRReasoner(casebase, activator, explainer, null, null, new StatementExtractor());
		reasoner.reason(new TravelCaseDescription());
	}

	@Test
	public void testThatActivatorGetsCalledOnReasoningWhenNonEmptyCasebase() throws OntologyAccessException {
		ColibreekCBRActivator activator = mock(ColibreekCBRActivator.class);
		ColibreekCBRExplainer explainer = mock(ColibreekCBRExplainer.class);

		ColibreekCBRReasoner reasoner = new ColibreekCBRReasoner(casebase, activator, explainer, new ColibreekCBRFocuser(), null, new StatementExtractor());
		TravelCaseDescription caseDescription = new TravelCaseDescription();
		caseDescription.setDuration(new Instance("I7"));
		caseDescription.setSeason(new Instance("July"));
		
		try {
			reasoner.reason(caseDescription);
		} catch (NullPointerException e) {
		} finally {
			verify(activator).activate(any(CBRCaseBase.class), anyListOf(Statement.class), eq(caseDescription));
		}
	}
	
	@Test
	public void testThatEmptyResultSetFromActivateDoesNotBreakEverything() throws InitializingException {
		ColibreekCBRActivator activator = mock(ColibreekCBRActivator.class);
		TravelCaseConfigurations travelCaseConfigurations = new TravelCaseConfigurations();
		ColibreekCBRExplainer explainer = new ColibreekCBRExplainer(new CaseSimilarityComputerFactory(new PathHandlerFactory(travelCaseConfigurations), null, null));
		ColibreekCBRFocuser focuser = new ColibreekCBRFocuser();

		IDIndexedLinearCaseBase casebase = StaticTravelDomainTestHelper.establishCasebase();
		
		ActivatedAreaAndCases activatedAreaAndCases = new ActivatedAreaAndCases();
		CaseDescription caseDescription = mock(CaseDescription.class);
		when(activator.activate(eq(casebase), anyListOf(Statement.class), eq(caseDescription))).thenReturn(activatedAreaAndCases);

		ColibreekCBRReasoner reasoner = new ColibreekCBRReasoner(casebase, activator, explainer, focuser, new ExplanationVisualiser(), new StatementExtractor());
		CBRCase solutionCase = reasoner.reason(caseDescription);
		Assert.assertTrue(solutionCase.getClass() == NullCBRCase.class);
		Assert.assertNull(solutionCase.getDescription());
		Assert.assertTrue(solutionCase.getSolution().getClass() == NullCaseSolution.class);
	}

	private ColibreekReasoner setUpReasoner() {
		TravelCaseConfigurations travelCaseConfigurations = new TravelCaseConfigurations();
		ColibreekReasoner reasoner = new ColibreekCBRReasoner(
				casebase, 
				new ColibreekCBRActivator(new ThresholdMatcher(travelCaseConfigurations), new ActivationSpreader(travelCaseConfigurations), travelCaseConfigurations), 
				new ColibreekCBRExplainer(new CaseSimilarityComputerFactory(new PathHandlerFactory(travelCaseConfigurations), null, null)),
				new ColibreekCBRFocuser(),
				new ExplanationVisualiser(),
				new StatementExtractor());
		return reasoner;
	}
}