package colibreek.domain.mechanicalcase;

import jcolibri.casebase.IDIndexedLinearCaseBase;
import jcolibri.datatypes.Instance;
import jcolibri.exception.InitializingException;
import jcolibri.exception.OntologyAccessException;
import colibreek.TestHelper;
import colibreek.caserepresentation.CaseDescription;

public final class MechanicalDomainTestHelper extends TestHelper {
	public static final String NAMESPACE = "http://www.semanticweb.org/mads/ontologies/2014/2/untitled-ontology-24#";

	public static CaseDescription setUpQuery() throws OntologyAccessException {
		MechanicalCaseDescription queryDesc = new MechanicalCaseDescription();
		queryDesc.setGeneratorPower(new Instance("3.7"));
		queryDesc.setGeneratorID(new Instance("EE-43-E660"));
		queryDesc.setExciterFieldVoltage(new Instance("45"));
		queryDesc.setTimestamp(new Instance("2014-04-13T00:00:00"));
		queryDesc.setFrequency(new Instance("60.2"));
		queryDesc.setExciterFieldCurrent(new Instance("1.8"));
		queryDesc.setReactiveLoad(new Instance("1.7"));
		queryDesc.setPhaseACurrent(new Instance("359.1"));
		queryDesc.setPhaseBCurrent(new Instance("349.1"));
		return queryDesc;
	}
	
	public static IDIndexedLinearCaseBase establishCasebase() throws InitializingException {
		return establishCasebase("config/mechanicalCaseConfig.xml");
	}
}
