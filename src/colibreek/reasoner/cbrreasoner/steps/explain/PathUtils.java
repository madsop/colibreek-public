package colibreek.reasoner.cbrreasoner.steps.explain;

import jcolibri.datatypes.Instance;
import colibreek.config.DomainIndependentConfigurations;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntTools;
import com.hp.hpl.jena.ontology.OntTools.Path;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.util.iterator.Filter;

public final class PathUtils {
	private PathUtils() { }
	
	public static Individual getIndividualFromInstanceName(OntModel model, Instance instance) {
		return getIndividualFromName(model, instance.toString());
	}
	
	public static Individual getIndividualFromName(OntModel model, String name) {
		String startingPointURI = jcolibri.util.OntoBridgeSingleton.getOntoBridge().getURI(name);
		return model.getIndividual(startingPointURI);
	}

	public static Path constructPathFromStartToEnd(OntModel model, Individual start, OntClass startClass, Individual end, OntClass endClass, OntClass lca) {
		Path returnPath = new Path();
		Property typeProperty = model.getProperty(DomainIndependentConfigurations.TYPE_URI);

		StmtIterator startFromIndividualToClassIterator = model.listStatements(start, typeProperty, startClass);
		returnPath.add(startFromIndividualToClassIterator.next());

		@SuppressWarnings({ "deprecation" })
		Path fromStartToLCA = OntTools.findShortestPath(model, startClass, lca, Filter.any);
		returnPath.addAll(fromStartToLCA);

		@SuppressWarnings({ "deprecation" })
		Path fromLCAToEnd = OntTools.findShortestPath(model, endClass, lca, Filter.any);
		returnPath.addAll(fromLCAToEnd);

		StmtIterator endFromIndividualToClassIterator  = model.listStatements(end, typeProperty, endClass);
		returnPath.add(endFromIndividualToClassIterator.next());

		return returnPath;
	}
}