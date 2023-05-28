import java.util.Scanner;
import java.util.LinkedHashMap;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.time.LocalDate;

public class VCMenu extends Menu {
    static int vcIndex;

    protected static void vcMenu1() {
        System.out.println("\n+-------------------------------------------+");
        System.out.println("|         Vaccination Center Menu           |");
        System.out.println("+-------------------------------------------+");
        System.out.println("| 1 - View recipients' data & status        |");
        System.out.println("| 2 - Set appointment date for recipient(s) |");
        System.out.println("| 3 - Update a recipient's status           |");
        System.out.println("| 4 - View statistics of this center        |");
        System.out.println("| 0 - Back                                  |");
        System.out.println("+-------------------------------------------+");
        int choice = getInput(DEFAULT_QUESTION, 4);

        switch (choice) {
            case 1 :
                viewRecipientUnderVC();
                pressEnterToContinue();
                vcMenu1();
                break;
            case 2 :
                try {
                    System.out.println("\nYou're currently managing recipient(s)'s appointment date.");
                    setRecipientsApmtDate();
                } 
                catch (IllegalArgumentException ex) {
                    System.out.println("\n" + ex.getMessage());
                    System.out.println("\nRedirecting to Vaccination Center menu...");
                } 
                catch (InputMismatchException ex) {
                    System.out.println("\nYou've entered an invalid input causing an Input Mismatch Exception.");
                    System.out.println("\nRedirecting to Vaccination Center menu...");
                }
                pressEnterToContinue();
                vcMenu1();
                break;
            case 3 :
                try {
                    System.out.println("\nYou're currently updating a recipient's status.");
                    int userIndex = inputRecipient();
                    updateRcpStatus(userIndex);
                }
                catch (IllegalArgumentException ex) {
                    System.out.println("\n" + ex.getMessage());
                    System.out.println("\nRedirecting to Vaccination Center menu...");
                }
                pressEnterToContinue();
                vcMenu1();
                break;
            case 4 :
                viewVCStats();
                pressEnterToContinue();
                vcMenu1();
                break;
            case 0 :
                System.out.println("\nRedirecting to Main Menu...");
                pressEnterToContinue();
                mainMenu();
                break;
        }
    }

    private static void viewRecipientUnderVC() {
        System.out.println("\n-------------------------------------------------------");
        System.out.println("                    Recipients List");
        System.out.println("-------------------------------------------------------");
        VC vacCenter = VCList.vacCenters.get(vcIndex);
        for (int i = 0; i < vacCenter.getRecipients().size(); i++) {
            Recipient recipient = vacCenter.getRecipient(i);

            if (recipient.getFirstApmtDate() == null) {
                System.out.println(i+1 + ". " + recipient.toString() + ", " + "Date unassigned");
            }
            else {
                if (recipient.getStatusIndex() == 1 || recipient.getStatusIndex() == 2) {
                    LocalDate firstAppointmentDate = recipient.getFirstApmtDate();

                    System.out.println(i+1 + ". " + recipient.toString() + ", " + dateTimeForm.format(firstAppointmentDate));
                }
                else if (recipient.getStatusIndex() == 3 || recipient.getStatusIndex() == 4) {
                    LocalDate secondAppointmentDate = recipient.getSecondApmtDate();

                    System.out.println(i+1 + ". " + recipient.toString() + ", " + dateTimeForm.format(secondAppointmentDate));
                }
            }
        }
        System.out.println("-------------------------------------------------------");
    }

    private static int inputRecipient() {
        Scanner input = new Scanner(System.in);

        System.out.print("\nPlease enter recipient's name: ");
        String name = input.nextLine();
        System.out.print("Please enter recipient's phone number: ");
        String phone = input.nextLine();

        int userIndex = verifyRecipient(name, phone);

        if (userIndex == -99)
            throw new IllegalArgumentException("Account not found, please try again.");

        return userIndex;
    }

    private static int verifyRecipient(String name, String phone) {
        int userIndex = -99;
        VC vacCentre = VCList.vacCenters.get(vcIndex);

        for (int i = 0; i < vacCentre.getRecipients().size(); i++) {
            Recipient recipient = vacCentre.getRecipient(i);

            if (name.equals(recipient.getName()) && phone.equals(recipient.getPhone())) {
                userIndex = i;
                System.out.println("\n**ACCOUNT FOUND**");
                break;
            }
        }
        return userIndex;
    }

