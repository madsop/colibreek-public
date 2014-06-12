package evaluation;

import org.junit.Test;

import colibreek.caserepresentation.CaseSolution;
import colibreek.caserepresentation.NullCaseSolution;
import colibreek.config.DomainDependentConfigurations;
import colibreek.domain.travelrecommender.TravelCaseConfigurations;
import colibreek.domain.travelrecommender.TravelCaseSolution;
import jcolibri.cbrcore.CBRCase;
import jcolibri.exception.InitializingException;

public class LeaveOneOutTravel extends LeaveOneOutTest {

	@Test
	public void testOneOutTravel() throws InitializingException {
		leaveOneOutTest();
	}

	protected boolean checkIsSuccess(CBRCase queryCase, CaseSolution caseSolution){
		TravelCaseSolution solution = castSolutionIntoTravelSpecificSolution(caseSolution);
		return solution != null && solution.getPrice() != null  && 
				solution.getPrice().toString().equals(((TravelCaseSolution) queryCase.getSolution()).getPrice().toString());
	}

	protected DomainDependentConfigurations getDomainDependentConfigurations() {
		return new TravelCaseConfigurations();
	}

	private TravelCaseSolution castSolutionIntoTravelSpecificSolution(CaseSolution caseSolution) {
		if (caseSolution instanceof NullCaseSolution) { return new TravelCaseSolution(); }
		return (TravelCaseSolution) caseSolution; 
	}
}