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
	
	
	/**
	 * @return the accomodation
	 */
	public Instance getAccommodation() {
		return Accommodation;
	}
	/**
	 * @param accomodation the accomodation to set
	 */
	public void setAccommodation(Instance accomodation) {
		Accommodation = accomodation;
	}
	/**
	 * @return the caseId
	 */
	public Instance getCaseId() {
		return mainConcept;
	}
	/**
	 * @param mc the main concept to set
	 */
	public void setMainConcept(Instance mc) {
		this.mainConcept = mc;
	}
	/**
	 * @return Returns the mainConcept.
	 */
	public Instance getMainConcept() {
		return mainConcept;
	}
	/**
	 * @return the duration
	 */
	public Instance getDuration() {
		return Duration;
	}
	/**
	 * @param duration the duration to set
	 */
	public void setDuration(Instance duration) {
		Duration = duration;
	}
	/**
	 * @return the holidayType
	 */
	public Instance getHolidayType() {
		return HolidayType;
	}
	/**
	 * @param holidayType the holidayType to set
	 */
	public void setHolidayType(Instance holidayType) {
		HolidayType = holidayType;
	}
	/**
	 * @return the numberOfPersons
	 */
	public Instance getNumberOfPersons() {
		return NumberOfPersons;
	}
	/**
	 * @param numberOfPersons the numberOfPersons to set
	 */
	public void setNumberOfPersons(Instance numberOfPersons) {
		NumberOfPersons = numberOfPersons;
	}

	/**
	 * @return the destination
	 */
	public Instance getDestination() {
		return Destination;
	}
	/**
	 * @param destination the destination to set
	 */
	public void setDestination(Instance destination) {
		Destination = destination;
	}
	/**
	 * @return the season
	 */
	public Instance getSeason() {
		return Season;
	}
	/**
	 * @param season the season to set
	 */
	public void setSeason(Instance season) {
		Season = season;
	}
	/**
	 * @return the transportation
	 */
	public Instance getTransportation() {
		return Transportation;
	}
	/**
	 * @param transportation the transportation to set
	 */
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