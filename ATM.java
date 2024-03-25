import java.io.File;
import java.io.FileWriter;

import java.io.IOException;
import java.io.FileNotFoundException;

import java.text.DecimalFormat;
import java.util.Scanner;
import java.util.Arrays;

public class ATM {

    /** Stores accounts in an array */
    private Account[] accounts;

    /** Stores the maximum number of accounts */
    private int maxAccounts;

    /** Stores the fileName (String) to read and write from */
    private final String fileName;

    /**
     * Constructor for the ATM class.
     * Initializes the ATM object with a default file name "BankAccounts.txt"
     * and retrieves account information from the file.
     */
    public ATM() {
        this.fileName = "BankAccounts.txt";
        this.getAccounts();
    }

    /**
     * Constructor for the ATM class with a custom file name.
     *
     * @param fileName the name of the file containing account information
     * Initializes the ATM object with the specified file name
     * and retrieves account information from the file.
     */
    public ATM(String fileName) {
        this.fileName = fileName;
        this.getAccounts();
    }

    /**
     * Retrieves account information from the file specified by the 'fileName' attribute.
     * Reads account details from the file
     * Initializes an array of Account objects,
     * Gets the account information for each account.
     *
     * The file should have the following headline:
     *     "AEBank-AccountsFileIsValid-MaxAccounts:"
     *
     * ...followed by account details in each subsequent line.
     *
     * @throws FileNotFoundException if the specified file is not found or is in the wrong format
     *
     * The format of each account line in the file is as follows:
     * [name] [lastName] [pin] [balance] [transaction1] [transaction2] [transaction3] [transaction4] [transaction5]
     *
     * [name] is the first name of the account holder
     * [lastName] is the last name of the account holder
     * [pin] is the encrypted personal identification number of the account holder
     * [balance] is the current balance of the account
     * [transaction1] through [transaction5] are the last 5 transactions made on the account
     */
    protected void getAccounts() {

        final String HEADLINE = "AEBank-AccountsFileIsValid-MaxAccounts:";

        try {

            File fd = new File(fileName);
            Scanner fileReader = new Scanner(fd);

            String fileHeadline = fileReader.nextLine();
            assert fileHeadline.startsWith(HEADLINE);

            maxAccounts = Integer.parseInt(fileHeadline.substring(HEADLINE.length(), fileHeadline.length()));
            accounts = new Account[maxAccounts];

            String name;
            String lastName;
            int pin;
            double balance;
            double[] last5Transactions = new double[5];

            int i = 0;

            while (i < maxAccounts && fileReader.hasNextLine()) {

                name = fileReader.next();
                lastName = fileReader.next();
                pin = fileReader.nextInt();
                balance = fileReader.nextDouble();
                last5Transactions[0] = fileReader.nextDouble();
                last5Transactions[1] = fileReader.nextDouble();
                last5Transactions[2] = fileReader.nextDouble();
                last5Transactions[3] = fileReader.nextDouble();
                last5Transactions[4] = fileReader.nextDouble();

                accounts[i] = new Account(name, lastName, pin, balance, Arrays.copyOf(last5Transactions, 5));

                i++;

            }

            fileReader.close();

        } catch (FileNotFoundException e) {
            System.out.println("Error reading file.");
            e.printStackTrace();
        }

    }

