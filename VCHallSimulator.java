import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;

public class VCHallSimulator extends Menu {
    private static LocalDate simulationDate;
    private static final DateTimeFormatter dateTimeForm = DateTimeFormatter.ofPattern("dd MMM yyyy");
    private static VC vacCenter;
    private static ArrayList<Recipient> recipients;
    private static ArrayList<Recipient> vaccinatedRecipients;
    private static LinkedList<Integer> vaccineStack;
    private static LinkedList<Recipient> seniorQueue;
    private static LinkedList<Recipient> normalQueue;

    protected static void run() {
        Scanner input = new Scanner(System.in);
        simulationDate = null;
        vacCenter = null;
        recipients = new ArrayList<>();
        vaccinatedRecipients = new ArrayList<>();
        vaccineStack = new LinkedList<>();
        seniorQueue = new LinkedList<>();
        normalQueue = new LinkedList<>();

        getVacCenter();
        if (vacCenter == null) {
            System.out.println("\nYou've chose to end the simulation.");
            System.out.println("\nRedirecting to Main Menu...");
            pressEnterToContinue();
            mainMenu();
            return;
        }
        System.out.println("\nYou are currently simulating a day in " + vacCenter.getName() + "'s hall. (-99 to exit simulating)");
        getSimulationDate();
        System.out.println("\nDate: " + dateTimeForm.format(simulationDate));
        getRecipients();
        if (recipients.size() == 0) {
            System.out.println("\nThis VC do not have recipient having appointment on this date.");
            System.out.println("\nRedirecting to Main Menu...");
            pressEnterToContinue();
            mainMenu();
            return;
        }
        getVaccineStack();
        if (vaccineStack.size() == 0) {
            System.out.println("\nThis VC do not have any vaccine doses left.");
            System.out.println("\nRedirecting to Main Menu...");
            pressEnterToContinue();
            mainMenu();
            return;
        }
        boolean vaccineSupplyEnough;
        if (vaccineStack.size() < recipients.size())
            vaccineSupplyEnough = false;
        else
            vaccineSupplyEnough = true;
        printRecipients();
        printVaccines();
        pressEnterToContinue();
        assignRecipientsToQueue();

        while ((seniorQueue.size() != 0 || normalQueue.size() != 0) && vaccineStack.size() != 0) {
            input.nextLine();       // discard unwanted line
            printSeniorQueue();
            printNormalQueue();
            printVaccines();
            printVaccinatedRecipients();
            pressEnterToContinue();
            vaccination();
        }
        
        input.nextLine();       // discard unwanted line
        printSeniorQueue();
        printNormalQueue();
        printVaccines();
        printVaccinatedRecipients();
        if (!vaccineSupplyEnough) 
            System.out.println("\nThis VC is currently out of vaccine, vaccination paused.");
        else
            System.out.println("\nSimulation completed.");
        System.out.println("\nRedirecting to Main Menu...");
        pressEnterToContinue();
        mainMenu();
    }

    private static void getVacCenter() {
        ArrayList<VC> vacCenters = VCList.vacCenters;
        System.out.println("\n------------------------------------------------------");
        System.out.println("               Vaccination Centers List");
        System.out.println("------------------------------------------------------");
        for (int i = 0; i < vacCenters.size(); i++) {           //To list out all of the vaccination centers
            System.out.println((i+1) + ". " + vacCenters.get(i).getName());
        }
        System.out.println("0. Back");
        System.out.println("------------------------------------------------------");
        int vcChoice = getInput("Please choose a VC to simulate its hall", vacCenters.size());
        if (vcChoice == 0)
            return;
        vacCenter = vacCenters.get(vcChoice-1);
    }

