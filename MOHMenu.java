import java.util.ArrayList;
import java.util.Collections;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.InputMismatchException;
import java.util.Scanner;

public class MOHMenu extends Menu {
    static int mohIndex;

    protected static void mohMenu1() {
        System.out.println("\n+-----------------------------------------------------+");
        System.out.println("|               Ministry of Health Menu               |");
        System.out.println("+-----------------------------------------------------+");
        System.out.println("| 1 - View all recipients' data                       |");
        System.out.println("| 2 - View one recipient's data                       |");
        System.out.println("| 3 - Distribute recipients to a vaccination center   |");
        System.out.println("| 4 - Distribute vaccine(s) to a vaccination center   |");
        System.out.println("| 5 - View vaccination centres' statistics            |");
        System.out.println("| 0 - Back                                            |");
        System.out.println("+-----------------------------------------------------+");
        int choice = getInput(DEFAULT_QUESTION, 5);

        switch (choice) {
            case 1:
                showAllRecipients();
                pressEnterToContinue();
                mohMenu1();
                break;
            case 2:
                try {
                    showOneRecipient();
                }
                catch (IllegalArgumentException ex) {
                    System.out.println("\n" + ex.getMessage());
                    System.out.println("\nRedirecting to MOH Menu...");
                }
                pressEnterToContinue();
                mohMenu1();
                break;
            case 3:
                try {    
                    assignRecipientToVC();
                }
                catch (IllegalArgumentException ex) {
                    System.out.println("\n" + ex.getMessage());
                    System.out.println("\nRedirecting to MOH Menu...");
                }
                pressEnterToContinue();
                mohMenu1();
                break;
            case 4:
                try {
                    assignVaccineToVC();
                }
                catch (IllegalArgumentException ex) {
                    System.out.println("\n" + ex.getMessage());
                    System.out.println("\nRedirecting to MOH Menu...");
                }
                pressEnterToContinue();
                mohMenu1();
                break;
            case 5:
                viewStats();
                pressEnterToContinue();
                mohMenu1();
                break;
            case 0:
                System.out.println("\nRedirecting to Main Menu...");
                pressEnterToContinue();
                mainMenu();
        }
    }

    private static void showAllRecipients() {
        System.out.println("\n-----------------------------------------------------");
        System.out.println("                   Recipients List");
        System.out.println("-----------------------------------------------------");
        for (int i = 0; i < RecipientsList.recipients.size(); i++) {
            Recipient recipient = RecipientsList.recipients.get(i);
            if (recipient.getAssignedToVC() == 0) {
                if (recipient.getFirstApmtDate() == null) {
                    System.out.print((i+1) + ". Unassigned, " + recipient.toString() + ", " + "Date unassigned\n");
                }
                else {
                    if (recipient.getStatusIndex() == 1 || recipient.getStatusIndex() == 2) {
                        LocalDate appointmentDate = recipient.getFirstApmtDate();
                        System.out.print((i+1) + ". Unassigned, " + recipient.toString() + ", " + dateTimeForm.format(appointmentDate) + "\n");
                    }
                    else if (recipient.getStatusIndex() == 3 || recipient.getStatusIndex() == 4) {
                        LocalDate appointmentDate = recipient.getSecondApmtDate();
                        System.out.print((i+1) + ". Unassigned, " + recipient.toString() + ", " + dateTimeForm.format(appointmentDate) + "\n");
                    }
                }
            }
            else {
                if (recipient.getFirstApmtDate() == null) {
                    int tempVC = recipient.getAssignedToVC();
                    System.out.print((i+1) + ". Assigned to " + VCList.vacCenters.get(tempVC - 1).getName() + ", " + recipient.toString() + ", " + "Date unassigned\n");
                }
                else{
                    int tempVC = recipient.getAssignedToVC();
                    if (recipient.getStatusIndex() == 1 || recipient.getStatusIndex() == 2) {
                        LocalDate appointmentDate = recipient.getFirstApmtDate();
                        System.out.print((i+1) + ". Assigned to " + VCList.vacCenters.get(tempVC - 1).getName() + ", " + recipient.toString() + ", " 
                                        + dateTimeForm.format(appointmentDate) + "\n");
                    }
                    else if (recipient.getStatusIndex() == 3 || recipient.getStatusIndex() == 4) {
                        LocalDate appointmentDate = recipient.getSecondApmtDate();
                        System.out.print((i+1) + ". Assigned to " + VCList.vacCenters.get(tempVC - 1).getName() + ", " + recipient.toString() + ", " 
                                        + dateTimeForm.format(appointmentDate) + "\n");
                    }
                }
            }
        }
        System.out.println("-----------------------------------------------------");
    }

