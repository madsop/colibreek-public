package colibreek.reasoner.cbrreasoner.steps.activate;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import jcolibri.cbrcore.CBRCase;

import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Statement;

public class ActivatedAreaAndCases {
	private final Set<CBRCase> cases;
	private final Map<RDFNode, Set<Statement>> statementsGroupedByResource;

	public ActivatedAreaAndCases() {
		cases = new HashSet<>();
		statementsGroupedByResource = new HashMap<>();
	}

	public ActivatedAreaAndCases(List<Statement> statementsPerResource) {
		this();
		addStatements(statementsPerResource);
	}

	public Set<CBRCase> getCases() {
		return cases;
	}

	public Map<RDFNode, Set<Statement>> getActivatedStatements() {
		return statementsGroupedByResource;
	}

	public List<Statement> getStatements() {
		return statementsGroupedByResource.values().stream().flatMap(x -> x.stream()).collect(Collectors.toList());
	}

	public void addStatements(Collection<Statement> statements) {
		statements.forEach(s -> addStatement(s));
	}

	private void addStatement(Statement statement) {
		statementsGroupedByResource.putIfAbsent(statement.getSubject(), new HashSet<Statement>());
		statementsGroupedByResource.get(statement.getSubject()).add(statement);
	}

	public void addCases(Collection<CBRCase> activatedCases) {
		cases.addAll(activatedCases);
	}

	public void removeCases(Collection<CBRCase> casesToRemove) {
		cases.removeAll(casesToRemove);
	}
}