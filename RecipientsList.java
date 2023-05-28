import java.util.ArrayList;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.nio.file.Files;
import java.util.Collections;

public class RecipientsList {
    static ArrayList<Recipient> recipients = new ArrayList<>();
    private static final String RECIPIENTS_PATH = "Recipients.csv";

    public static void readRecipientsFromFile() throws IOException {
        try{
            List<String> lines = Files.readAllLines(Paths.get(RECIPIENTS_PATH));
            for (int i = 0; i < lines.size(); i++){
                String[] items = lines.get(i).split(",");   // data seperated by comma
                int assignedToVC = Integer.parseInt(items[0]);
                int age = Integer.parseInt(items[3]);
                int firstDoseDateDay = Integer.parseInt(items[5]);
                int firstDoseDateMonth = Integer.parseInt(items[6]);
                int firstDoseDateYear = Integer.parseInt(items[7]);
                int secondDoseDateDay = Integer.parseInt(items[8]);
                int secondDoseDateMonth = Integer.parseInt(items[9]);
                int secondDoseDateYear = Integer.parseInt(items[10]);
                int firstDoseBatch = Integer.parseInt(items[11]);
                int secondDoseBatch = Integer.parseInt(items[12]);
                int statusIndex;

                if (items[4].equals("2nd Dose completed"))
                    statusIndex = 4;
                else if (items[4].equals("2nd Dose appointment"))
                    statusIndex = 3;
                else if (items[4].equals("1st Dose completed"))
                    statusIndex = 2;
                else if (items[4].equals("1st Dose appointment"))
                    statusIndex = 1;
                else
                    statusIndex = 0;

                recipients.add(new Recipient(assignedToVC,items[1], items[2], age, statusIndex,
                                             firstDoseDateYear, firstDoseDateMonth, firstDoseDateDay, firstDoseBatch,
                                             secondDoseDateYear, secondDoseDateMonth, secondDoseDateDay, secondDoseBatch));
            }

            // Assign recipients into VC they have been assigned to
            for (int i = 0; i < VCList.vacCenters.size(); i++)
                for (int j = 0; j < recipients.size(); j++)
                    if (i == recipients.get(j).getAssignedToVC() - 1)
                        VCList.vacCenters.get(i).addNewRecipient(recipients.get(j));

        } catch (IOException ex) {
            throw new IOException("\nAn error has occured while reading the file, failed to read recipients' data.");
        }
    }

    public static void saveRecipientsToFile() throws IOException {
        try{
            System.out.println("\nSaving recipients' data...");
            StringBuilder sb = new StringBuilder();
            Collections.sort(recipients);                                   // sort recipients by VCIndex
            for (int i = 0; i < VCList.vacCenters.size(); i++)
                Collections.sort(VCList.vacCenters.get(i).getRecipients(), (o1, o2)->o1.getStatusIndex() - o2.getStatusIndex());   // sort recipients by Status

            ArrayList<Recipient> tempRecipients = new ArrayList<>();        // stores sorted recipients
            
            for (int i = 0; i < recipients.size(); i++) {
                if (recipients.get(i).getAssignedToVC() == 0)
                    tempRecipients.add(recipients.get(i));
            }
            for (int i = 0; i < VCList.vacCenters.size(); i++) {
                for (int j = 0; j < VCList.vacCenters.get(i).getRecipients().size(); j++)
                    tempRecipients.add(VCList.vacCenters.get(i).getRecipient(j));
            }
            recipients = tempRecipients;

            for (int i = 0; i < recipients.size(); i++)
                sb.append (recipients.get(i).toCSVString() + "\n");
            Files.write(Paths.get(RECIPIENTS_PATH), sb.toString().getBytes());
            System.out.println("Recipients' data saved!");
        } catch (IOException ex) {
            throw new IOException("An error has occured, failed to save recipients' data.\n");
        }
    }
}
