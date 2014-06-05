package colibreek.config;

import jcolibri.casebase.LinearCaseBase;
import jcolibri.cbrcore.CBRCaseBase;
import jcolibri.cbrcore.Connector;
import jcolibri.connector.OntologyConnector;
import colibreek.DomainExecuter;
import colibreek.domain.mechanicalcase.MechanicalCaseConfigurations;
import colibreek.domain.mechanicalcase.MechanicalDomainExecuter;
import colibreek.domain.mechanicalcase.MechanicalInputRetriever;
import colibreek.domain.travelrecommender.TravelCaseConfigurations;
import colibreek.domain.travelrecommender.TravelDomainExecuter;
import colibreek.domain.turbineagbcase.TurbineAGBCaseConfigurations;
import colibreek.domain.turbineagbcase.TurbineAGBDomainExecuter;
import colibreek.getInput.InputRetriever;
import colibreek.reasoner.ColibreekReasoner;
import colibreek.reasoner.cbrreasoner.CBRReasonerFactory;
import colibreek.reasoner.cbrreasoner.ColibreekCBRReasoner;

import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;

public class ColibreekModule extends AbstractModule {
	private Domain domain;

	public ColibreekModule(Domain domain) {
		this.domain = domain;
	}	

	@Override
	protected void configure() {
		bind(CBRCaseBase.class).to(LinearCaseBase.class);
		bind(Connector.class).to(OntologyConnector.class);
		install(new FactoryModuleBuilder().implement(ColibreekReasoner.class, ColibreekCBRReasoner.class).build(CBRReasonerFactory.class));

		configureDomain();
	}

	private void configureDomain() {
		switch(domain) {
		case TurbineSensor:
			bindMechanical();
			break;
		case Travel:
			bindTravel();
			break;
		case TurbineAGB:
			bindTurbineAGB();
			break;
		default:
			bindMechanical();
		}
	}

	private void bindTurbineAGB() {
		bind(DomainExecuter.class).to(TurbineAGBDomainExecuter.class);
		bind(DomainDependentConfigurations.class).to(TurbineAGBCaseConfigurations.class);
	}

	private void bindMechanical() {
		bind(DomainExecuter.class).to(MechanicalDomainExecuter.class);
		bind(DomainDependentConfigurations.class).to(MechanicalCaseConfigurations.class);
		bind(InputRetriever.class).to(MechanicalInputRetriever.class);
	}

	private void bindTravel() {
		bind(DomainExecuter.class).to(TravelDomainExecuter.class);
		bind(DomainDependentConfigurations.class).to(TravelCaseConfigurations.class);
	}
}