    private static void getSimulationDate() {
        Scanner input = new Scanner(System.in);
        
        System.out.print("Please enter a year between 2020 and 2021: ");
        int simulateYear = input.nextInt();
        if (simulateYear == -99)
            throw new IllegalArgumentException("You've chose to exit simulation.");
        System.out.print("Please enter the desired month (1-12): ");
        int simulateMonth = input.nextInt();
        if (simulateMonth == -99)
            throw new IllegalArgumentException("You've chose to exit simulation.");
        System.out.print("Please enter the desired day (1-31): ");
        int simulateDay = input.nextInt();
        if (simulateDay == -99)
            throw new IllegalArgumentException("You've chose to exit simulation.");

        if (simulateYear < 2020 || simulateYear  > 2021) 
            throw new IllegalArgumentException("You've entered an invalid year, simulation failed.");
        if (simulateMonth < 1 || simulateMonth > 12)
            throw new IllegalArgumentException("You've entered an invalid month, simulation failed.");
        if (simulateDay < 1 || simulateDay > 31)
            throw new IllegalArgumentException("You've entered an invalid day, simulation failed.");
        else {
            if ((simulateMonth == 4 || simulateMonth == 6 || simulateMonth == 9 || simulateMonth == 11) && simulateDay == 31)
                throw new IllegalArgumentException("You've entered an invalid day, simulation failed.");
            if (simulateMonth == 2 && simulateDay > 28)
                throw new IllegalArgumentException("You've entered an invalid day, simulation failed.");
        }

        LocalDate tempDate = LocalDate.of (simulateYear, simulateMonth, simulateDay);
        
        if (tempDate.compareTo(LocalDate.now()) <= 0) {
            simulationDate = tempDate;
        }
        else
            throw new IllegalArgumentException("You've entered an invalid simulation date.");
    }

    private static void getRecipients() {
        for (int i = 0; i < vacCenter.getRecipients().size(); i++) {
            Recipient recipient = vacCenter.getRecipient(i);
            if (recipient.getFirstApmtDate() != null && recipients.size() < vacCenter.getCapacityPerDay()) {
                if (recipient.getStatusIndex() == 1) {
                    int recipientApmtDay = recipient.getFirstApmtDate().getDayOfMonth();
                    int recipientApmtMonth = recipient.getFirstApmtDate().getMonthValue();
                    int recipientApmtYear = recipient.getFirstApmtDate().getYear();

                    if (recipientApmtDay == simulationDate.getDayOfMonth() && recipientApmtMonth == simulationDate.getMonthValue() &&
                        recipientApmtYear == simulationDate.getYear())
                        {
                            recipients.add(recipient);
                        }
                }
                else if (recipient.getStatusIndex() == 3 && recipient.getSecondApmtDate() != null) {
                    int recipientApmtDay = recipient.getSecondApmtDate().getDayOfMonth();
                    int recipientApmtMonth = recipient.getSecondApmtDate().getMonthValue();
                    int recipientApmtYear = recipient.getSecondApmtDate().getYear();

                    if (recipientApmtDay == simulationDate.getDayOfMonth() && recipientApmtMonth == simulationDate.getMonthValue() &&
                        recipientApmtYear == simulationDate.getYear())
                        {
                            recipients.add(recipient);
                        }
                }
            }
        }
    }

    private static void getVaccineStack() {
        for (int i = 0; i < recipients.size(); i++) {
            if (vacCenter.getVaccineQty() == 0)
                break;
        vaccineStack.add(vacCenter.usedVaccine());
        }
    }

    private static void printRecipients() {
        System.out.print("\nAssigned recipients and their age");
        if (recipients.size() == 0) {
            System.out.println("\nEmpty");
            return;
        }

        int numOfBoxes = recipients.size();
        drawLine(numOfBoxes, 12);
        for (int i = 0; i < recipients.size(); i++) {
            String name = recipients.get(i).getName();
            String row = String.format("| %-10s ", name);
            System.out.print(row);
        }
        System.out.println("|");
        for (int i = 0; i < recipients.size(); i++) {
            int age = recipients.get(i).getAge();
            String row = String.format("| %-10d ", age);
            System.out.print(row);
        }
        System.out.print("|");
        drawLine(numOfBoxes, 12);
    }

    private static void printVaccines() {
        System.out.print("\nStack of vaccines");
        if (vaccineStack.size() == 0) {
            System.out.println("\nEmpty");
            return;
        }

        int numOfBoxes = vaccineStack.size();
        drawLine(numOfBoxes, 7);
        for (int i = 0; i < vaccineStack.size(); i++) {
            int vaccineBatchNum = vaccineStack.get(i);
            String row = String.format("| %-5d ", vaccineBatchNum);
            System.out.print(row);
        }
        System.out.print("|");
        drawLine(numOfBoxes, 7);
    }

