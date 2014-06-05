package colibreek.reasoner.cbrreasoner.steps.explain.datastructure;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import colibreek.caserepresentation.CaseDescription;
import colibreek.reasoner.cbrreasoner.steps.explain.FindingSimilarity;
import jcolibri.cbrcore.CBRCase;

public class ExplanationForOneCase implements Comparable<ExplanationForOneCase> {
	private final CBRCase activatedCase;
	private final Set<FindingSimilarity> findingSimilarities;

	public ExplanationForOneCase(CBRCase activatedCase, Set<FindingSimilarity> findingSimilarities) {
		this.activatedCase = activatedCase;
		this.findingSimilarities = findingSimilarities;
	}
	
	public CBRCase getCase() {
		return activatedCase;
	}
	
	public List<FindingSimilarity> getFindingSimilarities() {
		List<FindingSimilarity> findingsAsList = new ArrayList<>(findingSimilarities);
		Collections.sort(findingsAsList);
		return findingsAsList;
	}

	public double getSimilarity() {
		return findTotalFindingSimilarity() / findTotalRelevanceFactor();
	}
	
	private double findTotalFindingSimilarity() {
		return findingSimilarities.stream().mapToDouble(f -> f.getRelevanceFactor() * f.getSimilarityValue()).sum();
	}

	private double findTotalRelevanceFactor() {
		return findingSimilarities.stream().mapToDouble(f -> f.getRelevanceFactor()).sum();
	}

	@Override
	public int compareTo(ExplanationForOneCase otherExplanation) {
		return Double.compare(otherExplanation.getSimilarity(), getSimilarity());
	}

	public String toString() {
		CaseDescription activatedCaseDescription = (CaseDescription) activatedCase.getDescription();
		return "Case: " + activatedCase.getID() + " has similarity " + getSimilarity() + ": \n" +
		findingSimilarities.stream()
		.map(f -> f + ", this case has the finding value " + activatedCaseDescription.getCaseFindings().get(f.getFindingName()) + "\n")
		.sorted()
		.reduce((a,b) -> a + b).get()
		
		+ "The case has solution " + activatedCase.getSolution() +"\n";
	}
}