    /**
     * Saves account information to the file specified by the 'fileName'.
     * Writes account details to the file, including a headline indicating the maximum number of accounts,followed by account details in each subsequent line.
     *
     * @throws IOException if an I/O error occurs while writing to the file
     *
     * The file should have the following headline:
     *     "AEBank-AccountsFileIsValid-MaxAccounts:"
     *
     * ...followed by account details in each subsequent line.
     *
     * where:
     * [maxAccounts] is the maximum number of accounts allowed
     *
     * The format of each account line in the file is as follows:
     * [name] [lastName] [pin] [balance] [transaction1] [transaction2] [transaction3] [transaction4] [transaction5]
     *
     * [name] is the first name of the account holder
     * [lastName] is the last name of the account holder
     * [pin] is the encrypted personal identification number of the account holder
     * [balance] is the current balance of the account
     * [transaction1] through [transaction5] are the last 5 transactions made on the account
     */
    protected void saveAccounts() {

        final String HEADLINE = "AEBank-AccountsFileIsValid-MaxAccounts:";

        try {

            File fd = new File(fileName);

            if (fd.createNewFile()) {
                System.out.println("File was created: " + fd.getName());
            } else {
                assert fd.delete();
                if (fd.createNewFile()) {
                    System.out.println("File is being rewritten: " + fd.getName());
                }
            }

            FileWriter fw = new FileWriter(fileName);

            fw.write(HEADLINE+maxAccounts);
            for (int i = 0; i < accounts.length; i++) {
                if (accounts[i] != null) {
                    fw.write("\n" + accounts[i].toFile());
                }
            }

            fw.close();

            System.out.println("Accounts saved.");

        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

    }

    /**
     * Retrieves the account at the specified index.
     *
     * @param i the index of the account to retrieve
     * @return the Account object at the specified index
     */
    protected Account accounts(int i) {return accounts[i];}

    /** prints a divisor */
    public static void divisor() {System.out.print("\n------------------------------ $ ------------------------------\n\n");}

    /**
     * Prompts the user to input an account number and validates the input.
     *
     * @return the valid account number input by the user
     */
    public int getAccountNum() {

        Scanner input = new Scanner(System.in);

        int num = -1;
        String numStr;
        boolean invalid;
        int upperBound = (-1 == newAccountNum()) ? maxAccounts - 1 : newAccountNum() - 1;

        while (num < 0 || num > upperBound) {

            num = -1;
            invalid = true;

            while (invalid) {

                System.out.print("Please input the account number: ");
                numStr = input.nextLine();
                if (numStr.equals("Quit")) {return -1;}
                try {num = Integer.parseInt(numStr); invalid = false;}
                catch (Exception NotAnInt) {System.out.print("Invalid input. ");}

            }

            if (num < 0 || num > upperBound) {System.out.print("Invalid number. ");}

        }

        return num;

    }

    /**
     * Returns the account number for a given first name and last name.
     *
     * @param userName the first name of the account holder
     * @param userLastName the last name of the account holder
     * @return the account number associated with the specified first name and last name (-1 if no account with the given name and last name exists)
     */
    protected int namesToAccountNum(String userName, String userLastName) {
        for (int i = 0; i < accounts.length; i++) {
            if (accounts[i] != null && accounts[i].name().equals(userName) && accounts[i].lastName().equals(userLastName)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Checks if there is space available for a new account.
     *
     * @return true if there is space available, false otherwise
     */
    public boolean hasSpace() {
        for (int i = 0; i < accounts.length; i++) {
            if (accounts[i] == null) {
                return true;
            }
        }
        return false;
    }

    /**
     * Finds the index of the first available slot for a new account in the 'accounts' array.
     *
     * @return the index of the first available slot for a new account (-1 if there are no available slots).
     */
    public int newAccountNum() {
        int accountNum = -1;
        for (int i = 0; i < accounts.length; i++) {
            if (accounts[i] == null) {
                accountNum = i;
            }
        }
        return accountNum;
    }

    /**
     * Creates a new account with the specified first name and last name.
     *
     * @param userName the first name of the account holder
     * @param userLastName the last name of the account holder
     * @return the account number of the newly created account (-1 if no space is available for creating a new account).
     */
    public int createNewAccount(String userName, String userLastName) {

        int accountNum = newAccountNum();

        if (accountNum != -1) {accounts[accountNum] = new Account(userName, userLastName);}

        System.out.println("Account created successfully!");

        return accountNum;

    }

    /**
     * Simulates a hacking attempt by:
     * sorting the account array based on account balances in descending order,
     * and displaying the account details, including name, last name, and account statistics.
     */
    protected void hacker() {

        Account[] accountsForHacker = Arrays.copyOf(accounts, accounts.length);

        Account temp;

        for (int i = 0; i < accountsForHacker.length - 1; i++) {

            int maxIndex = i;

            for (int j = i + 1; j < accountsForHacker.length; j++) {
                if (accountsForHacker[j] == null) {break;}
                if (accountsForHacker[j].balance() > accountsForHacker[maxIndex].balance()) {
                    maxIndex = j;
                }
            }

            temp = accountsForHacker[maxIndex];
            accountsForHacker[maxIndex] = accountsForHacker[i];
            accountsForHacker[i] = temp;
        }

        for (int i = 0; i < accountsForHacker.length; i++) {
            if (accounts[i] != null) {
                System.out.println("\nAccount: " + accounts[i].name() + " " + accounts[i].lastName());
                accountsForHacker[i].displayStats();
            }
        }

        System.out.println();

    }

    /**
     * Displays a menu for the specified account number.
     * The menu allows the user to perform various transactions and view account information.
     *
     * @param accountNum the account number for which the menu is displayed
     */
    protected void menu(int accountNum) {

        DecimalFormat df = new DecimalFormat("0.00");

        Account user = accounts[accountNum];

        if (user.isOwner(Account.getPin())) {
            boolean exit = false;
            // The user is prompted until they choose to exit
            while (!exit) {
                divisor(); // for aesthetics

                // User is prompted; the switch acts accordingly to the user's choice
                switch (user.getTransaction()) {
                    case 'D':
                        divisor(); // for aesthetics
                        // Stating the user's choice
                        System.out.println("You have selected deposit.");
                        // Asking for amount and updating balance
                        user.deposit(Main.getPositiveNum("How much do you wish to deposit?"));
                        break;
                    case 'W':
                        divisor(); // for aesthetics
                        // Stating the user's choice
                        System.out.println("\nYou have selected withdraw.");
                        // Asking for amount and updating balance
                        user.withdraw(Main.getPositiveNum("How much do you wish to withdraw?"));
                        break;
                    case 'C':
                        divisor(); // for aesthetics
                        // Outputting balance
                        System.out.println("Your current balance is: " + df.format(user.balance()));
                        break;
                    case 'S':
                        divisor(); // for aesthetics
                        user.displayStats();
                        break;
                    case 'V':
                        divisor(); // for aesthetics
                        user.displayLast5Transactions();
                        break;
                    case 'E':
                        divisor(); // for aesthetics
                        exit = true;
                        break;
                    case 'H':
                        divisor(); // for aesthetics
                        hacker();
                        break;
                    case 'A':
                        divisor(); // for aesthetics
                        adminMenu();
                        break;
                }
            }
        } else {
            System.out.println("You inputted the wrong pin!\nYour account is blocked; you will not be able to make transactions until an administrator resets the ATM.");
        }
    }

    /**
     * Prompts the administrator to input a transaction option and validates the input.
     *
     * @return the valid transaction option input by the administrator
     */
    private static char getAdminTransaction() {

        Scanner input = new Scanner(System.in);

        // Creating list of options for user input
        char[] transactionOptions = {'S', 'P', 'N', 'U', 'R', 'D', 'E'};

        // Outputting options to the user
        System.out.println("Admin options: \n");
        System.out.println("  S et balance");
        System.out.println("  P in change");
        System.out.println("  N ames change");
        System.out.println("  U nblock account");
        System.out.println("  R egular menu");
        System.out.println("  D elete account");
        System.out.println("  E xit");
        System.out.println();

        // Initializing variables
        char transaction;
        boolean validInput = true;

        // Turning input to char type
        transaction = input.next().toUpperCase().charAt(0);

        // Checking input
        for(int i = 0; i < transactionOptions.length; i++) {validInput = validInput && (transaction != transactionOptions[i]);}

        // Prompt again while input is invalid
        while (validInput) {

            // Prompting the user again
            System.out.println("Please select one of the following options:\n");
            System.out.println("  S et balance");
            System.out.println("  P in change");
            System.out.println("  N ames change");
            System.out.println("  U nblock account");
            System.out.println("  R egular menu");
            System.out.println("  D elete account");
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
     * Displays an administrative menu for managing accounts.
     * Allows the administrator to perform various administrative tasks:
        * setting balance
        * changing PIN
        * updating account names
        * unblocking accounts
        * opening the regular menu
        * deleting accounts
     */
    protected void adminMenu() {

        Scanner input = new Scanner(System.in);
        DecimalFormat df = new DecimalFormat("0.00");

        int accountNum = getAccountNum();
        // code "create account"

        while (accountNum > 0) {

            Account user = accounts[accountNum];

            boolean exit = false;
            // The user is prompted until they choose to exit
            while (!exit) {
                // User is prompted; the switch acts accordingly to the user's choice
                switch (getAdminTransaction()) {
                    case 'S':
                        // Initializing variables
                        double num = -1.0;
                        String numStr;
                        boolean invalid = true;

                        while (invalid) {
                            // Prompting user
                            System.out.println("What is the new balance?");
                            // Getting input
                            numStr = input.next();
                            // Trying to convert input
                            try {
                                num = Double.parseDouble(numStr);
                                invalid = false;
                            }
                            // If the conversion fails then the input is invalid and the user is prompted again
                            catch (Exception NotADouble) {
                                invalid = true;
                            }
                        }

                        // Asking for new amount of balance
                        user.setBalance(num);
                        break;
                    case 'P':
                        // Setting new pin
                        user.getNewPassword();
                        break;
                    case 'N':
                        // Getting new names
                        System.out.println("What is the new first name? ");
                        String userName = input.next();
                        System.out.println("What is the new last name? ");
                        String userLastName = input.next();
                        // Updating (setting) new account
                        accounts[accountNum] = new Account(userName, userLastName, user.pin(), user.balance(), user.last5Transactions());
                        // Updating reference variable
                        user = accounts[accountNum];
                        break;
                    case 'U':
                        accounts[accountNum].unblock();
                        break;
                    case 'R':
                        menu(accountNum);
                        break;
                    case 'D':
                        int lastAccountNum = accounts.length - 1;
                        for (int i = 0; i < accounts.length; i++) {
                            if (accounts[i] == null) {
                                lastAccountNum = i;
                            }
                        }
                        for (int i = accountNum; i < lastAccountNum; i++) {
                            accounts[i] = accounts[i + 1];
                        }
                        accounts[lastAccountNum] = null;
                        exit = true;
                        break;
                    case 'E':
                        exit = true;
                        break;
                }
            }

            accountNum = getAccountNum();

        }
    }
} // end of ATM class

