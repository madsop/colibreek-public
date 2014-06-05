package colibreek.caserepresentation;

import java.util.Map;

import jcolibri.exception.InitializingException;
import jcolibri.exception.OntologyAccessException;
import junit.framework.Assert;

import org.junit.Test;

import colibreek.caserepresentation.CaseDescriptor;
import colibreek.caserepresentation.Finding;
import colibreek.domain.travelrecommender.StaticTravelDomainTestHelper;
import colibreek.domain.travelrecommender.TravelCaseDescription;
import colibreek.domain.travelrecommender.TravelCaseDescriptor;

public class CaseDescriptionTest {

	@Test
	public void testThatOneMethodCanGiveAllValuesAsAMap() throws OntologyAccessException, InitializingException {
		StaticTravelDomainTestHelper.establishCasebase();
		
		String accomodation = "HOLIDAYFLAT";
		
		TravelCaseDescription caseDescription = StaticTravelDomainTestHelper.createCaseDescription(accomodation, "FANO", "I5", "RECREATION", "CAR", "May", "I7", "I974");
		
		Map<CaseDescriptor, Finding> values = caseDescription.getCaseFindings();
		Assert.assertEquals(values.get(TravelCaseDescriptor.ACCOMODATION).getInstanceValue().toString().toLowerCase(), accomodation.toLowerCase());	
	}
	
	@Test
	public void testThatGetNonNullValuesReturnsOnlyValuesNotNull() throws InitializingException, OntologyAccessException {
		StaticTravelDomainTestHelper.establishCasebase();
		
		TravelCaseDescription caseDescription = StaticTravelDomainTestHelper.createCaseDescription("HOLIDAYFLAT", "FANO", "I5", "RECREATION", "CAR", "May", "I7", "I974");
		caseDescription.setAccommodation(null);
		
		Map<CaseDescriptor, Finding> values = caseDescription.getNonNullCaseFindings();
		Assert.assertFalse(values.containsKey(TravelCaseDescriptor.ACCOMODATION));
		Assert.assertTrue(values.containsKey(TravelCaseDescriptor.DURATION));
	}
}
