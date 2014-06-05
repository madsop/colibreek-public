package colibreek.domain.mechanicalcase;

import com.google.inject.Inject;

import jcolibri.cbrcore.CBRQuery;
import jcolibri.exception.OntologyAccessException;
import colibreek.DomainExecuter;
import colibreek.caserepresentation.CaseSolution;
import colibreek.caserepresentation.NullCaseSolution;
import colibreek.config.DomainDependentConfigurations;

public class MechanicalDomainExecuter extends DomainExecuter {
	private MechanicalInputRetriever inputRetriever;
	private MechanicalCaseConfigurations mechanicalCaseConfigurations;

	@Inject
	public MechanicalDomainExecuter(MechanicalInputRetriever inputRetriever, MechanicalCaseConfigurations mechanicalCaseConfigurations) {
		this.inputRetriever = inputRetriever;
		this.mechanicalCaseConfigurations = mechanicalCaseConfigurations;
	}

	@Override
	public DomainDependentConfigurations getDomainDependentConfigurations() {
		return mechanicalCaseConfigurations;
	}

	@Override
	public CBRQuery setUpQuery() throws OntologyAccessException {
		CBRQuery query = new CBRQuery();
		MechanicalCaseDescription queryDesc =  new MechanicalCaseDescription();
		queryDesc.setExciterFieldVoltage(inputRetriever.getExciterFieldVoltage());
		queryDesc.setGeneratorPower(inputRetriever.getGeneratorPower());
		queryDesc.setTimestamp(inputRetriever.getTimestamp());
		queryDesc.setGeneratorID(inputRetriever.getGeneratorID());
		queryDesc.setFrequency(inputRetriever.getFrequency());
		queryDesc.setExciterFieldCurrent(inputRetriever.getExciterFieldCurrent());
		queryDesc.setReactiveLoad(inputRetriever.getReactiveLoad());
		queryDesc.setPhaseACurrent(inputRetriever.getValueFromUser("Give phase A current", "Phase A Current"));
		queryDesc.setPhaseBCurrent(inputRetriever.getValueFromUser("Give phase B current", "Phase B Current"));
		query.setDescription(queryDesc);
		return query;
	}

	@Override
	public void printDomainSpecificSolutionAspects(CaseSolution caseSolution) {
		MechanicalCaseSolution solution = castSolutionIntoSpecificSolution(caseSolution);
		System.out.println("alarming: " + solution.getIsAlarmingSituation());
	}

	private MechanicalCaseSolution castSolutionIntoSpecificSolution(CaseSolution caseSolution) {
		if (caseSolution instanceof NullCaseSolution) { return new MechanicalCaseSolution(); }
		return (MechanicalCaseSolution) caseSolution; 
	}
}