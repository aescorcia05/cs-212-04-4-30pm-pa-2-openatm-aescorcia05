import java.util.Scanner;

public class Account {

    // Attributes

    /**
     * The first name of the account holder.
     */
    private String name;

    /**
     * The last name of the account holder.
     */
    private String lastName;

    /**
     * The encrypted PIN for the account.
     */
    private int pin;

    /**
     * An array storing the last 5 transactions made on the account.
     * Positive values represent deposits, while negative values represent withdrawals.
     * The most recent transaction is stored first in the array.
     */
    private double[] last5Transactions;

    /**
     * The current balance of the account.
     */
    private double balance;

    /**
     * True if the account is blocked
     */
    private boolean blocked;


    /**
     * Constructs an Account object with:
         * the specified first name and last name
         * a PIN inputted by the user
         * zero balance
     *
     * @param name the first name of the account holder
     * @param lastName the last name of the account holder
     */
    public Account(String name, String lastName) {

        this.name = name;
        this.lastName = lastName;
        this.setPin();
        this.balance = 0.00;
        this.last5Transactions = new double[5];
        this.blocked = false;

    }

    /**
     * Constructs an Account object with the specified first name, last name, PIN, balance, and last 5 transactions.
     *
     * @param name the first name of the account holder
     * @param lastName the last name of the account holder
     * @param pin the personal identification number (PIN) for the account
     * @param balance the current balance of the account
     * @param last5Transactions an array storing the last 5 transactions made on the account
     */
    protected Account(String name, String lastName, int pin, double balance, double[] last5Transactions) {

        this.name = name;
        this.lastName = lastName;
        this.pin = pin;
        this.balance = balance;
        this.last5Transactions = last5Transactions;
        this.blocked = false;

    }

    /**
     * Encrypts a PIN using a simple encryption algorithm.
     *
     * @param pin the PIN to be encrypted
     * @return the encrypted PIN value
     */
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

    /**
     * Prompts the user to input a valid PIN.
     *
     * @return the valid PIN input by the user
     */
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

    /**
     * Prompts the user to input a new PIN and encrypts it using the encryption algorithm.
     *
     * After prompting the user to input a new PIN and confirming it,
     * the method encrypts the PIN using the encryption algorithm and stores it in the account.
     */
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

    /**
     * Determines if the provided user key matches the stored encrypted PIN.
     *
     * @param userKey the user key to be checked
     * @return true if the provided user key matches the stored encrypted PIN and the account is not blocked, false otherwise
     */
    protected boolean isOwner(int userKey) {
        blocked = blocked || Account.encryptPassword(userKey) != pin;
        return !blocked;
    }

    /**
     * Updates the account balance after the user deposits an amount.
     *
     * @param amount the amount to be deposited
     */
    protected void deposit(double amount) {
        balance  += amount;
        last5Transactions[4] = last5Transactions[3];
        last5Transactions[3] = last5Transactions[2];
        last5Transactions[2] = last5Transactions[1];
        last5Transactions[1] = last5Transactions[0];
        last5Transactions[0] = amount;
    }

    /**
     * Updates the account balance after the user withdraws an amount.
     *
     * @param amount the amount to be withdrawn
     */
    protected void withdraw(double amount) {
        balance -= amount;
        last5Transactions[4] = last5Transactions[3];
        last5Transactions[3] = last5Transactions[2];
        last5Transactions[2] = last5Transactions[1];
        last5Transactions[1] = last5Transactions[0];
        last5Transactions[0] = -amount;
    }

    /**
     * Returns the summary of recent account activities as an array of strings.
     *
     * The array includes:
         * the minimum transaction amount
         * maximum transaction amount
         * average transaction amount
         * current account balance
     *
     * @return an array of strings containing the summary of recent account activities
     */
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

    /**
     * Prints the account information
     *
     * Including:
         * the minimum transaction amount
         * maximum transaction amount
         * average transaction amount
         * current account balance
     */
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

    /**
     * Returns a string containing the information of the account in a format suitable for saving to a file.
     *
     * @return a string with the account information formatted for file storage
     */
    public String toFile() {
        return name + " " + lastName + " " + pin + " " + balance + " " + last5Transactions[0] + " " + last5Transactions[1] + " " + last5Transactions[2] + " " + last5Transactions[3] + " " + last5Transactions[4];
    }

    /**
     * Prompts the user to select a transaction option and validates the input.
     *
     * @return the selected transaction option
     */
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

    /**
     * Displays the last 5 transactions made on the account.
     * Transactions with a value of 0 are skipped.
     */
    protected void displayLast5Transactions() {
        for (int i = 0; i < 5; i++) {
            if (last5Transactions[i] != 0) {
                System.out.println("Transaction " + (i + 1) + ": " + last5Transactions[i]);
            }
        }
    }

    /**
     * Prompts the user to set a new PIN for the account and validates the input.
     *
     * The method ensures that the user inputs a valid PIN and confirms it correctly.
     * Once the PIN is confirmed, it is encrypted and stored in the account.
     */
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

    /**
     * Sets the balance of the account to the specified value.
     *
     * @param newBalance the new balance to set for the account
     */
    protected void setBalance(double newBalance) {balance = newBalance;}
    /**
     * Unblocks the account.
     */
    protected void unblock() {blocked = false;}

    /**
     * Retrieves the current balance of the account.
     *
     * @return the current balance of the account
     */
    protected double balance() {
        return balance;
    }

    /**
     * Retrieves the first name of the account holder.
     *
     * @return the first name of the account holder
     */
    public String name() {
        return name;
    }

    /**
     * Retrieves the last name of the account holder.
     *
     * @return the last name of the account holder
     */
    public String lastName() {
        return lastName;
    }

    /**
     * Retrieves the encrypted PIN of the account.
     *
     * @return the encrypted PIN of the account
     */
    protected int pin() {
        return pin;
    }

    /**
     * Retrieves the array containing the last 5 transactions made on the account.
     *
     * @return an array containing the last 5 transactions made on the account
     */
    protected double[] last5Transactions() {
        return last5Transactions;
    }


}
