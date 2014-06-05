package colibreek.reasoner.cbrreasoner.steps.explain.datastructure;

import java.util.HashSet;

import colibreek.caserepresentation.NullCBRCase;
import colibreek.reasoner.cbrreasoner.steps.explain.FindingSimilarity;
import jcolibri.cbrcore.CBRCase;

public class EmptyExplanationForOneCase extends ExplanationForOneCase {
	private CBRCase cbrCase;
	
	public EmptyExplanationForOneCase() {
		super(null, new HashSet<FindingSimilarity>());
		cbrCase = new NullCBRCase();
	}
	
	public CBRCase getCase() {
		return cbrCase;
	}
	
	public Object getCaseID() {
		return cbrCase.getID();
	}
	
	public double getSimilarity() {
		return 0.0;
	}

}