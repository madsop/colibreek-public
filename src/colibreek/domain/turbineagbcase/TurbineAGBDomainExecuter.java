package colibreek.domain.turbineagbcase;

import com.google.inject.Inject;

import jcolibri.cbrcore.CBRQuery;
import jcolibri.exception.OntologyAccessException;
import colibreek.DomainExecuter;
import colibreek.caserepresentation.CaseSolution;
import colibreek.caserepresentation.NullCaseSolution;
import colibreek.config.DomainDependentConfigurations;
import colibreek.getInput.InputRetriever;

public class TurbineAGBDomainExecuter extends DomainExecuter {
	private TurbineAGBCaseConfigurations turbineAGBCaseConfigurations;
	private InputRetriever inputRetriever;

	@Inject
	public TurbineAGBDomainExecuter(InputRetriever inputRetriever, TurbineAGBCaseConfigurations turbineAGBCaseConfigurations) {
		this.inputRetriever = inputRetriever;
		this.turbineAGBCaseConfigurations = turbineAGBCaseConfigurations;
	}
	
	@Override
	protected DomainDependentConfigurations getDomainDependentConfigurations() {
		return turbineAGBCaseConfigurations;
	}

	@Override
	protected CBRQuery setUpQuery() throws OntologyAccessException {
		CBRQuery query = new CBRQuery();
		TurbineAGBCaseDescription queryDescription = new TurbineAGBCaseDescription();
		queryDescription.setTimestamp(inputRetriever.getValueFromUser("Give timestamp(Format: YYYY-MM-DDTHH:MM:SS)", "Timestamp"));
		queryDescription.setTurbineShaftSpeed(inputRetriever.getValueFromUser("Give turbine shaft speed", "Turbine shaft speed"));
		queryDescription.setAGBBearingSumpTemperature(inputRetriever.getValueFromUser("Give AGB bearing sump temperature", "AGB bearing sump temperature"));
		queryDescription.setLubeOilSupplyPressure(inputRetriever.getValueFromUser("Give lube oil supply pressure", "Lube oil supply pressure"));
		queryDescription.setLubeOilSupplyTemperature(inputRetriever.getValueFromUser("Give lube oil supply temperature", "Lube oil supply temperature"));
		queryDescription.setFuelGasPressure(inputRetriever.getValueFromUser("Give fuel gas pressure", "Fuel gas pressure"));
		queryDescription.setGGBSumpTemperature(inputRetriever.getValueFromUser("Give GGB sump temperature", "GGB sump temperature"));
		queryDescription.setGGCSumpTemperature(inputRetriever.getValueFromUser("Give GGC sump temperature", "GGC sump temperature"));
//		queryDescription.setTurbineShaftSpeed(new Instance("9104.4"));
//		queryDescription.setAGBBearingSumpTemperature(new Instance("94.3"));
//		queryDescription.setFuelGasPressure(new Instance("29.7"));
//		queryDescription.setGGBSumpTemperature(new Instance("117.8"));
//		queryDescription.setGGCSumpTemperature(new Instance("128.1"));
//		queryDescription.setLubeOilSupplyPressure(new Instance("4"));
//		queryDescription.setLubeOilSupplyTemperature(new Instance("65.7"));
		query.setDescription(queryDescription);
		return query;
	}

	@Override
	public void printDomainSpecificSolutionAspects(CaseSolution caseSolution) {
		TurbineAGBCaseSolution solution = castSolutionIntoSpecificSolution(caseSolution);
		System.out.println("alarming: " + solution.getIsAlarmingSituation());
	}

	private TurbineAGBCaseSolution castSolutionIntoSpecificSolution(CaseSolution caseSolution) {
		if (caseSolution instanceof NullCaseSolution) { return new TurbineAGBCaseSolution(); }
		return (TurbineAGBCaseSolution) caseSolution; 
	}
}
