package colibreek.domain.turbineagbcase;

import java.util.HashMap;
import java.util.Map;

import jcolibri.cbrcore.Attribute;
import jcolibri.datatypes.Instance;
import colibreek.caserepresentation.CaseDescription;
import colibreek.caserepresentation.CaseDescriptor;
import colibreek.caserepresentation.Finding;

public class TurbineAGBCaseDescription extends CaseDescription {
	Instance CaseID;
	Instance Timestamp;
	Instance TurbineShaftSpeed;
	Instance AGBBearingSumpTemperature;
	Instance LubeOilSupplyPressure;
	Instance LubeOilSupplyTemperature;
	Instance FuelGasPressure;
	Instance GGBSumpTemperature;
	Instance GGCSumpTemperature;
	
	public Instance getCaseID() {
		return CaseID;
	}

	public void setCaseID(Instance caseID) {
		CaseID = caseID;
	}

	public Instance getTimestamp() {
		return Timestamp;
	}

	public void setTimestamp(Instance timestamp) {
		Timestamp = timestamp;
	}

	public Instance getTurbineShaftSpeed() {
		return TurbineShaftSpeed;
	}

	public void setTurbineShaftSpeed(Instance turbineShaftSpeed) {
		TurbineShaftSpeed = turbineShaftSpeed;
	}

	public Instance getAGBBearingSumpTemperature() {
		return AGBBearingSumpTemperature;
	}

	public void setAGBBearingSumpTemperature(Instance aGBBearingSumpTemperature) {
		AGBBearingSumpTemperature = aGBBearingSumpTemperature;
	}

	public Instance getLubeOilSupplyPressure() {
		return LubeOilSupplyPressure;
	}

	public void setLubeOilSupplyPressure(Instance lubeOilSupplyPressure) {
		LubeOilSupplyPressure = lubeOilSupplyPressure;
	}

	public Instance getLubeOilSupplyTemperature() {
		return LubeOilSupplyTemperature;
	}

	public void setLubeOilSupplyTemperature(Instance lubeOilSupplyTemperature) {
		LubeOilSupplyTemperature = lubeOilSupplyTemperature;
	}

	public Instance getFuelGasPressure() {
		return FuelGasPressure;
	}

	public void setFuelGasPressure(Instance fuelGasPressure) {
		FuelGasPressure = fuelGasPressure;
	}

	public Instance getGGBSumpTemperature() {
		return GGBSumpTemperature;
	}

	public void setGGBSumpTemperature(Instance gGBSumpTemperature) {
		GGBSumpTemperature = gGBSumpTemperature;
	}

	public Instance getGGCSumpTemperature() {
		return GGCSumpTemperature;
	}

	public void setGGCSumpTemperature(Instance gGCSumpTemperature) {
		GGCSumpTemperature = gGCSumpTemperature;
	}
	
	@Override
	public Attribute getIdAttribute() {
		return new Attribute("CaseID", this.getClass());
	}

	@Override
	public Map<CaseDescriptor, Finding> getCaseFindings() {
		Map<CaseDescriptor, Finding> findings = new HashMap<>();
		findings.put(TurbineAGBCaseDescriptor.TURBINESHAFTSPEED, new Finding(TurbineShaftSpeed, 1.0));
		findings.put(TurbineAGBCaseDescriptor.AGBBEARINGSUMPTEMPERATURE, new Finding(AGBBearingSumpTemperature, 1.0));
		findings.put(TurbineAGBCaseDescriptor.LUBEOILSUPPLYPRESSURE, new Finding(LubeOilSupplyPressure, 1.0));
		findings.put(TurbineAGBCaseDescriptor.LUBEOILSUPPLYTEMPERATURE, new Finding(LubeOilSupplyTemperature, 1.0));
		findings.put(TurbineAGBCaseDescriptor.FUELGASPRESSURE, new Finding(FuelGasPressure, 1.0));
		findings.put(TurbineAGBCaseDescriptor.GGBSUMPTEMPERATURE, new Finding(GGBSumpTemperature, 1.0));
		findings.put(TurbineAGBCaseDescriptor.GGCSUMPTEMPERATURE, new Finding(GGCSumpTemperature, 1.0));
		return findings;
	}

	@Override
	public Instance getCaseId() {
		return CaseID;
	}
}