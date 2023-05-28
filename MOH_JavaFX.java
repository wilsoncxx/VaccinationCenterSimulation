import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;

public class MOH_JavaFX extends Application {
    private static final DateTimeFormatter dateTimeForm = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    //Login scene
    private Label passwordPrompt = new Label("Please enter the password");
    private PasswordField mohPasswordField = new PasswordField();
    private Button loginButton = new Button("Login");
    private Label verificationText = new Label("");

    //Menu scene
    private Label menuTitle = new Label("Ministry of Health Menu");
    private Button displayRecButton = new Button("Display All Recipients");
    private Button distributionButton = new Button("Distribute Vaccines and Recipients");
    private Button statsButton = new Button("Display Statistics");

    //Distribution scene
    private Button vacBasedOnCapacity = new Button("Distribute Vaccines based on Daily Capacity");
    private Button vacBasedOnOptional = new Button("Distribute Vaccines based on Optional Amount");
    private Button recBasedOnCapacity = new Button("Distribute Recipients based on Daily Capacity");
    private Button recBasedOnOptional = new Button("Distribute Recipients based on Optional Amount");
    private Button oneRecipient = new Button("Distribute One Recipient to a VC");
    private Button backToMenu = new Button("Back");
    private Label vcPrompt1 = new Label("Please enter the name of the VC");
    private Label vcPrompt2 = new Label("Please enter the name of the VC");
    private Label vcPrompt3 = new Label("Please enter the name of the VC");
    private Label vcPrompt4 = new Label("Please enter the name of the VC");
    private Label vcPrompt5 = new Label("Please enter the name of the VC");
    private Label amountPrompt1 = new Label("Please enter the desired amount");
    private Label amountPrompt2 = new Label("Please enter the desired amount");
    private Label recipientPrompt = new Label("Please enter the name of the recipient");
    private Label phonePrompt = new Label("Please enter the phone number of the recipient");
    private TextField vcName1 = new TextField();
    private TextField vcName2 = new TextField();
    private TextField vcName3 = new TextField();
    private TextField vcName4 = new TextField();
    private TextField vcName5 = new TextField();
    private TextField optionalAmount1 = new TextField();
    private TextField optionalAmount2 = new TextField();
    private TextField recName = new TextField();
    private TextField recPhone = new TextField();
    private Button confirm1 = new Button("Confirm");
    private Button confirm2 = new Button("Confirm");
    private Button confirm3 = new Button("Confirm");
    private Button confirm4 = new Button("Confirm");
    private Button confirm5 = new Button("Confirm");
    private Label result1 = new Label("");
    private Label result2 = new Label("");
    private Label result3 = new Label("");
    private Label result4 = new Label("");
    private Label result5 = new Label("");

