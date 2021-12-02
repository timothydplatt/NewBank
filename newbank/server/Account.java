package server;

public class Account {
	public int accountNumber;
	public int customerID;
	public double balance;
	public String accountType;
	public String openDate;

	public Account(int accountNumber, int customerID, double balance, String accountType, String openDate) {
		this.accountNumber = accountNumber;
		this.customerID = customerID;
		this.balance = balance;
		this.accountType = accountType;
		this.accountType = openDate;
	}

	public Account() {
		this.accountNumber = 0;
		this.customerID = 0;
		this.balance = 0;
		this.accountType = "";
		this.accountType = "";
	}

}
