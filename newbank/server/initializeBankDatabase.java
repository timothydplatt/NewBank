package server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class initializeBankDatabase {

    /**
     * initializeBankDatabase Class: Constitutes for initializing SQL Lite database tables account and customer, and add dummy data into respective tables.
     */

    public initializeBankDatabase() {
        Connection connection = null;
        try {
            // create a database connection
            connection = DriverManager.getConnection("jdbc:sqlite:newBank.db");
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);  // set timeout to 30 sec.

            //drop existing DB tables
            statement.executeUpdate("drop table if exists customer");
            statement.executeUpdate("drop table if exists account");

            System.out.println("Initializing Bank DataBase...");
            Thread.sleep(2000);
            String customerCreateStatement = "CREATE TABLE customer " +
                    "(id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "firstName STRING, " +
                    "lastName STRING, " +
                    "dateOfBirth STRING, " +
                    "email STRING, " +
                    "phoneNumber INTEGER, " +
                    "password STRING)";
            statement.executeUpdate(customerCreateStatement);

            /*Customer number sequence starts from 1001*/
            //String updateCustomerSeq = "UPDATE sqlite_sequence SET seq = 1000 WHERE NAME = 'customer'";
            String insertCustomerSeq = "INSERT INTO sqlite_sequence values('customer',1000)";
            statement.executeUpdate(insertCustomerSeq);

            String accountCreateStatement = "CREATE TABLE account " +
                    "(accountNumber INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "customerID INTEGER, " +
                    "balance DOUBLE, " +
                    "accountType STRING, " +
                    "openDate STRING," +
                    "FOREIGN KEY (customerID) REFERENCES customer(id))";
            statement.executeUpdate(accountCreateStatement);

            /*Account number sequence starts from 10001*/
            //String updateAccountSeq = "UPDATE sqlite_sequence SET seq = 10000 WHERE NAME = 'account'";
            String insertAccountSeq = "INSERT INTO sqlite_sequence values('account',10000)";
            statement.executeUpdate(insertAccountSeq);

            System.out.println("...Bank DataBase Initialized Successfully\n");
            Thread.sleep(2000);

            //Insert Sample Data
            statement.executeUpdate("insert into customer (firstName, LastName, dateOfBirth, email, phoneNumber, password) VALUES ('Johnny', 'Bravo', '01/01/1995', 'johnny@gmail.com', 07777777777, '1000:edc40a4e16834751005144df4706a2672cac5dc4b9a6c1e6:c69777db0f1681e09bfa2e7080b9294f1c8e4a9c83c53ca6')");
            /*user/pass johnny@gmail.com/johnny123*/
            statement.executeUpdate("insert into account (customerID, balance, accountType, openDate) VALUES (1001, 100, 'Current', '01/01/2021')");
            statement.executeUpdate("insert into account (customerID, balance, accountType, openDate) VALUES (1001, 299.99, 'Savings', '03/01/2021')");
            statement.executeUpdate("insert into customer (firstName, LastName, dateOfBirth, email, phoneNumber, password) VALUES ('Popeye', 'Sailor Man', '31/01/1929', 'popeye@gmail.com', 07926535905, '1000:9ddd9261f0ca44681cb599df379ed980e3a2965c95dcfb2f:f80cd4a8646acacbb29db2fee7a33029cac0068c83946f18')");
            /*user/pass popeye@gmail.com/popeye123*/
            statement.executeUpdate("insert into account (customerID, balance, accountType, openDate) VALUES (1002, 599.98, 'Loan', '01/01/2021')");
            statement.executeUpdate("insert into account (customerID, balance, accountType, openDate) VALUES (1002, 1000, 'Savings', '05/12/2021')");

            //Print Data in Customer Table
            ResultSet customer = statement.executeQuery("select * from customer");
            while (customer.next()) {
                // read the result set
                System.out.println("id = " + customer.getInt("id"));
                System.out.println("firstName = " + customer.getString("firstName"));
                System.out.println("lastName = " + customer.getString("lastName"));
                System.out.println("dateOfBirth = " + customer.getString("dateOfBirth"));
                System.out.println("email = " + customer.getString("email"));
                System.out.println("phoneNumber = " + customer.getString("phoneNumber"));
                System.out.println("password = " + customer.getString("password"));
                System.out.println("");
            }

            //Print Data in Account Table
            ResultSet account = statement.executeQuery("select * from account");
            while (account.next()) {
                // read the result set
                System.out.println("accountNumber = " + customer.getString("accountNumber"));
                System.out.println("customerID = " + customer.getInt("customerID"));
                System.out.println("balance = " + customer.getFloat("balance"));
                System.out.println("openDate = " + customer.getString("openDate"));
                System.out.println("");
            }
        } catch (SQLException e) {
            // if the error message is "out of memory",
            // it probably means no database file is found
            System.err.println(e.getMessage());
        } catch (InterruptedException ex) {
            //Java Pause
            Thread.currentThread().interrupt();
        } finally {
            try {
                if (connection != null)
                    connection.close();
            } catch (SQLException e) {
                // connection close failed.
                System.err.println(e.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        new initializeBankDatabase();
    }
}