    @SuppressWarnings("unchecked")
    @Override
    public void start(Stage mainStage) throws IOException {
        VCList.readVacCentersFromFile();
        RecipientsList.readRecipientsFromFile();

        //Login
        GridPane loginPane = new GridPane();
        loginPane.add(passwordPrompt, 0, 0);
        loginPane.add(mohPasswordField, 0, 1);
        loginPane.add(loginButton, 0, 2);
        loginPane.add(new Label(), 0, 3);
        loginPane.add(verificationText, 0, 4);
        loginPane.setAlignment(Pos.CENTER);
        loginPane.setStyle("-fx-background-color: #CCCCFF; -fx-font-size: 20;");
        passwordPrompt.setStyle("-fx-text-fill: #52595D; -fx-font-size: 30; -fx-font-family: Verdana");
        loginButton.setMaxWidth(200);

        Scene loginScene = new Scene(loginPane, 850, 400);

        mainStage.setTitle("Ministry of Health");
        mainStage.setScene(loginScene);
        mainStage.show();

        //Main Menu
        GridPane menuPane = new GridPane();
        menuPane.add(menuTitle, 0, 0);
        menuPane.add(new Label(), 0, 1);
        menuPane.add(displayRecButton, 0, 2);
        menuPane.add(distributionButton, 0, 3);
        menuPane.add(statsButton, 0, 4);
        menuPane.setAlignment(Pos.CENTER);
        menuPane.setStyle("-fx-background-color: #CCCCFF; -fx-font-size: 20;");
        menuTitle.setStyle("-fx-text-fill: #52595D; -fx-font-size: 40; -fx-font-weight: bold; -fx-font-family: Verdana");
        displayRecButton.setMaxWidth(800);
        distributionButton.setMaxWidth(800);
        statsButton.setMaxWidth(800);

        Scene menuScene = new Scene(menuPane, 1080, 720);

        loginButton.setOnAction(e -> {
            String password = mohPasswordField.getText();
            int verificationResult = verifyMOHAccess(password);
            if (verificationResult == -99) {
                verificationText.setText("You do not have the access to the Ministry of Health.");
            }
            else {
                mainStage.setScene(menuScene);
            }
        });
        
        //View All Recipients
        displayRecButton.setOnAction(e -> {
            TableView<Recipient> recipientsTable = new TableView<>();
            recipientsTable.getItems().addAll(MOH_ViewRecipient.getRecipientList());

            recipientsTable.getColumns().addAll(MOH_ViewRecipient.getNameColumn(), MOH_ViewRecipient.getPhoneColumn(),
                                                MOH_ViewRecipient.getAgeColumn(), MOH_ViewRecipient.getStatusColumn(), MOH_ViewRecipient.getAssignedToVCColumn(),
                                                MOH_ViewRecipient.getFirstAppointmentDateColumn(), MOH_ViewRecipient.getSecondAppointmentDateColumn());
            
            recipientsTable.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);

            VBox viewRecipientsBox = new VBox();
            viewRecipientsBox.getChildren().add(recipientsTable);
            viewRecipientsBox.setStyle("-fx-padding: 13;" +
                                "-fx-border-style: solid inside;" +
                                "-fx-border-width: 4;" +
                                "-fx-border-insets: 8;" +
                                "-fx-border-radius: 8;" +
                                "-fx-border-color: blue;");

            Scene viewRecipientsScene = new Scene(viewRecipientsBox, 700, 450);

            Stage viewRecipientsStage = new Stage();
            viewRecipientsStage.setTitle("All Recipients");
            viewRecipientsStage.initOwner(mainStage);
            viewRecipientsStage.initModality(Modality.WINDOW_MODAL);
            viewRecipientsStage.setScene(viewRecipientsScene);
            viewRecipientsStage.showAndWait();
        });

        //Distribute Vaccine or Recipients
        GridPane distributionPane = new GridPane();
        distributionPane.add(vacBasedOnCapacity, 0, 0);
        distributionPane.add(vacBasedOnOptional, 0, 1);
        distributionPane.add(recBasedOnCapacity, 0, 2);
        distributionPane.add(recBasedOnOptional, 0, 3);
        distributionPane.add(oneRecipient, 0, 4);
        distributionPane.add(backToMenu, 0, 5);
        distributionPane.setAlignment(Pos.CENTER);
        distributionPane.setStyle("-fx-background-color: #CCCCFF; -fx-font-size: 20;");
        vacBasedOnCapacity.setMaxWidth(500);
        vacBasedOnOptional.setMaxWidth(500);
        recBasedOnCapacity.setMaxWidth(500);
        recBasedOnOptional.setMaxWidth(500);
        oneRecipient.setMaxWidth(500);

        Scene distributionScene = new Scene(distributionPane, 650, 450);

        GridPane vacBasedOnCapacityPane = new GridPane();
        vacBasedOnCapacityPane.add(vcPrompt1, 0, 0);
        vacBasedOnCapacityPane.add(vcName1, 0, 1);
        vacBasedOnCapacityPane.add(confirm1, 0, 2);
        vacBasedOnCapacityPane.add(result1, 0, 3);
        vacBasedOnCapacityPane.setAlignment(Pos.CENTER);
        vacBasedOnCapacityPane.setStyle("-fx-background-color: #CCCCFF; -fx-font-size: 20;");
        confirm1.setMaxWidth(200);

