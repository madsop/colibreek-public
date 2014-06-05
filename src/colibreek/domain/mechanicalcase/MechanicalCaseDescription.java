package colibreek.domain.mechanicalcase;

import java.util.HashMap;
import java.util.Map;

import colibreek.caserepresentation.CaseDescription;
import colibreek.caserepresentation.CaseDescriptor;
import colibreek.caserepresentation.Finding;
import jcolibri.cbrcore.Attribute;
import jcolibri.datatypes.Instance;

public class MechanicalCaseDescription extends CaseDescription {
	Instance CaseID;
	Instance Timestamp;
	Instance GeneratorID;
	Instance GeneratorPower;
	Instance ExciterFieldVoltage;
	Instance Frequency;
	Instance ExciterFieldCurrent;
	Instance ReactiveLoad;
	Instance PhaseACurrent;
	Instance PhaseBCurrent;
	
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

	public Instance getGeneratorID() {
		return GeneratorID;
	}

	public void setGeneratorID(Instance generatorID) {
		GeneratorID = generatorID;
	}

	public Instance getGeneratorPower() {
		return GeneratorPower;
	}

	public void setGeneratorPower(Instance generatorPower) {
		GeneratorPower = generatorPower;
	}

	public Instance getExciterFieldVoltage() {
		return ExciterFieldVoltage;
	}

	public void setExciterFieldVoltage(Instance exciterFieldVoltage) {
		ExciterFieldVoltage = exciterFieldVoltage;
	}

	public Instance getFrequency() {
		return Frequency;
	}

	public void setFrequency(Instance frequency) {
		Frequency = frequency;
	}

	public Instance getExciterFieldCurrent() {
		return ExciterFieldCurrent;
	}

	public void setExciterFieldCurrent(Instance exciterFieldCurrent) {
		ExciterFieldCurrent = exciterFieldCurrent;
	}

	public Instance getReactiveLoad() {
		return ReactiveLoad;
	}

	public void setReactiveLoad(Instance reactiveLoad) {
		ReactiveLoad = reactiveLoad;
	}

	public Instance getPhaseACurrent() {
		return PhaseACurrent;
	}

	public void setPhaseACurrent(Instance phaseACurrent) {
		PhaseACurrent = phaseACurrent;
	}

	public Instance getPhaseBCurrent() {
		return PhaseBCurrent;
	}

	public void setPhaseBCurrent(Instance phaseBCurrent) {
		PhaseBCurrent = phaseBCurrent;
	}
	
	@Override
	public Attribute getIdAttribute() {
		return new Attribute("CaseID", this.getClass());
	}
	

	private double exciterFieldVoltageRelevanceFactor = 1.0;
	private double generatorIDRelevanceFactor = 0.0;
	private double generatorPowerRelevanceFactor = 1.0;
	private double timestampRelevanceFactor = 0.5;
	private double frequencyRelevanceFactor = 1.0;
	private double exciterFieldCurrentRelevanceFactor = 1.0;
	private double reactiveLoadRelevanceFactor = 1.0;
	private double phaseACurrentRelevanceFactor = 1.0;
	private double phaseBCurrentRelevanceFactor = 1.0;

	@Override
	public Map<CaseDescriptor, Finding> getCaseFindings() {
		Map<CaseDescriptor, Finding> findings = new HashMap<>();
		findings.put(MechanicalCaseDescriptor.EXCITERFIELDVOLTAGE, new Finding(ExciterFieldVoltage, exciterFieldVoltageRelevanceFactor));
		findings.put(MechanicalCaseDescriptor.GENERATORID, new Finding(GeneratorID, generatorIDRelevanceFactor));
		findings.put(MechanicalCaseDescriptor.GENERATORPOWER, new Finding(GeneratorPower, generatorPowerRelevanceFactor));
		findings.put(MechanicalCaseDescriptor.TIMESTAMP, new Finding(Timestamp, timestampRelevanceFactor));
		findings.put(MechanicalCaseDescriptor.FREQUENCY, new Finding(Frequency, frequencyRelevanceFactor));
		findings.put(MechanicalCaseDescriptor.EXCITERFIELDCURRENT, new Finding(ExciterFieldCurrent, exciterFieldCurrentRelevanceFactor));
		findings.put(MechanicalCaseDescriptor.REACTIVELOAD, new Finding(ReactiveLoad, reactiveLoadRelevanceFactor));
		findings.put(MechanicalCaseDescriptor.PHASEACURRENT, new Finding(PhaseACurrent, phaseACurrentRelevanceFactor));
		findings.put(MechanicalCaseDescriptor.PHASEBCURRENT, new Finding(PhaseBCurrent, phaseBCurrentRelevanceFactor));
		return findings;
	}

	@Override
	public Instance getCaseId() {
		return CaseID;
	}

}
