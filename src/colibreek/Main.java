package colibreek;

import javax.annotation.Generated;

import com.google.inject.Guice;
import com.google.inject.Injector;

import jcolibri.cbraplications.StandardCBRApplication;
import jcolibri.cbrcore.CBRCaseBase;
import jcolibri.cbrcore.CBRQuery;
import jcolibri.cbrcore.Connector;
import jcolibri.exception.ExecutionException;
import jcolibri.exception.InitializingException;
import jcolibri.exception.OntologyAccessException;
import colibreek.reasoner.ColibreekReasoner;
import colibreek.reasoner.cbrreasoner.CBRReasonerFactory;
import colibreek.caserepresentation.CaseDescription;
import colibreek.config.ColibreekModule;
import colibreek.config.Domain;

public class Main implements StandardCBRApplication {
	private Injector injector;
	private Connector connector;
	private CBRCaseBase casebase; 
	private CBRReasonerFactory reasonerFactory;
	private DomainExecuter domainExecuter;
	
	public Main(String[] args) {
		Domain chosenDomain = new DomainChooser().getChosenDomain(args);
		ColibreekModule module = new ColibreekModule(chosenDomain);
		injector = Guice.createInjector(module);
		connector = injector.getInstance(Connector.class);
		casebase = injector.getInstance(CBRCaseBase.class);
		reasonerFactory = injector.getInstance(CBRReasonerFactory.class);
		domainExecuter = injector.getInstance(DomainExecuter.class);
	}

	public CBRCaseBase setUp() throws ExecutionException {
		return setUp(domainExecuter);
	}

	public CBRCaseBase setUp(DomainExecuter domainExecuter) throws ExecutionException { 
		this.domainExecuter = domainExecuter; 
		configure();
		return preCycle();
	}

	@Override
	public void configure() throws InitializingException {
		connector.initFromXMLfile(jcolibri.util.FileIO.findFile(domainExecuter.getLocationOfXMLConfigFile()));
	}

	@Generated(value = { "ColibriStudio" })
	@Override
	public CBRCaseBase preCycle() throws ExecutionException {
		casebase.init(connector);
		return casebase;
	} 

	public void startReasoning() throws ExecutionException, OntologyAccessException {
		cycle(domainExecuter.setUpQuery());
		postCycle();
	}

	@Generated(value = { "ColibriStudio" })	
	@Override 
	public void cycle(CBRQuery query) throws ExecutionException {
		ColibreekReasoner reasoner = reasonerFactory.createReasoner(casebase);
		CaseDescription newCase = (CaseDescription) query.getDescription();
		domainExecuter.findSolution(reasoner, newCase);
	}

	@Generated(value = { "ColibriStudio" })
	@Override
	public void postCycle() throws ExecutionException {
		connector.close();
		casebase.close();
	}

	public static void main(String[] args) {
		Main main = new Main(args);
		try {
			main.setUp();
			main.startReasoning();
		} catch (ExecutionException | OntologyAccessException e) {
			e.printStackTrace();
		}
	}
}