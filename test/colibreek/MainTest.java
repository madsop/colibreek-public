package colibreek;

import jcolibri.cbrcore.CBRCaseBase;
import jcolibri.exception.ExecutionException;
import junit.framework.Assert;

import org.junit.Test;

public class MainTest {
	@Test
	public void testThatMainSetupIsOk() throws ExecutionException {
		String[] args = new String[0];
		Main main = new Main(args);
		CBRCaseBase casebase = main.setUp();
		Assert.assertNotNull(casebase);
	}
}