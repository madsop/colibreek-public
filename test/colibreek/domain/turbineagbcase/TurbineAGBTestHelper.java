package colibreek.domain.turbineagbcase;

import jcolibri.casebase.IDIndexedLinearCaseBase;
import jcolibri.datatypes.Instance;
import jcolibri.exception.InitializingException;
import jcolibri.exception.OntologyAccessException;
import colibreek.TestHelper;

public class TurbineAGBTestHelper extends TestHelper {

	public static IDIndexedLinearCaseBase establishCasebase() throws InitializingException {
		return establishCasebase("config/turbineAGBConfig.xml");
	}
	
	public static TurbineAGBCaseDescription setUpCase0Query() throws OntologyAccessException {
		TurbineAGBCaseDescription queryDescription = new TurbineAGBCaseDescription();	
		queryDescription.setTurbineShaftSpeed(new Instance("9104.4"));
		queryDescription.setAGBBearingSumpTemperature(new Instance("94.3"));
		queryDescription.setFuelGasPressure(new Instance("29.7"));
		queryDescription.setGGBSumpTemperature(new Instance("117.8"));
		queryDescription.setGGCSumpTemperature(new Instance("128.1"));
		queryDescription.setLubeOilSupplyPressure(new Instance("4"));
		queryDescription.setLubeOilSupplyTemperature(new Instance("65.7"));
		return queryDescription;
	}
	
	public static TurbineAGBCaseDescription setUpAlarmingQuery() throws OntologyAccessException {
		TurbineAGBCaseDescription queryDescription = new TurbineAGBCaseDescription();	
		queryDescription.setTurbineShaftSpeed(new Instance("9054.3"));
		queryDescription.setAGBBearingSumpTemperature(new Instance("133.2"));
		queryDescription.setLubeOilSupplyPressure(new Instance("4"));
		queryDescription.setLubeOilSupplyTemperature(new Instance("68"));
		queryDescription.setFuelGasPressure(new Instance("29.7"));
		queryDescription.setGGBSumpTemperature(new Instance("118.6"));
		queryDescription.setGGCSumpTemperature(new Instance("127.6"));
		return queryDescription;
	}
}
