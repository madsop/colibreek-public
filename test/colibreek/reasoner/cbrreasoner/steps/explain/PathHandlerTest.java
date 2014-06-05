package colibreek.reasoner.cbrreasoner.steps.explain;

import jcolibri.exception.InitializingException;
import junit.framework.Assert;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

import colibreek.TestHelper;
import colibreek.domain.mechanicalcase.MechanicalCaseConfigurations;
import colibreek.domain.mechanicalcase.MechanicalCaseDescriptor;
import colibreek.domain.mechanicalcase.MechanicalDomainTestHelper;
import colibreek.domain.travelrecommender.StaticTravelDomainTestHelper;
import colibreek.domain.travelrecommender.TravelCaseConfigurations;
import colibreek.domain.travelrecommender.TravelCaseDescriptor;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntTools.Path;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;

public class PathHandlerTest {
	private OntModel model;
	private PathHandler pathHandler;

	@Before
	public void setUp() throws InitializingException {
		model = mock(OntModel.class);
		PathHandlerFactory pathHandlerFactory = new PathHandlerFactory(new MechanicalCaseConfigurations());
		pathHandler = pathHandlerFactory.create(model);
		MechanicalDomainTestHelper.establishCasebase();
	}
	
	
	@Test
	public void testThatNonOntClassForActivatedFindingReturnsEmptyPath() {
		String activatedFindingURI = "3.1";
		String newFindingURI = "3.2";
		
		Individual mockIndividual = mock(Individual.class);
		when(mockIndividual.listOntClasses(true)).thenReturn(mock(ExtendedIterator.class));
		when(model.getIndividual(anyString())).thenReturn(mockIndividual);
		
		Path path = pathHandler.findPathBetweenTwoFindings(MechanicalCaseDescriptor.EXCITERFIELDVOLTAGE, activatedFindingURI, newFindingURI);
		Assert.assertEquals(new Path(), path);
		verify(mockIndividual, times(0)).getOntClass(true);
	}
	
	@Test
	public void testThatNonOntClassForNewFindingReturnsEmptyPath() {
		String activatedFindingURI = MechanicalDomainTestHelper.NAMESPACE + "3.1";
		String newFindingURI = MechanicalDomainTestHelper.NAMESPACE + "3.2";
		
		Individual activatedIndividual = mock(Individual.class);
		ExtendedIterator<OntClass> iteratorForActivatedIndividual = mock(ExtendedIterator.class);
		when(iteratorForActivatedIndividual.hasNext()).thenReturn(true);
		when(activatedIndividual.listOntClasses(true)).thenReturn(iteratorForActivatedIndividual);
		
		Individual newIndividual = mock(Individual.class);
		when(newIndividual.listOntClasses(true)).thenReturn(mock(ExtendedIterator.class));

		when(model.getIndividual(activatedFindingURI)).thenReturn(activatedIndividual);
		when(model.getIndividual(newFindingURI)).thenReturn(newIndividual);
		
		Path path = pathHandler.findPathBetweenTwoFindings(MechanicalCaseDescriptor.EXCITERFIELDVOLTAGE, activatedFindingURI, newFindingURI);
		Assert.assertEquals(new Path(), path);
		verify(newIndividual, times(0)).getOntClass(true);
	}
	
	@Test
	public void testThatCategoryIsNotIncludedInThePath() throws InitializingException {
		StaticTravelDomainTestHelper.establishCasebase();
		model = TestHelper.getOntModel();
		PathHandlerFactory pathHandlerFactory = new PathHandlerFactory(new TravelCaseConfigurations());
		pathHandler = pathHandlerFactory.create(model);
		String threestars = StaticTravelDomainTestHelper.NAMESPACE + "ACTIVE";
		String holidayflat = StaticTravelDomainTestHelper.NAMESPACE + "RECREATION";
				
		Path path = pathHandler.findPathBetweenTwoFindings(TravelCaseDescriptor.HOLIDAYTYPE, threestars, holidayflat);
		
		Assert.assertEquals(new Path(), path);
	}
}