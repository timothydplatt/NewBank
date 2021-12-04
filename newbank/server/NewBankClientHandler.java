package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class NewBankClientHandler extends Thread {

    private NewBank bank = new NewBank();
    private Customer customer = new Customer();
    private BufferedReader in;
    private PrintWriter out;


    public NewBankClientHandler(Socket s) throws IOException {
        in = new BufferedReader(new InputStreamReader(s.getInputStream()));
        out = new PrintWriter(s.getOutputStream(), true);
    }

    Boolean customerTypeSelected = false;
    String customerType = "";

    public void run() {
        // Ask user if they're an existing customer or a new customer.
        while (!customerTypeSelected) {
            out.println("If you're an existing customer press 1 to login.");
            out.println("If you're a new customer press 2 to sign-up.");

            try {
                customerType = in.readLine();
                if (customerType.equals("1") || customerType.equals("2")) {
                    customerTypeSelected = true;
                } else {
                    out.println("Invalid input, please enter 1 or 2\n");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (customerType.equals("2")) {
            try {
                out.println("\nEnter a username(email):");
                String userName = in.readLine();
                out.println("Enter a desired password:");
                String password = in.readLine();
                out.println("Enter your First Name:");
                String firstName = in.readLine();
                out.println("Enter your Last Name:");
                String lastName = in.readLine();
                out.println("Enter your Date of Birth (in dd/mm/yyy format):");
                String dateOfBirth = in.readLine();
                out.println("Enter your Phone Number:");
                String phoneNumber = in.readLine();

                // verify the user if already exists
                if (bank.verifyUser(userName))
                    out.println("User : " + userName + " already exists, please try to login\n\n");
                else
                    // Create account for the user
                    out.println(bank.createUserAccount(firstName, lastName, dateOfBirth, userName, Integer.valueOf(phoneNumber), password) + "\n\n");

                //Relogin Option
                customerTypeSelected = false;
                run();

            } catch (Exception e) {
                System.err.println(e.getMessage());
                out.println("Invalid User Input, please try again.\n");
                //Relogin Option
                customerTypeSelected = false;
                run();
            }
        }


        // keep getting requests from the client and processing them
        try {
            // ask for username
            out.println("\nEnter username(email)");
            String userName = in.readLine();
            // ask for password
            out.println("Enter password");
            String password = in.readLine();
            out.println("\nVerifying Details...\n");
            Thread.sleep(2000);

            // authenticate user and get customer ID token from bank for use in subsequent requests
            customer = bank.getCustomer(userName, password);
            //System.out.println("***"+customer.firstName);

            // if the user is authenticated then get requests from the user and process them
            if (customer != null) {
                out.println("Log-In Successful.");
                out.println("Welcome " + customer.firstName + " " + customer.lastName + "\n");
                Thread.sleep(2000);
                out.println("What do you want to do?\n");
                while (true) {
                    Thread.sleep(2000);
                    out.println("You have the following options:");
                    String options = bank.listOptions();
                    out.println(options);
                    String request = in.readLine();
                    System.out.println(request + " - request.num from customer.id " + customer.id);
                    //String response = bank.processRequest(customer, Integer.valueOf(request));
                    switch (request) {
                        case "1":
                            out.println(bank.showAccountBalance(customer.id));
                            continue;
                        case "2":
                            try {
                                out.println("Enter AccountNumber to which you want AddBalance:");
                                int accountNumber_a = Integer.valueOf(in.readLine());
                                out.println("Enter Balance to Add:");
                                float amount_a = Float.valueOf(in.readLine());
                                out.println(bank.cashTransaction(customer.id, accountNumber_a, amount_a) + "\n\n");
                                continue;
                            }catch(Exception e){
                                e.printStackTrace();
                                continue;
                            }
                        case "3":
                            try {
                                out.println("Enter AccountNumber from which you want to WithdrawBalance:");
                                int accountNumber_w = Integer.valueOf(in.readLine());
                                out.println("Enter Balance to Withdraw:");
                                float amount_w = Float.valueOf(in.readLine());
                                out.println(bank.cashTransaction(customer.id, accountNumber_w, (-amount_w)) + "\n\n");
                                continue;
                            } catch(Exception e){
                                e.printStackTrace();
                                continue;
                            }
                        case "4":
                            try {
                                out.println("Enter AccountNumber from which you want to TransferBalance:");
                                int fromAccountNumber = Integer.valueOf(in.readLine());
                                out.println("Enter AccountNumber to which you want to TransferBalance:");
                                int toAccountNumber = Integer.valueOf(in.readLine());
                                out.println("Enter Balance to Transfer:");
                                float amount = Float.valueOf(in.readLine());
                                out.println(bank.accountTransaction(customer.id, fromAccountNumber, amount, customer.id, toAccountNumber) + "\n\n");
                                continue;
                            } catch(Exception e){
                                e.printStackTrace();
                                continue;
                            }
                        case "5":
                            try {
                                out.println("Enter AccountNumber from which you want to TransferBalance:");
                                int fromAccountNumber = Integer.valueOf(in.readLine());
                                out.println("Enter CustomerID to which you want to TransferBalance:");
                                int toCustomerID = Integer.valueOf(in.readLine());
                                out.println("Enter AccountNumber to which you want to TransferBalance:");
                                int toAccountNumber = Integer.valueOf(in.readLine());
                                out.println("Enter Balance to Transfer:");
                                float amount = Float.valueOf(in.readLine());
                                out.println(bank.accountTransaction(customer.id, fromAccountNumber, amount, toCustomerID, toAccountNumber) + "\n\n");
                                continue;
                            } catch(Exception e){
                                e.printStackTrace();
                                continue;
                            }
                        case "6":
                            String accountType = "default";
                            out.println("Select Account Type:");
                            out.println("Option 1 : Current".concat("\n").concat("Option 2 : Savings"));
                            String choice = in.readLine();
                            if (choice.equals("1")) accountType = "Current";
                            else if (choice.equals("2")) accountType = "Savings";
                            out.println(bank.createBankAccount(customer, accountType)+"\n\n");
                            continue;
                        case "7":
                            out.println("To be filled\n");
                            continue;
                        case "8":
                            out.println("Thank you. Bye.\n");
                            break;
                        default:
                            out.println("Invalid Choice, please try again.\n");
                            continue;
                    }
                    //Relogin Option
                    customerTypeSelected = false;
                    run();
                }

            } else {
                out.println("Log-In Failed.\n");
                //Relogin Option
                customerTypeSelected = false;
                run();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
