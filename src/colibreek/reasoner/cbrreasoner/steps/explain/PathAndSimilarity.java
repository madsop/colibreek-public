package colibreek.reasoner.cbrreasoner.steps.explain;

import com.hp.hpl.jena.ontology.OntTools.Path;

public class PathAndSimilarity {
	public final double similarity;
	public final Path path;

	public PathAndSimilarity(double similarity, Path path) {
		this.similarity = similarity;
		this.path = path;
	}
	
	public PathAndSimilarity(double similarity) {
		this(similarity, new Path());
	}
}