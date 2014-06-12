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
	
	public Instance getMainConcept() {
		return mainConcept;
	}

	public void setMainConcept(Instance mc) {
		this.mainConcept = mc;
	}
	
	public Instance getPrice() {
		return price;
	}

	public void setPrice(Instance price) {
		this.price = price;
	}
}
