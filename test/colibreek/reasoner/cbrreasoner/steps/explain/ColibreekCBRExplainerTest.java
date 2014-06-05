package colibreek.reasoner.cbrreasoner.steps.explain;

import java.util.ArrayList;
import java.util.List;

import jcolibri.cbrcore.CBRCase;
import jcolibri.cbrcore.CBRCaseBase;
import jcolibri.datatypes.Instance;
import jcolibri.exception.InitializingException;
import jcolibri.exception.OntologyAccessException;
import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.hp.hpl.jena.rdf.model.Statement;

import colibreek.TestHelper;
import colibreek.caserepresentation.CaseDescription;
import colibreek.domain.travelrecommender.StaticTravelDomainTestHelper;
import colibreek.domain.travelrecommender.TravelCaseConfigurations;
import colibreek.domain.travelrecommender.TravelCaseDescription;
import colibreek.reasoner.cbrreasoner.steps.activate.ActivatedAreaAndCases;
import colibreek.reasoner.cbrreasoner.steps.activate.ColibreekCBRActivator;
import colibreek.reasoner.cbrreasoner.steps.activate.ActivationSpreader;
import colibreek.reasoner.cbrreasoner.steps.activate.ThresholdMatcher;
import colibreek.reasoner.cbrreasoner.steps.explain.datastructure.ColibreekCBRExplanation;
import colibreek.reasoner.cbrreasoner.steps.explain.datastructure.ExplanationForOneCase;

public class ColibreekCBRExplainerTest {
	private ColibreekCBRActivator activator;
	private ColibreekCBRExplainer explainer;
	private CBRCaseBase casebase;
	private List<Statement> statements;

	@Before
	public void setUp() throws InitializingException {
		TravelCaseConfigurations travelCaseConfigurations = new TravelCaseConfigurations();
		activator = new ColibreekCBRActivator(new ThresholdMatcher(travelCaseConfigurations), new ActivationSpreader(travelCaseConfigurations), travelCaseConfigurations);
		explainer = new ColibreekCBRExplainer(new CaseSimilarityComputerFactory(new PathHandlerFactory(travelCaseConfigurations), null, null));
		casebase = StaticTravelDomainTestHelper.establishCasebase();
		statements = TestHelper.getAllStatementsFromOWLFileAsIterator().toList();
	}

	@Test
	public void testThatExplanationOfTheOneNetworkStatementReturnsSomethingUseful() throws OntologyAccessException {
		TravelCaseDescription newCase = StaticTravelDomainTestHelper.createExampleCase("January");

		ActivatedAreaAndCases activatedArea = activator.activate(casebase, statements, newCase);
		ColibreekCBRExplanation explanationResult = explainer.explain(activatedArea, newCase);

		double similarityMaxInListing = explanationResult.getMostSimilar().getSimilarity();
		double similarityOfFirst = explanationResult.getExplanations().get(0).getSimilarity();

		Assert.assertEquals(similarityMaxInListing,similarityOfFirst, 0.000001);
	}

	//	@Test
	public void testThatFindingsNotEqualButSomewhatRelatedAreAlsoCounted() throws OntologyAccessException {
		CaseDescription newCase = StaticTravelDomainTestHelper.createExampleCase("January");

		ActivatedAreaAndCases activatedArea = activator.activate(casebase, statements, newCase);
		ColibreekCBRExplanation explanationResult = explainer.explain(activatedArea, newCase);

		Assert.assertTrue(0.5 < explanationResult.getMostSimilar().getSimilarity());
		System.out.println("simstart: " + explanationResult.getExplanations().get(explanationResult.getExplanations().size()-1).getSimilarity() + "...");
		Assert.assertTrue(0.5 > explanationResult.getExplanations().get(explanationResult.getExplanations().size()-1).getSimilarity());
	}

	@Test
	public void testThatMayAndAprilAreConnected() throws OntologyAccessException {
		TravelCaseDescription newCase = StaticTravelDomainTestHelper.createExampleCase("May");

		ActivatedAreaAndCases activatedArea = activator.activate(casebase, statements, newCase);
		ColibreekCBRExplanation explanationResult = explainer.explain(activatedArea, newCase);

		System.out.println(explanationResult.getMostSimilar().getCase().getID() + ": " + explanationResult.getMostSimilar().getSimilarity());
		System.out.println(explanationResult.getExplanations().get(explanationResult.getExplanations().size()-1).getSimilarity());
		
		for (FindingSimilarity findingSimilarity : explanationResult.getMostSimilar().getFindingSimilarities()) {
			if (findingSimilarity.getTrace().contains("May")) {
				Assert.assertTrue(Math.abs(findingSimilarity.getSimilarityValue() - 0.3333333333) < 0.0001);
				Assert.assertTrue(findingSimilarity.getTrace().toString().contains("May") && findingSimilarity.getTrace().toString().contains("April"));
			}
		}
	}

	@Test
	public void testThatExplanationIsProvided() throws OntologyAccessException {
		TravelCaseDescription newCase = StaticTravelDomainTestHelper.createExampleCase("January");

		ActivatedAreaAndCases activatedArea = activator.activate(casebase, statements, newCase);
		ColibreekCBRExplanation explanationResult = explainer.explain(activatedArea, newCase);

		ExplanationForOneCase explanationForBestHit = explanationResult.getMostSimilar();
		System.out.println(explanationForBestHit.getCase().getID() + ": ------------ " + explanationForBestHit.getSimilarity() + " ------------------ ");

		Assert.assertEquals(newCase.getCaseFindings().size(), explanationForBestHit.getFindingSimilarities().size());
	}

	
	@Test
	public void testWithFindingFromNewCaseNull() throws OntologyAccessException {
		TravelCaseDescription newCaseDescription = StaticTravelDomainTestHelper.createExampleCase("May");
		newCaseDescription.setAccommodation(null);
		ActivatedAreaAndCases activatedArea = new ActivatedAreaAndCases();
		CBRCase oldCase = new CBRCase();
		TravelCaseDescription oldCaseDescription = StaticTravelDomainTestHelper.createExampleCase("April");
		oldCaseDescription.setMainConcept(new Instance("I50"));
		oldCase.setDescription(oldCaseDescription);
		ArrayList<CBRCase> caseToAddAsCollection = new ArrayList<>();
		caseToAddAsCollection.add(oldCase);
		activatedArea.addCases(caseToAddAsCollection);
		activatedArea.addStatements(statements);
		
		explainer.explain(activatedArea, newCaseDescription);
		Assert.assertTrue(newCaseDescription.getAccommodation() == null);
	}
	
	@After
	public void tearDown() {
		casebase.close();
	}
}