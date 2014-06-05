package colibreek;

import colibreek.caserepresentation.CaseDescription;
import colibreek.reasoner.cbrreasoner.steps.explain.datastructure.ColibreekCBRExplanation;
import colibreek.reasoner.cbrreasoner.steps.explain.datastructure.ExplanationForOneCase;

public class ExplanationVisualiser {
	public void visualise(CaseDescription caseDescription, ColibreekCBRExplanation explanation, ExplanationForOneCase solution) {
		System.out.println("Reasoning finished for new situation \n" + caseDescription);
		System.out.println("This returned the explanation \n");
		for (int i = 0; i < 10 && i < explanation.getExplanations().size(); i++) {
			System.out.println(explanation.getExplanations().get(i));
		}
		
		System.out.println("The case proposed as solution was: " + solution.getCase().getSolution() + ", with similarity " + solution.getSimilarity()
				+ ". This is based on the explanation");
		solution.getFindingSimilarities().forEach(f -> System.out.println(f));
		System.out.println("The proposed case's description is \n" + solution.getCase().getDescription());
		
		System.out.println("Your query was \n" + caseDescription);
		System.out.println(" ---------------- ");
		System.out.println("The proposed solution for your query is thus " + solution.getCase().getSolution() + ".");
	}
}