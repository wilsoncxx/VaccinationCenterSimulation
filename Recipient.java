import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * A recipient represents an individual receiving the vaccination appointment and dose.
 */
public class Recipient implements Comparable<Recipient> {
    /**
     * @author Cho Xuan Xian & Steven Tan Chung Hong
     * @version 1.0
     */
    private static final DateTimeFormatter dateTimeForm = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private int assignedToVC = 0;       // 0 indicates recipient has not been assigned, 1 indicates recipient has been assigned to VC1, etc...
    private String name;
    private String phone;
    private int age = 0;
    private int firstDoseBatch = 0;
    private int secondDoseBatch = 0;
    private int statusIndex = 0;
    private LocalDate firstApmtDate;
    private LocalDate secondApmtDate;
    private String status[] = {"Pending", "1st Dose appointment", "1st Dose completed",
                                          "2nd Dose appointment", "2nd Dose completed"};
    // data fields below are for table viewing purpose
    private String assignedToVCString = "Unassigned";
    private String formattedFirstApmtDate = "Unassigned";
    private String formattedSecondApmtDate = "Unassigned";
    private String currentStatus;

    /** 
     * Constructs a recipient with default data.
     */
    public Recipient() {}

    /** 
     * Constructs a recipient with the specified name and phone.
     *
     * @param name the name of the recipient
     * @param phone the phone number of the recipient
     */
    public Recipient(String name, String phone, int age) {
        this.name = name;
        this.phone = phone;
        this.age = age;
    }
    
    /** 
     * Constructs a recipient with the specified arguments.
     *
     * @param assignedToVC the index of the Vaccination Center which the recipient is assigned to
     * @param name the name of the recipient
     * @param phone the phone number of the recipient
     * @param age the age of the recipient
     * @param firstDoseDateYear the year of the first appointment date of the recipient
     * @param firstDoseDateMonth the month of the first appointment date of the recipient
     * @param firstDoseDateDay the day of the first appointment date of the recipient
     * @param firstDoseBatch the batch number of first vaccine the recipient received
     * @param secondDoseDateYear the year of the second appointment date of the recipient
     * @param secondDoseDateMonth the month of the second appointment date of the recipient
     * @param secondDoseDateDay the day of the second appointment date of the recipient
     * @param secondDoseBatch the batch number of second vaccine the recipient received
     * @param statusIndex the index representing the recipient's status
     */
    public Recipient(int assignedToVC, String name, String phone, int age, int statusIndex,
                     int firstDoseDateYear, int firstDoseDateMonth, int firstDoseDateDay, int firstDoseBatch,
                     int secondDoseDateYear, int secondDoseDateMonth, int secondDoseDateDay, int secondDoseBatch)
    {
        this.assignedToVC = assignedToVC;
        this.name = name;
        this.phone = phone;
        this.firstDoseBatch = firstDoseBatch;
        this.secondDoseBatch = secondDoseBatch;
        this.statusIndex = statusIndex;
        if (firstDoseDateDay != 0)
            firstApmtDate = LocalDate.of(firstDoseDateYear, firstDoseDateMonth, firstDoseDateDay);
        if(secondDoseDateDay != 0)
            secondApmtDate = LocalDate.of(secondDoseDateYear, secondDoseDateMonth, secondDoseDateDay);
        this.age = age;
        setFormattedLatestApmtDate();
        setCurrentStatus();
        setAssignedToVCString();
    }

    /**
     * Returns the name of the specified recipient.
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the phone number of the specified recipient.
     * @return phone
     */
    public String getPhone() {
        return phone;
    }

    /**
     * Returns the age of the specified recipient.
     * @return age
     */
    public int getAge() {
        return age;
    }

    /**
     * Returns the status index of the specified recipient.
     * @return statusIndex
     */
    public int getStatusIndex() {
        return statusIndex;
    }

    /**
     * Returns the status of the specified recipient based on his status index.
     * @return status based on status index
     */
    public String getStatus() {
        return status[statusIndex];
    }

    public String getCurrentStatus() {
        return currentStatus;
    }

    /**
     * Returns the index of the Vaccination Center which the specified recipient is assigned to.
     * @return assignedToVC
     */
    public int getAssignedToVC() {
        return assignedToVC;
    }

    public String getAssignedToVCString() {
        return assignedToVCString;
    }

    /**
     * Returns the first appointment date of the specified recipient.
     * @return firstApmtDate
     */
    public LocalDate getFirstApmtDate() {
        return firstApmtDate;
    }

    /**
     * Returns the second appointment date of the specified recipient.
     * @return secondApmtDate
     */
    public LocalDate getSecondApmtDate() {
        return secondApmtDate;
    }

    public String getFormattedFirstApmtDate() {
        return formattedFirstApmtDate;
    }

    public String getFormattedSecondApmtDate() {
        return formattedSecondApmtDate;
    }

