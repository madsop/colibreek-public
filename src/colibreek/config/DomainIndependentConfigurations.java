package colibreek.config;

public final class DomainIndependentConfigurations {
	private DomainIndependentConfigurations() { }
	
	public static final String TYPE_URI = "http://www.w3.org/1999/02/22-rdf-syntax-ns#type";
	public static final String TOP_NODE_URI = "http://www.w3.org/2002/07/owl#Thing";
	public static final String DOUBLE = "http://www.w3.org/2001/XMLSchema#double";
	public static final String TOP_DATA_PROPERTY = "http://www.w3.org/2002/07/owl#topDataProperty";
	public static final String DATE_TIME_PROPERTY = "http://www.w3.org/2001/XMLSchema#dateTime";
	public static final String OWL_NAMED_INDIVIDUAL = "http://www.w3.org/2002/07/owl#NamedIndividual";
	
	public static final int NUMBER_OF_ALLOWED_INPUT_RETRIES = 30;	
}