import java.util.Scanner;
import java.time.LocalDate;

public class RecipientMenu extends Menu {
    protected static void recipientMenu() {
        System.out.println("\n+----------------------------------------+");
        System.out.println("|             Recipient Menu             |");
        System.out.println("+----------------------------------------+");
        System.out.println("| 1 - Register a new account             |");
        System.out.println("| 2 - Sign in to check your status       |");
        System.out.println("| 0 - Back                               |");
        System.out.println("+----------------------------------------+");
        int choice = getInput(DEFAULT_QUESTION, 2);

        switch (choice) {
            case 1 : 
                registration();
                pressEnterToContinue();
                recipientMenu();
                break;
            case 2 :
                try{
                    loginToViewStatus();
                } catch (IllegalArgumentException ex) {
                    System.out.println("\n" + ex.getMessage());
                }
                pressEnterToContinue();
                recipientMenu();
                break;
            case 0 :
                System.out.println("\nRedirecting to Main Menu...");
                pressEnterToContinue();
                mainMenu();
                break;
        }
    }

    private static void registration() {
        Scanner input = new Scanner(System.in);

        System.out.println("\n------REGISTRATION------");
        System.out.print("Please enter your full name: ");
        String name = input.nextLine().trim();                      // get rid of all spaces before words
        String name_SpacesRemoved = name.replaceAll("\\s+", " ");   // replace all spaces with one single space

        System.out.print("Please enter your phone number: ");
        String phone = input.nextLine().trim();                     // get rid of all spaces before words
        String phone_SpacesRemoved = phone.replaceAll("\\s+", "");  // delete all spaces

        System.out.println("Please enter your age: ");
        int age = input.nextInt();

        boolean validName = checkName(name_SpacesRemoved);
        boolean validPhone = checkPhone(phone_SpacesRemoved);
        boolean validAge = checkAge(age);
        boolean phoneRegistered = checkPhoneRegistered(phone_SpacesRemoved);

        try {
            if (validName && validPhone && validAge && !phoneRegistered) {
                RecipientsList.recipients.add(new Recipient(name_SpacesRemoved, phone_SpacesRemoved, age));
                System.out.println("\n**REGISTRATION SUCCESSFUL**");
                System.out.println("Your account has been created.");
            }
            else if (phoneRegistered) {
                throw new IllegalArgumentException("\nRegistration failed, phone has already been used.");
            }
            else if (!validName && !validPhone) {
                throw new IllegalArgumentException("\nRegistration failed, invalid name and phone number.\n" +
                                                   "Example of valid name: Steven Tan Chung Hong\n" +
                                                   "Example of valid phone: 0123456789");
            }
            else if (!validName) {
                throw new IllegalArgumentException("\nRegistration failed, invalid name.\n" +
                                                   "Example of valid name: Steven Tan Chung Hong");
            }
            else if (!validPhone){
                throw new IllegalArgumentException("\nRegistration failed, invalid phone number.\n" +
                                                   "Example of valid phone: 0123456789");
            }
            else {
                throw new IllegalArgumentException("\nRegistration failed, invalid age.\n" +
                                                   "Please enter an age between 0 and 122.");
            }
        } catch (IllegalArgumentException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private static boolean checkName(String name) {
        return name.matches("^[a-zA-Z ]+$");
    }

    private static boolean checkPhone(String phone) {
        return phone.matches("[0][1-9][0-9]{8,9}");
    }

    private static boolean checkAge(int age) {
        return age >= 0 && age <= 122;
    }

    private static boolean checkPhoneRegistered(String phone) {
        boolean phoneRegistered;
        for (int i = 0; i < RecipientsList.recipients.size(); i++) {
            phoneRegistered = phone.equals(RecipientsList.recipients.get(i).getPhone());
            if (phoneRegistered)
                return true;        // this phone number already registered
        }
        return false;               // this phone number has not yet been registered before
    }

    private static void loginToViewStatus() {
        Scanner input = new Scanner(System.in);

        System.out.println("\n-----Sign in to view status-----");
        System.out.print("Please enter your name: ");
        String name = input.nextLine();
        System.out.print("Please enter your phone number: ");
        String phone = input.nextLine();

        int userIndex = verifyAcc(name, phone);
        Recipient recipient = RecipientsList.recipients.get(userIndex);

        if (recipient.getAssignedToVC() == 0) {
            System.out.println("\nHello, " + recipient.getName() + ".\n" +
                               "Your current status: " + recipient.getStatus());
        }
        else {
            for (int i = 0; i < VCList.vacCenters.size(); i++) {
                if (i == recipient.getAssignedToVC() - 1) {
                    System.out.println("\nHello, " + recipient.getName() + ".\n" +
                                        "Your current status: " + recipient.getStatus() +
                                        " at " + VCList.vacCenters.get(i).getName());
                    if (recipient.getStatusIndex() >= 1) {
                        LocalDate appointmentDate = recipient.getFirstApmtDate();
                        if (recipient.getStatusIndex() == 1)
                            System.out.print("\n1st Dose Appointment - ");
                        else
                            System.out.print("\n1st Dose Completed - ");
                        
                        if (recipient.getFirstApmtDate() != null)
                            System.out.println(dateTimeForm.format(appointmentDate));
                        else
                            System.out.println("Date haven't been assigned");
                    }
                    if (recipient.getStatusIndex() >= 2)
                        System.out.println("Batch: " + recipient.getFirstDoseBatch());

                    if (recipient.getStatusIndex() >= 3) {
                        LocalDate appointmentDate = recipient.getSecondApmtDate();
                        if (recipient.getSecondApmtDate() != null)
                            System.out.println(recipient.getStatus() + " - " + dateTimeForm.format(appointmentDate));
                        else
                            System.out.println(recipient.getStatus() + " - Date haven't been assigned");
                    }
                    if (recipient.getStatusIndex() == 4) 
                        System.out.println("Batch: " + recipient.getSecondDoseBatch());
                }
            }
        }
    }

    private static int verifyAcc(String name, String phone) {
        int userIndex = -99;

        for (int i = 0; i < RecipientsList.recipients.size(); i++)
            if (name.equals(RecipientsList.recipients.get(i).getName()) && phone.equals(RecipientsList.recipients.get(i).getPhone())) {
                userIndex = i;
                break;
            }

        if (userIndex == -99)
            throw new IllegalArgumentException("Account not found, please try again.");

        return userIndex;
    }
}
