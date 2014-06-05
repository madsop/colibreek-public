package colibreek;

import java.util.EnumSet;
import java.util.Set;

import jcolibri.casebase.IDIndexedLinearCaseBase;
import jcolibri.connector.OntologyConnector;
import jcolibri.exception.InitializingException;

import org.mindswap.pellet.jena.ModelExtractor;
import org.mindswap.pellet.jena.ModelExtractor.StatementType;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;

import es.ucm.fdi.gaia.ontobridge.OntoBridge;

public abstract class TestHelper {
	protected TestHelper() {
	}

	public static StmtIterator getAllStatementsFromOWLFileAsIterator() {
		Model model = getModel();
		return model.listStatements();
	}
	
	public static Set<Statement> getAllStatementsFromOWLFileAsSetIterator() {
		return getAllStatementsFromOWLFileAsIterator().toSet();
	}
	
	public static Model getModel() {
		OntoBridge ontobridge = jcolibri.util.OntoBridgeSingleton.getOntoBridge();
		Model model = ontobridge.getModel();
		
		return getModelWithOnlyInterestingStatementTypes(model);
	}

	private static Model getModelWithOnlyInterestingStatementTypes(Model model) {
		ModelExtractor extractor = new ModelExtractor(model);
		extractor.setSelector(EnumSet.of(
				StatementType.DIRECT_SUBCLASS,
				StatementType.DIRECT_INSTANCE,
				StatementType.DIRECT_SUBPROPERTY,
				StatementType.DATA_PROPERTY_VALUE,
				StatementType.SAME_AS
			)
		);
		return extractor.extractModel();
	}
	
	public static OntModel getOntModel() {
		OntModel model = ModelFactory.createOntologyModel();
		model.add(getModel());
		return model;
	}
	
	protected static IDIndexedLinearCaseBase establishCasebase(String locationOfXMLConfigFile) throws InitializingException {
		IDIndexedLinearCaseBase casebase = new IDIndexedLinearCaseBase();
		OntologyConnector connector = new OntologyConnector();
		connector.initFromXMLfile(jcolibri.util.FileIO.findFile(locationOfXMLConfigFile));
		casebase.init(connector);
		return casebase;
	}
}