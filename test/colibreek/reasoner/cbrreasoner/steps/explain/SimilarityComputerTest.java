package colibreek.reasoner.cbrreasoner.steps.explain;

import java.util.List;

import jcolibri.datatypes.Instance;
import jcolibri.exception.InitializingException;
import jcolibri.exception.OntologyAccessException;
import junit.framework.Assert;

import org.junit.Test;

import colibreek.TestHelper;
import colibreek.caserepresentation.Finding;
import colibreek.config.DomainDependentConfigurations;
import colibreek.domain.mechanicalcase.MechanicalCaseConfigurations;
import colibreek.domain.mechanicalcase.MechanicalCaseDescriptor;
import colibreek.domain.mechanicalcase.MechanicalDomainTestHelper;
import colibreek.domain.travelrecommender.StaticTravelDomainTestHelper;
import colibreek.domain.travelrecommender.TravelCaseConfigurations;
import colibreek.domain.travelrecommender.TravelCaseDescriptor;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Statement;

public class SimilarityComputerTest {
	private CaseSimilarityComputer similarityComputer;

	private void mechanicalSetUp() throws InitializingException {
		MechanicalDomainTestHelper.establishCasebase();
		setUp(new MechanicalCaseConfigurations());
	}
	
	private void travelDomainSetUp() throws InitializingException {
		StaticTravelDomainTestHelper.establishCasebase();
		setUp(new TravelCaseConfigurations()); 
	}

	private void setUp(DomainDependentConfigurations domainDependentConfigurations) {
		List<Statement> statements = TestHelper.getAllStatementsFromOWLFileAsIterator().toList();
		OntModel model = ModelFactory.createOntologyModel();
		model.add(statements);
		CaseSimilarityComputerFactory similarityComputerFactory = new CaseSimilarityComputerFactory(new PathHandlerFactory(domainDependentConfigurations), new DateTimeSimilarityComputer(domainDependentConfigurations), new NumberSimilarityComputer());
		similarityComputer = similarityComputerFactory.create(model);
	}

	@Test
	public void testThatNonCloselyRelatedGeneratorPowersAreNotConsideredSimilar() throws OntologyAccessException, InitializingException {
		mechanicalSetUp();
		Finding generatorPower50 = new Finding(new Instance("50"), 1.0);
		Finding generatorPower0 = new Finding(new Instance("0"), 1.0);
		

		FindingSimilarity similarity = similarityComputer.findPathAndThenComputeSimilarity(MechanicalCaseDescriptor.GENERATORPOWER, generatorPower50, generatorPower0);

		Assert.assertEquals(0.0, similarity.getSimilarityValue(), 0.001 );
	}

	@Test
	public void testThatCloselyRelatedGeneratorPowersAreConsideredSimilar() throws OntologyAccessException, InitializingException {
		mechanicalSetUp();
		Finding generatorPower46 = new Finding(new Instance("46"), 1.0);
		Finding generatorPower47 = new Finding(new Instance("47"), 1.0);

		FindingSimilarity similarity = similarityComputer.findPathAndThenComputeSimilarity(MechanicalCaseDescriptor.GENERATORPOWER, generatorPower46, generatorPower47);

		Assert.assertEquals(0.98, similarity.getSimilarityValue(), 0.001);
	}

	@Test
	public void testThatSimilarityIsNotAffectedByRelevanceFactor() throws OntologyAccessException, InitializingException {
		travelDomainSetUp();
		Finding fromActivatedCase = new Finding(new Instance("May"), 0.0);
		Finding fromNewCase = new Finding(new Instance("April"), 0.0);

		FindingSimilarity computeSimilarity = similarityComputer.findPathAndThenComputeSimilarity(TravelCaseDescriptor.SEASON, fromActivatedCase, fromNewCase);
		Assert.assertEquals(0.5, computeSimilarity.getSimilarityValue(), 0.001);
	}


	@Test
	public void testThatSimilarityIsNotAffectedByRelevanceFactorEvenWhen0Point5() throws OntologyAccessException, InitializingException {
		travelDomainSetUp();
		Finding fromActivatedCase = new Finding(new Instance("May"), 0.5);
		Finding fromNewCase = new Finding(new Instance("April"), 0.5);

		FindingSimilarity computeSimilarityHalf = similarityComputer.findPathAndThenComputeSimilarity(TravelCaseDescriptor.SEASON, fromActivatedCase, fromNewCase);
		Assert.assertEquals(0.5, computeSimilarityHalf.getSimilarityValue(), 0.001);
	}