    private static void drawLine(int numOfBoxes, int numOfSpaces) {
        System.out.println();
        for (int i = 0; i < numOfBoxes; i++) {
            System.out.print("+");
            for (int j = 0; j < numOfSpaces; j++)
                System.out.print("-");
        }
        System.out.println("+");
    }

    private static void assignRecipientsToQueue() {
        for (int i = 0; i < recipients.size(); i++) {
            Recipient recipient = recipients.get(i);
            if (recipient.getAge() >= 60)
                seniorQueue.add(recipient);
            else
                normalQueue.add(recipient);
        }
    }

    private static void printSeniorQueue() {
        System.out.print("\nSenior Queue");
        if (seniorQueue.size() == 0) {
            System.out.println("\nEmpty");
            return;
        }

        int numOfBoxes = seniorQueue.size();
        drawLine(numOfBoxes, 12);
        for (int i = 0; i < seniorQueue.size(); i++) {
            String name = seniorQueue.get(i).getName();
            String row = String.format("| %-10s ", name);
            System.out.print(row);
        }
        System.out.println("|");
        for (int i = 0; i < seniorQueue.size(); i++) {
            int age = seniorQueue.get(i).getAge();
            String row = String.format("| %-10d ", age);
            System.out.print(row);
        }
        System.out.print("|");
        drawLine(numOfBoxes, 12);
    }

    private static void printNormalQueue() {
        System.out.print("\nNormal Queue");
        if (normalQueue.size() == 0) {
            System.out.println("\nEmpty");
            return;
        }

        int numOfBoxes = normalQueue.size();
        drawLine(numOfBoxes, 12);
        for (int i = 0; i < normalQueue.size(); i++) {
            String name = normalQueue.get(i).getName();
            String row = String.format("| %-10s ", name);
            System.out.print(row);
        }
        System.out.println("|");
        for (int i = 0; i < normalQueue.size(); i++) {
            int age = normalQueue.get(i).getAge();
            String row = String.format("| %-10d ", age);
            System.out.print(row);
        }
        System.out.print("|");
        drawLine(numOfBoxes, 12);
    }

    private static void printVaccinatedRecipients() {
        System.out.print("\nVaccinated Recipients");
        if (vaccinatedRecipients.size() == 0) {
            System.out.println("\nEmpty");
            return;
        }

        int numOfBoxes = vaccinatedRecipients.size();
        drawLine(numOfBoxes, 12);
        for (int i = 0; i < vaccinatedRecipients.size(); i++) {
            int vaccineBatchNum = vaccinatedRecipients.get(i).getFirstDoseBatch();
            if (vaccinatedRecipients.get(i).getStatusIndex() == 4)
                vaccineBatchNum = vaccinatedRecipients.get(i).getSecondDoseBatch();

            String row = String.format("| %-10d ", vaccineBatchNum);
            System.out.print(row);
        }
        System.out.println("|");
        for (int i = 0; i < vaccinatedRecipients.size(); i++) {
            String name = vaccinatedRecipients.get(i).getName();
            String row = String.format("| %-10s ", name);
            System.out.print(row);
        }
        System.out.println("|");
        for (int i = 0; i < vaccinatedRecipients.size(); i++) {
            int age = vaccinatedRecipients.get(i).getAge();
            String row = String.format("| %-10d ", age);
            System.out.print(row);
        }
        System.out.print("|");
        drawLine(numOfBoxes, 12);
    }

    private static void vaccination() {
        if (seniorQueue.size() != 0 && vaccineStack.size() != 0) {
            Recipient seniorRecipient = seniorQueue.removeFirst();
            if (seniorRecipient.getStatusIndex() == 1)
                seniorRecipient.setFirstDoseBatch(vaccineStack.removeFirst());
            else
                seniorRecipient.setSecondDoseBatch(vaccineStack.removeFirst());
            seniorRecipient.updateStatus();
            vaccinatedRecipients.add(seniorRecipient);
        }

        if (normalQueue.size() != 0 && vaccineStack.size() != 0) {
            Recipient recipient = normalQueue.removeFirst();
            if (recipient.getStatusIndex() == 1)
                recipient.setFirstDoseBatch(vaccineStack.removeFirst());
            else
                recipient.setSecondDoseBatch(vaccineStack.removeFirst());
            recipient.updateStatus();
            vaccinatedRecipients.add(recipient);
        }
    }
}
