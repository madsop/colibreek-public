package colibreek.caserepresentation;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import jcolibri.cbrcore.CaseComponent;
import jcolibri.datatypes.Instance;

public abstract class CaseDescription  implements CaseComponent {
	public abstract Map<CaseDescriptor, Finding> getCaseFindings();
	public abstract Instance getCaseId();
	
	public Map<CaseDescriptor, Finding> getNonNullCaseFindings() {
		Map<CaseDescriptor, Finding> findings = new HashMap<>();
		for (Entry<CaseDescriptor, Finding> possiblyNullFinding : getCaseFindings().entrySet()) {
			if (!possiblyNullFinding.getValue().isNull()) {
				findings.put(possiblyNullFinding.getKey(), possiblyNullFinding.getValue());
			}
		}
		return findings;
	}
	
	public String toString() {
		return getNonNullCaseFindings().entrySet().stream().map(x -> x.getKey() + ": " + x.getValue() + ", ").sorted().reduce((a, b) -> a + b).get();
	}
}