        confirm1.setOnAction(e -> {
            int vcIndex = verifyVC(vcName1.getText());
            
            if(!vcName1.getText().isEmpty()){
                if (vcIndex != -99) {
                    int dailyMax = VCList.vacCenters.get(vcIndex).getCapacityPerDay();
                    VCList.vacCenters.get(vcIndex).addVaccine(dailyMax);
                    try {
                        VCList.saveVacCentersToFile();
                        result1.setText("Vaccine amount successfully saved.");
                    } catch (IOException e1) {
                        result1.setText("Failed to save vaccine amount.");
                    }
                }

                else {
                    result1.setText("VC not found.");
                }
            }

            else {
                result1.setText("Please enter an input.");
            }
        });

        Scene vacBasedOnCapacityScene = new Scene(vacBasedOnCapacityPane, 650, 450);
        
        GridPane vacBasedOnOptionalPane = new GridPane();
        vacBasedOnOptionalPane.add(vcPrompt2, 0, 0);
        vacBasedOnOptionalPane.add(vcName2, 0, 1);
        vacBasedOnOptionalPane.add(new Label(), 0, 2);
        vacBasedOnOptionalPane.add(amountPrompt1, 0, 3);
        vacBasedOnOptionalPane.add(optionalAmount1, 0, 4);
        vacBasedOnOptionalPane.add(confirm2, 0, 5);
        vacBasedOnOptionalPane.add(result2, 0, 6);
        vacBasedOnOptionalPane.setAlignment(Pos.CENTER);
        vacBasedOnOptionalPane.setStyle("-fx-background-color: #CCCCFF; -fx-font-size: 20;");
        confirm2.setMaxWidth(200);

        confirm2.setOnAction(e -> {
            int vcIndex = verifyVC(vcName2.getText());

            if(!vcName2.getText().isEmpty() && !optionalAmount1.getText().isEmpty()) {
                if (checkIndex(optionalAmount1.getText())) {
                    if (vcIndex != -99) {
                        int optionalVac = Integer.parseInt(optionalAmount1.getText());
                        VCList.vacCenters.get(vcIndex).addVaccine(optionalVac);
                        try {
                            VCList.saveVacCentersToFile();
                            result2.setText("Vaccine amount successfully saved.");
                        } catch (IOException e1) {
                            result2.setText("Failed to save vaccine amount.");
                        }
                    }

                    else {
                        result2.setText("VC not found.");
                    }
                }

                else {
                    result2.setText("Please enter only integers for vaccine amount.");
                }
            }

            else {
                result2.setText("Please fill up all the text boxes.");
            }
        });

        Scene vacBasedOnOptionalScene = new Scene(vacBasedOnOptionalPane, 650, 450);

        GridPane recBasedOnCapacityPane = new GridPane();
        recBasedOnCapacityPane.add(vcPrompt3, 0, 0);
        recBasedOnCapacityPane.add(vcName3, 0, 1);
        recBasedOnCapacityPane.add(confirm3, 0, 2);
        recBasedOnCapacityPane.add(result3, 0, 3);
        recBasedOnCapacityPane.setAlignment(Pos.CENTER);
        recBasedOnCapacityPane.setStyle("-fx-background-color: #CCCCFF; -fx-font-size: 20;");
        confirm3.setMaxWidth(200);

