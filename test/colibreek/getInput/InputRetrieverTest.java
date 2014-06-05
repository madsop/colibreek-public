package colibreek.getInput;

import jcolibri.datatypes.Instance;
import jcolibri.exception.InitializingException;
import jcolibri.exception.OntologyAccessException;
import junit.framework.Assert;

import org.junit.Test;

import colibreek.domain.mechanicalcase.MechanicalDomainTestHelper;
import colibreek.getInput.InputRetriever;
import colibreek.getInput.ScannerWrapper;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class InputRetrieverTest {
	@Test
	public void testThatNoInputReturnsNull() throws OntologyAccessException {
		ScannerWrapper scanner = mock(ScannerWrapper.class);
		when(scanner.nextLine()).thenReturn("");
		InputRetriever inputRetriever = new InputRetriever(scanner);
		Instance returnedInstance = inputRetriever.getValueFromUser("", "");
		Assert.assertNull(returnedInstance);
	}
	
	@Test
	public void testThatActualInputReturnsActualInstance() throws OntologyAccessException, InitializingException {
		ScannerWrapper scanner = mock(ScannerWrapper.class);
		MechanicalDomainTestHelper.establishCasebase();
		when(scanner.nextLine()).thenReturn("3.5");
		InputRetriever inputRetriever = new InputRetriever(scanner);
		Instance returnedInstance = inputRetriever.getValueFromUser("", "");
		Assert.assertEquals(new Instance("3.5"), returnedInstance);
	}
	
	@Test(expected = OntologyAccessException.class)
	public void testThatNonRegisteredInputReturnsNullInstance() throws OntologyAccessException, InitializingException {
		ScannerWrapper scanner = mock(ScannerWrapper.class);
		MechanicalDomainTestHelper.establishCasebase();
		when(scanner.nextLine()).thenReturn("mockDataNotInOntology");
		InputRetriever inputRetriever = new InputRetriever(scanner);
		Instance returnedInstance = inputRetriever.getValueFromUser("", "");
		Assert.assertEquals(null, returnedInstance);
	}
}