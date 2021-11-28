import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Sample
{
    public static void main(String[] args)
    {
        Connection connection = null;
        try
        {
            // create a database connection
            connection = DriverManager.getConnection("jdbc:sqlite:newBank.db");
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);  // set timeout to 30 sec.

            statement.executeUpdate("drop table if exists customer");
            statement.executeUpdate("drop table if exists account");
            statement.executeUpdate("drop table if exists credentials");
            statement.executeUpdate("drop table if exists person");

            String customerCreateStatement = "CREATE TABLE customer " +
                    "(id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "firstName STRING, " +
                    "lastName STRING, " +
                    "dateOfBirth STRING, " +
                    "email STRING, " +
                    "phoneNumber INTEGER, " +
                    "password STRING)";
            statement.executeUpdate(customerCreateStatement);

            String accountCreateStatement = "CREATE TABLE account " +
                    "(accountNumber INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "customerID INTEGER, " +
                    "sortCode INTEGER, " +
                    "openingBalance INTEGER, " +
                    "currentBalance INTEGER, " +
                    "accountType STRING, " +
                    "openDate STRING," +
                    "FOREIGN KEY (customerID) REFERENCES customer(id))";
            statement.executeUpdate(accountCreateStatement);

            statement.executeUpdate("insert into customer (firstName, LastName, dateOfBirth, email, phoneNumber, password) VALUES ('Timothy', 'Platt', '01/01/1988', 'timothy@gmail.com', 07777777777, 'timothy123')");
            statement.executeUpdate("insert into customer (firstName, LastName, dateOfBirth, email, phoneNumber, password) VALUES ('Sarah', 'Smith', '01/01/1989', 'sarah@gmail.com', 07777777778, 'sarah123')");
            statement.executeUpdate("insert into customer (firstName, LastName, dateOfBirth, email, phoneNumber, password) VALUES ('William', 'Wilson', '01/01/1990', 'william@gmail.com', 07777777779, 'william123')");

            statement.executeUpdate("insert into account (customerID, sortCode, openingBalance, currentBalance, accountType, openDate) VALUES (1, 000001, 100, 100, 'Current', '01/01/2021')");
            statement.executeUpdate("insert into account (customerID, sortCode, openingBalance, currentBalance, accountType, openDate) VALUES (1, 000001, 0, 0, 'Savings', '01/01/2021')");
            statement.executeUpdate("insert into account (customerID, sortCode, openingBalance, currentBalance, accountType, openDate) VALUES (2, 000001, 100, 100, 'Current', '02/01/2021')");
            statement.executeUpdate("insert into account (customerID, sortCode, openingBalance, currentBalance, accountType, openDate) VALUES (2, 000001, 0, 0, 'Savings', '02/01/2021')");
            statement.executeUpdate("insert into account (customerID, sortCode, openingBalance, currentBalance, accountType, openDate) VALUES (3, 000001, 100, 100, 'Current', '03/01/2021')");
            statement.executeUpdate("insert into account (customerID, sortCode, openingBalance, currentBalance, accountType, openDate) VALUES (3, 000001, 0, 0, 'Savings', '03/01/2021')");


//            statement.executeUpdate("insert into account values(1, 1,000001,100,100,'Current','01/01/2021)");
            ResultSet customer = statement.executeQuery("select * from customer");
            while(customer.next())
            {
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

            ResultSet account = statement.executeQuery("select * from account");
            while(customer.next())
            {
                // read the result set
//                System.out.println("accountNumber = " + customer.getString("accountNumber"));
//                System.out.println("sortCode = " + customer.getInt("sortCode"));
//                System.out.println("balance = " + customer.getInt("currentBalance"));
//                System.out.println("customerId = " + customer.getInt("customerId"));
//                System.out.println("");
            }
        }
        catch(SQLException e)
        {
            // if the error message is "out of memory",
            // it probably means no database file is found
            System.err.println(e.getMessage());
        }
        finally
        {
            try
            {
                if(connection != null)
                    connection.close();
            }
            catch(SQLException e)
            {
                // connection close failed.
                System.err.println(e.getMessage());
            }
        }
    }
}
