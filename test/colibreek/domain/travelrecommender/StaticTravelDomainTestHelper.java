package colibreek.domain.travelrecommender;

import jcolibri.casebase.IDIndexedLinearCaseBase;
import jcolibri.datatypes.Instance;
import jcolibri.exception.InitializingException;
import jcolibri.exception.OntologyAccessException;
import colibreek.TestHelper;
import colibreek.domain.travelrecommender.TravelCaseDescription;

public final class StaticTravelDomainTestHelper extends TestHelper {
	private StaticTravelDomainTestHelper() { }
	
	public static final String LOCATION_OF_XML_CONFIG_FILE = "config/travelDomainOntologyConfig.xml";
	public static final String LOCATION_OF_XML_TEST_CONFIG_FILE = "config/testConfigTravelDomain.xml";
	public static final String NAMESPACE = "http://gaia.fdi.ucm.es/ontologies/vacation.owl#";
	
	public static IDIndexedLinearCaseBase establishCasebase() throws InitializingException {
		return establishCasebase(LOCATION_OF_XML_CONFIG_FILE);
	}

	public static TravelCaseDescription createExampleCase(String seasonAsText) throws OntologyAccessException {
		TravelCaseDescription caseDescription = new TravelCaseDescription();
		Instance season = new Instance(seasonAsText);
		caseDescription.setSeason(season);
		Instance accomodation = new Instance("HOLIDAYFLAT");
		caseDescription.setAccommodation(accomodation);
		Instance destination = new Instance("BLACKFOREST");		
		caseDescription.setDestination(destination);
		Instance duration = new Instance("I7");
		caseDescription.setDuration(duration);
		Instance holidayType = new Instance("ACTIVE");
		caseDescription.setHolidayType(holidayType);
		Instance numberOfPersons = new Instance("I4");
		caseDescription.setNumberOfPersons(numberOfPersons);
		Instance transportation = new Instance("CAR");
		caseDescription.setTransportation(transportation);
		return caseDescription; 
	}

	public static TravelCaseDescription createCaseDescription(String accomodation, 
			String destination, String numberOfPersons, String holidayType, 
			String transportation, String season, String duration, String mainConcept)
					throws OntologyAccessException {
		TravelCaseDescription caseDescription = new TravelCaseDescription();
		caseDescription.setAccommodation(new Instance(accomodation));
		caseDescription.setDestination(new Instance(destination));
		caseDescription.setNumberOfPersons(new Instance(numberOfPersons));
		caseDescription.setHolidayType(new Instance(holidayType));
		caseDescription.setTransportation(new Instance(transportation));
		caseDescription.setSeason(new Instance(season));
		caseDescription.setDuration(new Instance(duration));
		caseDescription.setMainConcept(new Instance(mainConcept));
		return caseDescription;
	}
}