    private static void setRecipientsApmtDate() {
        Scanner input = new Scanner(System.in);
        VC vacCenter = VCList.vacCenters.get(vcIndex);
        ArrayList<Recipient> recipients = vacCenter.getRecipients();

        viewRecipientUnderVC();
        System.out.print("\nPlease enter the start number: ");
        int firstUserIndex = input.nextInt();
        System.out.print("\nPlease enter the end number: ");
        int lastUserIndex = input.nextInt();
        int userQuantity = (lastUserIndex - firstUserIndex) + 1;

        if(firstUserIndex > 0 && lastUserIndex > 0 && userQuantity <= vacCenter.getCapacityPerDay() && firstUserIndex <= lastUserIndex &&
           firstUserIndex <= recipients.size() && lastUserIndex <= recipients.size())
            setAppointmentsDate(firstUserIndex, lastUserIndex);
        else if (firstUserIndex > lastUserIndex) {
            System.out.println("\nThe start number should be less than or equal to the end number");
        }
        else if (firstUserIndex < 0 || lastUserIndex < 0 || firstUserIndex > recipients.size() || lastUserIndex > recipients.size()) {
            System.out.println("\nYou've entered invalid recipient indexes");
        }
        else
            System.out.println("\nUsers selected have exceeded the capacity per day of the Vaccination Center.");
    }

    private static void setAppointmentsDate(int firstUserIndex, int lastUserIndex) {
        Scanner input = new Scanner(System.in);
        System.out.println("\nYou are currently setting the appointment date for :");

        for(int i = firstUserIndex - 1; i < lastUserIndex; i++) {
            Recipient recipient = VCList.vacCenters.get(vcIndex).getRecipient(i);
            System.out.print("\n" + recipient.getName());
        }
        System.out.println("\n\n(-99 to exit setting)");

        System.out.print("\nPlease enter the desired year between 2020 and 2021: ");
        int tempYear = input.nextInt();
        if (tempYear == -99)
            return;
        System.out.print("Please enter the desired month (1-12): ");
        int tempMonth = input.nextInt();
        if (tempMonth == -99)
            return;
        System.out.print("Please enter the desired day (1-31): ");
        int tempDay = input.nextInt();
        if (tempDay == -99)
            return;

        if (tempYear < 2020 || tempYear  > 2021)
            throw new IllegalArgumentException("You've entered an invalid year, the appointment date is not set.");
        if (tempMonth < 1 || tempMonth > 12)
            throw new IllegalArgumentException("You've entered an invalid month, the appointment date is not set.");
        if (tempDay < 1 || tempDay > 31)
            throw new IllegalArgumentException("You've entered an invalid day, the appointment date is not set.");
        else {
            if ((tempMonth == 4 || tempMonth == 6 || tempMonth == 9 || tempMonth == 11) && tempDay == 31)
                throw new IllegalArgumentException("You've entered an invalid day, the appointment date is not set.");
            if (tempMonth == 2 && tempDay > 28)
                throw new IllegalArgumentException("You've entered an invalid day, the appointment date is not set.");
        }

        LocalDate appointmentDate = LocalDate.of (tempYear, tempMonth, tempDay);

        for(int i = firstUserIndex - 1; i < lastUserIndex; i++) {

            Recipient recipient = VCList.vacCenters.get(vcIndex).getRecipient(i);
            if (recipient.getStatusIndex() == 1) {
                recipient.setFirstApmtDate(tempYear, tempMonth, tempDay);
            }
            else if (recipient.getStatusIndex() == 2) {
                System.out.println("\n" + recipient.getName() +
                                    "'s appointment date failed to be updated as he/she has just completed 1st dose.");
            }
            else if (recipient.getStatusIndex() == 3) {
                recipient.setSecondApmtDate(tempYear, tempMonth, tempDay);
            }
            else if (recipient.getStatusIndex() == 4) {
                System.out.println("\n" + recipient.getName() +
                                    "'s appointment date failed to be updated as he/she is already fully vaccinated.");
            }
            else {
                System.out.println("\n" + recipient.getName() +
                                    "'s appointment date failed to be updated as his/her status is still currently pending.");
            }
        }
        System.out.println("\n**UPDATE SUCCESSFUL**");
        System.out.println("\nExcept the Invalid Appointment Date Recipients\n"+
                            "All Recipients's appointment date has been successfully updated to " + dateTimeForm.format(appointmentDate) + ".");

    }

