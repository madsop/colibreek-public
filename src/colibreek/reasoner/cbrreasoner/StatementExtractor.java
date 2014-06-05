package colibreek.reasoner.cbrreasoner;

import java.util.EnumSet;
import java.util.List;

import org.mindswap.pellet.jena.ModelExtractor;
import org.mindswap.pellet.jena.ModelExtractor.StatementType;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;

import es.ucm.fdi.gaia.ontobridge.OntoBridge;

public class StatementExtractor {
	public List<Statement> getInitialStatements() {
		OntoBridge ontobridge = jcolibri.util.OntoBridgeSingleton.getOntoBridge();
		Model model = findModelWithOnlyNecessaryStatements(ontobridge);
		
		StmtIterator statements = model.listStatements();
		return statements.toList();
	}

	private Model findModelWithOnlyNecessaryStatements(OntoBridge ontobridge) {
		ModelExtractor extractor = new ModelExtractor(ontobridge.getModel());
		
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
}