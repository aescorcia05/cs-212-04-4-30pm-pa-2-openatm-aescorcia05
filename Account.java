import java.util.Scanner;

public class Account {

    // Attributes

    // name - name of the account holder
    private String name;
    // lastName - last name of the account holder
    private String lastName;
    // password - password for the account
    private int pin;
    // last5Transactions - stores the last 5 transactions (positive for deposits and negative for withdrawals)
    private double[] last5Transactions; // most recent first
    // balance - current balance of the account
    private double balance;
    private boolean blocked;

    //Constructor:
    public Account(String name, String lastName) {

        this.name = name;
        this.lastName = lastName;
        this.setPin();
        this.balance = 0.00;
        this.last5Transactions = new double[5];
        this.blocked = false;

    }

    protected Account(String name, String lastName, int pin, double balance, double[] last5Transactions) {

        this.name = name;
        this.lastName = lastName;
        this.pin = pin;
        this.balance = balance;
        this.last5Transactions = last5Transactions;
        this.blocked = false;

    }

    // encryptPassword - encrypts a String
    protected static int encryptPassword(int pin) {
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
                try {num = Integer.parseInt(numStr); invalid = false;}
                catch(Exception NotAnInt) {invalid = true; System.out.print("Invalid pin. ");}

            }

            if (num < 2 || num > 9999) {System.out.print("Invalid pin. ");}

        }

        return num;

    }

    // getNewPassword - gets an encrypted password and stores it
    protected void getNewPassword() {

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
    protected boolean isOwner(int userKey) {
        blocked = blocked || Account.encryptPassword(userKey) != pin;
        return !blocked;
    }

    // void deposit(amount) - updates balance after the user deposits an amount
    protected void deposit(double amount) {
        balance  += amount;
        last5Transactions[4] = last5Transactions[3];
        last5Transactions[3] = last5Transactions[2];
        last5Transactions[2] = last5Transactions[1];
        last5Transactions[1] = last5Transactions[0];
        last5Transactions[0] = amount;
    }

    //void withdraw(amount) - updates balance after the user withdraws an amount
    protected void withdraw(double amount) {
        balance -= amount;
        last5Transactions[4] = last5Transactions[3];
        last5Transactions[3] = last5Transactions[2];
        last5Transactions[2] = last5Transactions[1];
        last5Transactions[1] = last5Transactions[0];
        last5Transactions[0] = -amount;
    }

    // String[] statsToStrings() - returns the summary of recent account activities as an array: min, max, average, currentBalance
    protected String[] statsToStrings() {
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

        returnArray[0] = "Minimum transaction: " + min;
        returnArray[1] = "Maximum transaction: " + max;
        returnArray[2] = "Average transaction: " + average;
        returnArray[3] = "Current balance: " + balance;

        return returnArray;

    }

    // displayStats() - prints the account info
    protected void displayStats() {
        double min = last5Transactions[0];
        double max = last5Transactions[0];
        double average = 0.0;

        // min
        for(int i = 1; i < last5Transactions.length; i++) {
            if(last5Transactions[i] < min) {min = last5Transactions[i];}
        }

        // max
        for(int i = 1; i < last5Transactions.length; i++) {
            if(last5Transactions[i] > max) {max = last5Transactions[i];}
        }

        // average
        for(int i = 0; i < last5Transactions.length - 1; i++) {
            average += last5Transactions[i];
        }
        average /= last5Transactions.length;

        System.out.println("Minimum transaction: " + min);
        System.out.println("Maximum transaction: " + max);
        System.out.println("Average transaction: " + average);
        System.out.println("Current balance: " + balance);
    }

    // toFile - returns a String with the information of the account to save to a file
    public String toFile() {
        return name + " " + lastName + " " + pin + " " + balance + " " + last5Transactions[0] + " " + last5Transactions[1] + " " + last5Transactions[2] + " " + last5Transactions[3] + " " + last5Transactions[4];
    }

    public char getTransaction() {

        Scanner input = new Scanner(System.in);

        // Creating list of options for user input
        char[] transactionOptions = {'D', 'W', 'C', 'S', 'V', 'E'};

        // Outputting options to the user
        System.out.println("Hi " + name + ", what can this AEBank's ATM do for you today?\n");
        System.out.println("  D eposit");
        System.out.println("  W ithdraw");
        System.out.println("  C heck balance");
        System.out.println("  S how account statistics");
        System.out.println("  V iew recent transactions");
        System.out.println("  E xit");
        System.out.println();

        // Initializing variables
        String transactionStr;
        char transaction;
        boolean validInput = true;

        // Getting input
        transactionStr = input.next();

        // User can only opt for hacker mode the first time.
        if (transactionStr.equals("H4CK3R")) {return 'H';}

        // Turning input to char type
        transaction = transactionStr.toUpperCase().charAt(0);

        // Checking input
        for(int i = 0; i < transactionOptions.length; i++) {validInput = validInput && (transaction != transactionOptions[i]);}

        // Prompt again while input is invalid
        while (validInput) {

            // Prompting the user again
            System.out.println("Please select one of the following options:\n");
            System.out.println("  D eposit");
            System.out.println("  W ithdraw");
            System.out.println("  C heck balance");
            System.out.println("  S how account statistics");
            System.out.println("  V iew recent transactions");
            System.out.println("  E xit");
            System.out.println();

            // Turning input to char type
            transaction = input.next().toUpperCase().charAt(0);

            // Checking input
            validInput = true;
            for(int i = 0; i < transactionOptions.length; i++) {validInput = validInput && (transaction != transactionOptions[i]);}
        }

        return transaction;

    }

    protected void displayLast5Transactions() {
        for (int i = 0; i < 5; i++) {
            if (last5Transactions[i] != 0) {
                System.out.println("Transaction " + (i + 1) + ": " + last5Transactions[i]);
            }
        }
    }

    private void setPin() {
        Scanner input = new Scanner(System.in);

        int num1;
        int num2;
        String numStr;
        boolean invalid;

        num1 = -1;
        num2 = 0;

        while (num1 != num2) {

            num1 = -1;
            num2 = 0;

            while (num1 < 2 || num1 > 9999) {

                invalid = true;

                while (invalid) {

                    System.out.print("Please input your pin: ");
                    numStr = input.next();
                    //System.out.print("\n");
                    try {
                        num1 = Integer.parseInt(numStr);
                        invalid = false;
                    } catch (Exception NotAnInt) {
                        invalid = true;
                        System.out.print("Invalid pin. ");
                    }

                }

                if (num1 < 2 || num1 > 9999) {
                    System.out.print("Invalid pin. ");
                }

            }

            while (num2 < 2 || num2 > 9999) {

                invalid = true;

                while (invalid) {

                    System.out.print("Please input your pin again: ");
                    numStr = input.next();
                    //System.out.print("\n");
                    try {
                        num2 = Integer.parseInt(numStr);
                        invalid = false;
                    } catch (Exception NotAnInt) {
                        invalid = true;
                        System.out.print("Invalid pin. ");
                    }

                }

                if (num2 < 2 || num2 > 9999) {
                    System.out.print("Invalid pin. ");
                }

            }

            if (num1 != num2) {
                System.out.print("Pins don't match, try again!");
            }

        }

        pin = encryptPassword(num1);

    }

    protected void setBalance(double newBalance) {balance = newBalance;}
    protected void unblock() {blocked = false;}

    public double balance() {return balance;}
    public String name() {return name;}
    public String lastName() {return lastName;}
    protected int pin() {return pin;}
    protected double[] last5Transactions() {return last5Transactions;}

}