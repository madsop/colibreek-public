package colibreek.getInput;

import com.google.inject.Inject;

import colibreek.config.DomainIndependentConfigurations;
import jcolibri.datatypes.Instance;
import jcolibri.exception.OntologyAccessException;
import jcolibri.util.OntoBridgeSingleton;

public class InputRetriever {
	private ScannerWrapper scanner;

	@Inject
	public InputRetriever(ScannerWrapper scanner) {
		this.scanner = scanner;
	}

	public String nextLine() {
		return scanner.nextLine();
	}

	public Instance getValueFromUser(String callToUser, String classname) throws OntologyAccessException {
		System.out.println(callToUser);
		return promptUserForValidInput(classname);
	}

	protected Instance promptUserForValidInput(String classname) throws OntologyAccessException {
		String next = getAllowedInputFromUser(classname);
		if (next.length() == 0) { return null; }
		return new Instance(next);
	}

	private String getAllowedInputFromUser(String classname) {
		String next = nextLine();
		int count = 0; 
		while (count < DomainIndependentConfigurations.NUMBER_OF_ALLOWED_INPUT_RETRIES && next.length() > 0 && !OntoBridgeSingleton.getOntoBridge().existsInstance(next)) {
			System.out.println(classname + " value not found in ontology, please try again or give empty string to skip.");
			next = nextLine();
			count++;
		}
		return next;
	}
}