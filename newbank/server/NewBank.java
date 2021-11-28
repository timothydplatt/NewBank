package newbank.server;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
//import java.sql.SQLException;
import java.sql.*;
import java.util.HashMap;
import java.sql.SQLException;

public class NewBank {

	private static final NewBank bank = new NewBank();
	private HashMap<String,Customer> customers;
	
	private NewBank() {
		customers = new HashMap<>();
		addTestData();
	}

	// Creating an object of Gson class
	Gson gson = new Gson();

	private Connection connect() {
		// SQLite connection string
		String url = "jdbc:sqlite:newBank.db";
		Connection connection = null;
		try {
			connection = DriverManager.getConnection(url);
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return connection;
	}

	public void selectAll(){
		String sql = "SELECT * FROM customer";

		try (Connection conn = this.connect();
			 Statement stmt  = conn.createStatement();
			 ResultSet rs    = stmt.executeQuery(sql)){

			// loop through the result set
			while (rs.next()) {
				System.out.println(rs.getInt("id") +  "\t" +
						rs.getString("firstName") + "\t" +
						rs.getString("lastName"));
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	private void addTestData() {

//		Customer bhagy = new Customer("Bhagy", "Bhagy123");
//		bhagy.addAccount(new Account("Main", 1000.0));
//		customers.put("Bhagy", bhagy);
		// Generating json from emp object

//		String custJson = gson.toJson(bhagy);
//		System.out.println("Emp json is " + custJson);
//
//		Customer christina = new Customer("Christina", "Christina123");
//		christina.addAccount(new Account("Savings", 1500.0));
//		customers.put("Christina", christina);
//
//		Customer john = new Customer("John", "John123");
//		john.addAccount(new Account("Checking", 250.0));
//		customers.put("John", john);

		Customer tim = new Customer(1, "Tim", "Platt", "01/01/1988","timothy@gmail.com", 07777777777,"timothy123");
		customers.put("Tim", tim);

//		selectAll();
//		getUserByEmail("timothy@gmail.com");
//		getUserByEmail("william@gmail.com");

//		System.out.println(userWithEmailExists("timothy@gmail.com"));
//		System.out.println(userWithEmailExists("timothy@gmai.com"));
//		System.out.println(userWithEmailExists("sarah@gmail.com"));
//		System.out.println(userWithEmailExists("sarha@gmail.com"));
//		System.out.println(userWithEmailExists("william@gmail.com"));
//		System.out.println(userWithEmailExists(""));
//
//		System.out.println(passwordMatchesForEmail("timothy@gmail.com", "timothy123"));
//		System.out.println(passwordMatchesForEmail("timothy@gmai.com", "timothy123"));
//		System.out.println(passwordMatchesForEmail("timothy@gmail.com", "timothy12"));
	}
	
	public static NewBank getBank() {
		return bank;
	}

	public synchronized Customer checkLogInDetails(String userName, String password) {
//		if(userNameExists(userName) && customersPasswordIsCorrect(userName,password)) {
//			return new CustomerID(userName);
//		}
//		return null;

		if(userWithEmailExists(userName) && passwordMatchesForEmail(userName,password)) {
			Customer customer = getCustomer(userName,password);
			return customer;
//			return new CustomerID(userName);
		}
		return null;
	}

	private boolean userNameExists(String userName) {
		return customers.containsKey(userName);
	}

	private boolean customersPasswordIsCorrect(String userName, String password) {
		return customers.get(userName).validatePassword(password);
	}

	// commands from the NewBank customer are processed in this method
	public synchronized String processRequest(Customer customer, String request) {
//		if(customers.containsKey(customer.id)) {
			switch(request) {
			case "HELP" : return listOptions();
//			case "SHOWMYACCOUNTS" : return showMyAccounts(customer);
			default : return "FAIL";
			}
//		}
//		return "FAIL";
	}

//	private String showMyAccounts(CustomerID customer) {
//		return (customers.get(customer.getKey())).accountsToString();
//	}

//	public String createNewCustomer(String username, String password) {
//		Customer newCustomer = new Customer(username,password);
//		newCustomer.addAccount(new Account("Checking", 0.0));
//		customers.put(username, newCustomer);
//		return("Account created" + username);
//	}

	private String listOptions() {
		String newLine = System.getProperty("line.separator");

		return "Option 1 : Show balance"
				.concat(newLine)
				.concat("Option 2 : Add balance")
				.concat(newLine)
				.concat("Option 3 : Withdraw balance")
				.concat(newLine)
				.concat("Option 4 : Transfer balance")
				.concat(newLine)
				.concat("Option 5 : Send money/pay bill")
				.concat(newLine)
				.concat("Option 6 : Apply for loan");
	}

//	public void getUserByEmail(String email) {
//		String sql = "SELECT * FROM customer WHERE email = ? ";
//
//		try (Connection conn = this.connect();
//			 PreparedStatement pstmt  = conn.prepareStatement(sql)){
//
//			// set the value
//			pstmt.setString(1, email);
//			//
//			ResultSet rs  = pstmt.executeQuery();
//
//			// loop through the result set
//			while (rs.next()) {
//				System.out.println(rs.getInt("id") +  "\t" +
//						rs.getString("firstName") + "\t" +
//						rs.getString("lastName"));
//			}
//		} catch (SQLException e) {
//			System.out.println(e.getMessage());
//		}
//	}

	public Boolean userWithEmailExists(String email) {
		String sql = "SELECT * FROM customer WHERE email = ? ";

		try (Connection conn = this.connect();
			 PreparedStatement pstmt  = conn.prepareStatement(sql)){

			// set the value
			pstmt.setString(1, email);
			//
			ResultSet rs  = pstmt.executeQuery();

			if (!rs.isBeforeFirst() ) {
				return false;
			} else {
				return true;
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			return false;
		}
	}

	public Boolean passwordMatchesForEmail(String email, String password) {
		String sql = "SELECT * FROM customer WHERE email = ? AND password = ?";

		try (Connection conn = this.connect();
			 PreparedStatement pstmt  = conn.prepareStatement(sql)){

			// set the value
			pstmt.setString(1, email);
			pstmt.setString(2, password);
			//
			ResultSet rs  = pstmt.executeQuery();

			if (!rs.isBeforeFirst() ) {
				return false;
			} else {
				return true;
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			return false;
		}
	}

	public Customer getCustomer(String email, String password) {

		String sql = "SELECT * FROM customer WHERE email = ? AND password = ? ";
//		Customer customer("");
		try (Connection conn = this.connect();
			 PreparedStatement pstmt  = conn.prepareStatement(sql)){

			// set the value
			pstmt.setString(1, email);
			pstmt.setString(2, password);
			//
			ResultSet rs  = pstmt.executeQuery();

			if (!rs.isBeforeFirst() ) {
				return null;
			} else {
				Customer customer = new Customer();
				while (rs.next()) {
					customer.id = rs.getInt("id");
					customer.firstName = rs.getString("firstName");
					customer.lastName = rs.getString("lastName");
					customer.dateOfBirth = rs.getString("lastName");
					customer.email = rs.getString("lastName");
					customer.phoneNumber = rs.getInt("lastName");
					customer.password = rs.getString("password");
				}
				return customer;
			}

			// loop through the result set
//			while (rs.next()) {
//				System.out.println(rs.getInt("id") +  "\t" +
//						rs.getString("firstName") + "\t" +
//						rs.getString("lastName"));
//			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			return null;
		}

//		Customer newCustomer = new Customer(username,password);
//		newCustomer.addAccount(new Account("Checking", 0.0));
//		customers.put(username, newCustomer);
//		return("Account created" + username);
	}

}
