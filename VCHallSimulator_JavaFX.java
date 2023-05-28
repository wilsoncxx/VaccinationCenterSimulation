import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedList;

public class VCHallSimulator_JavaFX extends Application {
    private static LocalDate simulationDate;
    private static final DateTimeFormatter dateTimeForm = DateTimeFormatter.ofPattern("dd MMM yyyy");
    private static VC vacCenter;
    private static ArrayList<Recipient> recipients;
    private static ArrayList<Recipient> vaccinatedRecipients;
    private static LinkedList<Integer> vaccineStack;
    private static LinkedList<Recipient> seniorQueue;
    private static LinkedList<Recipient> normalQueue;
    boolean vaccineSupplyEnough;
    
    // labels
    private Label menuTitle = new Label("Vaccination Center Hall Simulator");
    private Label lblSelectVC = new Label("Please select an Vaccination Center: ");
    private Label lblSelectVCMsg = new Label();
    private Label pickDateTitle = new Label();
    private Label lblPickDate = new Label("Please choose a date to simulate");
    private Label lblPickDateMsg = new Label();
    private Label simulationTitle = new Label();
    private Label lblSimulation = new Label();
    private Label lblNextSimulation = new Label();
    private Label lblSimulationCompletedMsg = new Label();
    private Label simulationFailedTitle = new Label();
    private Label lblSimulationFailed = new Label();
    
    // buttons
    private Button selectBtn = new Button("Select");
    private Button simulateBtn = new Button("Simulate");
    private Button backBtn = new Button("Back");
    private Button nextBtn1 = new Button("Next");
    private Button nextBtn2 = new Button("Next");

    // textfields
    private TextField vcSelected = new TextField();

    // date picker
    private DatePicker datePicker = new DatePicker();

