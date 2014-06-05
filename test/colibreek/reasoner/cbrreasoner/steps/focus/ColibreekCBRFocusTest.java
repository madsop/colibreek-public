package colibreek.reasoner.cbrreasoner.steps.focus;

import java.util.HashSet;
import java.util.Set;

import jcolibri.cbrcore.CBRCase;
import jcolibri.datatypes.Instance;
import jcolibri.exception.InitializingException;
import jcolibri.exception.OntologyAccessException;
import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import com.hp.hpl.jena.ontology.OntTools.Path;

import colibreek.caserepresentation.CaseSolution;
import colibreek.caserepresentation.NullCBRCase;
import colibreek.caserepresentation.NullCaseSolution;
import colibreek.domain.travelrecommender.StaticTravelDomainTestHelper;
import colibreek.domain.travelrecommender.TravelCaseDescriptor;
import colibreek.domain.travelrecommender.TravelCaseSolution;
import colibreek.reasoner.cbrreasoner.steps.explain.FindingSimilarity;
import colibreek.reasoner.cbrreasoner.steps.explain.datastructure.ColibreekCBRExplanation;
import colibreek.reasoner.cbrreasoner.steps.explain.datastructure.ExplanationForOneCase;

public class ColibreekCBRFocusTest {
	
	private ColibreekCBRFocuser focuser;
	
	@Before
	public void setUp() {
		focuser = new ColibreekCBRFocuser();
	}
	
	@Test
	public void testThatFocusReturnsAnEmptySolutionIfNooundFound() {
		ColibreekCBRExplanation emptyExplanation = new ColibreekCBRExplanation();
		CBRCase caseSolution = focuser.focus(emptyExplanation).getCase();
		Assert.assertTrue(caseSolution.getClass() == NullCBRCase.class);
		Assert.assertTrue(caseSolution.getSolution().getClass() == NullCaseSolution.class);
	}
	
	@Test
	public void testThatFocusReturnsTheSolutionIfThereIsOnlyOneFromExplain() throws OntologyAccessException, InitializingException {
		StaticTravelDomainTestHelper.establishCasebase();
		ColibreekCBRExplanation explanation = new ColibreekCBRExplanation();
		CBRCase activatedCase = createSingleCaseWithSolution("January");
		
		Set<FindingSimilarity> findingSimilarities = new HashSet<>();
		FindingSimilarity seasonSimilarity = new FindingSimilarity(TravelCaseDescriptor.SEASON, 1.0, 1.0, new Path());
		findingSimilarities.add(seasonSimilarity);
		ExplanationForOneCase explanationForOneCase = new ExplanationForOneCase(activatedCase, findingSimilarities);
		explanation.addExplanationForOneCase(explanationForOneCase);
		
		CaseSolution solution = (CaseSolution) focuser.focus(explanation).getCase().getSolution();
		CaseSolution expectedSolution = (CaseSolution) explanationForOneCase.getCase().getSolution();
		Assert.assertEquals(expectedSolution, solution);
	}

	@Test
	public void testThatFocusReturnsTheSolutionIfThereAreSeveralSolutions() throws OntologyAccessException, InitializingException {
		StaticTravelDomainTestHelper.establishCasebase();
		ColibreekCBRExplanation explanation = new ColibreekCBRExplanation();
		CBRCase activatedCase1 = createSingleCaseWithSolution("January");
		CBRCase activatedCase2 = createSingleCaseWithSolution("April");
		CBRCase activatedCase3 = createSingleCaseWithSolution("May");
		
		explanation.addExplanationForOneCase(addSingleExplanationToExplanationSet(activatedCase1, 1.0));
		explanation.addExplanationForOneCase(addSingleExplanationToExplanationSet(activatedCase2, 0.5));
		explanation.addExplanationForOneCase(addSingleExplanationToExplanationSet(activatedCase3, 0.0));		
		
		CaseSolution solution = (CaseSolution) focuser.focus(explanation).getCase().getSolution();
		CaseSolution expectedSolution = (CaseSolution) activatedCase1.getSolution();
		Assert.assertEquals(expectedSolution, solution);
	}

	private ExplanationForOneCase addSingleExplanationToExplanationSet(CBRCase activatedCase, double matchDegree) {
		FindingSimilarity findingSimilarity = new FindingSimilarity(TravelCaseDescriptor.SEASON, matchDegree, 1.0, new Path());
		Set<FindingSimilarity> findingSimilarities = new HashSet<>();
		findingSimilarities.add(findingSimilarity);
		return new ExplanationForOneCase(activatedCase, findingSimilarities);
	}
	
	private CBRCase createSingleCaseWithSolution(String season) throws OntologyAccessException {
		CBRCase activatedCase = new CBRCase();
		activatedCase.setDescription(StaticTravelDomainTestHelper.createExampleCase(season));
		TravelCaseSolution solutionForActivatedCase = new TravelCaseSolution();
		solutionForActivatedCase.setMainConcept(new Instance("I974"));
		solutionForActivatedCase.setPrice(new Instance("I454.00"));
		
		activatedCase.setSolution(solutionForActivatedCase);
		return activatedCase;
	}
}
