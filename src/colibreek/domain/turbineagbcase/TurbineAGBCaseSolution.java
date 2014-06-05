package colibreek.domain.turbineagbcase;

import colibreek.caserepresentation.CaseSolution;
import jcolibri.cbrcore.Attribute;
import jcolibri.datatypes.Instance;

public class TurbineAGBCaseSolution extends CaseSolution {
	Instance CaseID;
	Instance IsAlarmingSituation;

	@Override
	public Instance getMainConcept() {
		return CaseID;
	}

	public Instance getCaseID() {
		return CaseID;
	}

	public void setCaseID(Instance caseID) {
		CaseID = caseID;
	}

	public Instance getIsAlarmingSituation() {
		return IsAlarmingSituation;
	}

	public void setIsAlarmingSituation(Instance isAlarmingSituation) {
		IsAlarmingSituation = isAlarmingSituation;
	}

	@Override
	public Attribute getIdAttribute() {
		return new Attribute("CaseID", this.getClass());
	}
	
	public String toString() {
		return "CaseID: " + CaseID + ", isAlarmingSituation: " + IsAlarmingSituation;
	}

}
