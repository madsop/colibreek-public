package colibreek.reasoner.cbrreasoner.steps.explain;

import java.util.Set;
import java.util.stream.Collectors;

import jcolibri.cbrcore.CBRCase;
import colibreek.caserepresentation.CaseDescription;
import colibreek.caserepresentation.CaseDescriptor;
import colibreek.caserepresentation.Finding;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntTools.Path;

class CaseSimilarityComputer {
	private OntModel model;
	private PathHandler pathHandler;
	private DateTimeSimilarityComputer dateTimeSimilarityComputer;
	private NumberSimilarityComputer numberSimilarityComputer;

	CaseSimilarityComputer(OntModel model, PathHandlerFactory pathHandlerFactory, DateTimeSimilarityComputer dateTimeSimilarityComputer, NumberSimilarityComputer numberSimilarityComputer) {
		this.model = model;
		this.dateTimeSimilarityComputer = dateTimeSimilarityComputer;
		this.numberSimilarityComputer = numberSimilarityComputer;
		this.pathHandler = pathHandlerFactory.create(model);
	}

	public Set<FindingSimilarity> findSimilaritiesBetweenCases(CaseDescription newCaseDescription, CBRCase activatedCase) {
		CaseDescription activatedCaseDescription = (CaseDescription) activatedCase.getDescription();

		return activatedCaseDescription.getCaseFindings().keySet().stream()
		.map(findingClass -> compareTwoFindings(findingClass, newCaseDescription, activatedCaseDescription))
		.collect(Collectors.toSet());
	}

	private FindingSimilarity compareTwoFindings(CaseDescriptor findingClass, CaseDescription newCaseDescription, CaseDescription activatedCaseDescription)  {
		Finding findingFromActivatedCase = activatedCaseDescription.getNonNullCaseFindings().get(findingClass);
		Finding findingFromNewCase = newCaseDescription.getCaseFindings().get(findingClass);
		return createFindingSimilarity(findingClass, findingFromActivatedCase, findingFromNewCase);
	}

	private FindingSimilarity createFindingSimilarity(CaseDescriptor findingClass, Finding findingFromActivatedCase, Finding findingFromNewCase) {
		if (findingFromNewCase.isNull()) { return new FindingSimilarity(findingClass, 0.0, findingFromActivatedCase.getRelevanceFactor(), new Path()); }
		if (findingFromNewCase.equals(findingFromActivatedCase)) { return createFindingSimilarityForEqualFinding(findingClass, findingFromActivatedCase); } 
		return findPathAndThenComputeSimilarity(findingClass, findingFromActivatedCase, findingFromNewCase);
	}

	private FindingSimilarity createFindingSimilarityForEqualFinding(CaseDescriptor findingKey, Finding findingFromActivatedCase) {
		return new FindingSimilarity(findingKey, 1.0, findingFromActivatedCase.getRelevanceFactor(), new Path()); 
	}

	protected FindingSimilarity findPathAndThenComputeSimilarity(CaseDescriptor findingKey, Finding findingFromActivatedCase, Finding findingFromNewCase) {
		Path pathFromNewToActivatedFinding = pathHandler.findPathBetweenTwoFindings(findingKey, findingFromActivatedCase.getInstanceValue().toString(), findingFromNewCase.getInstanceValue().toString());
		return computeSimilarity(findingKey, pathFromNewToActivatedFinding, findingFromActivatedCase, findingFromNewCase);
	}

	private FindingSimilarity computeSimilarity(CaseDescriptor findingKey, Path pathFromNewToActivatedFinding, Finding findingFromActivatedCase, Finding findingFromNewCase) {
		PathAndSimilarity pathAndSimilarity = extractIndividualsAndGetSimilarityFromPathFromOntology(findingFromActivatedCase, findingFromNewCase, pathFromNewToActivatedFinding);
		return new FindingSimilarity(findingKey, pathAndSimilarity.similarity, findingFromActivatedCase.getRelevanceFactor(), pathAndSimilarity.path);
	}

	private PathAndSimilarity extractIndividualsAndGetSimilarityFromPathFromOntology(Finding findingFromActivatedCase, Finding findingFromNewCase, Path pathFromNewToActivatedFinding) {
		Individual individualActivatedCase = PathUtils.getIndividualFromInstanceName(model, findingFromActivatedCase.getInstanceValue());
		Individual individualNewCase = PathUtils.getIndividualFromInstanceName(model, findingFromNewCase.getInstanceValue());
		return getSimilarityFromOntology(individualActivatedCase, individualNewCase, pathFromNewToActivatedFinding);
	}

	private PathAndSimilarity getSimilarityFromOntology(Individual individualActivatedCase, Individual individualNewCase, Path path) {
		if (individualIsDateTime(individualActivatedCase, individualNewCase)) {
			return dateTimeSimilarityComputer.computeDateTimeSimilarity(individualActivatedCase, individualNewCase);
		}
		if (individualIsNotNumber(individualActivatedCase, individualNewCase)) {
			return new PathAndSimilarity(pathHandler.getSimilarityFromPath(path), path);
		}
		return numberSimilarityComputer.computeSimilarity(model, individualActivatedCase, individualNewCase);
	}

	private boolean individualIsDateTime(Individual individualActivatedCase, Individual individualNewCase) {
		return SimilarityUtils.individualIsADateTime(individualActivatedCase) || SimilarityUtils.individualIsADateTime(individualNewCase);
	}

	private boolean individualIsNotNumber(Individual individualActivatedCase, Individual individualNewCase) {
		return SimilarityUtils.individualIsNotADouble(individualActivatedCase) || SimilarityUtils.individualIsNotADouble(individualNewCase);
	}
}