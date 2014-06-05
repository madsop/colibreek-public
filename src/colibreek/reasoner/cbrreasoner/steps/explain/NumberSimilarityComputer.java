package colibreek.reasoner.cbrreasoner.steps.explain;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntModel;

public final class NumberSimilarityComputer {
	private OntModel model;
	private Individual individualActivatedCase;
	private Individual individualNewCase;

	PathAndSimilarity computeSimilarity(OntModel model, Individual individualActivatedCase, Individual individualNewCase) {
		this.model = model;
		this.individualActivatedCase = individualActivatedCase;
		this.individualNewCase = individualNewCase;
		return new PathAndSimilarity(1.0 - similarityDegree());
	}

	private double similarityDegree() {
		return findDifferenceBetweenTheTwoFindings() / findMaxMinDiff();
	}

	private double findDifferenceBetweenTheTwoFindings() {
		double activatedFindingAsNumber = SimilarityUtils.getDoubleFromIndividual(individualActivatedCase);
		double newFindingAsNumber = SimilarityUtils.getDoubleFromIndividual(individualNewCase);
		return Math.abs(newFindingAsNumber - activatedFindingAsNumber);
	}

	private double findMaxMinDiff() {
		if (individualActivatedCase.listOntClasses(false).hasNext() && individualNewCase.listOntClasses(false).hasNext()) {
			return SimilarityUtils.getRangeOfValuesOfSameClassAsTheInputIndividual(model, individualActivatedCase);
		}
		return SimilarityUtils.getRangeOfValuesOfSameClassAsTheInputIndividual(model, individualNewCase);
	}
}