    private static void showOneRecipient() {
        System.out.println("\n-----------------------------------------------------");
        System.out.println("                   Recipients List");
        System.out.println("-----------------------------------------------------");
        ArrayList<Recipient> recipients = RecipientsList.recipients;
        
        for (int i = 0; i < recipients.size(); i++) {
            System.out.print((i+1) + ". " + recipients.get(i).getName() + "\n");
        }
        System.out.println("0. Back");
        System.out.println("-----------------------------------------------------");

        int userIndex = getInput("Please choose a recipient to view", recipients.size());
        Recipient recipient = recipients.get(userIndex-1);

        if (userIndex == 0) {
            System.out.println("\nRedirecting to MOH Menu...");
        }
    
        else {
            System.out.println("\nRecipient's name: " + recipient.getName());
            System.out.println("Recipient's phone: " + recipient.getPhone());
            System.out.println("Recipient's age: " + recipient.getAge());
            System.out.println("Recipient's Vaccination Status: " + recipient.getStatus());
            
            if (recipient.getAssignedToVC() == 0) {
                System.out.println("Recipient is currently not assigned to a vaccination center.");
            }
            else {
                int tempVC = recipient.getAssignedToVC();
                System.out.println("Recipient is currently assigned to " + VCList.vacCenters.get(tempVC - 1).getName() + ".");
            }

            if (recipient.getFirstApmtDate() == null) {
                System.out.println("Recipient's Appointment Date: Unassigned");
            }
            else {
                if (recipient.getStatusIndex() == 1 || recipient.getStatusIndex() == 2) {
                    LocalDate appointmentDate = recipient.getSecondApmtDate();
                    System.out.println("Recipient's Appointment Date: " + dateTimeForm.format(appointmentDate));
                }
                else if (recipient.getStatusIndex() == 3 || recipient.getStatusIndex() == 4) {
                    LocalDate appointmentDate = recipient.getSecondApmtDate();
                    System.out.println("Recipient's Appointment Date: " + dateTimeForm.format(appointmentDate));
                }
            }
        }
    }