        confirm3.setOnAction(e -> {
            int vcIndex = verifyVC(vcName3.getText());

            if(!vcName3.getText().isEmpty()) {
                if (vcIndex != -99) {
                    int dailyMax = VCList.vacCenters.get(vcIndex).getCapacityPerDay();
                    for (int i = 0; i < dailyMax; i++) {
                        if (RecipientsList.recipients.get(i).getAssignedToVC() == 0) {
                            VCList.vacCenters.get(vcIndex).addNewRecipient(RecipientsList.recipients.get(i));
                            RecipientsList.recipients.get(i).setAssignedToVC(vcIndex+1);
                        }
                    }

                    try {
                        RecipientsList.saveRecipientsToFile();
                        result3.setText("Recipients successfully assigned.");
                    } catch (IOException e1) {
                        result3.setText("Failed to assign recipients.");
                    }
                }

                else {
                    result3.setText("VC not found.");
                }
            }

            else {
                result3.setText("Please enter an input.");
            }
        });

        Scene recBasedOnCapacityScene = new Scene(recBasedOnCapacityPane, 650, 450);

        GridPane recBasedOnOptionalPane = new GridPane();
        recBasedOnOptionalPane.add(vcPrompt4, 0, 0);
        recBasedOnOptionalPane.add(vcName4, 0, 1);
        recBasedOnOptionalPane.add(new Label(), 0, 2);
        recBasedOnOptionalPane.add(amountPrompt2, 0, 3);
        recBasedOnOptionalPane.add(optionalAmount2, 0, 4);
        recBasedOnOptionalPane.add(confirm4, 0, 5);
        recBasedOnOptionalPane.add(result4, 0, 6);
        recBasedOnOptionalPane.setAlignment(Pos.CENTER);
        recBasedOnOptionalPane.setStyle("-fx-background-color: #CCCCFF; -fx-font-size: 20;");
        confirm4.setMaxWidth(200);

        confirm4.setOnAction(e -> {
            int vcIndex = verifyVC(vcName4.getText());

            if (!vcName4.getText().isEmpty() && !optionalAmount2.getText().isEmpty()) {
                if (checkIndex(optionalAmount2.getText())) { 
                    if (vcIndex != -99) {
                        int optionalRec = Integer.parseInt(optionalAmount2.getText());
                        for (int i = 0; i < optionalRec; i++) {
                            if (RecipientsList.recipients.get(i).getAssignedToVC() == 0){
                                VCList.vacCenters.get(vcIndex).addNewRecipient(RecipientsList.recipients.get(i));
                                RecipientsList.recipients.get(i).setAssignedToVC(vcIndex+1);
                            }
                        }

                        try {
                            RecipientsList.saveRecipientsToFile();
                            result4.setText("Recipients successfully assigned.");
                        } catch (IOException e1) {
                            result4.setText("Failed to assign recipients.");
                        }
                    }

                    else {
                        result4.setText("VC not found.");
                    }
                }

                else {
                    result4.setText("Please enter only integers for vaccine amount.");
                }
            }

            else {
                result4.setText("Please fill up all the text boxes.");
            }
        });

        Scene recBasedOnOptionalScene = new Scene(recBasedOnOptionalPane, 650, 450);

        GridPane oneRecipientPane = new GridPane();
        oneRecipientPane.add(vcPrompt5, 0, 0);
        oneRecipientPane.add(vcName5, 0, 1);
        oneRecipientPane.add(new Label(), 0, 2);
        oneRecipientPane.add(recipientPrompt, 0, 3);
        oneRecipientPane.add(recName, 0, 4);
        oneRecipientPane.add(new Label(), 0, 5);
        oneRecipientPane.add(phonePrompt, 0, 6);
        oneRecipientPane.add(recPhone, 0, 7);
        oneRecipientPane.add(confirm5, 0, 8);
        oneRecipientPane.add(result5, 0, 9);
        oneRecipientPane.setAlignment(Pos.CENTER);
        oneRecipientPane.setStyle("-fx-background-color: #CCCCFF; -fx-font-size: 20;");
        confirm5.setMaxWidth(200);

