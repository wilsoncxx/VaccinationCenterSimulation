import java.util.ArrayList;

/**
 * A vaccination center represents an agency administering vaccine doses to recipients.
 */
public class VC {
    /**
     * @author Cho Xuan Xian & Fam Jia Wen
     * @version 1.0
     */
    private String name;
    private String password;
    private static int vaccineBatchNum;
    private int capacityPerDay;
    private int vaccineQty = 0;
    private ArrayList<Recipient> recipients = new ArrayList<>();

    /** 
     * Constructs a vaccination center with default data.
     */
    public VC() {}

    /** 
     * Constructs a vaccination center with the specified name, password, capacity per day, and the quantity of vaccine.
     *
     * @param name the name of the vaccination center
     * @param password the password of the vaccination center
     * @param capacityPerDay the maximum number of recipients that can be handled by the specified vaccination center per day
     * @param vaccineQty the quantity of vaccines in the specified vaccination center
     * @param vaccineBatchNum the batch number of vaccines
     */
    public VC(String name, String password, int capacityPerDay, int vaccineQty, int vaccineBatchNum) {
        this.name = name;
        this.password = password;
        this.capacityPerDay = capacityPerDay;
        this.vaccineQty = vaccineQty;
        VC.vaccineBatchNum = vaccineBatchNum;
    }

    /**
     * Returns the name of the specified vaccination center.
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the password of the specified vaccination center.
     * @return password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Returns the maximum number of recipients that can be handled by the specified vaccination center per day.
     * @return capacityPerDay
     */
    public int getCapacityPerDay() {
        return capacityPerDay;
    }

    /**
     * Returns the quantity of vaccines in the specified vaccination center.
     * @return vaccineQty
     */
    public int getVaccineQty() {
        return vaccineQty;
    }

    /**
     * Returns recipient of index(i) in the recipients arraylist of the specified vaccination center.
     * 
     * @param i index of the recipient in the recipients arraylist of the specified vaccination center
     * @return recipients(i)
     */
    public Recipient getRecipient(int i) {
        return recipients.get(i);
    }

    /**
     * Returns the recipients arraylist of the specified vaccination center.
     * 
     * @return recipients arraylist
     */
    public ArrayList<Recipient> getRecipients() {
        return recipients;
    }

    /**
     * Deduct the quantity of vaccines in the specified vaccination center by 1 and increment the vaccine batch number by 1.
     * 
     * @return vaccineBatchNum
     */
    public int usedVaccine() {
        vaccineQty -= 1;
        return vaccineBatchNum++;
    }

    /**
     * Add the quantity of vaccines in the specified vaccination center by the value passed in.
     * @param vaccineQty the quantity of vaccines in the specified vaccination center
     */
    public void addVaccine(int vaccineQty){
        this.vaccineQty = this.vaccineQty + vaccineQty;
    }

    /**
     * Add a recipient into the recipients arraylist of the specified vaccination center.
     * @param recipient the recipient that assigned to this vaccination center
     */
    public void addNewRecipient(Recipient recipient) {
        recipients.add(recipient);
    }

    /**
     * Returns a string representation of the name, password, capacity per day, and quantity of vaccine of this vaccination center.
     * @return a string representation of this vaccination center
     */
    @Override
    public String toString() {
        return name + ", " + password + ", " + capacityPerDay + ", " + vaccineQty;
    }

    /**
     * Returns a seperated-by-comma string representation of the name, password, capacity per day, and quantity of vaccine of this vaccination center.
     * @return a seperated-by-comma string representation of this vaccination center with all data
     */
    public String toCSVString() {
        return name + "," + password + "," + capacityPerDay + "," + vaccineQty + "," + vaccineBatchNum;
    }
}
