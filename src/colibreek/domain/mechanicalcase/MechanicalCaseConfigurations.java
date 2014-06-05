package colibreek.domain.mechanicalcase;

import colibreek.config.DomainDependentConfigurations;

public class MechanicalCaseConfigurations extends DomainDependentConfigurations {
	@Override
	public String getLocationOfXMLConfigFile() {
		return "config/mechanicalCaseConfig.xml";
	}
}