package colibreek.domain.travelrecommender;

import java.util.Collection;

import colibreek.config.DomainDependentConfigurations;

public class TravelCaseConfigurations extends DomainDependentConfigurations {
	@Override
	public String getLocationOfXMLConfigFile() {
		return "config/travelDomainOntologyConfig.xml";
	}
	
	@Override
	public Collection<String> getNodesLeadingToPathDiscard() {
		Collection<String> toReturn = super.getNodesLeadingToPathDiscard();
		toReturn.add("http://gaia.fdi.ucm.es/ontologies/vacation.owl#PERSONS");
		toReturn.add("http://gaia.fdi.ucm.es/ontologies/vacation.owl#CATEGORY");
		return toReturn;
	}
	
}