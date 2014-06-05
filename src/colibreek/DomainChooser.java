package colibreek;

import colibreek.config.Domain;

public class DomainChooser {
	Domain getChosenDomain(String[] args) {
		if (args.length == 0) { return Domain.Travel; }
		try {
			return Domain.valueOf(args[0]);
		} catch (IllegalArgumentException e) {
			printAvailableDomains();
			System.exit(0);
		}
		return null;
	}

	void printAvailableDomains() {
		System.out.println("Domain provided was not found along the system domains. The registered domains are: ");
		for (Domain domain : Domain.values()) {
			System.out.println(domain.name());
		}
	}
}