    @Override
    public void start(Stage mainStage) throws IOException {
        VCList.readVacCentersFromFile();
        RecipientsList.readRecipientsFromFile();

        Label lblVCList = new Label(getVacCenter());

        GridPane gridPane = new GridPane();
        gridPane.add(menuTitle, 0, 0);
        gridPane.add(new Label(), 0, 1);
        gridPane.add(lblVCList, 0, 2, 2, 1);
        gridPane.add(new Label(), 0, 3);
        gridPane.add(lblSelectVC, 0, 4);
        gridPane.add(vcSelected, 0, 5);
        gridPane.add(selectBtn, 0, 7);
        gridPane.add(new Label(), 0, 8);
        gridPane.add(lblSelectVCMsg, 0, 9);
        gridPane.setAlignment(Pos.CENTER);

        gridPane.setStyle("-fx-background-color: #CCCCFF; -fx-font-size: 20;");
        menuTitle.setStyle("-fx-text-fill: #52595D; -fx-font-size: 40; -fx-font-weight: bold; -fx-font-family: Verdana");
        lblVCList.setStyle("-fx-font-size: 30;");
        lblSelectVCMsg.setStyle("-fx-font-size: 15;");
        vcSelected.setMaxSize(400, 50);
        selectBtn.setMaxWidth(150);

        Scene selectVCScene = new Scene(gridPane, 1080, 720); 

        mainStage.setTitle("VC Hall Simulator");
        mainStage.setScene(selectVCScene);
        mainStage.show();

        backBtn.setOnAction(e -> mainStage.setScene(selectVCScene));

        GridPane pickDatePane = new GridPane();
        pickDatePane.add(pickDateTitle, 0, 0);
        pickDatePane.add(new Label(), 0, 1);
        pickDatePane.add(lblPickDate, 0, 2);
        pickDatePane.add(datePicker, 0, 3);
        pickDatePane.add(simulateBtn, 0, 4);
        pickDatePane.add(backBtn, 0, 5);
        pickDatePane.add(new Label(), 0, 6);
        pickDatePane.add(lblPickDateMsg, 0, 7);
        pickDatePane.setAlignment(Pos.CENTER);

        pickDatePane.setStyle("-fx-background-color: #CCCCFF; -fx-font-size: 20;");
        pickDateTitle.setStyle("-fx-text-fill: #52595D; -fx-font-size: 40; -fx-font-weight: bold; -fx-font-family: Verdana");
        datePicker.setMaxWidth(300);
        simulateBtn.setMaxWidth(150);

        Scene pickDateScene = new Scene(pickDatePane, 1080, 720);

        selectBtn.setOnAction(e -> {
            if (checkIndex(vcSelected.getText())) {
                int vcIndex = Integer.parseInt(vcSelected.getText()) - 1;

                if (vcIndex < VCList.vacCenters.size()) {
                    vacCenter = VCList.vacCenters.get(vcIndex);
                    pickDateTitle.setText("Simulating a day in " + vacCenter.getName() + "'s hall");
                    mainStage.setScene(pickDateScene);
                }
                else {
                    lblSelectVCMsg.setText("You've entered an invalid VC index.");
                }
            }
            else {
                lblSelectVCMsg.setText("Please enter an integer represents the VC index.");
            }
        });

        Stage simulationStage = new Stage();
        simulationStage.setTitle("Simulation");
        simulationStage.initModality(Modality.APPLICATION_MODAL);

        simulateBtn.setOnAction(e -> {
            simulationDate = datePicker.getValue();
            recipients = new ArrayList<>();
            vaccinatedRecipients = new ArrayList<>();
            vaccineStack = new LinkedList<>();
            seniorQueue = new LinkedList<>();
            normalQueue = new LinkedList<>();

            BorderPane simulationPane = new BorderPane();
            simulationPane.setPadding(new Insets(20));
            simulationPane.setTop(simulationTitle);
            simulationPane.setLeft(lblSimulation);
            simulationPane.setBottom(nextBtn1);
            simulationPane.setStyle("-fx-background-color: #CCCCFF; -fx-font-size: 15;  -fx-font-family: Verdana;");
            nextBtn1.setMaxWidth(150);
            nextBtn2.setMaxWidth(150);
            Scene simulationScene = new Scene(simulationPane, 900, 720);

            BorderPane simulationFailedPane = new BorderPane();
            simulationFailedPane.setPadding(new Insets(20));
            simulationFailedPane.setTop(simulationFailedTitle);
            simulationFailedPane.setLeft(lblSimulationFailed);
            simulationFailedPane.setStyle("-fx-background-color: #CCCCFF; -fx-font-size: 15;  -fx-font-family: Verdana;");
            Scene simulationFailedScene = new Scene(simulationFailedPane, 600, 400);

            if (simulationDate != null && simulationDate.compareTo(LocalDate.now()) <= 0) {
                simulationTitle.setText("Date : " + dateTimeForm.format(simulationDate));
                simulationFailedTitle.setText("Date : " + dateTimeForm.format(simulationDate));

                getRecipients();
                if (recipients.size() == 0) {
                    lblSimulationFailed.setText("\nThis VC do not have recipient having appointment on this date.");
                    simulationStage.setScene(simulationFailedScene);
                    simulationStage.showAndWait();
                    return;
                }
                getVaccineStack();
                if (vaccineStack.size() == 0) {
                    lblSimulationFailed.setText("\nThis VC do not have any vaccine doses left.");
                    simulationStage.setScene(simulationFailedScene);
                    simulationStage.showAndWait();
                    return;
                }

                try {
                    VCList.saveVacCentersToFile();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }

                if (vaccineStack.size() < recipients.size())
                    vaccineSupplyEnough = false;
                else
                    vaccineSupplyEnough = true;
                lblSimulation.setText(printRecipientsAndVaccines());
                assignRecipientsToQueue();
                simulationStage.setScene(simulationScene);
                simulationStage.showAndWait();
            }
            else if (simulationDate == null) {
                lblPickDateMsg.setText("Please select a date.");
            }
            else {
                lblPickDateMsg.setText("You've selected an invalid simulation date.\n" +
                                       "Please try again.");
            }
        });

        nextBtn1.setOnAction(e -> {
            BorderPane nextPane = new BorderPane();
            nextPane.setPadding(new Insets(20));
            nextPane.setLeft(lblNextSimulation);
            nextPane.setBottom(nextBtn2);

            nextPane.setStyle("-fx-background-color: #CCCCFF; -fx-font-size: 15;  -fx-font-family: Verdana;");

            Scene nextScene = new Scene(nextPane, 900, 720);
            lblNextSimulation.setText(printNextScene());
            simulationStage.setScene(nextScene);
        });

        nextBtn2.setOnAction(e -> {
            if ((seniorQueue.size() != 0 || normalQueue.size() != 0) && vaccineStack.size() != 0) {
                vaccination();
                try {
                    RecipientsList.saveRecipientsToFile();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }

                BorderPane nextPane = new BorderPane();
                nextPane.setPadding(new Insets(20));
                nextPane.setLeft(lblNextSimulation);
                nextPane.setBottom(nextBtn2);
                nextPane.setStyle("-fx-background-color: #CCCCFF; -fx-font-size: 15;  -fx-font-family: Verdana;");
                Scene nextScene = new Scene(nextPane, 900, 720);
                lblNextSimulation.setText(printNextScene());

                simulationStage.setScene(nextScene);
            }
            else {
                BorderPane nextPane = new BorderPane();
                nextPane.setPadding(new Insets(20));
                nextPane.setTop(lblNextSimulation);
                nextPane.setLeft(lblSimulationCompletedMsg);
                nextPane.setStyle("-fx-background-color: #CCCCFF; -fx-font-size: 15;  -fx-font-family: Verdana;");
                Scene nextScene = new Scene(nextPane, 900, 720);
                lblNextSimulation.setText(printNextScene());

                if (!vaccineSupplyEnough) 
                    lblSimulationCompletedMsg.setText("\nThis VC is currently out of vaccine, vaccination paused.");
                else
                    lblSimulationCompletedMsg.setText("\nSimulation completed.");
                simulationStage.setScene(nextScene);
            }
        });
    }

