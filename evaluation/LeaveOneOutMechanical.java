package evaluation;

import jcolibri.cbrcore.CBRCase;
import jcolibri.exception.InitializingException;

import org.junit.Test;

import colibreek.caserepresentation.CaseSolution;
import colibreek.caserepresentation.NullCaseSolution;
import colibreek.config.DomainDependentConfigurations;
import colibreek.domain.mechanicalcase.MechanicalCaseConfigurations;
import colibreek.domain.mechanicalcase.MechanicalCaseSolution;

public class LeaveOneOutMechanical extends LeaveOneOutTest {

	@Test
	public void testOneOutMechanical() throws InitializingException {
		leaveOneOutTest();
	}

	protected boolean checkIsSuccess(CBRCase queryCase, CaseSolution caseSolution) {
		MechanicalCaseSolution solution = castSolutionIntoMechanicalSpecificSolution(caseSolution);
		return solution != null && solution.getIsAlarmingSituation() != null  && 
				solution.getIsAlarmingSituation().toString().equals(((MechanicalCaseSolution) queryCase.getSolution()).getIsAlarmingSituation().toString());
	}
	
	private MechanicalCaseSolution castSolutionIntoMechanicalSpecificSolution(CaseSolution caseSolution) {
		if (caseSolution instanceof NullCaseSolution) { return new MechanicalCaseSolution(); }
		return (MechanicalCaseSolution) caseSolution; 
	}
	
	protected DomainDependentConfigurations getDomainDependentConfigurations() {
		return new MechanicalCaseConfigurations();
	}
}