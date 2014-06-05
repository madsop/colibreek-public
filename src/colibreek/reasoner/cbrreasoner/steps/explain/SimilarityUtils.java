package colibreek.reasoner.cbrreasoner.steps.explain;

import java.util.DoubleSummaryStatistics;
import java.util.Optional;
import colibreek.config.DomainIndependentConfigurations;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;

public final class SimilarityUtils {
	private SimilarityUtils() { }
	
	public static double getDoubleFromIndividual(Individual individual) {
		String localnameNewFinding = getLocalNameFromIndividual(individual);
		return Double.parseDouble(localnameNewFinding);
	}

	public static String getLocalNameFromIndividual(Individual individual) {
		return individual.getURI().split("#")[1];
	}
	
	public static boolean isANumber(String uri) {
		return uri.matches("[-+]?\\d*\\.?\\d+");
	}
	
	public static double getRangeOfValuesOfSameClassAsTheInputIndividual(OntModel model, Individual individual) {
		Resource findingKeyResource = getLowestLevelClassForIndividual(individual);
		ExtendedIterator<Individual> maxIterator = model.listIndividuals(findingKeyResource);
		return findDistanceFromMaxToMin(maxIterator);
	}
	
	private static Resource getLowestLevelClassForIndividual(Individual individual) {
		Optional<Resource> res = individual.listRDFTypes(true).toList().stream().filter(x -> !x.getURI().equals(DomainIndependentConfigurations.OWL_NAMED_INDIVIDUAL)).findAny();
		if (!res.isPresent()) { return null; }
		return res.get();
	}
	
	public static double findDistanceFromMaxToMin(ExtendedIterator<Individual> iterator) {
		DoubleSummaryStatistics a = iterator.toList().stream().map(x -> getLocalNameFromIndividual(x))
				.filter(y -> isANumber(y)).mapToDouble(z -> Double.parseDouble(z)).summaryStatistics();
		return a.getMax() - a.getMin();
	}
	
	public static boolean individualIsNotADouble(Individual individualActivatedCase) {
		return !individualIsOfGivenType(individualActivatedCase, DomainIndependentConfigurations.DOUBLE);
	}

	public static boolean individualIsADateTime(Individual individual) {
		return individualIsOfGivenType(individual, DomainIndependentConfigurations.DATE_TIME_PROPERTY);
	}
	
	private static boolean individualIsOfGivenType(Individual individual, String type) {
		return individual.listProperties().toList().stream().
				filter(x -> x.getPredicate().getURI().equals(DomainIndependentConfigurations.TOP_DATA_PROPERTY) && x.getObject().toString().endsWith(type))
						.findAny().isPresent();
	}
}