import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.time.format.DateTimeFormatter;

public class Menu {
    protected static final String DEFAULT_QUESTION = "Please select an action by entering its number";
    protected static final DateTimeFormatter dateTimeForm = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public static void printHeader() {
        System.out.println("\n-------------------------------------");
        System.out.println("     COVID-19 Vaccination System     ");
        System.out.println("-------------------------------------");
    }

    public static void mainMenu() {
        System.out.println("\n+-----------------------------------+");
        System.out.println("|             Main Menu             |");
        System.out.println("+-----------------------------------+");
        System.out.println("| 1 - Recipient                     |");
        System.out.println("| 2 - Vaccination Center            |");
        System.out.println("| 3 - Ministry Of Health            |");
        System.out.println("| 4 - VC Hall Simulator             |");
        System.out.println("| 0 - Exit                          |");
        System.out.println("+-----------------------------------+");
        int role = getInput("Please select your role", 4);

        switch (role) {
            case 1 :
                RecipientMenu.recipientMenu();
                break;
            case 2 :
                VCMenu.vcIndex = verifyVCAccess();
                if (VCMenu.vcIndex != -99) {
                    System.out.println("\nWelcome to " + VCList.vacCenters.get(VCMenu.vcIndex).getName() + ".");
                    VCMenu.vcMenu1();
                }
                else {
                    System.out.println("\nYou do not have the access to the Vaccination Center.");
                    System.out.println("\nRedirecting to Main Menu...");
                    pressEnterToContinue();
                    mainMenu();
                }
                break;
            case 3 :
                MOHMenu.mohIndex = verifyMOHAccess();
                if (MOHMenu.mohIndex != -99) {
                    System.out.println("\nWelcome to the Ministry of Health Menu.");
                    MOHMenu.mohMenu1();
                }
                else {
                    System.out.println("\nYou do not have the access to the Ministry of Health.");
                    System.out.println("\nRedirecting to Main Menu...");
                    pressEnterToContinue();
                    mainMenu();
                }
                break;
            case 4:
                try{
                    VCHallSimulator.run();
                }
                catch (IllegalArgumentException ex) {
                    System.out.println("\n" + ex.getMessage());
                    System.out.println("\nRedirecting to Main menu...");
                    pressEnterToContinue();
                    mainMenu();
                }
                catch (InputMismatchException ex) {
                    System.out.println("\nPlease enter an integer for simulation date.");
                    System.out.println("\nRedirecting to Vaccination Center menu...");
                    pressEnterToContinue();
                    mainMenu();
                }
                break;
            case 0 :
                exit();
                break;
        }
    }

    protected static int getInput(String question, int options) {
        int choice;
        Scanner input = new Scanner(System.in);

        while (true) {
            System.out.print("\n" + question + " (0-" + options + "): " );

            try {
                choice = input.nextInt();
                if (choice >= 0 && choice <= options) 
                    return choice;
                else {
                    System.err.println("Invalid input, please try again.");
                    input.nextLine();   // discard unwanted line
                }
            }
            catch (InputMismatchException e) {
                System.err.println("Invalid input, please try again.");
                input.nextLine();       // discard unwanted line
            }
        }
    }

    private static int verifyVCAccess() {
        Scanner input = new Scanner(System.in);
        System.out.print("\nPlease enter the password of your vaccination center: ");
        String password = input.nextLine();
        for (int i = 0; i < VCList.vacCenters.size();  i++)
            if (password.equals(VCList.vacCenters.get(i).getPassword()))
                return i;
        return -99;
    }

    private static int verifyMOHAccess() {
        Scanner input = new Scanner(System.in);
        System.out.print("\nPlease enter the password to access the Ministry of Health: ");
        String password = input.nextLine();
        if (password.equals(MOH.getPassword()))
            return 1;
        else 
            return -99;
    }

    protected static void pressEnterToContinue() {
        System.out.print("\nPress ENTER to continue...");
        try {
            System.in.read();
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }
    }

    private static void exit() {
        Scanner input = new Scanner(System.in);

        System.out.print("\nAre you sure you want to exit? (Y/N): ");
        String yesOrNo = input.next();
        if (yesOrNo.equals("Y") || yesOrNo.equals("y")) {
            try {
                VCList.saveVacCentersToFile();
                RecipientsList.saveRecipientsToFile();
            } catch (IOException e) {
                    System.out.println(e.getMessage());
            }
        }
        else if (yesOrNo.equals("N") || yesOrNo.equals("n")) {
            System.out.println("\nRedirecting to Main Menu...");
            mainMenu();
        }  
        else {
            System.out.println("\nPlease enter \'Y\'or\'y\' if you wish to quit the program, " +
                               "or enter \'N\'or\'n\' if you wish to continue.");
            exit();
        }
    }
}
