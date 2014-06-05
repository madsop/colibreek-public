package colibreek.reasoner.cbrreasoner.steps.activate;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.Set;

import jcolibri.cbrcore.CBRCase;
import jcolibri.cbrcore.CBRCaseBase;
import colibreek.caserepresentation.CaseDescription;
import colibreek.caserepresentation.CaseDescriptor;
import colibreek.caserepresentation.Finding;
import colibreek.config.DomainDependentConfigurations;

import com.google.inject.Inject;
import com.hp.hpl.jena.rdf.model.Statement;

public class ColibreekCBRActivator {
	private ThresholdMatcher thresholdMatcher;
	private ActivationSpreader activationSpreader;
	private final int maxSizeBeforeReturnEntireNetwork;

	@Inject
	public ColibreekCBRActivator(ThresholdMatcher thresholdMatcher, ActivationSpreader activationSpreader, DomainDependentConfigurations domainDependentConfigurations) {
		this.thresholdMatcher = thresholdMatcher;
		this.activationSpreader = activationSpreader;
		this.maxSizeBeforeReturnEntireNetwork = domainDependentConfigurations.getMaxSizeBeforeReturnEntireNetwork();
	}

	public ActivatedAreaAndCases activate(CBRCaseBase casebase, List<Statement> statements, CaseDescription caseDescription) {
		if (statements.size() <= maxSizeBeforeReturnEntireNetwork) {				
			return new ActivatedAreaAndCases(statements);
		}

		ActivatedAreaAndCases activatedAreaAndCases = spreadActivationThroughNetwork(casebase, statements, caseDescription.getNonNullCaseFindings());
		activatedAreaAndCases.removeCases(thresholdMatcher.findCasesWithMatchLowerThanThreshold(caseDescription, activatedAreaAndCases.getCases()));
		return activatedAreaAndCases;
	}

	private ActivatedAreaAndCases spreadActivationThroughNetwork(CBRCaseBase casebase, List<Statement> statements, Map<CaseDescriptor, Finding> givenCaseFindings) {
		ActivatedAreaAndCases activatedAreaToReturn = activateDirectFindingsFromNewCaseAndEverythingDirectlyRelatedToThese(casebase, statements, givenCaseFindings);
		activatedAreaToReturn = activateAlongSubclassLinks(activatedAreaToReturn, statements);
		return activatedAreaToReturn;
	}

	private ActivatedAreaAndCases activateDirectFindingsFromNewCaseAndEverythingDirectlyRelatedToThese(CBRCaseBase casebase, List<Statement> statements, Map<CaseDescriptor, Finding> newCaseFindings) {
		ActivatedAreaAndCases activatedAreaAndCases = new ActivatedAreaAndCases();
		activatedAreaAndCases.addCases(findCasesWithAtLeastOneEqualFinding(casebase, newCaseFindings));
		activatedAreaAndCases.addStatements(activateStatementsFromFindings(statements, newCaseFindings.values()));
		activatedAreaAndCases.addStatements(getStatementsFromActivatedCases(statements, activatedAreaAndCases.getCases()));

		return activatedAreaAndCases;
	}

	private Set<CBRCase> findCasesWithAtLeastOneEqualFinding(CBRCaseBase casebase, Map<CaseDescriptor, Finding> newCaseValues) {
		Set<CBRCase> cases = new HashSet<>();
		Iterator<CBRCase> caseBaseIterator = casebase.getCases().iterator();
		while (caseBaseIterator.hasNext()) {
			CBRCase caseInCaseBase = caseBaseIterator.next();
			if (newCaseValues.entrySet().stream()
					.filter(findingFromNewCase -> !thisFindingIsNotRegisteredForOldCase(caseInCaseBase, findingFromNewCase))
					.filter(findingFromNewCase -> isSimilarFinding(caseInCaseBase, findingFromNewCase)).findAny().isPresent()) {
				cases.add(caseInCaseBase);
			}
		}
		return cases;
	}

	private boolean thisFindingIsNotRegisteredForOldCase(CBRCase caseInCaseBase, Entry<CaseDescriptor, Finding> findingFromNewCase) {
		return !((CaseDescription) caseInCaseBase.getDescription()).getNonNullCaseFindings().containsKey(findingFromNewCase.getKey());
	}

	private boolean isSimilarFinding(CBRCase caseInCaseBase, Entry<CaseDescriptor, Finding> findingFromNewCase) {
		return ((CaseDescription) caseInCaseBase.getDescription()).getNonNullCaseFindings().get(findingFromNewCase.getKey()).equals(findingFromNewCase.getValue());
	}

	private Collection<Statement> getStatementsFromActivatedCases(List<Statement> statements, Set<CBRCase> activatedCases) {
		return activatedCases.stream()
				.map(activatedCase -> ((CaseDescription) activatedCase.getDescription()).getNonNullCaseFindings().values())
				.flatMap(findingsInThisActivatedCase -> activateStatementsFromFindings(statements, findingsInThisActivatedCase).stream())
				.collect(Collectors.toSet());
	}

	private ActivatedAreaAndCases activateAlongSubclassLinks(ActivatedAreaAndCases activatedAreaAlready, List<Statement> originalStatements) {
		ActivatedAreaAndCases activatedAreaToReturn = activatedAreaAlready;
		activatedAreaToReturn.addStatements(activationSpreader.spreadActivation(originalStatements));
		return activatedAreaToReturn;
	}

	Set<Statement> activateStatementsFromFindings(List<Statement> statements, Collection<Finding> newCaseFindings) {
		return newCaseFindings.stream().flatMap(x -> activateAllStatementsLinkedToThisFinding(statements, x).stream()).collect(Collectors.toSet());
	}

	private Set<Statement> activateAllStatementsLinkedToThisFinding(List<Statement> statements, Finding newCaseFinding) {
		return statements.stream()
				.filter(statement -> StatementUtils.statementHasFindingAsSubjectOrObject(statement, newCaseFinding.getInstanceValue().toString()))
				.collect(Collectors.toSet());
	}
}