	@Test
	public void testThatRelevanceFactorMattersWhenSimFacIs1() throws OntologyAccessException, InitializingException {
		travelDomainSetUp();
		Finding fromActivatedCase = new Finding(new Instance("May"), 1.0);
		Finding fromNewCase = new Finding(new Instance("April"), 1.0);

		FindingSimilarity computeSimilarity = similarityComputer.findPathAndThenComputeSimilarity(TravelCaseDescriptor.SEASON, fromActivatedCase, fromNewCase);
		Assert.assertEquals(0.5, computeSimilarity.getSimilarityValue(), 0.001);
	}

	@Test
	public void testThatNonRelatedFindingsHaveSimilarity0AndNoPathBetween() throws OntologyAccessException, InitializingException {
		travelDomainSetUp();
		Finding fromActivatedCase = new Finding(new Instance("May"), 1.0);		
		Finding fromNewCase = new Finding(new Instance("January"), 1.0);

		FindingSimilarity computeSimilarity = similarityComputer.findPathAndThenComputeSimilarity(TravelCaseDescriptor.SEASON, fromActivatedCase, fromNewCase);
		Assert.assertEquals(0.0, computeSimilarity.getSimilarityValue(), 0.001);
		Assert.assertEquals(0, computeSimilarity.getTrace().size());
	}
	
	@Test
	public void testThatTwoTimestampsCloseToEachOtherHasSimilarityMoreThanZero() throws OntologyAccessException, InitializingException {
		mechanicalSetUp();
		
		Finding fromActivatedCase = new Finding(new Instance("2013-04-15T00:00:00"), 0.5);
		Finding fromNewCase = new Finding(new Instance("2013-04-16T00:10:00"), 0.5);
		
		FindingSimilarity computeSimilarity = similarityComputer.findPathAndThenComputeSimilarity(MechanicalCaseDescriptor.TIMESTAMP, fromActivatedCase, fromNewCase);
		
		Assert.assertFalse(Math.abs(0.0 - computeSimilarity.getSimilarityValue()) < 0.001);
	}
	
	@Test
	public void testThatTwoTimestampsWithOnlyDifferentMinutes() throws OntologyAccessException, InitializingException {
		mechanicalSetUp();
		
		Finding fromActivatedCase = new Finding(new Instance("2013-04-15T00:00:00"), 0.5);
		Finding fromNewCase = new Finding(new Instance("2013-04-15T00:10:00"), 0.5);
		
		FindingSimilarity computeSimilarity = similarityComputer.findPathAndThenComputeSimilarity(MechanicalCaseDescriptor.TIMESTAMP, fromActivatedCase, fromNewCase);

		Assert.assertEquals(0.85, computeSimilarity.getSimilarityValue(), 0.001);
	}
	
	@Test
	public void testThatTwoTimestampsWithOnlySameYearAndMonthHaveSomeSimilarity() throws OntologyAccessException, InitializingException {
		mechanicalSetUp();
		
		Finding fromActivatedCase = new Finding(new Instance("2013-04-14T20:10:00"), 0.5);
		Finding fromNewCase = new Finding(new Instance("2013-04-15T00:00:00"), 0.5);
		
		FindingSimilarity computeSimilarity = similarityComputer.findPathAndThenComputeSimilarity(MechanicalCaseDescriptor.TIMESTAMP, fromActivatedCase, fromNewCase);

		Assert.assertEquals(0.45, computeSimilarity.getSimilarityValue(), 0.001);
	}
	
	@Test
	public void testThatNumberComparisonIsRun() throws OntologyAccessException, InitializingException {
		mechanicalSetUp();
		
		Finding fromActivatedCase = new Finding(new Instance("292.5"), 1.0);
		Finding fromNewCase = new Finding(new Instance("295.3"), 1.0);
		
		FindingSimilarity computeSimilarity = similarityComputer.findPathAndThenComputeSimilarity(MechanicalCaseDescriptor.PHASEBCURRENT, fromActivatedCase, fromNewCase);
		
		Assert.assertEquals(0.963, computeSimilarity.getSimilarityValue(), 0.001);
	}
	
	@Test
	public void testThatNumberComparisonIsRunWithNewValues() throws OntologyAccessException, InitializingException {
		mechanicalSetUp();
		
		Finding fromActivatedCase = new Finding(new Instance("294.9"), 1.0);
		Finding fromNewCase = new Finding(new Instance("336.5"), 1.0);
		
		FindingSimilarity computeSimilarity = similarityComputer.findPathAndThenComputeSimilarity(MechanicalCaseDescriptor.PHASEBCURRENT, fromActivatedCase, fromNewCase);
		
		Assert.assertEquals(0.4511, computeSimilarity.getSimilarityValue(), 0.001);
	}
}