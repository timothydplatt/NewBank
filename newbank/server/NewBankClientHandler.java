package newbank.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class NewBankClientHandler extends Thread{
	
	private NewBank bank;
	private BufferedReader in;
	private PrintWriter out;
	
	
	public NewBankClientHandler(Socket s) throws IOException {
		bank = NewBank.getBank();
		in = new BufferedReader(new InputStreamReader(s.getInputStream()));
		out = new PrintWriter(s.getOutputStream(), true);
	}

	Boolean customerTypeSelected = false;
	String customerType = "";
	
	public void run() {
		// Ask user if they're an existing customer or a new customer.
		while(!customerTypeSelected) {
			out.println("If you're an existing customer press 1 to login.");
			out.println("If you're a new customer press 2 to sign-up.");

			try {
				customerType = in.readLine();
				if (customerType.equals("1") || customerType.equals("2")) {
					customerTypeSelected = true;
				} else {
					out.println("Invalid input, please enter 1 or 2");
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		if (customerType.equals("2")) {
			try {
				String newCustomerUserName;
				String newCustomerPassword;

				out.println("Enter a username:");
				newCustomerUserName = in.readLine();
				out.println("Enter a password:");
				newCustomerPassword = in.readLine();
				String result = bank.createNewCustomer(newCustomerUserName, newCustomerPassword);
				out.println(result);
				customerType = "1";
				run();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}


		// keep getting requests from the client and processing them
		try {
			// ask for username
			out.println("Enter username");
			String userName = in.readLine();
			// ask for password
			out.println("Enter password");
			String password = in.readLine();
			out.println("Checking Details...");
			// authenticate user and get customer ID token from bank for use in subsequent requests
			CustomerID customer = bank.checkLogInDetails(userName, password);
			// if the user is authenticated then get requests from the user and process them 
			if(customer != null) {
				out.println("Log In Successful. What do you want to do?");
				while(true) {
					String request = in.readLine();
					System.out.println("Request from " + customer.getKey());
					String response = bank.processRequest(customer, request);
					out.println(response);
				}
			}
			else {
				out.println("Log In Failed");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		finally {
			try {
				in.close();
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
				Thread.currentThread().interrupt();
			}
		}
	}

}
