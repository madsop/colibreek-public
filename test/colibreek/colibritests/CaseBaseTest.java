package colibreek.colibritests;

import java.util.ArrayList;
import java.util.Collection;

import jcolibri.casebase.LinearCaseBase;
import jcolibri.cbrcore.CBRCase;
import jcolibri.cbrcore.CBRCaseBase;
import jcolibri.cbrcore.Connector;
import jcolibri.exception.InitializingException;
import static org.mockito.Mockito.mock;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class CaseBaseTest {

	private CBRCaseBase casebase;
	private Connector mockedConnector;
	
	@Before
	public void setUp() throws InitializingException {
		casebase = new LinearCaseBase();
		mockedConnector = mock(Connector.class);
		casebase.init(mockedConnector);
	}
	
	@Test
	public void testNewCasebaseShouldReturnEmptyCasebase() {
		Assert.assertTrue(casebase.getCases().isEmpty());
	}
	
	@Test
	public void testPutOneCaseIntoCasebaseReturnsOnlyThatCase() {
		CBRCase newCase = new CBRCase();
		Collection<CBRCase> newCases = new ArrayList<>();
		newCases.add(newCase);
		casebase.learnCases(newCases);
		Assert.assertEquals(newCase, casebase.getCases().iterator().next());
	}
	
	@Test
	public void testForgetCasesWhenNoCasesExistShouldNotBreak() {
		casebase.forgetCases(new ArrayList<CBRCase>());
	}

	@Test
	public void testThatForgetCasesStillDontWork() {		
		CBRCase newCase = new CBRCase();
		ArrayList<CBRCase> newCases = new ArrayList<>();
		newCases.add(newCase);
		
		casebase.learnCases(newCases);
		Assert.assertEquals(newCase, casebase.getCases().iterator().next());
		Assert.assertEquals(1, casebase.getCases().size());
		
		casebase.forgetCases(newCases);
		Assert.assertNotSame(0, casebase.getCases().size());
		Assert.assertEquals(newCase, casebase.getCases().iterator().next());
		Assert.assertEquals(1, casebase.getCases().size());
	}
	
	
}