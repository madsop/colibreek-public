package colibreek.domain.mechanicalcase;

import jcolibri.cbrcore.CBRQuery;
import jcolibri.datatypes.Instance;
import jcolibri.exception.InitializingException;
import jcolibri.exception.OntologyAccessException;
import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MechanicalDomainExecuterTest {
	private MechanicalDomainExecuter mechanicalDomainExecuter;
	private MechanicalInputRetriever inputRetriever;

	@Before
	public void setUp() throws InitializingException {
		MechanicalDomainTestHelper.establishCasebase();
		inputRetriever = mock(MechanicalInputRetriever.class);
		mechanicalDomainExecuter = new MechanicalDomainExecuter(inputRetriever, new MechanicalCaseConfigurations());
	}

	@Test
	public void testThatInstanceValuesCanBeSet() throws OntologyAccessException {
		Instance exciterFieldVoltage = new Instance("3.4");
		when(inputRetriever.getExciterFieldVoltage()).thenReturn(exciterFieldVoltage);

		CBRQuery query = mechanicalDomainExecuter.setUpQuery();
		MechanicalCaseDescription queryDesc = (MechanicalCaseDescription) query.getDescription();
		Assert.assertEquals(exciterFieldVoltage, queryDesc.getExciterFieldVoltage());
	}

	@Test
	public void testThat35AlsoWorks() throws OntologyAccessException {
		Instance exciterFieldVoltage35 = new Instance("3.5");
		when(inputRetriever.getExciterFieldVoltage()).thenReturn(exciterFieldVoltage35);
		CBRQuery query = mechanicalDomainExecuter.setUpQuery();
		MechanicalCaseDescription queryDescription = (MechanicalCaseDescription) query.getDescription();
		Assert.assertEquals(exciterFieldVoltage35, queryDescription.getExciterFieldVoltage());
	}

	@Test
	public void testThatGeneratorPowerCanBeSet() throws OntologyAccessException {
		Instance generatorPower = new Instance("47");
		when(inputRetriever.getGeneratorPower()).thenReturn(generatorPower);

		CBRQuery query = mechanicalDomainExecuter.setUpQuery();
		MechanicalCaseDescription queryDescription = (MechanicalCaseDescription) query.getDescription();

		Assert.assertNotSame(generatorPower, queryDescription.getExciterFieldVoltage());
		Assert.assertEquals(generatorPower, queryDescription.getGeneratorPower());
	}
}