    private String getVacCenter() {
        StringBuilder vacCenterList = new StringBuilder();
        ArrayList<VC> vacCenters = VCList.vacCenters;

        for (int i = 0; i < vacCenters.size(); i++)
            vacCenterList.append ((i+1) + ". " + vacCenters.get(i).getName() + "\n");

        return vacCenterList.toString();
    }

    private static boolean checkIndex(String index) {
        return index.matches("^[1-9]\\d?$|^[1-9]\\d\\d$");
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

    private static String printRecipientsAndVaccines() {
        StringBuilder simulation = new StringBuilder();

        simulation.append("Assigned recipients and their age");
        if (recipients.size() == 0) {
            simulation.append("\n\nEmpty");
        }
        else {
            int numOfRcpBoxes = recipients.size();
            simulation.append(drawLine(numOfRcpBoxes, 12));
            for (int i = 0; i < recipients.size(); i++) {
                String name = recipients.get(i).getName();
                String row = String.format("| %-10s ", name);
                simulation.append(row);
            }
            simulation.append("|\n");
            for (int i = 0; i < recipients.size(); i++) {
                int age = recipients.get(i).getAge();
                String row = String.format("| %-10d ", age);
                simulation.append(row);
            }
            simulation.append("|");
            simulation.append(drawLine(numOfRcpBoxes, 12));
        }

        simulation.append("\nStack of vaccines");
        if (vaccineStack.size() == 0) {
            simulation.append("\nEmpty\n");
            return simulation.toString();
        }
        else {
            int numOfVacBoxes = vaccineStack.size();
            simulation.append(drawLine(numOfVacBoxes, 7));
            for (int i = 0; i < vaccineStack.size(); i++) {
                int vaccineBatchNum = vaccineStack.get(i);
                String row = String.format("| %-5d ", vaccineBatchNum);
                simulation.append(row);
            }
            simulation.append("|");
            simulation.append(drawLine(numOfVacBoxes, 7));
        }

        return simulation.toString();
    }

    private static String drawLine(int numOfBoxes, int numOfSpaces) {
        StringBuilder line = new StringBuilder();
        line.append("\n");
        for (int i = 0; i < numOfBoxes; i++) {
            // line.append("+");
            line.append("-");
            for (int j = 0; j < numOfSpaces; j++)
                line.append("-");
        }
        // line.append("+\n");
        line.append("-\n");

        return line.toString();
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

    private static String printNextScene() {
        StringBuilder nextSceneString = new StringBuilder();
    
        nextSceneString.append("\nSenior Queue");
        if (seniorQueue.size() == 0) {
            nextSceneString.append("\nEmpty\n");
        }
        else {
            int numOfSQBoxes = seniorQueue.size();
            nextSceneString.append(drawLine(numOfSQBoxes, 12));
            for (int i = 0; i < seniorQueue.size(); i++) {
                String name = seniorQueue.get(i).getName();
                String row = String.format("| %-10s ", name);
                nextSceneString.append(row);
            }
            nextSceneString.append("|\n");
            for (int i = 0; i < seniorQueue.size(); i++) {
                int age = seniorQueue.get(i).getAge();
                String row = String.format("| %-10d ", age);
                nextSceneString.append(row);
            }
            nextSceneString.append("|");
            nextSceneString.append(drawLine(numOfSQBoxes, 12));
        }

        nextSceneString.append("\nNormal Queue");
        if (normalQueue.size() == 0) {
            nextSceneString.append("\nEmpty\n");
        }
        else {
            int numOfNQBoxes = normalQueue.size();
            nextSceneString.append(drawLine(numOfNQBoxes, 12));
            for (int i = 0; i < normalQueue.size(); i++) {
                String name = normalQueue.get(i).getName();
                String row = String.format("| %-10s ", name);
                nextSceneString.append(row);
            }
            nextSceneString.append("|\n");
            for (int i = 0; i < normalQueue.size(); i++) {
                int age = normalQueue.get(i).getAge();
                String row = String.format("| %-10d ", age);
                nextSceneString.append(row);
            }
            nextSceneString.append("|");
            nextSceneString.append(drawLine(numOfNQBoxes, 12));
        }

        nextSceneString.append("\nStack of vaccines");
        if (vaccineStack.size() == 0) {
            nextSceneString.append("\nEmpty\n");
        }
        else {
            int numOfVSBoxes = vaccineStack.size();
            nextSceneString.append(drawLine(numOfVSBoxes, 7));
            for (int i = 0; i < vaccineStack.size(); i++) {
                int vaccineBatchNum = vaccineStack.get(i);
                String row = String.format("| %-5d ", vaccineBatchNum);
                nextSceneString.append(row);
            }
            nextSceneString.append("|");
            nextSceneString.append(drawLine(numOfVSBoxes, 7));
        }

        nextSceneString.append("\nVaccinated Recipients");
        if (vaccinatedRecipients.size() == 0) {
            nextSceneString.append("\nEmpty\n");
        }
        else {
            int numOfVRBoxes = vaccinatedRecipients.size();
            nextSceneString.append(drawLine(numOfVRBoxes, 12));
            for (int i = 0; i < vaccinatedRecipients.size(); i++) {
                int vaccineBatchNum = vaccinatedRecipients.get(i).getFirstDoseBatch();
                if (vaccinatedRecipients.get(i).getStatusIndex() == 4)
                    vaccineBatchNum = vaccinatedRecipients.get(i).getSecondDoseBatch();

                String row = String.format("| %-10d ", vaccineBatchNum);
                nextSceneString.append(row);
            }
            nextSceneString.append("|\n");
            for (int i = 0; i < vaccinatedRecipients.size(); i++) {
                String name = vaccinatedRecipients.get(i).getName();
                String row = String.format("| %-10s ", name);
                nextSceneString.append(row);
            }
            nextSceneString.append("|\n");
            for (int i = 0; i < vaccinatedRecipients.size(); i++) {
                int age = vaccinatedRecipients.get(i).getAge();
                String row = String.format("| %-10d ", age);
                nextSceneString.append(row);
            }
            nextSceneString.append("|");
            nextSceneString.append(drawLine(numOfVRBoxes, 12));
        }

        return nextSceneString.toString();
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

    public static void main(String[] args) {
        Application.launch(args);
    }
}
