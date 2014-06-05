package colibreek.reasoner.cbrreasoner.steps.activate;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import com.google.inject.Inject;

import jcolibri.cbrcore.CBRCase;
import colibreek.caserepresentation.CaseDescription;
import colibreek.caserepresentation.CaseDescriptor;
import colibreek.caserepresentation.Finding;
import colibreek.config.DomainDependentConfigurations;

public class ThresholdMatcher {
	private final double matchingThresholdForActivationOfCases;

	@Inject
	public ThresholdMatcher(DomainDependentConfigurations domainDependentConfigurations) {
		matchingThresholdForActivationOfCases = domainDependentConfigurations.getMatchingThresholdForActivationOfCases();
	}

	protected Set<CBRCase> findCasesWithMatchLowerThanThreshold(CaseDescription newCaseDescription, Collection<CBRCase> cases) {
		return cases.stream().filter(c -> caseMatchesBelowThreshold(newCaseDescription, c)).collect(Collectors.toSet());
	}

	private boolean caseMatchesBelowThreshold(CaseDescription newCaseDescription, CBRCase caseToTest) {
		return findRelationStrengthBetweenTwoCases(newCaseDescription, (CaseDescription) caseToTest.getDescription()) < matchingThresholdForActivationOfCases;
	}

	protected double findRelationStrengthBetweenTwoCases(CaseDescription newCaseDescription, CaseDescription oldCaseDescription) {
		Map<CaseDescriptor, Finding> findingsFromOldCase = oldCaseDescription.getNonNullCaseFindings();
		Map<CaseDescriptor, Finding> findingsFromNewCase = newCaseDescription.getNonNullCaseFindings();
		double numberOfSimilarFindings = findNumberAndStrengthOfEqualFindings(findingsFromNewCase, findingsFromOldCase);
		double totalNumberAndStrengthOfFindings = findTotalNumberAndStrengthOfFindings(findingsFromOldCase, findingsFromNewCase);
		return numberOfSimilarFindings / totalNumberAndStrengthOfFindings;
	}

	private double findTotalNumberAndStrengthOfFindings(Map<CaseDescriptor, Finding> findingsFromOldCase, Map<CaseDescriptor, Finding> findingsFromNewCase) {
		Collection<Finding> biggest = findingsFromNewCase.size() > findingsFromOldCase.size() ? findingsFromNewCase.values() : findingsFromOldCase.values();
		return biggest.stream().mapToDouble(finding -> finding.getRelevanceFactor()).sum();
	}

	private double findNumberAndStrengthOfEqualFindings(Map<CaseDescriptor, Finding> findingsFromNewCase, Map<CaseDescriptor, Finding> findingsFromOldCase) {
		return findingsFromNewCase.entrySet().stream()
				.filter(newCaseFinding -> findingsFromOldCase.containsKey(newCaseFinding.getKey()))
				.filter(newCaseFinding -> newCaseFinding.getValue().equals(findingsFromOldCase.get(newCaseFinding.getKey())))
				.mapToDouble(newCaseFinding -> 1.0 * newCaseFinding.getValue().getRelevanceFactor())
				.sum();
	}
}