package server;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Loan {
    public int loanAccountNumber;
    public int id;
    public double loanAmount;
    private double interestRate = 3;

    public Loan(int loanAccountNumber, double loanAmount, double interestRate) {
        this.loanAccountNumber = loanAccountNumber;
        this.id = id;
        this.loanAmount = loanAmount;
        this.interestRate = interestRate;
    }

    public Loan() {
        this.loanAccountNumber = 0;
        this.loanAmount = 0.0;
        this.interestRate = 0.0;
        this.id = 0;
    }

    public String loanProcessing(int id, double loanAmount) {
       //to be filled;
        return null;
    }

}
