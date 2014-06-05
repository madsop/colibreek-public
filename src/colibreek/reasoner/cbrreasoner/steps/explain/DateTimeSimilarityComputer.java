package colibreek.reasoner.cbrreasoner.steps.explain;

import java.time.LocalDateTime;
import colibreek.config.DomainDependentConfigurations;

import com.google.inject.Inject;
import com.hp.hpl.jena.ontology.Individual;

public final class DateTimeSimilarityComputer {
	
	private DomainDependentConfigurations domainDependentConfigurations;

	@Inject
	public DateTimeSimilarityComputer(DomainDependentConfigurations domainDependentConfigurations) {
		this.domainDependentConfigurations = domainDependentConfigurations;
	}

	PathAndSimilarity computeDateTimeSimilarity(Individual individualActivatedCase, Individual individualNewCase) {
		LocalDateTime activatedDateTime = getDateTimeFromIndividual(individualActivatedCase);
		LocalDateTime newDateTime = getDateTimeFromIndividual(individualNewCase);

		double similarity = computeSimilarity(activatedDateTime, newDateTime);

		return new PathAndSimilarity(similarity);
	}

	private LocalDateTime getDateTimeFromIndividual(Individual individualActivatedCase) {
		String localname = SimilarityUtils.getLocalNameFromIndividual(individualActivatedCase);
		return LocalDateTime.parse(localname);
	}

	private double computeSimilarity(LocalDateTime activatedDateTime, LocalDateTime newDateTime) {
		return domainDependentConfigurations.computeDateOrTimeSimilarity(activatedDateTime, newDateTime);
	}
}