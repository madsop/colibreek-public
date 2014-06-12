package colibreek.domain.travelrecommender;

import java.util.HashMap;
import java.util.Map;

import jcolibri.cbrcore.Attribute;
import jcolibri.datatypes.Instance;
import colibreek.caserepresentation.CaseDescription;
import colibreek.caserepresentation.CaseDescriptor;
import colibreek.caserepresentation.Finding;


public class TravelCaseDescription extends CaseDescription { 

	Instance  mainConcept;
	Instance  HolidayType;
	Instance  NumberOfPersons;
	Instance  Destination;
	Instance  Transportation;
	Instance  Duration;
	Instance  Season;
	Instance  Accommodation;
	
	public Instance getAccommodation() {
		return Accommodation;
	}

	public void setAccommodation(Instance accomodation) {
		Accommodation = accomodation;
	}

	public Instance getCaseId() {
		return mainConcept;
	}

	public void setMainConcept(Instance mc) {
		this.mainConcept = mc;
	}

	public Instance getMainConcept() {
		return mainConcept;
	}

	public Instance getDuration() {
		return Duration;
	}

	public void setDuration(Instance duration) {
		Duration = duration;
	}

	public Instance getHolidayType() {
		return HolidayType;
	}

	public void setHolidayType(Instance holidayType) {
		HolidayType = holidayType;
	}

	public Instance getNumberOfPersons() {
		return NumberOfPersons;
	}

	public void setNumberOfPersons(Instance numberOfPersons) {
		NumberOfPersons = numberOfPersons;
	}

	public Instance getDestination() {
		return Destination;
	}

	public void setDestination(Instance destination) {
		Destination = destination;
	}

	public Instance getSeason() {
		return Season;
	}

	public void setSeason(Instance season) {
		Season = season;
	}

	public Instance getTransportation() {
		return Transportation;
	}

	public void setTransportation(Instance transportation) {
		Transportation = transportation;
	}
	
	public Attribute getIdAttribute() {
		return new Attribute("mainConcept", this.getClass());
	}
	
	public Map<CaseDescriptor, Finding> getCaseFindings() {
		Map<CaseDescriptor, Finding> findings = new HashMap<>();
		findings.put(TravelCaseDescriptor.ACCOMODATION, new Finding(Accommodation, 1.0));
		findings.put(TravelCaseDescriptor.DESTINATION, new Finding(Destination, 1.0));
		findings.put(TravelCaseDescriptor.DURATION, new Finding(Duration, 1.0));
		findings.put(TravelCaseDescriptor.HOLIDAYTYPE, new Finding(HolidayType, 1.0));
		findings.put(TravelCaseDescriptor.NUMBEROFPERSONS, new Finding(NumberOfPersons, 1.0));
		findings.put(TravelCaseDescriptor.SEASON, new Finding(Season, 1.0));
		findings.put(TravelCaseDescriptor.TRANSPORTATION, new Finding(Transportation, 1.0));
		return findings;
	}
}