import java.io.IOException;

public class Main {
    public static void main (String[] args) {
        try {
            System.out.println("\nReading vaccination centers from file...");
            VCList.readVacCentersFromFile();
            
            System.out.println("\nReading recipients from file...");
            RecipientsList.readRecipientsFromFile();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }

        System.out.println("\nIMPORTANT: You must end the program properly in order to save every change.");
        Menu.printHeader();
        Menu.mainMenu();
    }
}
