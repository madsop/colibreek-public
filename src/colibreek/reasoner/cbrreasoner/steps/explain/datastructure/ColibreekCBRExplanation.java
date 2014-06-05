package colibreek.reasoner.cbrreasoner.steps.explain.datastructure;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ColibreekCBRExplanation  {
	private List<ExplanationForOneCase> explanationsForSingleCases;

	public ColibreekCBRExplanation() {
		explanationsForSingleCases = new ArrayList<>();
	}

	public void addExplanationForOneCase(ExplanationForOneCase explanationForOneCase) {
		explanationsForSingleCases.add(explanationForOneCase);
	}

	public List<ExplanationForOneCase> getExplanations()  {
		Collections.sort(explanationsForSingleCases);
		return explanationsForSingleCases;
	}
	
	public ExplanationForOneCase getMostSimilar()  {
		if (explanationsForSingleCases.isEmpty()) { return new EmptyExplanationForOneCase(); }
		return getExplanations().get(0);
	}

	public String toString() {
		return explanationsForSingleCases.stream().map(e -> e + "\n").toString();
	}
}