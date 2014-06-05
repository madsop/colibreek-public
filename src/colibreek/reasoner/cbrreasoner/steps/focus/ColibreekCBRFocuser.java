package colibreek.reasoner.cbrreasoner.steps.focus;

import colibreek.reasoner.cbrreasoner.steps.explain.datastructure.ColibreekCBRExplanation;
import colibreek.reasoner.cbrreasoner.steps.explain.datastructure.ExplanationForOneCase;

public class ColibreekCBRFocuser {
	public ExplanationForOneCase focus(ColibreekCBRExplanation explanation) {
		return explanation.getMostSimilar();
	}
}