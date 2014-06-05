package colibreek.reasoner.cbrreasoner.steps.activate;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Statement;

public final class StatementUtils {
	private StatementUtils() { }
	
	public static Map<RDFNode, Set<Statement>> getOriginalStatementsPerSubject(List<Statement> originalStatements) {
		Map<RDFNode, Set<Statement>> originalStatementsPerSubject = new HashMap<>();
		for (Statement statement : originalStatements) {
			originalStatementsPerSubject.putIfAbsent(statement.getSubject(), new HashSet<Statement>());
			originalStatementsPerSubject.get(statement.getSubject()).add(statement);
		}
		return originalStatementsPerSubject;
	}

	public static Set<RDFNode> findAllObjectsFromStatements(List<Statement> originalStatements) {
		return originalStatements.stream().map(x -> x.getObject()).collect(Collectors.toSet());
	}
	
	public static boolean statementHasFindingAsSubjectOrObject(Statement statement, String instancevalue) {
		return statement.getSubject().toString().endsWith(instancevalue) || statement.getObject().toString().endsWith(instancevalue);
	}

}