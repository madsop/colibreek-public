package colibreek.reasoner.cbrreasoner.steps.explain;

import java.util.Set;

import jcolibri.cbrcore.CBRCase;
import jcolibri.datatypes.Instance;
import jcolibri.exception.InitializingException;
import jcolibri.exception.OntologyAccessException;
import junit.framework.Assert;

import org.junit.Test;

import colibreek.TestHelper;
import colibreek.domain.mechanicalcase.MechanicalCaseConfigurations;
import colibreek.domain.mechanicalcase.MechanicalCaseDescription;
import colibreek.domain.mechanicalcase.MechanicalDomainTestHelper;
import colibreek.reasoner.cbrreasoner.steps.explain.datastructure.ExplanationForOneCase;

import com.hp.hpl.jena.ontology.OntModel;

public class ExplanationForOneCaseSimilarityComputerIntegrationTest {
	@Test
	public void testThatRelevanceFactorCountsWhenSummingUpSimilarity() throws OntologyAccessException, InitializingException {
		MechanicalDomainTestHelper.establishCasebase();
		
		MechanicalCaseDescription case1 = (MechanicalCaseDescription) MechanicalDomainTestHelper.setUpQuery();
		MechanicalCaseDescription case2 = (MechanicalCaseDescription) MechanicalDomainTestHelper.setUpQuery();
		case2.setExciterFieldVoltage(new Instance("0"));
		
		CBRCase case1Case = new CBRCase();
		case1Case.setDescription(case1);
		CBRCase case2Case = new CBRCase();
		case2Case.setDescription(case2);
		
		OntModel model = TestHelper.getOntModel();

		CaseSimilarityComputerFactory similarityComputerFactory = new CaseSimilarityComputerFactory(new PathHandlerFactory(new MechanicalCaseConfigurations()), null, new NumberSimilarityComputer());
		CaseSimilarityComputer similarityComputer = similarityComputerFactory.create(model);
		Set<FindingSimilarity> findingSimilarities = similarityComputer.findSimilaritiesBetweenCases(case1, case2Case);
		
		ExplanationForOneCase explanation = new ExplanationForOneCase(case1Case, findingSimilarities);
		
		Assert.assertEquals(0.88, explanation.getSimilarity(), 0.01);
	}
}