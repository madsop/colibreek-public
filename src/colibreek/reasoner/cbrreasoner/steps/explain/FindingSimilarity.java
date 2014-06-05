package colibreek.reasoner.cbrreasoner.steps.explain;

import colibreek.caserepresentation.CaseDescriptor;
import com.hp.hpl.jena.ontology.OntTools.Path;

public class FindingSimilarity implements Comparable<FindingSimilarity>{
	private CaseDescriptor findingName;
	private double similarityValue;
	private Path trace;
	private double relevanceFactor;

	public FindingSimilarity(CaseDescriptor finding, double similarityValue, double relevanceFactor, Path trace) {
		this.findingName = finding;
		this.similarityValue = similarityValue;
		this.relevanceFactor = relevanceFactor;
		this.trace = trace;
	}
	
	public CaseDescriptor getFindingName() {
		return findingName;
	}
	
	public double getSimilarityValue() {
		return similarityValue;
	}

	public double getRelevanceFactor() {
		return relevanceFactor;
	}
	
	public Path getTrace() {
		return trace;
	}
	
	public String toString() {
		return findingName + ": similarity is " + similarityValue + ", through " + trace;
	}

	@Override
	public int compareTo(FindingSimilarity otherFindingSimilarity) {
		return getFindingName().toString().compareTo(otherFindingSimilarity.getFindingName().toString());
	}
}