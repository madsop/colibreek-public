package colibreek.reasoner.cbrreasoner.steps.explain;

import org.junit.Test;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.StmtIterator;

import jcolibri.cbrcore.CBRCaseBase;
import jcolibri.datatypes.Instance;
import jcolibri.exception.InitializingException;
import jcolibri.exception.OntologyAccessException;
import junit.framework.Assert;
import colibreek.TestHelper;
import colibreek.caserepresentation.CaseDescription;
import colibreek.caserepresentation.Finding;
import colibreek.domain.mechanicalcase.MechanicalCaseConfigurations;
import colibreek.domain.mechanicalcase.MechanicalCaseDescriptor;
import colibreek.domain.mechanicalcase.MechanicalDomainTestHelper;
import colibreek.domain.travelrecommender.StaticTravelDomainTestHelper;
import colibreek.domain.travelrecommender.TravelCaseDescriptor;
import colibreek.reasoner.cbrreasoner.steps.activate.ActivatedAreaAndCases;
import colibreek.reasoner.cbrreasoner.steps.activate.ColibreekCBRActivator;
import colibreek.reasoner.cbrreasoner.steps.activate.ActivationSpreader;
import colibreek.reasoner.cbrreasoner.steps.activate.ThresholdMatcher;
import colibreek.reasoner.cbrreasoner.steps.explain.CaseSimilarityComputer;
import colibreek.reasoner.cbrreasoner.steps.explain.DateTimeSimilarityComputer;
import colibreek.reasoner.cbrreasoner.steps.explain.FindingSimilarity;
import colibreek.reasoner.cbrreasoner.steps.explain.NumberSimilarityComputer;
import colibreek.reasoner.cbrreasoner.steps.explain.PathHandlerFactory;
import colibreek.reasoner.cbrreasoner.steps.explain.CaseSimilarityComputerFactory;

public class ActivatorExplainerIntegrationTest {
	private CBRCaseBase casebase;
	private StmtIterator allStatements;
	private CaseDescription caseDescription;
	private OntModel model;
	private CaseSimilarityComputer similarityComputer;

	public void mechanicalSetUp() throws InitializingException, OntologyAccessException {
		casebase = MechanicalDomainTestHelper.establishCasebase();
		caseDescription = MechanicalDomainTestHelper.setUpQuery();
		commonSetUp();
	}

	private void travelSetUp() throws InitializingException, OntologyAccessException {
		casebase = StaticTravelDomainTestHelper.establishCasebase();
		caseDescription = StaticTravelDomainTestHelper.createExampleCase("July");
		commonSetUp();
	}

	private void commonSetUp() {
		allStatements = TestHelper.getAllStatementsFromOWLFileAsIterator();
		model = ModelFactory.createOntologyModel();
	}

	private void getSimilarityComputer() {
		MechanicalCaseConfigurations mechanicalCaseConfigurations = new MechanicalCaseConfigurations();
		CaseSimilarityComputerFactory similarityComputerFactory = new CaseSimilarityComputerFactory(new PathHandlerFactory(mechanicalCaseConfigurations), new DateTimeSimilarityComputer(mechanicalCaseConfigurations), new NumberSimilarityComputer());
		similarityComputer = similarityComputerFactory.create(model);
	}

	@Test
	public void testThatHasDataValueGetsIncluded() throws OntologyAccessException, InitializingException {
		mechanicalSetUp();
		MechanicalCaseConfigurations mechanicalCaseConfigurations = new MechanicalCaseConfigurations();
		ColibreekCBRActivator activator = new ColibreekCBRActivator(new ThresholdMatcher(mechanicalCaseConfigurations), new ActivationSpreader(mechanicalCaseConfigurations), mechanicalCaseConfigurations);
		ActivatedAreaAndCases activatedArea = activator.activate(casebase, allStatements.toList(), caseDescription);

		model.add(activatedArea.getStatements());
		getSimilarityComputer();

		Finding findingFromActivatedCase = new Finding(new Instance("46"), 1.0);

		FindingSimilarity similarity = similarityComputer.findPathAndThenComputeSimilarity(MechanicalCaseDescriptor.EXCITERFIELDVOLTAGE, 
				findingFromActivatedCase, caseDescription.getCaseFindings().get(MechanicalCaseDescriptor.EXCITERFIELDVOLTAGE));

		Assert.assertTrue(similarity.getSimilarityValue() > 0.8);
	}

	@Test
	public void testThatHasDataValueGetsIncludedInOnlyActivate() throws OntologyAccessException, InitializingException {
		mechanicalSetUp();
		model.add(allStatements);
		getSimilarityComputer();

		Finding findingFromActivatedCase = new Finding(new Instance("42"), 1.0);

		FindingSimilarity similarity = similarityComputer.findPathAndThenComputeSimilarity(MechanicalCaseDescriptor.EXCITERFIELDVOLTAGE, 
				findingFromActivatedCase, caseDescription.getCaseFindings().get(MechanicalCaseDescriptor.EXCITERFIELDVOLTAGE));
		System.out.println(similarity.getSimilarityValue());
		Assert.assertEquals(0.94, similarity.getSimilarityValue(), 0.01);
	}

	@Test
	public void testThatGeneratorPowerAlsoWorks() throws OntologyAccessException, InitializingException {		
		mechanicalSetUp();		
		model.add(allStatements);
		getSimilarityComputer();

		Finding findingFromActivatedCase = new Finding(new Instance("2.9"), 1.0);

		FindingSimilarity similarity = similarityComputer.findPathAndThenComputeSimilarity(MechanicalCaseDescriptor.GENERATORPOWER, 
				findingFromActivatedCase, caseDescription.getCaseFindings().get(MechanicalCaseDescriptor.GENERATORPOWER));
		System.out.println(similarity.getSimilarityValue());
		Assert.assertEquals(0.2, similarity.getSimilarityValue(), 0.01);
	}

	@Test
	public void testThatNoPathIsReturnedOnSimpleNumberComparison() throws OntologyAccessException, InitializingException {
		mechanicalSetUp();
		model.add(allStatements);
		getSimilarityComputer();

		Finding findingFromActivatedCase = new Finding(new Instance("2.9"), 1.0);

		FindingSimilarity similarity = similarityComputer.findPathAndThenComputeSimilarity(MechanicalCaseDescriptor.GENERATORPOWER, 
				findingFromActivatedCase, caseDescription.getCaseFindings().get(MechanicalCaseDescriptor.GENERATORPOWER));
		Assert.assertEquals(0, similarity.getTrace().size());
	}

	@Test
	public void testThatPathIsReturnedOnNormalNonSimpleNumberComparison() throws OntologyAccessException, InitializingException {				
		travelSetUp();
		model.add(allStatements);

		getSimilarityComputer();

		Finding findingFromActivatedCase = new Finding(new Instance("July"), 1.0);

		FindingSimilarity similarity = similarityComputer.findPathAndThenComputeSimilarity(TravelCaseDescriptor.SEASON, 
				findingFromActivatedCase, caseDescription.getCaseFindings().get(TravelCaseDescriptor.SEASON));
		Assert.assertNotSame(0, similarity.getTrace().size());
	}
}
