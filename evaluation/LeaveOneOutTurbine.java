package evaluation;

import jcolibri.cbrcore.CBRCase;
import jcolibri.exception.InitializingException;

import org.junit.Test;

import colibreek.caserepresentation.CaseSolution;
import colibreek.caserepresentation.NullCaseSolution;
import colibreek.config.DomainDependentConfigurations;
import colibreek.domain.turbineagbcase.TurbineAGBCaseConfigurations;
import colibreek.domain.turbineagbcase.TurbineAGBCaseSolution;

public class LeaveOneOutTurbine extends LeaveOneOutTest {

	@Test
	public void testOneOutTurbine() throws InitializingException {
		leaveOneOutTest();
	}

	protected boolean checkIsSuccess(CBRCase queryCase, CaseSolution caseSolution){
		TurbineAGBCaseSolution solution = castSolutionIntoTurbineSpecificSolution(caseSolution);
		return solution != null && solution.getIsAlarmingSituation() != null  && 
				solution.getIsAlarmingSituation().toString().equals(((TurbineAGBCaseSolution) queryCase.getSolution()).getIsAlarmingSituation().toString());
	}

	protected DomainDependentConfigurations getDomainDependentConfigurations() {
		return new TurbineAGBCaseConfigurations();
	}

	private TurbineAGBCaseSolution castSolutionIntoTurbineSpecificSolution(CaseSolution caseSolution) {
		if (caseSolution instanceof NullCaseSolution) { return new TurbineAGBCaseSolution(); }
		return (TurbineAGBCaseSolution) caseSolution; 
	}
}