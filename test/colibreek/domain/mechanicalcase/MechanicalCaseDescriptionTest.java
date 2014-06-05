package colibreek.domain.mechanicalcase;

import java.util.Map;
import java.util.Map.Entry;

import jcolibri.exception.InitializingException;
import jcolibri.exception.OntologyAccessException;
import junit.framework.Assert;

import org.junit.Test;

import colibreek.caserepresentation.CaseDescriptor;
import colibreek.caserepresentation.Finding;

public class MechanicalCaseDescriptionTest {
	@Test
	public void testThatRelevancefactorsAreTheSameForGetFindingsAndGetNonNullFindings() throws InitializingException, OntologyAccessException {
		MechanicalDomainTestHelper.establishCasebase();
		MechanicalCaseDescription query = (MechanicalCaseDescription) MechanicalDomainTestHelper.setUpQuery();
		Map<CaseDescriptor, Finding> findings = query.getCaseFindings();
		Map<CaseDescriptor, Finding> nonNullFindings = query.getNonNullCaseFindings();
		
		for (Entry<CaseDescriptor, Finding> nonNullFindingEntry : nonNullFindings.entrySet()) {
			Finding nonNullFinding = nonNullFindingEntry.getValue();
			Finding allFinding = findings.get(nonNullFindingEntry.getKey());
			Assert.assertEquals(nonNullFinding.getRelevanceFactor(), allFinding.getRelevanceFactor());
		}
		
	}

}
