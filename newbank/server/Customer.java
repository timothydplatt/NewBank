package server;

public class Customer {
	public int id;
	public String firstName;
	public String lastName;
	public String dateOfBirth;
	public String email;
	public int phoneNumber;
	public String password;
	
	public Customer(int id, String firstName, String lastName, String dateOfBirth, String email, int phoneNumber, String password) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.dateOfBirth = dateOfBirth;
		this.email = email;
		this.phoneNumber = phoneNumber;
		this.password = password;
	}

	public Customer() {
		this.id = 0;
		this.firstName = "";
		this.lastName = "";
		this.dateOfBirth = "";
		this.email = "";
		this.phoneNumber = 0;
		this.password = "";
	}

}
