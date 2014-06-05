package colibreek;

import colibreek.caserepresentation.CaseDescription;
import colibreek.caserepresentation.CaseSolution;
import colibreek.config.DomainDependentConfigurations;
import colibreek.reasoner.ColibreekReasoner;
import jcolibri.cbrcore.CBRQuery;
import jcolibri.exception.OntologyAccessException;

public abstract class DomainExecuter {
	protected abstract DomainDependentConfigurations getDomainDependentConfigurations(); 
	protected abstract CBRQuery setUpQuery() throws OntologyAccessException;
	protected abstract void printDomainSpecificSolutionAspects(CaseSolution solution);
	
	String getLocationOfXMLConfigFile() {
		return getDomainDependentConfigurations().getLocationOfXMLConfigFile();
	}
	
	void findSolution(ColibreekReasoner reasoner, CaseDescription newCase) {
		CaseSolution solution = (CaseSolution) reasoner.reason(newCase).getSolution();
		System.out.println("\n \n-------------------------------------------------------------");
		System.out.println("Case id of the most similar case: " + solution.getMainConcept());
		printDomainSpecificSolutionAspects(solution);
	}
}