package colibreek.domain.travelrecommender;

import jcolibri.cbrcore.CBRQuery;
import jcolibri.datatypes.Instance;
import jcolibri.exception.OntologyAccessException;
import colibreek.DomainExecuter;
import colibreek.caserepresentation.CaseSolution;
import colibreek.caserepresentation.NullCaseSolution;
import colibreek.config.DomainDependentConfigurations;

public class TravelDomainExecuter extends DomainExecuter {
	private TravelCaseConfigurations travelCaseConfigurations;
	
	public TravelDomainExecuter() {
		travelCaseConfigurations = new TravelCaseConfigurations();
	}
	
	public CBRQuery setUpQuery() throws OntologyAccessException {
		CBRQuery query = new CBRQuery();
		query.setDescription(setUpCaseDescription());
		return query;
	}

	@Override
	public DomainDependentConfigurations getDomainDependentConfigurations() {
		return travelCaseConfigurations;
	}

	private TravelCaseDescription setUpCaseDescription() throws OntologyAccessException {
		TravelCaseDescription queryDesc = new TravelCaseDescription();
		queryDesc.setAccommodation(new Instance("HOLIDAYFLAT"));
		queryDesc.setDuration(new Instance("I7"));
		queryDesc.setHolidayType(new Instance("WANDERING"));
		queryDesc.setNumberOfPersons(new Instance("I4"));
		queryDesc.setSeason(new Instance("July"));
		queryDesc.setDestination(new Instance("TYROL"));
		queryDesc.setTransportation(new Instance("PLANE"));
		return queryDesc;
	}

	@Override
	public void printDomainSpecificSolutionAspects(CaseSolution caseSolution) {
		TravelCaseSolution solution = castSolutionIntoSpecificSolution(caseSolution);
		System.out.println("price: " + solution.getPrice());
	}

	public TravelCaseSolution castSolutionIntoSpecificSolution(CaseSolution caseSolution) {
		if (caseSolution instanceof NullCaseSolution) { return new TravelCaseSolution(); }
		return (TravelCaseSolution) caseSolution; 
	}
}