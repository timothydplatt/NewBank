package newbank.server;

import java.util.ArrayList;

public class Customer {
	private String userName;
	private String password;
	private ArrayList<Account> accounts;
	
	public Customer(String userName, String password) {
		this.userName = userName;
		this.password = password;
		accounts = new ArrayList<>();
	}
	
	public String accountsToString() {
		String s = "";
		for(Account a : accounts) {
			s += a.toString();
		}
		return s;
	}

	public Boolean validatePassword (String password) {
		return this.password.equals(password);
	}

	public void addAccount(Account account) {
		accounts.add(account);		
	}
}
