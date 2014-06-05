package colibreek.reasoner.cbrreasoner.steps.explain;

import com.google.inject.Inject;
import com.hp.hpl.jena.ontology.OntModel;

public class CaseSimilarityComputerFactory {
	private PathHandlerFactory pathHandlerFactory;
	private DateTimeSimilarityComputer dateTimeSimilarityComputer;
	private NumberSimilarityComputer numberSimilarityComputer;
	
	@Inject
	public CaseSimilarityComputerFactory(PathHandlerFactory pathHandlerFactory, DateTimeSimilarityComputer dateTimeSimilarityComputer, NumberSimilarityComputer numberSimilarityComputer) {
		this.pathHandlerFactory = pathHandlerFactory;
		this.dateTimeSimilarityComputer = dateTimeSimilarityComputer;
		this.numberSimilarityComputer = numberSimilarityComputer;
	}
	
	CaseSimilarityComputer create(OntModel model) {
		return new CaseSimilarityComputer(model, pathHandlerFactory, dateTimeSimilarityComputer, numberSimilarityComputer);
	}
}