    /**
     * Returns the batch number of this specified recipient's first dose vaccine.
     * @return firstDoseBatch
     */
    public int getFirstDoseBatch() {
        return firstDoseBatch;
    }

    /**
     * Returns the batch number of this specified recipient's second dose vaccine.
     * @return secondDoseBatch
     */
    public int getSecondDoseBatch() {
        return secondDoseBatch;
    }

    /**
     * Change the index of the vaccination center which the specified is assigned to.
     * 
     * @param assignedToVC the index of the Vaccination Center which the recipient is assigned to
     */
    public void setAssignedToVC(int assignedToVC) {
        this.assignedToVC = assignedToVC;
        setAssignedToVCString();
    }

    private void setAssignedToVCString() {
        if (assignedToVC != 0)
            assignedToVCString = VCList.vacCenters.get(assignedToVC - 1).getName();
    }

    private void setCurrentStatus() {
        currentStatus = status[statusIndex];
    }
    
    /**
     * Increase the status index by 1 if the current status index is lower than 4.
     */
    public void updateStatus() {
        if (statusIndex < 4) {
            statusIndex += 1;
            setCurrentStatus();
        }
    }

    private void setFormattedLatestApmtDate() {
        if (firstApmtDate != null)
            formattedFirstApmtDate = dateTimeForm.format(firstApmtDate);
        if (secondApmtDate != null)
            formattedSecondApmtDate = dateTimeForm.format(secondApmtDate);
    }

    /**
     * Change the first appointment date of the specified recipient.
     * 
     * @param year the year of the first appointment date
     * @param month the month of the first appointment date
     * @param day the day of the first appointment date
     */
    public void setFirstApmtDate(int year, int month, int day) {
        firstApmtDate = LocalDate.of(year, month, day);
        setFormattedLatestApmtDate();
    }

    /**
     * Change the second appointment date of the specified recipient.
     * 
     * @param year the year of the first appointment date
     * @param month the month of the first appointment date
     * @param day the day of the first appointment date
     */
    public void setSecondApmtDate(int year, int month, int day) {
        secondApmtDate = LocalDate.of(year, month, day);
        setFormattedLatestApmtDate();
    }

    /**
     * Set the first dose vaccine's batch number of the specified recipient.
     * 
     * @param firstDoseBatch the batch number of the first dose vaccine of the recipient
     */
    public void setFirstDoseBatch(int firstDoseBatch) {
        this.firstDoseBatch = firstDoseBatch;
    }

    /**
     * Set the second dose vaccine's batch number of the specified recipient.
     * 
     * @param secondDoseBatch the batch number of the second dose vaccine of the recipient
     */
    public void setSecondDoseBatch(int secondDoseBatch) {
        this.secondDoseBatch = secondDoseBatch;
    }

    /**
     * Compares this recipient with the specified recipient for order based on the indexes of their assigned vaccination center.
     * 
     * @param o the specified recipient to be compared
     * @return a negative integer, zero, or a positive integer as this recipient's vaccination center index is less than,
     *         equal to, or greater than the specified recipient's vaccination center index.
     */
    @Override
    public int compareTo(Recipient o) {
        return this.getAssignedToVC() - o.assignedToVC;
    }

    /**
     * Returns a string representation of the name, phone number and status of the specified recipient.
     * @return a string representation of the specified recipient
     */
    @Override
    public String toString() {
        return name + ", " + phone + ", " + age + ", " + status[statusIndex];
    }

    /**
     * Returns a seperated-by-comma string representation of the name, phone number, status and appointment date of this recipient.
     * @return a seperated-by-comma string representation of this recipient
     */
    public String toCSVString() {
        int firstApmtDay, firstApmtMonth, firstApmtYear;
        int secondApmtDay, secondApmtMonth, secondApmtYear;
        if (firstApmtDate != null) {
            firstApmtDay = firstApmtDate.getDayOfMonth();
            firstApmtMonth = firstApmtDate.getMonthValue();
            firstApmtYear = firstApmtDate.getYear();
        }
        else {
            firstApmtDay = 0;
            firstApmtMonth = 0;
            firstApmtYear = 0;
        }

        if (secondApmtDate != null) {
            secondApmtDay = secondApmtDate.getDayOfMonth();
            secondApmtMonth = secondApmtDate.getMonthValue();
            secondApmtYear = secondApmtDate.getYear();
        }
        else {
            secondApmtDay = 0;
            secondApmtMonth = 0;
            secondApmtYear = 0;
        }

        return assignedToVC + "," + name + "," + phone + "," + age + "," + status[statusIndex] + "," + 
               firstApmtDay + "," + firstApmtMonth + "," + firstApmtYear + "," +
               secondApmtDay + "," + secondApmtMonth + "," + secondApmtYear + "," +
               firstDoseBatch + "," + secondDoseBatch;
    }
}
