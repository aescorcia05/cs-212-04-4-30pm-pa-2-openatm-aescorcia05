import java.util.Scanner;

public class Main {

    /**
     * Prompts the user to input a positive number and validates the input.
     *
     * @param prompt the prompt message to display to the user
     * @return the positive number input by the user
     */
    public static double getPositiveNum(String prompt) {

        Scanner input = new Scanner(System.in);

        // Initializing variables
        double num = -1.0;
        String numStr;
        boolean invalid;

        // Prompt again while input is invalid
        while (num <= 0.0) {

            // [Re]setting invalid to true
            invalid = true;

            while (invalid) {
                // Prompting user
                System.out.println(prompt);
                // Getting input
                numStr = input.next();
                // Trying to convert input
                try {num = Double.parseDouble(numStr); invalid = false;}
                // If the conversion fails then the input is invalid and the user is prompted again
                catch(Exception NotADouble) {invalid = true;}
            }
        }
        // Returning input
        return num;
    }

    /**
     * Prompts the user whether they want to create a new account for the specified name.
     *
     * @param name the name for which the new account is being created
     * @return 'Y' if the user wants to create a new account, 'N' otherwise
     */
    public static char wantNewAccount(String name) {

        Scanner input = new Scanner(System.in);

        // Creating list of options for user input
        char[] options = {'Y', 'N'};

        // Outputting options to the user
        System.out.println("Do you want to create a new one " + name + "?");

        // Initializing variables
        char choice;
        boolean validInput = true;

        // Turning input to char type
        choice = input.next().toUpperCase().charAt(0);

        // Checking input
        for(int i = 0; i < options.length; i++) {validInput = validInput && (choice != options[i]);}

        // Prompt again while input is invalid
        while (validInput) {

            // Prompting the user again
            System.out.println("Do you want to create a new one " + name + "?");

            // Turning input to char type
            choice = input.next().toUpperCase().charAt(0);

            // Checking input
            validInput = true;
            for(int i = 0; i < options.length; i++) {validInput = validInput && (choice != options[i]);}

        }

        return choice;

    }

    /**
     * Prints a headline for the ATM
     */
    public static void headline() {
        System.out.println("------------------------------ $ ------------------------------");
        System.out.println("                          AE Bank ATM                          ");
        System.out.println("------------------------------ $ ------------------------------");
        System.out.println();
    }

    /**
     * Main method to run the ATM program.
     *
     * Prompts the user to enter their first name and last name.
     * If the user is an admin, it directs them to the admin menu.
     * If the user wants to quit, the program ends.
     * Otherwise, it checks if the user has an existing account and proceeds accordingly.
     * If no account is found and there is available space, it offers to create a new account.
     * After each interaction, it clears the screen for the next user.
     */
    public static void main(String[] args) {

        for (int i = 0; i < 80; i++) {System.out.println();}

        Scanner input = new Scanner(System.in);
        ATM Atm = new ATM();

        boolean quit = false;

        while (!quit) {

            headline();
            System.out.println("Welcome to AEBank's ATM");

            System.out.println("What is your first name? ");
            String userName = input.next();
            System.out.println("What is your last name? ");
            String userLastName = input.next();

            if (userName.equals("4DM1N") && userLastName.equals("157R470R")) {Atm.adminMenu();} else {
                if (userName.equals("Quit") && userLastName.equals("Quit")) {
                    quit = true;
                } else {

                    int accountNum = Atm.namesToAccountNum(userName, userLastName);

                    if (accountNum == -1) {
                        System.out.print("No account was found under your name");
                        if (Atm.hasSpace()) {
                            System.out.print(", but you can set one up right now.\n");
                            if(wantNewAccount(userName) == 'Y') {
                                System.out.println("A new account is being created for you.");
                                Atm.menu(Atm.createNewAccount(userName, userLastName));
                            }
                        } else {
                            System.out.print(", and no space is available at the moment.");
                        }
                    } else {
                        Atm.menu(accountNum);
                    }

                    for (int i = 0; i < 80; i++) {System.out.println();}

                }
            }

            // Atm.saveAccounts();

        }

        Atm.saveAccounts();

    } // end of main method

}
