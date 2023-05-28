import java.util.ArrayList;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class VCList {
    static ArrayList<VC> vacCenters = new ArrayList<>();
    private static final String VACCENTERS_PATH = "VacCenters.csv";

    public static void readVacCentersFromFile() throws IOException {
        try{
            List<String> lines = Files.readAllLines(Paths.get(VACCENTERS_PATH));
            for (int i = 0; i < lines.size(); i++){
                String[] items = lines.get(i).split(",");   // data seperated by comma
                int capacityPerDay = Integer.parseInt(items[2]);
                int vaccineQty = Integer.parseInt(items[3]);
                int vaccineBatchNum = Integer.parseInt(items[4]);
                vacCenters.add(new VC(items[0], items[1], capacityPerDay, vaccineQty, vaccineBatchNum));
            }
        } catch (IOException ex) {
            throw new IOException("\nAn error has occured while reading the file, failed to read vaccination centers' data");
        }
    }

    public static void saveVacCentersToFile() throws IOException {
        try{
            System.out.println("\nSaving Vaccination Centers' data...");
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < vacCenters.size(); i++) {
                sb.append (vacCenters.get(i).toCSVString() + "\n");
            }
            Files.write(Paths.get(VACCENTERS_PATH), sb.toString().getBytes());
            System.out.println("Vaccination centers' data saved!");
        } catch (IOException ex) {
            throw new IOException("An error has occured, failed to save vaccination centers' data.\n");
        }
    }
}
