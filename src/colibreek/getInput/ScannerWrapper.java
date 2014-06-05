package colibreek.getInput;

import java.util.Scanner;

import com.google.inject.Inject;

public class ScannerWrapper {
	private Scanner scanner;
	
	@Inject
	public ScannerWrapper() {
		this.scanner = new Scanner(System.in);
	}
	
	public String nextLine() {
		return scanner.nextLine();
	}

}