package colibreek.domain.travelrecommender;

import jcolibri.cbrcore.Attribute;
import jcolibri.datatypes.Instance;
import colibreek.caserepresentation.CaseSolution;


public class TravelCaseSolution extends CaseSolution { 
	Instance mainConcept;
	Instance price;
	
	public String toString() {
		return "(" + mainConcept + ";" + price + ")";
	}
	
	public Attribute getIdAttribute() {
		
		return new Attribute("mainConcept", this.getClass());
	}
	/**
	 * @return Returns the id.
	 */
	public Instance getMainConcept() {
		return mainConcept;
	}

	/**
	 * @param mc The main concept to set.
	 */
	public void setMainConcept(Instance mc) {
		this.mainConcept = mc;
	}

	/**
	 * @return Returns the price.
	 */
	public Instance getPrice() {
		return price;
	}

	/**
	 * @param price The price to set.
	 */
	public void setPrice(Instance price) {
		this.price = price;
	}
}
