/*
 Wesley Hadnot
 2/15/24
 Professor Raza 
 COSC 2436
 */
import java.awt.Desktop;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import javax.swing.JOptionPane;

public class Amortization {
    private double loanAmount, intRate, loanBalance, term, payment;
    private int loanYears;

// Constructor to initialize loan amount, interest rate, and loan years.
    public Amortization(double loan, double rate, int years) {
        loanAmount = loan;
        loanBalance = loan;
        intRate = rate / 100.0; // Convert rate to decimal.
        loanYears = years;
        calcPayment();
    }

// Calculate payment using the banks algorithims.
    private void calcPayment() {
        if (intRate == 0) {
            payment = loanAmount / (12.0 * loanYears);
        } else {
            term = Math.pow((1 + (intRate / 12.0)), 12.0 * loanYears);
            payment = (loanAmount * (intRate / 12.0) * term) / (term - 1);
        }
    }

// Get the total number of payments over the loan term.
    public int getNumberOfPayments() {
        return 12 * loanYears;
    }

// Save the amortization report to a file and open it with the default associated application.
    public void saveReport(String filename) throws IOException {
        double monthlyInterest; double principal;
        FileWriter fwriter = new FileWriter(filename);
        PrintWriter outputFile = new PrintWriter(fwriter);

// Create the rows and columns for the text file.
        outputFile.println(String.format("Monthly Payment: $%.2f", payment));
        outputFile.println("Month   Interest   Principal   Balance");
        outputFile.println("-----------------------------------------------");

// The text file should create an iteration for each month of the loan years.
        for (int month = 1; month <= getNumberOfPayments(); month++) {
           
// Calculate monthly interest based on the annual interest rate and remaining loan balance.
            monthlyInterest = intRate / 12.0 * loanBalance;

// The number of payments should be equal to the number of months on the loan.
            if (month != getNumberOfPayments()) {
                principal = payment - monthlyInterest;
            } else {
                principal = loanBalance;
                payment = loanBalance + monthlyInterest;
            }
            loanBalance -= principal;
            outputFile.println(String.format("%-7d %-10.2f %-11.2f %-11.2f", month, monthlyInterest, principal, loanBalance));
        }
         outputFile.close();

// Open the saved report file with the default associated application.
        Desktop desktop = Desktop.getDesktop();
        File reportFile = new File(filename);

        if (Desktop.isDesktopSupported() && desktop.isSupported(Desktop.Action.OPEN)) {
            desktop.open(reportFile);
        } else {
            System.out.println("Opening the file is not supported on this system.");
        }
    }

// Get loan amount, years and interestRate.
    public double getLoanAmount() { return loanAmount; }
    public double getIntRate() { return intRate; }
    public int getLoanYears() { return loanYears; }

// Main method to get user input and generate loan amortization reports.
public static void main(String[] args) throws IOException {
        String input;
        double loan, intRate;
        int years;
        char again;

// Prompt the user to enter loan amount.
        do {
            input = JOptionPane.showInputDialog(null, "Enter the loan amount");
            loan = Double.parseDouble(input);

// Negative input values are not accepted. prompt the user with an error message.
            while (loan < 0) {
                input = JOptionPane.showInputDialog(null, "Invalid amount. Enter the loan amount.");
                loan = Double.parseDouble(input);
            }

// Prompt the user to enter the annual interest rate.
            input = JOptionPane.showInputDialog("Enter the annual interest rate.");
            intRate = Double.parseDouble(input);

// Negative input values are not accepted. prompt the user with an error message.
            while (intRate < 0) {
                input = JOptionPane.showInputDialog("Invalid amount. Enter the annual interest rate.");
                intRate = Double.parseDouble(input);
            }

// Prompt the user to enter  the years of the loan.
            input = JOptionPane.showInputDialog("Enter the years of the loan.");
            years = Integer.parseInt(input);

// Negative input values are not accepted. prompt the user with an error message.
            while (years < 0) {
                input = JOptionPane.showInputDialog("Invalid amount. Enter the years of the loan.");
                years = Integer.parseInt(input);
            }

// Save the report to a text file.
            Amortization am = new Amortization(loan, intRate, years);
            am.saveReport("Amortization.txt");
            JOptionPane.showMessageDialog(null, "Report saved to the file Amortization.txt.");

// Ask the user if they want to run the program again, if not terminate the program.
            input = JOptionPane.showInputDialog(null, "Would you like to run another report? Enter Y for yes or N for no: ");
            again = input.charAt(0);
        } while (again == 'Y' || again == 'y');

        System.exit(0);
    }
}
