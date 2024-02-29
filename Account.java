import java.net.SocketOption;
import java.sql.SQLOutput;
import java.util.Scanner;

public class Account {

    // Attributes

    // name - name of the account holder
    String name;
    // lastName - last name of the account holder
    String lastName;
    // password - password for the account
    int pin;
    // last5Transactions - stores the last 5 transactions (positive for deposits and negative for withdrawals)
    double[] last5Transactions; // most recent first
    // balance - current balance of the account
    double balance;

    //Constructor:
    Account(String name, String lastName) {

        this.name = name;
        this.lastName = lastName;
        this.pin = 1;
        this.balance = 0.00;
        this.last5Transactions = new double[5];

    }

    // encryptPassword - encrypts a String
    static int encryptPassword(int pin) {
        int g = 5;
        int p = 10007;
        int publicKey = 1;

        while (pin > 0) {
            if (pin % 2 == 1) {publicKey = (publicKey * g);}
            g = g * g;
            pin = pin >> 1;
        }

        return publicKey % p;

    }

    // getPin - gets a valid pin from user
    public static int getPin() {
        Scanner input = new Scanner(System.in);

        int num = -1;
        String numStr;
        boolean invalid;

        while (num < 2 || num > 9999) {

            invalid = true;

            while (invalid) {

                System.out.print("Please input your pin: ");
                numStr = input.next();
                //System.out.print("\n");
                try {num = Integer.parseInt(numStr); invalid = false;}
                catch(Exception NotAnInt) {invalid = true; System.out.print("Invalid pin. ");}

            }

            if (num < 2 || num > 9999) {System.out.print("Invalid pin. ");}

        }

        return num;

    }

    // getNewPassword - gets an encrypted password and stores it
    void getNewPassword() {

        int pin1 = 1;
        int pin2 = 0;

        while (pin1 != pin2) {
            pin1 = Account.getPin();
            System.out.println("\nPlease confirm your pin...");
            pin2 = Account.getPin();
            if (pin1 != pin2) {System.out.println("They have to match!");}
        }

        pin = Account.encryptPassword(pin1);

    }

    // isOwner(userKey) - determines if userKey matches the stored encrypted key
    boolean isOwner(int userKey) {
        return Account.encryptPassword(userKey) == pin;
    }

    // void deposit(amount) - updates balance after the user deposits an amount
    void deposit(double amount) {
        balance  += amount;
        for(int i = 0; i < last5Transactions.length - 1; i++) {
            last5Transactions[i+1] = last5Transactions[i];
        }
        last5Transactions[0] = amount;
    }

    //void withdraw(amount) - updates balance after the user withdraws an amount
    void withdraw(double amount) {
        balance -= amount;
        for(int i = 0; i < last5Transactions.length - 1; i++) {
            last5Transactions[i+1] = last5Transactions[i];
        }
        last5Transactions[0] = -amount;
    }

    // String[] statsToStrings() - returns the summary of recent account activities as an array: min, max, average, currentBalance
    String[] statsToStrings() {
        double min = last5Transactions[0];
        double max = last5Transactions[0];
        double average = 0.0;
        String[] returnArray = new String[4];

        // min
        for(int i = 1; i < last5Transactions.length - 1; i++) {
            if(last5Transactions[i] < min) {min = last5Transactions[i];}
        }

        // max
        for(int i = 1; i < last5Transactions.length - 1; i++) {
            if(last5Transactions[i] > max) {max = last5Transactions[i];}
        }

        // average
        for(int i = 0; i < last5Transactions.length - 1; i++) {
            average += last5Transactions[i];
        }
        average /= last5Transactions.length;

        returnArray[0] = String.valueOf(min);
        returnArray[1] = String.valueOf(max);
        returnArray[2] = String.valueOf(average);
        returnArray[3] = String.valueOf(balance);

        return returnArray;

    }

    // void displayStats() - prints the account info
    // NEED TO CODE

}