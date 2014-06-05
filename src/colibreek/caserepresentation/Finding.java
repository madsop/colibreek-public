package colibreek.caserepresentation;

import jcolibri.datatypes.Instance;

public class Finding {
	private double relevanceFactor;
	private Instance instanceValue;
	
	public Finding(Instance instanceValue, double relevanceFactor) {
		this.instanceValue = instanceValue;
		this.relevanceFactor = relevanceFactor;
	}

	public double getRelevanceFactor() {
		return relevanceFactor;
	}

	public Instance getInstanceValue() {
		return instanceValue;
	}
	
	public boolean isNull() {
		return instanceValue == null;
	}
	
	public int hashCode() {
		return getInstanceValue().hashCode();
	}
	
	public boolean equals(Object obj) {
		if (obj == null) { return false; }
		Finding findingToCompareTo = (Finding) obj;
		return findingToCompareTo.getInstanceValue().equals(getInstanceValue());
	}
	
	public String toString() {
		return instanceValue.toString();
	}
}