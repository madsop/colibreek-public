package colibreek.reasoner.cbrreasoner.steps.activate;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import colibreek.config.DomainDependentConfigurations;

import com.google.inject.Inject;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Statement;

public class ActivationSpreader {
	private Map<RDFNode, Set<Statement>> originalStatementsGroupedBySubject;
	private Set<RDFNode> nodesWithNoMoreUncheckedStatements;
	private Set<RDFNode> activatedNodesThatMayHaveNotYetCheckedSuperclasses;
	private Collection<String> relationTypesThatShouldLeadToActivation;
	
	@Inject
	public ActivationSpreader(DomainDependentConfigurations domainDependentConfigurations) {
		this.relationTypesThatShouldLeadToActivation = domainDependentConfigurations.getRelationTypesThatShouldLeadToActivation();
	}
	
	Set<Statement> spreadActivation(List<Statement> originalStatements) {
		extractSubjectsAndObjectsFromOriginalStatements(originalStatements);
		Set<Statement> statementsToActivate = new HashSet<>();
		nodesWithNoMoreUncheckedStatements = new HashSet<>();
		
		while (!activatedNodesThatMayHaveNotYetCheckedSuperclasses.isEmpty()) {
			statementsToActivate.addAll(checkObjectForSuperclassesAndFindStatementsToActivate());
		}
		return statementsToActivate;
	}

	private void extractSubjectsAndObjectsFromOriginalStatements(List<Statement> originalStatements) {
		originalStatementsGroupedBySubject = StatementUtils.getOriginalStatementsPerSubject(originalStatements);
		activatedNodesThatMayHaveNotYetCheckedSuperclasses = StatementUtils.findAllObjectsFromStatements(originalStatements);
	}

	private Collection<Statement> checkObjectForSuperclassesAndFindStatementsToActivate() {
		RDFNode uncheckedNode = findNextUncheckedObject();
		Collection<Statement> statementsToActivate = new HashSet<>();		
		if (thereAreMoreStatementsWhereTheUncheckedNodeIsSubject(uncheckedNode)) {
			statementsToActivate.addAll(findStatementsToActivateWhereTheUncheckedNodeIsTheSubject(uncheckedNode));
		}
		nodesWithNoMoreUncheckedStatements.add(uncheckedNode);
		return statementsToActivate;
	}

	private RDFNode findNextUncheckedObject() {
		RDFNode untriedObject = activatedNodesThatMayHaveNotYetCheckedSuperclasses.iterator().next();
		activatedNodesThatMayHaveNotYetCheckedSuperclasses.remove(untriedObject);
		return untriedObject;
	}

	private boolean thereAreMoreStatementsWhereTheUncheckedNodeIsSubject(RDFNode objectToTryAsSubject) {
		return !nodesWithNoMoreUncheckedStatements.contains(objectToTryAsSubject) && originalStatementsGroupedBySubject.containsKey((objectToTryAsSubject));
	}

	private Collection<Statement> findStatementsToActivateWhereTheUncheckedNodeIsTheSubject(RDFNode subject) {
		Collection<Statement> statementsToActivate = new HashSet<>();
		for (Statement statement : originalStatementsGroupedBySubject.get(subject)) {
			if (shouldStatementBeActivated(statement)) {
				statementsToActivate.add(statement);
				activatedNodesThatMayHaveNotYetCheckedSuperclasses.add(statement.getObject());
			}
		}
		return statementsToActivate;
	}
	
	private boolean shouldStatementBeActivated(Statement statement) {
		if (statement.getObject().isAnon()) { return false; }
		String predicateURI = statement.getPredicate().getURI();
		String predicateLocalName = statement.getPredicate().getLocalName();
		return checkStatementAgainstDomainDefinedRelationTypesThatShouldLeadToActivation(predicateURI, predicateLocalName);
	}

	private boolean checkStatementAgainstDomainDefinedRelationTypesThatShouldLeadToActivation(String predicateURI,	String predicateLocalName) {
		return relationTypesThatShouldLeadToActivation.stream()
		.filter(relationTypesLeadingToActivation -> predicateURI.equals(relationTypesLeadingToActivation) || predicateLocalName.equals(relationTypesLeadingToActivation))
		.findAny().isPresent();
	}
}