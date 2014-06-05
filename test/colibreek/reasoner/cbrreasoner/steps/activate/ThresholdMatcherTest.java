package colibreek.reasoner.cbrreasoner.steps.activate;

import jcolibri.cbrcore.CBRCase;
import jcolibri.datatypes.Instance;
import jcolibri.exception.InitializingException;
import jcolibri.exception.OntologyAccessException;
import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import colibreek.caserepresentation.CaseDescription;
import colibreek.domain.travelrecommender.StaticTravelDomainTestHelper;
import colibreek.domain.travelrecommender.TravelCaseConfigurations;
import colibreek.domain.travelrecommender.TravelCaseDescription;

public class ThresholdMatcherTest {
	private ThresholdMatcher thresholdMatcher;
	
	@Before
	public void setUp() throws InitializingException {
		TravelCaseConfigurations travelCaseConfigurations = new TravelCaseConfigurations();
		thresholdMatcher = new ThresholdMatcher(travelCaseConfigurations);
		StaticTravelDomainTestHelper.establishCasebase();
	}
	
	@Test
	public  void testTotallyDifferentAndTotallyEqualCasesForRelationStrength() throws OntologyAccessException {
		CBRCase case1 = createCase("May");
		CBRCase case2 = createCase("January");
		CaseDescription caseDescription1 = (CaseDescription) case1.getDescription();
		CaseDescription caseDescription2 = (CaseDescription) case2.getDescription();
		
		Assert.assertEquals(1.00, thresholdMatcher.findRelationStrengthBetweenTwoCases(caseDescription1, caseDescription1));
		Assert.assertTrue(1.00 != thresholdMatcher.findRelationStrengthBetweenTwoCases(caseDescription1, caseDescription2));	
	}
	
	@Test
	public  void testThatCasesWithSomeSimilarityReturnANonZeroNon1Value() throws OntologyAccessException {		
		CBRCase case1 = createCase("May", "RECREATION");
		CBRCase case2 = createCase("January", "RECREATION");
		CaseDescription caseDescription1 = (CaseDescription) case1.getDescription();
		CaseDescription caseDescription2 = (CaseDescription) case2.getDescription();
		
		double numberOfEqualFindings = 0.5;
		
		Assert.assertEquals(numberOfEqualFindings, thresholdMatcher.findRelationStrengthBetweenTwoCases(caseDescription1, caseDescription2));		
	}
	
	@Test
	public void testWithSomeNullValues() throws OntologyAccessException {
		CaseDescription newCaseDescription = StaticTravelDomainTestHelper.createExampleCase("May");
		TravelCaseDescription oldCaseDescription = new TravelCaseDescription();
		oldCaseDescription.setSeason(null);
		
		double match = thresholdMatcher.findRelationStrengthBetweenTwoCases(newCaseDescription, oldCaseDescription);
		Assert.assertEquals(0.0, match, 0.001);
	}
	
	private CBRCase createCase(String... caseValues) throws OntologyAccessException {
		CBRCase newCase = new CBRCase(); 
		TravelCaseDescription caseDescription = new TravelCaseDescription();
		caseDescription.setSeason(new Instance(caseValues[0]));
		if (caseValues.length > 1) {
			caseDescription.setHolidayType(new Instance(caseValues[1]));
		}
		newCase.setDescription(caseDescription);
		return newCase;
	}
}
