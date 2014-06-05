package colibreek.reasoner.cbrreasoner.steps.explain;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import colibreek.caserepresentation.CaseDescriptor;
import colibreek.config.DomainIndependentConfigurations;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntTools;
import com.hp.hpl.jena.ontology.OntTools.Path;
import com.hp.hpl.jena.rdf.model.Statement;

final class PathHandler  {
	private OntModel model;
	private Collection<String> nodesLeadingToPathDiscard;

	PathHandler(OntModel model, Collection<String> nodesLeadingToPathDiscard) {
		this.model = model;
		this.nodesLeadingToPathDiscard = nodesLeadingToPathDiscard;
	}

	Path findPathBetweenTwoFindings(CaseDescriptor findingKey, String activatedFindingURI, String newFindingURI) {
		Individual activatedFinding = PathUtils.getIndividualFromName(model, activatedFindingURI);
		Individual newFinding = PathUtils.getIndividualFromName(model, newFindingURI);			

		if (atLeastOneFindingLacksClass(activatedFinding, newFinding)) { return new Path(); }

		OntClass startClass = activatedFinding.getOntClass(true);
		OntClass endClass = newFinding.getOntClass(true);

		Path returnPath = findPathBetweenAandB(activatedFinding, startClass, newFinding, endClass);
		return returnsPathWithoutExcessiveStatements(findingKey, returnPath);
	}

	double getSimilarityFromPath(Path pathFromNewToActivatedFinding) {
		if (pathFromNewToActivatedFinding.isEmpty()) { return 0.0; }
		return 1.0 / pathFromNewToActivatedFinding.size();
	}

	private boolean atLeastOneFindingLacksClass(Individual activatedFinding, Individual newFinding) {
		return !activatedFinding.listOntClasses(true).hasNext() || !newFinding.listOntClasses(true).hasNext();
	}

	private Path findPathBetweenAandB(Individual start, OntClass startClass, Individual end, OntClass endClass) {
		OntClass lowestCommonAncestor = findTheLowestCommonAncestor(startClass,	endClass);
		if (theLowestCommonAncestorIsTheGraphsTopNode(lowestCommonAncestor)) { return new Path(); }
		return PathUtils.constructPathFromStartToEnd(model, start, startClass, end, endClass, lowestCommonAncestor);
	}

	private OntClass findTheLowestCommonAncestor(OntClass startClass, OntClass endClass) {
		OntClass lowestCommonAncestor = OntTools.getLCA(model, startClass, endClass);
		if (theLowestCommonAncestorIsTheGraphsTopNode(lowestCommonAncestor)) {
			lowestCommonAncestor = OntTools.getLCA(model, endClass, startClass);
		}
		return lowestCommonAncestor;
	}

	private boolean theLowestCommonAncestorIsTheGraphsTopNode(OntClass lowestCommonAncestor) {
		return lowestCommonAncestor.getURI().equals(DomainIndependentConfigurations.TOP_NODE_URI);
	}

	private Path returnsPathWithoutExcessiveStatements(CaseDescriptor findingKey, Path inputPath) {
		Path returnPath = inputPath;
		Set<Statement> statementsToRemove = new HashSet<>();
		for (Statement statement : returnPath) {
			if (thePathGoesAllTheWayUpToCategoryLevelAndItIsThusNoSimilarity(findingKey, statement)) { return new Path(); }
			if (statementPointsToItselfAndIsThusToBeRemovedFromExplanation(statement)) {
				statementsToRemove.add(statement);
			}
		}
		returnPath.removeAll(statementsToRemove);
		return returnPath;
	}

	private boolean thePathGoesAllTheWayUpToCategoryLevelAndItIsThusNoSimilarity(CaseDescriptor findingKey, Statement statement) {
		String subject = normalizeStringForComparison(statement.getSubject());
		String object = normalizeStringForComparison(statement.getObject());
		String finding = normalizeStringForComparison(findingKey);

		return (subject.endsWith(finding) || object.endsWith(finding) || pathGoesViaDomainDependentTooGeneralLevel(subject, object));
	}

	private String normalizeStringForComparison(Object object) {
		return object.toString().toLowerCase()
				.replaceAll("_", "")
				.replaceAll("-", "");
	}

	private boolean pathGoesViaDomainDependentTooGeneralLevel(String subject, String object) {
		return nodesLeadingToPathDiscard.stream().map(x -> normalizeStringForComparison(x))
				.filter(nodeAtTooGeneralLevel -> subject.endsWith(nodeAtTooGeneralLevel) || object.endsWith(nodeAtTooGeneralLevel))
				.findAny().isPresent();
	}

	private boolean statementPointsToItselfAndIsThusToBeRemovedFromExplanation(Statement statement) {
		return statement.getSubject().getLocalName().equals(statement.getObject().asNode().getLocalName());
	}
}