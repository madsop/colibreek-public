package colibreek.reasoner.cbrreasoner.steps.explain.datastructure;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import com.hp.hpl.jena.ontology.OntTools.Path;

import jcolibri.exception.InitializingException;
import junit.framework.Assert;
import colibreek.domain.mechanicalcase.MechanicalCaseDescriptor;
import colibreek.domain.mechanicalcase.MechanicalDomainTestHelper;
import colibreek.reasoner.cbrreasoner.steps.explain.FindingSimilarity;
import colibreek.reasoner.cbrreasoner.steps.explain.datastructure.ExplanationForOneCase;

public class ExplanationForOneCaseTest {

	@Test
	public void testThatGetSimilaritySumsAccordingToRelevancefactors() throws InitializingException {
		MechanicalDomainTestHelper.establishCasebase();
		
		FindingSimilarity findingSimilarity = new FindingSimilarity(MechanicalCaseDescriptor.GENERATORID, 1.0, 0.5, new Path());
		FindingSimilarity findingSimilarity2 = new FindingSimilarity(MechanicalCaseDescriptor.EXCITERFIELDVOLTAGE, 0.0, 0.0, new Path());
		Set<FindingSimilarity> findingSimilarities = new HashSet<>();
		findingSimilarities.add(findingSimilarity);
		findingSimilarities.add(findingSimilarity2);
		
		ExplanationForOneCase explanationForOneCase = new ExplanationForOneCase(null, findingSimilarities);
		
		Assert.assertEquals(1.0, explanationForOneCase.getSimilarity(), 0.001);
	}
}