    private static void assignRecipientToVC() {
        System.out.println("\n------------------------------------------------------");
        System.out.println("               Vaccination Centers List");
        System.out.println("------------------------------------------------------");
        
        ArrayList<VC> vacCenters = VCList.vacCenters;
        ArrayList<Recipient> recipients = RecipientsList.recipients;

        //To list out all of the vaccination centers
        for (int i = 0; i < vacCenters.size(); i++) {
            System.out.println((i+1) + ". " + vacCenters.get(i).getName());
        }
        
        System.out.println("0. Back");
        System.out.println("------------------------------------------------------");
        int vcChoice = getInput("Please choose a VC to be managed", vacCenters.size());
        VC vacCenter = vacCenters.get(vcChoice-1);

        if (vcChoice == 0) {
            System.out.println("\nRedirecting to MOH Menu...");
        }
        
        else {
            System.out.print("\nYou are currently managing " + vacCenter.getName() + ".");
            System.out.println("\n\n-----------------------------------------------------");
            System.out.println("                        Actions");
            System.out.println("-----------------------------------------------------");
            System.out.println("1. Assign one recipient to this VC.");
            System.out.println("2. Assign recipients to " + vacCenter.getName() + " based on its daily max capacity.");
            System.out.println("3. Assign recipients to " + vacCenter.getName() + " with an optional amount.");
            System.out.println("0. Back.");
            System.out.println("-----------------------------------------------------");
            int action = getInput("Please choose an action", 3);
            
            int capacity = vacCenter.getCapacityPerDay();

            if (action == 1) {
                Collections.sort(recipients);
                System.out.println("\n\n-----------------------------------------------------");
                System.out.println("            List of Unassigned Recipients");
                System.out.println("-----------------------------------------------------");
                
                int unassignedSize = 0;

                for (int i = 0; i < recipients.size(); i++) {
                    int checkVC = recipients.get(i).getAssignedToVC();

                    if (checkVC == 0) {
                        System.out.println((i+1) + ". " + recipients.get(i).getName());
                        unassignedSize += 1;
                    }
                }
                System.out.println("0. Back");
                System.out.println("-----------------------------------------------------");

                int userIndex = getInput("Please select a recipient to be assigned to this VC", unassignedSize);

                if (userIndex == 0) {
                    System.out.println("\nRedirecting to MOH Menu...");
                }
                
                else{
                    vacCenter.addNewRecipient(recipients.get(userIndex-1));
                    recipients.get(userIndex-1).setAssignedToVC(vcChoice);

                    System.out.println("Recipient has been assigned to " + vacCenter.getName() + ".");
                    System.out.println("This VC currently has " + vacCenter.getRecipients().size() + " recipients.");
                }
            }

            if (action == 2) {
                for (int i = 0; i < capacity; i++) {
                    if (recipients.get(i).getAssignedToVC() == 0) {
                        vacCenter.addNewRecipient(recipients.get(i));
                        recipients.get(i).setAssignedToVC(vcChoice);
                    }
                }

                System.out.println("\nThe VC has been assigned recipients based on its daily max capacity.");
                System.out.println("This VC currently has " + vacCenter.getRecipients().size() + " recipients.");
            }

            if (action == 3) {
                int unassignedRec = 0;
                
                for (int i = 0; i < recipients.size(); i++) {
                    if (recipients.get(i).getAssignedToVC() == 0) {
                        unassignedRec += 1;
                    }
                }
                
                int optionalAmount = getInput("Please enter the desired amount of recipient(s)", unassignedRec);
                int addedAmount = 0;

                for (int i = 0; addedAmount < optionalAmount; i++) {
                    if (recipients.get(i).getAssignedToVC() == 0) {
                        vacCenter.addNewRecipient(recipients.get(i));
                        recipients.get(i).setAssignedToVC(vcChoice);
                        addedAmount += 1;
                    }
                }

                System.out.println("\nThe VC has been assigned recipients based on your optional amount.");
                System.out.println("This VC currently has " + vacCenter.getRecipients().size() + " recipients.");
            }
        }
    }

    private static void assignVaccineToVC() {
        System.out.println("\n------------------------------------------------------");
        System.out.println("               Vaccination Centers List");
        System.out.println("------------------------------------------------------");
        ArrayList<VC> vacCenters = VCList.vacCenters;

        for (int i = 0; i < vacCenters.size(); i++) {
            System.out.println((i+1) + ". " + vacCenters.get(i).getName());
        }
        System.out.println("0. Back");
        System.out.println("------------------------------------------------------");

        int vcChoice = getInput("Please choose a VC to be managed", vacCenters.size());
        VC vacCenter = vacCenters.get(vcChoice-1);

        if (vcChoice ==0) {
            System.out.println("\nRedirecting to MOH Menu...");
        }
        
        else {
            int capacity = vacCenter.getCapacityPerDay();

            System.out.println("\nYou are currently managing " + vacCenter.getName() + ".");
            System.out.println("This VC currently has " + vacCenter.getVaccineQty() + " vaccines.");
            System.out.println("\n-----------------------------------------------------");
            System.out.println("                        Actions");
            System.out.println("-----------------------------------------------------");
            System.out.println("1. Assign vaccines to " + vacCenter.getName() + " based on its daily max capacity.");
            System.out.println("2. Assign vaccines to " + vacCenter.getName() + " with an optional amount.");
            System.out.println("0. Back.");
            System.out.println("-----------------------------------------------------");
            
            int Choice = getInput("Please choose an action", 2);

            if (Choice == 1) {
                vacCenter.addVaccine(capacity);
                System.out.println("\nThe VC has been assigned vaccines based on its daily max capacity.");
                System.out.println("This VC currently has " + vacCenter.getVaccineQty() + " vaccines.");
            }
            
            if (Choice == 2) {
                Scanner input = new Scanner(System.in);
                
                try {
                    System.out.print("Please enter the desired amount of vaccines: ");
                    int optionalAmount = input.nextInt();
                    vacCenter.addVaccine(optionalAmount);
                    System.out.println("\nThe VC has been assigned vaccines based on your optional amount.");
                    System.out.println("This VC currently has " + vacCenter.getVaccineQty() + " vaccines.");
                } catch (InputMismatchException e) {
                    System.err.println("Invalid input, please try again.");
                    input.nextLine();       // discard unwanted line
                }
            }
            
            if (Choice == 0) {
                System.out.println("\nRedirecting to MOH Menu...");
            }
        }
    }

