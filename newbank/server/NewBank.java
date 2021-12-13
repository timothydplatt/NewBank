package server;

import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class NewBank {

    /**
     *NewBank Class: Constitutes of various Banking transaction specific functions like creating user account, bank account, show balance, transactions.
     * 	Function createUserAccount() is used of creating new user banking account.
     * Function createBankAccount() helps user create either Current or Saving account.
     * Function showAccountBalance(), show all user’s account and available respective balances
     * Function cashTransaction() is used for adding or withdrawing balance from a user’s account
     * Function accountTransaction() does the banking transaction like transfer of amount between accounts inter or intra.
     */

    public String listOptions() {
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
                .concat("Option 6 : Create Bank Account")
                .concat(newLine)
                .concat("Option 7 : Apply for loan")
                .concat(newLine)
                .concat("Option 8 : Logout");
    }

    private Connection connect() {
        // SQLite connection string
        String url = "jdbc:sqlite:newBank.db";
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(url);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return connection;
    }

    public Boolean verifyUser(String email) {
        String sql = "SELECT * FROM customer WHERE email = ? ";
        System.out.println("User Verification request for email: " + email);

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // set the value
            pstmt.setString(1, email);
            //
            ResultSet rs = pstmt.executeQuery();

            if (!rs.isBeforeFirst())
                return false;
            else
                return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public String getHashedPassword(String email) {
        String sql = "SELECT password FROM customer WHERE email = ?";
        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // set the value
            pstmt.setString(1, email);
            //Execute query
            ResultSet rs = pstmt.executeQuery();

            if (!rs.isBeforeFirst()) {
                System.out.println("User not found");
                return null;
            } else {
                return rs.getString("password");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Customer getCustomer(String email) {

        String sql = "SELECT * FROM customer WHERE email = ?";
        System.out.println("Login Request for email: " + email);

        try (Connection conn = this.connect();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // set the value
            pstmt.setString(1, email);
            //pstmt.setString(2, password);

            //Execute query
            ResultSet rs = pstmt.executeQuery();

            if (!rs.isBeforeFirst()) {
                System.out.println("Email not found");
                return null;
            } else {
                Customer customer = new Customer();
                while (rs.next()) {
                    customer.id = rs.getInt("id");
                    customer.firstName = rs.getString("firstName");
                    customer.lastName = rs.getString("lastName");
                    customer.dateOfBirth = rs.getString("dateOfBirth");
                    customer.email = rs.getString("email");
                    customer.phoneNumber = rs.getInt("phoneNumber");
                    customer.password = rs.getString("password");
                }
                System.out.println("Email/Password combination found");
                return customer;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Account getAccount(int customerID, String accountType) {

        String sql = "SELECT * FROM account WHERE customerID = ? AND accountType = ? ";
        System.out.println("Get Account Request for customerID: " + customerID + ", accountType: " + accountType);

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // set the value
            pstmt.setInt(1, customerID);
            pstmt.setString(2, accountType);
            //Execute Query
            ResultSet rs = pstmt.executeQuery();

            if (!rs.isBeforeFirst()) {
                System.out.println("customerID/accountType combination not found");
                return null;
            } else {
                Account account = new Account();
                while (rs.next()) {
                    account.accountNumber = rs.getInt("accountNumber");
                    account.customerID = rs.getInt("customerID");
                    account.balance = rs.getDouble("balance");
                    account.accountType = rs.getString("accountType");
                    account.openDate = rs.getString("openDate");
                }
                System.out.println("customerID/accountType combination found");
                return account;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String createUserAccount(String firstName, String lastName, String dateOfBirth, String userName, String phoneNumber, String password) {
        String sql = "insert into customer (firstName, lastName, dateOfBirth, email, phoneNumber, password) VALUES (?, ?, ?, ?, ?, ?)";
        System.out.println("Create User account request for userName: " + userName);

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            // set the value
            pstmt.setString(1, firstName);
            pstmt.setString(2, lastName);
            if(dateOfBirth.contains("/"))
                pstmt.setString(3, dateOfBirth);
            else
                throw new Exception("Invalid Date of Birth, please enter a valid DOB e.g. - '01/01/1990'");
            if(userName.contains("@") && userName.contains("."))
                pstmt.setString(4, userName);
            else
                throw new Exception("Invalid User Name, please enter a valid user e.g. - 'abc@test.com'");
            try {
                pstmt.setString(5, String.valueOf(Integer.parseInt(phoneNumber)));
            }catch(Exception exp) {
                throw new Exception("Invalid Phone Number, please enter a valid phone number e.g. - '123456789'");
            }
            pstmt.setString(6, password);

            //Execute Statement and return
            return "Welcome User : '" + userName + "', Your User creation request Status (0: Failure, 1: Success): " + pstmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
            return "User : '" + userName + "', creation request failed. Reason: " + e.getMessage();
        }
    }

    public String createBankAccount(Customer customer, String accountType) {
        String sql = "insert into account (customerID, balance, accountType, openDate) VALUES (?, 00.00, ?, ?)";

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate localDate = LocalDate.now();
        System.out.println("Bank Account Creation request for Customer ID: " + customer.id + " for Account Type: " + accountType + " on Date time: " + dtf.format(localDate));

        //Verify user account creation request
        Account acc = getAccount(customer.id, accountType);
        if (acc != null) {
            System.out.println("Account ID: " + acc.accountNumber + " of accountType: " + acc.accountType + " for Customer ID: " + acc.customerID + ", already exists");
            return "Account Creation request for CustomerID: " + customer.id + ", failed; as accountType: " + accountType + " with accountNumber: " + acc.accountNumber + " already exists for Customer";
        } else {
            try (Connection conn = this.connect();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {
                // set the value
                pstmt.setString(1, String.valueOf(customer.id));
                pstmt.setString(2, accountType);
                pstmt.setString(3, dtf.format(localDate));
                System.out.println("Account creation request for accountType: " + accountType + " for Customer ID:" + customer.id + ", created successfully");

                //Execute Statement and return
                return "Account Creation request for CustomerID :" + customer.id + ", successful. Status (0: Failure, 1: Success): " + pstmt.executeUpdate();

            } catch (Exception e) {
                e.printStackTrace();
                return "Account Creation request for CustomerID :" + customer.id + ", failed. Reason: " + e.getMessage();
            }
        }
    }

    public String showAccountBalance(int customerID) {
        String sql = "SELECT * FROM account WHERE customerID = ?";
        System.out.println("Show account(s) request for customerID: " + customerID);

        String statement = "";
        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // set the value
            pstmt.setInt(1, customerID);
            //Execute Query
            ResultSet rs = pstmt.executeQuery();

            if (!rs.isBeforeFirst()) {
                statement = "No Bank account(s) found for customerID: " + customerID;
            } else {
                statement = "Account(s) details for customerID '" + customerID + "': \n\n";
                while (rs.next()) {
                    statement = statement + "accountNumber: " + rs.getInt("accountNumber") + ","
                            + " accountType: " + rs.getString("accountType") + "\n"
                            + "accountOpeningDate: " + rs.getString("openDate") + ","
                            + " availableBalance: " + rs.getDouble("balance") + "\n\n";
                }
                return statement;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Show account(s) balance request for CustomerID :" + customerID + ", failed. Reason: " + e.getMessage();
        }
        return statement;
    }

    public String cashTransaction(int customerID, int accountNumber, double amount) {
        String sql = "SELECT * FROM account WHERE customerID = ? and accountNumber = ?";
        System.out.println("cashTransaction request for customerID: " + customerID+" and accountNumber: "+accountNumber);

        String statement = null;
        try (Connection conn = this.connect()) {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            // set the value
            pstmt.setInt(1, customerID);
            pstmt.setInt(2, accountNumber);
            //Execute Query
            ResultSet rs = pstmt.executeQuery();

            if (!rs.isBeforeFirst()) {
                statement = "Bank accountNumber/customerID combination invalid: "+accountNumber+"/"+customerID+"\n";
            } else {
                double accountBalance = rs.getDouble("balance");
                //Verify account balance for transaction
                if(amount<0 && accountBalance<(amount*-1))
                    throw new Exception("Insufficient Balance.");
                //Account Update Statement
                String sqlUp = "UPDATE account SET balance = ? WHERE customerID = ? and accountNumber = ?";
                PreparedStatement pstmtUp = conn.prepareStatement(sqlUp);
                double newAccountBalance = Math.round((accountBalance+amount) * 100.0) / 100.0;
                // set the value
                pstmtUp.setDouble(1, newAccountBalance);
                pstmtUp.setInt(2, customerID);
                pstmtUp.setInt(3, accountNumber);
                //Execute Update account Query
                pstmtUp.executeUpdate();

                if(amount>0)
                    statement = "\nFor accountNumber: " + rs.getInt("accountNumber") + "\n"
                            + "oldAccountBalance: " + accountBalance + "\n"
                            + "amountAdded: " + amount + "\n"
                            + "newAccountBalance: " + newAccountBalance + "\n";
                else
                    statement = "\nFor accountNumber: " + rs.getInt("accountNumber") + "\n"
                            + "oldAccountBalance: " + accountBalance + "\n"
                            + "amountWithdrawn: " + amount + "\n"
                            + "newAccountBalance: " + newAccountBalance + "\n";

                return statement;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "cashTransaction request for CustomerID :" + customerID + ", failed. Reason: " + e.getMessage();
        }
        return statement;
    }

    public String accountTransaction(int fromCustomerID, int fromAccountNumber, double amount, int toCustomerID, int toAccountNumber) {
        String sqlFrom = "SELECT * FROM account WHERE customerID = ? and accountNumber = ?";
        String sqlTo = "SELECT * FROM account WHERE customerID = ? and accountNumber = ?";

        System.out.println("accountTransaction request for fromCustomerID: " + fromCustomerID+" - fromAccountNumber:"+fromAccountNumber
                +" to toCustomerID: "+toCustomerID+" - toAccountNumber:"+toAccountNumber);

        String statement = null;
        try (Connection conn = this.connect()) {
            PreparedStatement pstmtFrom = conn.prepareStatement(sqlFrom);
            PreparedStatement pstmtTo = conn.prepareStatement(sqlTo);

            // set the value
            pstmtFrom.setInt(1, fromCustomerID);
            pstmtFrom.setInt(2, fromAccountNumber);
            pstmtTo.setInt(1, toCustomerID);
            pstmtTo.setInt(2, toAccountNumber);

            //Execute Query
            ResultSet rsFrom = pstmtFrom.executeQuery();
            ResultSet rsTo = pstmtTo.executeQuery();

            if (!rsFrom.isBeforeFirst()) {
                statement = "Bank fromAccountNumber/fromCustomerID combination invalid: "+fromAccountNumber+"/"+fromCustomerID+"\n";
            } else if (!rsTo.isBeforeFirst()) {
                statement = "Bank toAccountNumber/toCustomerID combination invalid: "+toAccountNumber+"/"+toCustomerID+"\n";
            } else {
                double fromAccountBalance = rsFrom.getDouble("balance");
                double toAccountBalance = rsTo.getDouble("balance");
                double newFromAccountBalance = Math.round((fromAccountBalance-amount) * 100.0) / 100.0;
                double newToAccountBalance = Math.round((toAccountBalance+amount) * 100.0) / 100.0;

                //Verify account balance for transaction
                if(fromAccountBalance<amount)
                    throw new Exception("Insufficient Balance.");

                //Account Update Statement
                String sqlUpFrom = "UPDATE account SET balance = ? WHERE customerID = ? and accountNumber = ?";
                String sqlUpTo = "UPDATE account SET balance = ? WHERE customerID = ? and accountNumber = ?";
                PreparedStatement pstmtUpFrom = conn.prepareStatement(sqlUpFrom);
                PreparedStatement pstmtUpTo = conn.prepareStatement(sqlUpTo);

                // set the value
                pstmtUpFrom.setDouble(1, (newFromAccountBalance));
                pstmtUpFrom.setInt(2, fromCustomerID);
                pstmtUpFrom.setInt(3, fromAccountNumber);
                pstmtUpTo.setDouble(1, (newToAccountBalance));
                pstmtUpTo.setInt(2, toCustomerID);
                pstmtUpTo.setInt(3, toAccountNumber);
                //Execute Update account Query
                pstmtUpFrom.executeUpdate();
                pstmtUpTo.executeUpdate();

                if(fromCustomerID==toCustomerID){
                    statement = "\nIntra-Bank Transaction for customerID: "+fromCustomerID+"\n"
                            +"TransferAmount: " +amount+"\n"
                            +"Transferred fromAccountNumber: " + rsFrom.getInt("accountNumber") + "\n"
                            +"Old accountBalance: " + (fromAccountBalance) +", New accountBalance: " + (newFromAccountBalance) + "\n"
                            +"Transferred toAccountNumber: " + rsTo.getInt("accountNumber") + "\n"
                            +"Old accountBalance: " + (toAccountBalance) +", New accountBalance: " + (newToAccountBalance) + "\n";
                }else{
                    statement = "\nInter-Bank Transaction: \n"
                            +"TransferAmount: " +amount+"\n"
                            +"Transferred fromCustomerID/fromAccountNumber: " + rsFrom.getInt("customerID")+"/"+rsFrom.getInt("accountNumber") + "\n"
                            +"Old accountBalance: " + (fromAccountBalance) +", New accountBalance: " + (newFromAccountBalance) + "\n"
                            +"Transferred toCustomerID/toAccountNumber: " + rsTo.getInt("customerID")+"/"+rsTo.getInt("accountNumber") + "\n"
                            +"Old accountBalance: " + (toAccountBalance) +", New accountBalance: " + (newToAccountBalance) + "\n";
                }


                return statement;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "accountTransaction request for CustomerID: " + fromCustomerID + ", failed. Reason: " + e.getMessage();
        }
        return statement;
    }

    /*public static void main(String[] args) {
        NewBank bank = new NewBank();
        //System.out.println("***"+bank.createAccount("Tom", "Jerry", "21/01/1992", "tom@gmail.com", 87585858, "tom123"));
        Customer cust = bank.getCustomer("johnny@gmail.com", "johnny123");
        System.out.println("***" + cust.firstName);
    }*/
}
