package colibreek.reasoner.cbrreasoner.steps.activate;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import jcolibri.casebase.IDIndexedLinearCaseBase;
import jcolibri.cbrcore.CBRCase;
import jcolibri.cbrcore.CBRCaseBase;
import jcolibri.connector.OntologyConnector;
import jcolibri.datatypes.Instance;
import jcolibri.exception.InitializingException;
import jcolibri.exception.OntologyAccessException;
import junit.framework.Assert;
import colibreek.TestHelper;
import colibreek.caserepresentation.CaseDescription;
import colibreek.config.DomainIndependentConfigurations;
import colibreek.domain.travelrecommender.StaticTravelDomainTestHelper;
import colibreek.domain.travelrecommender.TravelCaseConfigurations;
import colibreek.domain.travelrecommender.TravelCaseDescription;

import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntTools;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.ResourceFactory;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ColibreekCBRActivatorTest {
	private CBRCaseBase casebase;
	private OntologyConnector connector;
	private ColibreekCBRActivator activator;
	private TravelCaseConfigurations travelCaseConfigurations;
	
	@Before
	public void setUp() throws InitializingException {
		casebase = new IDIndexedLinearCaseBase();
		connector = new OntologyConnector();
		connector.initFromXMLfile(jcolibri.util.FileIO.findFile(StaticTravelDomainTestHelper.LOCATION_OF_XML_TEST_CONFIG_FILE));
		casebase.init(connector);

		travelCaseConfigurations = new TravelCaseConfigurations();
		activator = new ColibreekCBRActivator(new ThresholdMatcher(travelCaseConfigurations), new ActivationSpreader(travelCaseConfigurations), travelCaseConfigurations);
	}
	
		@Test
	public void testActivateExists() {
		activator.activate(casebase, new ArrayList<Statement>(), new TravelCaseDescription());
		Assert.assertNotNull(activator);
	}

		@Test
	public void testEmptyCasebaseIsNoProblem() {
		List <Statement> returnvalue = activator.activate(casebase, new ArrayList<Statement>(), new TravelCaseDescription()).getStatements();
		Assert.assertEquals(new ArrayList<Statement>(), returnvalue);
	}
 
	@Test
	public void testActivateTheEntireNetworWhenFedWithLessThanNStatements() {
		StmtIterator iterator = TestHelper.getAllStatementsFromOWLFileAsIterator();

		List<Statement> statements = new ArrayList<>();
		while (iterator.hasNext() && statements.size() < travelCaseConfigurations.getMaxSizeBeforeReturnEntireNetwork() - 1) {
			statements.add(iterator.next());
		}

		List<Statement> returnvalue = activator.activate(casebase, statements, new TravelCaseDescription()).getStatements();
		Assert.assertEquals(statements.size(), returnvalue.size());
	}

	@Test
	public void testNotActivateTheEntireNetworWhenFedWithMoreThanNStatements() {
		StmtIterator iterator = TestHelper.getAllStatementsFromOWLFileAsIterator();

		List<Statement> statements = new ArrayList<>();
		while (iterator.hasNext() && statements.size() < travelCaseConfigurations.getMaxSizeBeforeReturnEntireNetwork() + 1) {
			statements.add(iterator.next());
		}

		List<Statement> returnvalue = activator.activate(casebase, statements, new TravelCaseDescription()).getStatements();
		Set<Statement> returnValuesAsSet = new HashSet<>();
		returnValuesAsSet.addAll(returnvalue);
		Assert.assertNotSame(statements, returnValuesAsSet);
	}

	@Test
	public void activatedNetworkShouldBeOneGraph() throws OntologyAccessException {
		List<Statement> statements = TestHelper.getAllStatementsFromOWLFileAsIterator().toList();

		TravelCaseDescription caseDescription = new TravelCaseDescription();
		Instance season = new Instance("WINTER");
		caseDescription.setSeason(season);
		Instance accomodation = new Instance("HOLIDAYFLAT");
		caseDescription.setAccommodation(accomodation);
		
		activator.activate(casebase, statements, caseDescription);
		OntModel model = TestHelper.getOntModel();
		
		List<OntClass> roots = OntTools.namedHierarchyRoots(model);
		Resource thingNode = ResourceFactory.createResource(DomainIndependentConfigurations.TOP_NODE_URI);
		for (OntClass root : roots) {
			assureThatOnlySuperNodeIsThing(thingNode, root);
		}
	}

	private void assureThatOnlySuperNodeIsThing(Resource thingNode,	OntClass root) {
		Assert.assertTrue(root.hasSuperClass(thingNode));
		ExtendedIterator<OntClass> it = root.listSuperClasses();
		while (it.hasNext()) {
			Assert.assertTrue(it.next().getURI().equals(DomainIndependentConfigurations.TOP_NODE_URI));
		}
	}

	@Test
	public void testAllDirectFindingsShouldBeActivated() throws OntologyAccessException {
		List<Statement> statements = TestHelper.getAllStatementsFromOWLFileAsIterator().toList();

		TravelCaseDescription caseDescription = new TravelCaseDescription();
		Instance season = new Instance("January");
		caseDescription.setSeason(season);
		Instance accomodation = new Instance("HOLIDAYFLAT");
		caseDescription.setAccommodation(accomodation);
		Instance destination = new Instance("BLACKFOREST");		
		caseDescription.setDestination(destination);
		Instance duration = new Instance("I7");
		caseDescription.setDuration(duration);
		Instance holidayType = new Instance("NO_RELAX");
		caseDescription.setHolidayType(holidayType);
		Instance numberOfPersons = new Instance("I4");
		caseDescription.setNumberOfPersons(numberOfPersons);
		Instance transportation = new Instance("CAR");
		caseDescription.setTransportation(transportation);

		List<Statement> activatedParts = activator.activate(casebase, statements, caseDescription).getStatements();

		Assert.assertNotSame(statements, activatedParts);
	
		Assert.assertTrue(resultContainsResource(activatedParts, season));
		Assert.assertTrue(resultContainsResource(activatedParts, accomodation));
		Assert.assertTrue(resultContainsResource(activatedParts, destination));
		Assert.assertTrue(resultContainsResource(activatedParts, duration));
		Assert.assertTrue(resultContainsResource(activatedParts, holidayType));
		Assert.assertTrue(resultContainsResource(activatedParts, numberOfPersons));
		Assert.assertTrue(resultContainsResource(activatedParts, transportation));
	}
	
	
	@Test
	public  void testThatCasesInActivatedNetworkIsFoundAndSeparated() throws OntologyAccessException {		
		List<Statement> statements = TestHelper.getAllStatementsFromOWLFileAsIterator().toList();
		CaseDescription caseDescription = StaticTravelDomainTestHelper.createExampleCase("April");

		ActivatedAreaAndCases activatedArea = activator.activate(casebase, statements, caseDescription);
		Set<CBRCase> foundCasesInActivatedNetwork = activatedArea.getCases();
		
		System.out.println("CASES FOUND: " + foundCasesInActivatedNetwork); 
		
		Assert.assertTrue(1 <= foundCasesInActivatedNetwork.size());
		Assert.assertTrue(2 >= foundCasesInActivatedNetwork.size());
	}
	
	@Test
	public  void testThatOnlyCasesWhichMatchAboveACertainThresholdIsReturned() throws OntologyAccessException {
		ThresholdMatcher thresholdMatcher = new ThresholdMatcher(travelCaseConfigurations);
		activator = new ColibreekCBRActivator(thresholdMatcher, new ActivationSpreader(travelCaseConfigurations), travelCaseConfigurations);
		
		CaseDescription caseDescription = StaticTravelDomainTestHelper.createExampleCase("April");
		ActivatedAreaAndCases activatedArea = activator.activate(casebase, TestHelper.getAllStatementsFromOWLFileAsIterator().toList(), caseDescription);
		
		for (CBRCase cbrcase : activatedArea.getCases()) {
			Assert.assertTrue(thresholdMatcher.findRelationStrengthBetweenTwoCases(caseDescription, (CaseDescription) cbrcase.getDescription()) >= travelCaseConfigurations.getMatchingThresholdForActivationOfCases());
		}
	}

	@Test
	public void testThatLinkToSpringIsActivatedWhenMayIsActivated() throws OntologyAccessException {
		TravelCaseDescription caseDescription = StaticTravelDomainTestHelper.createExampleCase("April");
		caseDescription.setSeason(new Instance("May"));
		ActivatedAreaAndCases activatedArea = activator.activate(casebase, TestHelper.getAllStatementsFromOWLFileAsIterator().toList(), caseDescription);
		Assert.assertTrue(isActivatedAreaContainingElementAsSubject(activatedArea.getActivatedStatements(), new Instance("May")));
		Assert.assertTrue(isLinkBetween(activatedArea.getActivatedStatements(), new Instance("May"), new Instance("SPRING")));
	}
	
	@Test
	public void testThatSeasonIsActivatedWhenMayIsActivated() throws OntologyAccessException {
		TravelCaseDescription caseDescription = new TravelCaseDescription();
		caseDescription.setSeason(new Instance("May"));
		ActivatedAreaAndCases activatedArea = activator.activate(casebase, TestHelper.getAllStatementsFromOWLFileAsIterator().toList(), caseDescription);
		Assert.assertTrue(isActivatedAreaContainingElementAsSubject(activatedArea.getActivatedStatements(), new Instance("SPRING")));
		Assert.assertTrue(isLinkBetween(activatedArea.getActivatedStatements(), new Instance("SPRING"), new Instance("SEASON")));
	}
	
	@Test
	public void testThatActivationOfMayActivatesTheSeasonCBRIndexRelation() throws OntologyAccessException {
		TravelCaseDescription caseDescription = new TravelCaseDescription();
		caseDescription.setSeason(new Instance("May"));
		ActivatedAreaAndCases activatedArea = activator.activate(casebase, TestHelper.getAllStatementsFromOWLFileAsIterator().toList(), caseDescription);
		Assert.assertTrue(isActivatedAreaContainingElementAsSubject(activatedArea.getActivatedStatements(), new Instance("SEASON")));
		Assert.assertTrue(isLinkBetween(activatedArea.getActivatedStatements(), new Instance("SEASON"), new Instance("CBR_INDEX")));
		Assert.assertFalse(isLinkBetween(activatedArea.getActivatedStatements(), new Instance("AIR"), new Instance("SEASON")));
	}

	@Test
	public void testThatActivationOfMayDoesNotActivateJanuaryWinterRelation() throws OntologyAccessException {
		TravelCaseDescription caseDescription = new TravelCaseDescription();
		caseDescription.setSeason(new Instance("May"));
		ActivatedAreaAndCases activatedArea = activator.activate(casebase, TestHelper.getAllStatementsFromOWLFileAsIterator().toList(), caseDescription);
		Assert.assertFalse(isLinkBetween(activatedArea.getActivatedStatements(), new Instance("January"), new Instance("WINTER")));
	}
	
	@Test
	public void testThatActivationOfMayCaseActivatesTheLinkFromAprilToSpring() throws OntologyAccessException {
		TravelCaseDescription caseDescription = StaticTravelDomainTestHelper.createExampleCase("April");
		caseDescription.setSeason(new Instance("May"));
		ActivatedAreaAndCases activatedArea = activator.activate(casebase, TestHelper.getAllStatementsFromOWLFileAsIterator().toList(), caseDescription);
		
		Assert.assertTrue(isActivatedAreaContainingElementAsSubject(activatedArea.getActivatedStatements(), new Instance("April")));
		Assert.assertTrue(isLinkBetween(activatedArea.getActivatedStatements(), new Instance("April"), new Instance("SPRING")));
		Assert.assertFalse(isLinkBetween(activatedArea.getActivatedStatements(), new Instance("July"), new Instance("SUMMER")));
	}
	
	@Test
	public void testThatSubclassActivationPropagatesAllTheWayUp() throws OntologyAccessException {
		TravelCaseDescription caseDescription = StaticTravelDomainTestHelper.createExampleCase("April");
		caseDescription.setSeason(new Instance("May"));
		ActivatedAreaAndCases activatedArea = activator.activate(casebase, TestHelper.getAllStatementsFromOWLFileAsIterator().toList(), caseDescription);
		
		Instance destinationInstance = new Instance("DESTINATION");
		Instance cbrIndexInstance = new Instance("CBR_INDEX");
		
		Instance seasonInstance = new Instance("SEASON");
		
		boolean isLinkBetweenDestinationnAndCBRIndex = isLinkBetween(activatedArea.getActivatedStatements(), destinationInstance, cbrIndexInstance);
		boolean isLinkBetweenSeasonAndCBRIndex = isLinkBetween(activatedArea.getActivatedStatements(), seasonInstance, cbrIndexInstance);
		
		Assert.assertTrue(isLinkBetweenDestinationnAndCBRIndex);
		Assert.assertTrue(isLinkBetweenSeasonAndCBRIndex);
	}
	
	@Test
	public void testThatSubclassActivationPropagatesAllTheWayUpWithFullCasebase() throws OntologyAccessException, InitializingException {
		connector = new OntologyConnector();
		connector.initFromXMLfile(jcolibri.util.FileIO.findFile(StaticTravelDomainTestHelper.LOCATION_OF_XML_CONFIG_FILE));
		casebase.init(connector);
		
		TravelCaseDescription caseDescription = StaticTravelDomainTestHelper.createExampleCase("April");
		caseDescription.setSeason(new Instance("May"));
		ActivatedAreaAndCases activatedArea = activator.activate(casebase, TestHelper.getAllStatementsFromOWLFileAsIterator().toList(), caseDescription);
		
		Instance destinationInstance = new Instance("DESTINATION");
		Instance cbrIndexInstance = new Instance("CBR_INDEX");
		
		Instance seasonInstance = new Instance("SEASON");
		
		boolean isLinkBetweenDestinationnAndCBRIndex = isLinkBetween(activatedArea.getActivatedStatements(), destinationInstance, cbrIndexInstance);
		boolean isLinkBetweenSeasonAndCBRIndex = isLinkBetween(activatedArea.getActivatedStatements(), seasonInstance, cbrIndexInstance);
		
		Assert.assertTrue(isLinkBetweenDestinationnAndCBRIndex);
		Assert.assertTrue(isActivatedAreaContainingElementAsSubject(activatedArea.getActivatedStatements(), seasonInstance));
		Assert.assertTrue(isLinkBetweenSeasonAndCBRIndex);
	}
	
	
	
	private boolean resultContainsResource(Collection<Statement> activatedParts,  Instance instance) {
		String instanceName = StaticTravelDomainTestHelper.NAMESPACE + instance.toString();
		for (Statement statement : activatedParts) {
			if (statement.getSubject().toString().equals(instanceName) || statement.getObject().toString().equals(instanceName)) {
				return true;
			}
		}
		return false;
	}

	private boolean isActivatedAreaContainingElementAsSubject(Map<RDFNode, Set<Statement>> statementsPerResource, Instance instance) {
		RDFNode keyR = ResourceFactory.createResource(StaticTravelDomainTestHelper.NAMESPACE + instance.toString());
		
		Set<Statement> statements = statementsPerResource.get(keyR);
		return statements != null;
	}
		
	private boolean isLinkBetween(Map<RDFNode, Set<Statement>> statementsPerResource, Instance child, Instance parent) {
		if (!isActivatedAreaContainingElementAsSubject(statementsPerResource, child)) { return false; }
		RDFNode res = ResourceFactory.createResource(StaticTravelDomainTestHelper.NAMESPACE + child);
		Set<Statement> statementsWithChildAsSubject = statementsPerResource.get(res);

		if (statementsWithChildAsSubject == null) { return false; }
		
		for (Statement statement : statementsWithChildAsSubject) {
			if (statement.getObject().asNode().getLocalName().equals(parent.toString())) {
				return true;
			}
		}
		return false;
	}

	@After
	public void tearDown() {
		casebase.close();
		connector.close();
	}
}