    private static void viewStats() {
        int totalPending = 0;
        int totalFirstDoseAppointments = 0;
        int totalFirstDoseCompletions = 0;
        int totalSecondDoseAppointments = 0;
        int totalSecondDoseCompletions = 0;
        LinkedHashMap<LocalDate, Integer> apmtsOnDays = new LinkedHashMap<LocalDate, Integer>();
        int totalVaccination = 0;
        int totalCapacityPerDay = 0;

        for (int i = 0; i < VCList.vacCenters.size(); i++) {
            totalCapacityPerDay += VCList.vacCenters.get(i).getCapacityPerDay();
        }

        for (int i = 0; i < RecipientsList.recipients.size(); i++) {
            int statusIndex = RecipientsList.recipients.get(i).getStatusIndex();
            
            if (statusIndex == 0)
                totalPending += 1;

            else if (statusIndex == 1)
                totalFirstDoseAppointments +=1;

            else if (statusIndex == 2)
                totalFirstDoseCompletions += 1;

            else if (statusIndex == 3)
                totalSecondDoseAppointments += 1;

            else if (statusIndex == 4)
                totalSecondDoseCompletions += 1;

            if (statusIndex == 2 || statusIndex == 3)
                totalVaccination += 1;
            else if (statusIndex == 4)
                totalVaccination += 2;
        }

        for (LocalDate date = LocalDate.now(); date.compareTo(LocalDate.of(2020, 1, 1)) >= 0; date = date.minusDays(1)) {
            int numOfApmtsOnDay = 1;
            for (int i = 0; i < VCList.vacCenters.size(); i++) {
                for (int j = 0; j < VCList.vacCenters.get(i).getRecipients().size(); j++) {
                    Recipient recipient = VCList.vacCenters.get(i).getRecipient(j);
                    if (recipient.getFirstApmtDate() != null)
                        if (recipient.getStatusIndex() > 1 && date.equals(recipient.getFirstApmtDate()))
                            apmtsOnDays.put(date, numOfApmtsOnDay++);
                    if (recipient.getSecondApmtDate() != null)
                        if (recipient.getStatusIndex() > 3 && date.equals(recipient.getSecondApmtDate()))
                            apmtsOnDays.put(date, numOfApmtsOnDay++);
                }
            }
        }
        
        System.out.println("\nThe following section shows the statistics across all vaccination centers.");
        System.out.println("\n---------------------------------------------------------");
        System.out.println("                  Cumulative Statistics");
        System.out.println("---------------------------------------------------------");
        System.out.println("Total Pending Recipients = " + totalPending);
        System.out.println("Total First Dose Appointments = " + totalFirstDoseAppointments);
        System.out.println("Total First Dose Completions = " + totalFirstDoseCompletions);
        System.out.println("Total Second Dose Appointments = " + totalSecondDoseAppointments);
        System.out.println("Total Second Dose Completions = " + totalSecondDoseCompletions);
        System.out.println("Total Vaccinations = " + totalVaccination);
        System.out.println("---------------------------------------------------------");
        System.out.println("Total Capacity Per Day = " + totalCapacityPerDay);
        System.out.println("---------------------------------------------------------");
        for (LocalDate date = LocalDate.now(); date.compareTo(LocalDate.of(2020, 1, 1)) >= 0; date = date.minusDays(1)) {
            if (apmtsOnDays.containsKey(date))
                System.out.println("Total Vaccinations on " + dateTimeForm.format(date) + " = " + apmtsOnDays.get(date));
        }
        System.out.println("------------------------------------------------------");
    }
}
