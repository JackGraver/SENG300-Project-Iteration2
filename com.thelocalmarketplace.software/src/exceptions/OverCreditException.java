package exceptions;

public class OverCreditException extends Exception {
	public OverCreditException(String amountOwed) {
		super("You Are Over The Credit Limit. " + amountOwed);
	}
}