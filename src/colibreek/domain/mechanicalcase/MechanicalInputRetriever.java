package colibreek.domain.mechanicalcase;

import com.google.inject.Inject;

import colibreek.getInput.InputRetriever;
import colibreek.getInput.ScannerWrapper;
import jcolibri.datatypes.Instance;
import jcolibri.exception.OntologyAccessException;

public class MechanicalInputRetriever extends InputRetriever {

	@Inject
	public MechanicalInputRetriever(ScannerWrapper scanner) {
		super(scanner);
	}

	public Instance getExciterFieldVoltage() throws OntologyAccessException {
		return getValueFromUser("Give exciter field voltage", "Exciter field voltage");
	}

	public Instance getGeneratorPower() throws OntologyAccessException {
		return getValueFromUser("Give generator power","Generator power");
	}
	
	public Instance getTimestamp() throws OntologyAccessException {
		return getValueFromUser("Give timestamp (Format: YYYY-MM-DDTHH:MM:SS", "Timestamp");
	}
	
	public Instance getGeneratorID() throws OntologyAccessException {
		return getValueFromUser("Give generator ID","Generator ID");
	}
	
	public Instance getFrequency() throws OntologyAccessException {
		return getValueFromUser("Give frequency", "Frequency");
	}
	
	public Instance getExciterFieldCurrent() throws OntologyAccessException {
		return getValueFromUser("Give exciter field current", "Exciter field current");
	}
	
	public Instance getReactiveLoad() throws OntologyAccessException {
		return getValueFromUser("Give reactive load", "Reactive load");
	}
}
