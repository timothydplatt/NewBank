package newbank.server;

import java.util.ArrayList;

public class Customer {
	private int id; //Should this be a String?
	private String firstName;
	private String lastName;
	private String dateOfBirth;
	private String email;
	private int phoneNumber;
	private String password;
//	private ArrayList<Account> accounts;
	
	public Customer(int id, String firstName, String lastName, String dateOfBirth, String email, int phoneNumber, String password) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.dateOfBirth = dateOfBirth;
		this.email = email;
		this.phoneNumber = phoneNumber;
		this.password = password;
//		accounts = new ArrayList<>();
	}
	
//	public String accountsToString() {
//		String s = "";
//		for(Account a : accounts) {
//			s += a.toString();
//		}
//		return s;
//	}

	public Boolean validatePassword (String password) {
		return this.password.equals(password);
	}

//	public void addAccount(Account account) {
//		accounts.add(account);
//	}
}