    private static void updateRcpStatus(int userIndex) {
        VC vacCentre = VCList.vacCenters.get(vcIndex);
        int vaccineQty = vacCentre.getVaccineQty();

        Recipient recipient = vacCentre.getRecipient(userIndex);
        int userStatus = recipient.getStatusIndex();
        String userStatusString = recipient.getStatus();

        if (userStatus < 4) {
            if (userStatus == 1 && recipient.getFirstApmtDate() != null &&
                recipient.getFirstApmtDate().compareTo(LocalDate.now()) <= 0)
            {
                if (vaccineQty == 0) {
                    System.out.println("\nRecipient's status failed to be updated as vaccination center is currently out of vaccine.");
                    return;
                }
                recipient.setFirstDoseBatch(vacCentre.usedVaccine());
            }
            else if (userStatus == 3 && recipient.getSecondApmtDate() != null &&
                     recipient.getSecondApmtDate().compareTo(LocalDate.now()) <= 0)
            {
                if (vaccineQty == 0) {
                    System.out.println("\nRecipient's status failed to be updated as vaccination center is currently out of vaccine.");
                    return;
                }
                recipient.setSecondDoseBatch(vacCentre.usedVaccine());
            }
            else if (userStatus == 1 && recipient.getFirstApmtDate() == null ||
                     userStatus == 3 && recipient.getSecondApmtDate() == null)
            {
                System.out.println("\nRecipient's status failed to be updated as his/her appointment date hasn't been updated.");
                return;
            }
            else if (userStatus == 1 && recipient.getFirstApmtDate().compareTo(LocalDate.now()) > 0 ||
                     userStatus == 3 && recipient.getSecondApmtDate().compareTo(LocalDate.now()) > 0)
            {
                System.out.println("\nRecipient's status failed to be updated as his/her appointment date is beyond current date.");
                return;
            }
            recipient.updateStatus();
            System.out.println("\n**UPDATE SUCCESSFUL**");
            System.out.println("Recipient's status has been updated from " + userStatusString + " to " + recipient.getStatus() + ".");
        }
        else
            System.out.println("Recipient status failed to be updated as the recipient is already fully vaccinated.");
    }

    private static void viewVCStats() {
        int totalPending = 0;
        int totalFirstDoseAppointments = 0;
        int totalFirstDoseCompletions = 0;
        int totalSecondDoseAppointments = 0;
        int totalSecondDoseCompletions = 0;
        LinkedHashMap<LocalDate, Integer> apmtsOnDays = new LinkedHashMap<LocalDate, Integer>();
        int totalVaccination = 0;
        VC vacCentre = VCList.vacCenters.get(vcIndex);

        for (int i = 0; i < vacCentre.getRecipients().size(); i++) {
            Recipient recipient = vacCentre.getRecipient(i);
            int statusIndex = recipient.getStatusIndex();

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

            if (recipient.getStatusIndex() == 2 || recipient.getStatusIndex() == 3)
                totalVaccination += 1;
            else if (recipient.getStatusIndex() == 4)
                totalVaccination += 2;
        }

        for (LocalDate date = LocalDate.now(); date.compareTo(LocalDate.of(2020, 1, 1)) >= 0; date = date.minusDays(1)) {
            int numOfApmtsOnDay = 1;
            for (int i = 0; i < vacCentre.getRecipients().size(); i++) {
                Recipient recipient = vacCentre.getRecipient(i);
                if (recipient.getFirstApmtDate() != null)
                    if (recipient.getStatusIndex() > 1 && date.equals(recipient.getFirstApmtDate()))
                        apmtsOnDays.put(date, numOfApmtsOnDay++);
                if (recipient.getSecondApmtDate() != null)
                    if (recipient.getStatusIndex() > 3 && date.equals(recipient.getSecondApmtDate()))
                        apmtsOnDays.put(date, numOfApmtsOnDay++);
            }
        }

        System.out.println("\n------------------------------------------------------");
        System.out.println("           Statistics of " + vacCentre.getName());
        System.out.println("------------------------------------------------------");
        System.out.println("Capacity Per Day = " + vacCentre.getCapacityPerDay());
        System.out.println("Vaccine Quantity = " + vacCentre.getVaccineQty());
        System.out.println("------------------------------------------------------");
        System.out.println("Total Pending Recipients = " + totalPending);
        System.out.println("Total First Dose Appointments = " + totalFirstDoseAppointments);
        System.out.println("Total First Dose Completions = " + totalFirstDoseCompletions);
        System.out.println("Total Second Dose Appointments = " + totalSecondDoseAppointments);
        System.out.println("Total Second Dose Completions = " + totalSecondDoseCompletions);
        System.out.println("Total Vaccinations = " + totalVaccination);
        System.out.println("------------------------------------------------------");
        for (LocalDate date = LocalDate.now(); date.compareTo(LocalDate.of(2020, 1, 1)) >= 0; date = date.minusDays(1)) {
            if (apmtsOnDays.containsKey(date))
                System.out.println("Total Vaccinations on " + dateTimeForm.format(date) + " = " + apmtsOnDays.get(date));
        }
        System.out.println("------------------------------------------------------");
    }
}