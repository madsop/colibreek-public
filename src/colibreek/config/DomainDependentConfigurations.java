package colibreek.config;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public abstract class DomainDependentConfigurations {
	public abstract String getLocationOfXMLConfigFile();
	
	public int getMaxSizeBeforeReturnEntireNetwork() {
		return 50;
	}
	public double getMatchingThresholdForActivationOfCases() {
		return 0.40;
	}
	
	public double computeDateOrTimeSimilarity(LocalDateTime activatedDateTime, LocalDateTime newDateTime) {
		double similarity = 0.0;
		if (activatedDateTime.getYear() == newDateTime.getYear()) { similarity += 0.25; }
		if (activatedDateTime.getMonth() == newDateTime.getMonth()) { similarity += 0.2; }
		if (activatedDateTime.getDayOfMonth() == newDateTime.getDayOfMonth()) { similarity += 0.2; }
		if (activatedDateTime.getHour() == newDateTime.getHour()) { similarity += 0.2; }
		if (activatedDateTime.getMinute() == newDateTime.getMinute()) { similarity += 0.15; }
		return similarity;
	}

	/**
	 * The default set of spreading relations are the instances of transitive-relation, i.e:
	subclass-of, instance-of, part-of, member-of, function-of, caused-by (p292)
	 */
	public Collection<String> getRelationTypesThatShouldLeadToActivation() {
		Set<String> relationTypesLeadingToActivation = new HashSet<>();
		relationTypesLeadingToActivation.add("subClassOf");
		relationTypesLeadingToActivation.add("subPropertyOf");
		relationTypesLeadingToActivation.add(DomainIndependentConfigurations.TOP_DATA_PROPERTY);
		relationTypesLeadingToActivation.add("http://www.loa-cnr.it/ontologies/DUL.owl#hasDataValue");
		return relationTypesLeadingToActivation;
	}
	
	public Collection<String> getNodesLeadingToPathDiscard() {
		Set<String> nodesLeadingToPathDiscard = new HashSet<>();
		nodesLeadingToPathDiscard.add("featureofinterest");
		return nodesLeadingToPathDiscard;
	}
}