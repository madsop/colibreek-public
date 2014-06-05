package colibreek.colibritests;

import jcolibri.cbrcore.CBRCase;
import junit.framework.Assert;

import org.junit.Test;

public class CaseTest {
	@Test
	public void testSolutionIsInitiallyNull() {
		CBRCase newCase = new CBRCase();
		Assert.assertNull(newCase.getSolution());
	}
	
	@Test
	public void testThatCaseIsNotInitialised() {
		CBRCase newCase = new CBRCase();
		Assert.assertNull(newCase.getDescription());
		Assert.assertNull(newCase.getSolution());
		Assert.assertNull(newCase.getID());
	}
}