        confirm5.setOnAction(e -> {
            if (!vcName5.getText().isEmpty() && !recName.getText().isEmpty() && !recPhone.getText().isEmpty()) {
                if (checkIndex(recPhone.getText())) {
                    int vcIndex = verifyVC(vcName5.getText());
                    int userIndex = verifyRecipient(recName.getText(), recPhone.getText());

                    if (vcIndex == -99 || userIndex == -99){
                        result5.setText("VC or Recipient not found, please check your inputs.");
                    }

                    else {
                        VCList.vacCenters.get(vcIndex).addNewRecipient(RecipientsList.recipients.get(userIndex));
                        RecipientsList.recipients.get(userIndex).setAssignedToVC(vcIndex+1);

                        try {
                            RecipientsList.saveRecipientsToFile();
                            result5.setText("This recipient has been successfully assigned.");
                        } catch (IOException e1) {
                            result5.setText("Failed to assign this recipient.");
                        }
                    }
                }

                else {
                    result5.setText("Please enter only integers for vaccine amount.");
                }
            }

            else {
                result5.setText("Please fill in all the text boxes.");
            }
        });

        Scene oneRecipientScene = new Scene (oneRecipientPane, 650, 450);

        distributionButton.setOnAction(e -> {
            mainStage.setScene(distributionScene);
        });

        backToMenu.setOnAction(e -> {
            mainStage.setScene(menuScene);
        });

        vacBasedOnCapacity.setOnAction(e -> {
            Stage vacBasedOnCapacityStage = new Stage();
            vacBasedOnCapacityStage.setTitle("Distribute Vaccine");
            vacBasedOnCapacityStage.initModality(Modality.APPLICATION_MODAL);
            vacBasedOnCapacityStage.setScene(vacBasedOnCapacityScene);
            vacBasedOnCapacityStage.showAndWait();
        });

        vacBasedOnOptional.setOnAction(e -> {
            Stage vacBasedOnOptionalStage = new Stage();
            vacBasedOnOptionalStage.setTitle("Distribute Vaccine");
            vacBasedOnOptionalStage.initModality(Modality.APPLICATION_MODAL);
            vacBasedOnOptionalStage.setScene(vacBasedOnOptionalScene);
            vacBasedOnOptionalStage.showAndWait();
        });

        recBasedOnCapacity.setOnAction(e -> {
            Stage recBasedOnCapacityStage = new Stage();
            recBasedOnCapacityStage.setTitle("Distribute Recipients");
            recBasedOnCapacityStage.initModality(Modality.APPLICATION_MODAL);
            recBasedOnCapacityStage.setScene(recBasedOnCapacityScene);
            recBasedOnCapacityStage.showAndWait();
        });

        recBasedOnOptional.setOnAction(e -> {
            Stage recBasedOnOptionalStage = new Stage();
            recBasedOnOptionalStage.setTitle("Distribute Recipients");
            recBasedOnOptionalStage.initModality(Modality.APPLICATION_MODAL);
            recBasedOnOptionalStage.setScene(recBasedOnOptionalScene);
            recBasedOnOptionalStage.showAndWait();
        });

        oneRecipient.setOnAction(e -> {
            Stage oneRecipientStage = new Stage();
            oneRecipientStage.setTitle("Distribute Recipients");
            oneRecipientStage.initModality(Modality.APPLICATION_MODAL);
            oneRecipientStage.setScene(oneRecipientScene);
            oneRecipientStage.showAndWait();
        });

