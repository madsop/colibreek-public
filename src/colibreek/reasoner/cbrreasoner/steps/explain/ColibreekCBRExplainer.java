package colibreek.reasoner.cbrreasoner.steps.explain;

import java.util.Set;

import jcolibri.cbrcore.CBRCase;
import colibreek.caserepresentation.CaseDescription;
import colibreek.reasoner.cbrreasoner.steps.activate.ActivatedAreaAndCases;
import colibreek.reasoner.cbrreasoner.steps.explain.datastructure.ColibreekCBRExplanation;
import colibreek.reasoner.cbrreasoner.steps.explain.datastructure.ExplanationForOneCase;

import com.google.inject.Inject;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.ModelFactory;

public class ColibreekCBRExplainer {
	private CaseSimilarityComputer similarityComputer;
	private CaseSimilarityComputerFactory similarityComputerFactory;

	@Inject
	public ColibreekCBRExplainer(CaseSimilarityComputerFactory similarityComputerFactory) {
		this.similarityComputerFactory = similarityComputerFactory;
	}
	
	public ColibreekCBRExplanation explain(ActivatedAreaAndCases activatedArea, CaseDescription newCaseDescription) {
		this.similarityComputer = similarityComputerFactory.create(makeModel(activatedArea));
		return explainEachCase(activatedArea, newCaseDescription);
	}
	
	private OntModel makeModel(ActivatedAreaAndCases activatedArea) {
		OntModel model = ModelFactory.createOntologyModel();
		model.add(activatedArea.getStatements());
		return model;
	}

	private ColibreekCBRExplanation explainEachCase(ActivatedAreaAndCases activatedArea, CaseDescription newCaseDescription) {
		ColibreekCBRExplanation explanation = new ColibreekCBRExplanation();
		activatedArea.getCases().forEach(activatedCase -> explanation.addExplanationForOneCase(explainSingleCase(newCaseDescription, activatedCase)));
		return explanation;
	}

	private ExplanationForOneCase explainSingleCase(CaseDescription newCaseDescription, CBRCase activatedCase) {
		Set<FindingSimilarity> findingSimilarities = similarityComputer.findSimilaritiesBetweenCases(newCaseDescription, activatedCase);
		return new ExplanationForOneCase(activatedCase, findingSimilarities);
	}
}