package colibreek.reasoner.cbrreasoner.steps.explain;

import colibreek.config.DomainDependentConfigurations;

import com.google.inject.Inject;
import com.hp.hpl.jena.ontology.OntModel;

public class PathHandlerFactory {

	private DomainDependentConfigurations domainDependentConfigurations;

	@Inject
	public PathHandlerFactory(DomainDependentConfigurations domainDependentConfigurations) {
		this.domainDependentConfigurations = domainDependentConfigurations;
	}
	
	PathHandler create(OntModel model) {
		return new PathHandler(model, domainDependentConfigurations.getNodesLeadingToPathDiscard());
	}
}