        //View Cumulative Statistics
        statsButton.setOnAction(e -> {
            int totalPending = 0;
            int totalFirstDoseAppointments = 0;
            int totalFirstDoseCompletions = 0;
            int totalSecondDoseAppointments = 0;
            int totalSecondDoseCompletions = 0;
            LinkedHashMap<LocalDate, Integer> appointmentsOnDays = new LinkedHashMap<LocalDate, Integer>();
            int totalVaccinations = 0;
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
                        totalVaccinations += 1;
                    else if (statusIndex == 4)
                        totalVaccinations += 2;
            }

            for (LocalDate date = LocalDate.now(); date.compareTo(LocalDate.of(2020, 1, 1)) >= 0; date = date.minusDays(1)) {
                int numOfApmtsOnDay = 1;
                for (int i = 0; i < RecipientsList.recipients.size(); i++) {
                    if (RecipientsList.recipients.get(i).getFirstApmtDate() != null)
                        if (RecipientsList.recipients.get(i).getStatusIndex() > 1 && date.equals(RecipientsList.recipients.get(i).getFirstApmtDate()))
                            appointmentsOnDays.put(date, numOfApmtsOnDay++);
                    if (RecipientsList.recipients.get(i).getSecondApmtDate() != null)
                        if (RecipientsList.recipients.get(i).getStatusIndex() > 3 && date.equals(RecipientsList.recipients.get(i).getSecondApmtDate()))
                            appointmentsOnDays.put(date, numOfApmtsOnDay++);
                }
            }

            StringBuilder totalStatistics = new StringBuilder();

            totalStatistics.append("Total Pending Recipients = " + totalPending);
            totalStatistics.append("\nTotal First Dose Appointments = " + totalFirstDoseAppointments);
            totalStatistics.append("\nTotal First Dose Completions = " + totalFirstDoseCompletions);
            totalStatistics.append("\nTotal Second Dose Appointments = " + totalSecondDoseAppointments);
            totalStatistics.append("\nTotal Second Dose Completions = " + totalSecondDoseCompletions);
            totalStatistics.append("\nTotal Vaccinations = " + totalVaccinations);
            totalStatistics.append("\nTotal Capacity Per Day = " + totalCapacityPerDay);
            for (LocalDate date = LocalDate.now(); date.compareTo(LocalDate.of(2020, 1, 1)) >= 0; date = date.minusDays(1)) {
                if (appointmentsOnDays.containsKey(date))
                    totalStatistics.append ("\nTotal Vaccinations on " + dateTimeForm.format(date) + " = " + appointmentsOnDays.get(date));
            }

            Label viewStatsTitle = new Label("Cumulative Statistics");
            Label cumulativeStatistics = new Label(totalStatistics.toString());
            GridPane statisticsPane = new GridPane();
            statisticsPane.add(viewStatsTitle, 0, 0);
            statisticsPane.add(new Label(), 0 , 1);
            statisticsPane.add(cumulativeStatistics, 0, 2);
            statisticsPane.setAlignment(Pos.CENTER);
            statisticsPane.setStyle("-fx-background-color: #aaaaff; -fx-font-size: 20; -fx-font-family: Verdana;");
            viewStatsTitle.setStyle("-fx-text-fill: #52595D; -fx-font-size: 40; -fx-font-weight: bold;");
            
            Scene statisticsScene = new Scene(statisticsPane, 600, 750);

            Stage statisticsStage = new Stage();
            statisticsStage.setTitle("Cumulative Statistics");
            statisticsStage.setScene(statisticsScene);
            statisticsStage.initModality(Modality.APPLICATION_MODAL);
            statisticsStage.showAndWait();
        });
    }

    //Password Verification
    private static int verifyMOHAccess(String password) {
        if (password.equals(MOH.getPassword()))
            return 1;
        
        else
            return -99;
    }

    private static int verifyVC(String name) {
        int vcIndex = -99;

        for (int i = 0; i < VCList.vacCenters.size(); i++) {
            if (name.equals(VCList.vacCenters.get(i).getName())){
                vcIndex = i;
                break;
            }
        }

        return vcIndex;
    }

    private static int verifyRecipient(String name, String phone) {
        int userIndex = -99;

        for (int i = 0; i < RecipientsList.recipients.size(); i++) {
            if (name.equals(RecipientsList.recipients.get(i).getName()) && phone.equals(RecipientsList.recipients.get(i).getPhone())) {
                userIndex = i;
                break;
            }
        }
        return userIndex;
    }

    private static boolean checkIndex(String index) {
        return index.matches("^[1-9]\\d?$|^[1-9]\\d\\d$");
    }

    public static void main (String[] args) {
        Application.launch(args);
    }
}
