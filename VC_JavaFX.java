import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.ReadOnlyObjectProperty;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;

public class VC_JavaFX extends Application {
    private static final DateTimeFormatter dateTimeForm = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    static int vcIndex;

    // labels
    private Label lblAskForPassword = new Label("Please enter your Vaccination Center's Password");
    private Label menuTitle = new Label("VACCINATION CENTER MENU");
    private Label setApmtTitle = new Label("Set Appointment Date");
    private Label lblStartIndex = new Label("Please enter the start number: ");
    private Label lblEndIndex = new Label("Please enter the end number: ");
    private Label lblPickDate = new Label("Please select a date: ");
    private Label updateStatusTitle = new Label("Update Recipient's Status");
    private Label lblRecipientName = new Label("Please enter recipient's name: ");
    private Label lblRecipientPhone = new Label("Please enter recipient's phone number: ");
    private Label lblVerifyPasswordMsg = new Label();
    private Label lblSetDateMsg = new Label();
    private Label lblUpdateStatusMsg = new Label();

    // buttons
    private Button passwordBtn = new Button("Enter");
    private Button btnOne = new Button("View all recipients' data");
    private Button btnTwo = new Button("Set recipient(s) appointment date");
    private Button btnThree = new Button("Update a recipient's status");
    private Button btnFour = new Button("View statistics of this VC");
    private Button setApmtBtn = new Button("Set Date");
    private Button updateStatusBtn = new Button("Update");
    private Button backBtn1 = new Button("Back");
    private Button backBtn2 = new Button("Back");
    private Button backBtn3 = new Button("Back");

    // textfields
    private TextField startIndex = new TextField();
    private TextField endIndex = new TextField();
    private TextField recipientName = new TextField();
    private TextField recipientPhone = new TextField();

    // passwordfields
    PasswordField vcPasswordField = new PasswordField();

    // date pickers
    DatePicker datePicker = new DatePicker();

    @SuppressWarnings("unchecked")
    @Override
    public void start(Stage mainStage) throws IOException {
        VCList.readVacCentersFromFile();
        RecipientsList.readRecipientsFromFile();

        GridPane gridPane = new GridPane();
        gridPane.add(lblAskForPassword, 0, 0);
        gridPane.add(vcPasswordField, 0, 1);
        gridPane.add(passwordBtn, 0, 2);
        gridPane.add(new Label(), 0, 3);
        gridPane.add(lblVerifyPasswordMsg, 0, 4);
        gridPane.setAlignment(Pos.CENTER);

        gridPane.setStyle("-fx-background-color: #CCCCFF; -fx-font-size: 20;");
        lblAskForPassword.setStyle("-fx-text-fill: #52595D; -fx-font-size: 30; -fx-font-family: Verdana");

        passwordBtn.setMaxWidth(200);

        Scene verifyPasswordScene = new Scene(gridPane, 850, 400);

        mainStage.setTitle("Vaccination Center");
        mainStage.setScene(verifyPasswordScene);
        mainStage.show();

        GridPane vcMenu = new GridPane();
        vcMenu.add(menuTitle, 0, 0);
        vcMenu.add(new Label(), 0, 1);
        vcMenu.add(btnOne, 0, 2);
        vcMenu.add(btnTwo, 0, 3);
        vcMenu.add(btnThree, 0, 4);
        vcMenu.add(btnFour, 0, 5);
        vcMenu.add(backBtn1, 0, 6);
        vcMenu.setAlignment(Pos.CENTER);

        vcMenu.setStyle("-fx-background-color: #CCCCFF; -fx-font-size: 20;");
        menuTitle.setStyle("-fx-text-fill: #52595D; -fx-font-size: 40; -fx-font-weight: bold; -fx-font-family: Verdana");

        btnOne.setMaxWidth(800);
        btnTwo.setMaxWidth(800);
        btnThree.setMaxWidth(800);
        btnFour.setMaxWidth(800);
        backBtn1.setMaxWidth(800);
        backBtn2.setMaxWidth(100);
        backBtn3.setMaxWidth(100);

        Scene vcMenuScene = new Scene(vcMenu, 1080, 720);

        backBtn1.setOnAction(e -> mainStage.setScene(verifyPasswordScene));
        backBtn2.setOnAction(e -> mainStage.setScene(vcMenuScene));
        backBtn3.setOnAction(e -> mainStage.setScene(vcMenuScene));

        passwordBtn.setOnAction(e -> {
            String password = vcPasswordField.getText();
            vcIndex = verifyVCAccess(password);
            if (vcIndex == -99) {
                lblVerifyPasswordMsg.setText("You do not have the access to the Vaccination Center.");
            }
            else {
                lblVerifyPasswordMsg.setText("");
                mainStage.setScene(vcMenuScene);
            }
        });

        btnOne.setOnAction(e -> {
            TableView<Recipient> table = new TableView<>();
            table.getItems().addAll(VC_ViewRecipient.getRecipientList());

            TableColumn<Recipient, Integer> indexColumn = new TableColumn<>("#");
            indexColumn.setCellFactory(col -> {
                TableCell<Recipient, Integer> indexCell = new TableCell<>();
                ReadOnlyObjectProperty<TableRow<Recipient>> rowProperty = indexCell.tableRowProperty();
                ObjectBinding<String> rowBinding = Bindings.createObjectBinding(() -> {
                    TableRow<Recipient> row = rowProperty.get();
                    if (row != null) { // can be null during CSS processing
                        int rowIndex = row.getIndex() + 1;
                        if (rowIndex <= row.getTableView().getItems().size()) {
                            return Integer.toString(rowIndex);
                        }
                    }
                    return null;
                }, rowProperty);
                indexCell.textProperty().bind(rowBinding);
                return indexCell;
            });

            // suppressed the warnings here
            table.getColumns().addAll(indexColumn, VC_ViewRecipient.getNameColumn(), VC_ViewRecipient.getPhoneColumn(),
                                      VC_ViewRecipient.getAgeColumn(),VC_ViewRecipient.getStatusColumn(),
                                      VC_ViewRecipient.getFirstApmtDateColumn(), VC_ViewRecipient.getSecondApmtDateColumn());
            // Set the column resize policy to constrained resize policy
            table.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
            // Set the Placeholder for an empty table
            table.setPlaceholder(new Label("No recipient currently in this Vaccination Center."));

            VBox displayBox = new VBox();
            displayBox.getChildren().add(table);
            displayBox.setStyle("-fx-padding: 10;" +
                                "-fx-border-style: solid inside;" +
                                "-fx-border-width: 4;" +
                                "-fx-border-insets: 8;" +
                                "-fx-border-radius: 8;" +
                                "-fx-border-color: blue;");
            
            Scene displayScene = new Scene(displayBox, 700, 450);

            Stage viewRecipientStage = new Stage();
            viewRecipientStage.setTitle("Recipients Data");
            viewRecipientStage.initOwner(mainStage);
            viewRecipientStage.initModality(Modality.WINDOW_MODAL);
            viewRecipientStage.setScene(displayScene);
            viewRecipientStage.showAndWait();
        });

        GridPane setApmtPane = new GridPane();
        setApmtPane.add(setApmtTitle, 0, 0);
        setApmtPane.add(new Label(), 0, 1);
        setApmtPane.add(lblStartIndex, 0, 2);
        setApmtPane.add(startIndex, 1, 2);
        setApmtPane.add(lblEndIndex, 0, 3);
        setApmtPane.add(endIndex, 1, 3);
        setApmtPane.add(lblPickDate, 0, 4);
        setApmtPane.add(datePicker, 1, 4);
        setApmtPane.add(new Label(), 0, 5);
        setApmtPane.add(setApmtBtn, 0, 6);
        setApmtPane.add(lblSetDateMsg, 1, 6, 1, 3);
        setApmtPane.add(backBtn2, 0, 7);
        setApmtPane.setAlignment(Pos.CENTER);

        setApmtPane.setStyle("-fx-background-color: #CCCCFF; -fx-font-size: 20;");
        setApmtTitle.setStyle("-fx-text-fill: #52595D; -fx-font-size: 40; -fx-font-weight: bold; -fx-font-family: Verdana");
        setApmtBtn.setMaxWidth(200);

        startIndex.setMaxSize(300, 50);
        endIndex.setMaxSize(300, 50);

        Scene setApmtDateScene = new Scene(setApmtPane, 1080, 720);

        btnTwo.setOnAction(e -> {
            mainStage.setScene(setApmtDateScene);

            TableView<Recipient> table = new TableView<>();
            table.getItems().addAll(VC_ViewRecipient.getRecipientList());

            TableColumn<Recipient, Integer> indexColumn = new TableColumn<>("#");
            indexColumn.setCellFactory(col -> {
                TableCell<Recipient, Integer> indexCell = new TableCell<>();
                ReadOnlyObjectProperty<TableRow<Recipient>> rowProperty = indexCell.tableRowProperty();
                ObjectBinding<String> rowBinding = Bindings.createObjectBinding(() -> {
                    TableRow<Recipient> row = rowProperty.get();
                    if (row != null) { // can be null during CSS processing
                        int rowIndex = row.getIndex() + 1;
                        if (rowIndex <= row.getTableView().getItems().size()) {
                            return Integer.toString(rowIndex);
                        }
                    }
                    return null;
                }, rowProperty);
                indexCell.textProperty().bind(rowBinding);
                return indexCell;
            });

            // suppressed the warnings here
            table.getColumns().addAll(indexColumn, VC_ViewRecipient.getNameColumn(), VC_ViewRecipient.getPhoneColumn(),
                                      VC_ViewRecipient.getAgeColumn(),VC_ViewRecipient.getStatusColumn(),
                                      VC_ViewRecipient.getFirstApmtDateColumn(), VC_ViewRecipient.getSecondApmtDateColumn());
            // Set the column resize policy to constrained resize policy
            table.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
            // Set the Placeholder for an empty table
            table.setPlaceholder(new Label("No recipient currently in this Vaccination Center."));

            VBox displayBox = new VBox();
            displayBox.getChildren().add(table);
            displayBox.setStyle("-fx-padding: 10;" +
                                "-fx-border-style: solid inside;" +
                                "-fx-border-width: 4;" +
                                "-fx-border-insets: 8;" +
                                "-fx-border-radius: 8;" +
                                "-fx-border-color: blue;");
            
            Scene displayScene = new Scene(displayBox, 700, 450);

            Stage viewRecipientStage = new Stage();
            viewRecipientStage.setTitle("Recipients Data");
            viewRecipientStage.initOwner(mainStage);
            viewRecipientStage.initModality(Modality.NONE);
            viewRecipientStage.setScene(displayScene);
            viewRecipientStage.showAndWait();
        });

        setApmtBtn.setOnAction(e -> {
            try {
                if (!startIndex.getText().isEmpty() && !endIndex.getText().isEmpty() && datePicker.getValue() != null &&
                    datePicker.getValue().isAfter(LocalDate.of(2019, 12, 31)) && checkIndex(startIndex.getText()) && checkIndex(endIndex.getText()))
                {
                    int day = datePicker.getValue().getDayOfMonth();
                    int month = datePicker.getValue().getMonthValue();
                    int year = datePicker.getValue().getYear();
                    int startNum = Integer.parseInt(startIndex.getText());
                    int endNum = Integer.parseInt(endIndex.getText());
                    int userQuantity = (endNum - startNum) + 1;
                    VC vacCenter = VCList.vacCenters.get(vcIndex);
                    ArrayList<Recipient> recipients = vacCenter.getRecipients();

                    if (userQuantity <= vacCenter.getCapacityPerDay() && startNum <= endNum &&
                        startNum <= recipients.size() && endNum <= recipients.size())
                    {
                        lblSetDateMsg.setText(setAppointmentsDate(startNum, endNum, year, month, day));
                        VCList.saveVacCentersToFile();
                        RecipientsList.saveRecipientsToFile();
                    }
                    else if (startNum > endNum) {
                        lblSetDateMsg.setText("The start number should be less than or equal to the end number");
                    }
                    else if (startNum > recipients.size() || endNum > recipients.size()) {
                        lblSetDateMsg.setText("You've entered invalid recipient indexes");
                    }
                    else {
                        lblSetDateMsg.setText("Users selected have exceeded the capacity per day of the Vaccination Center.");
                    }
                }
                else {
                    lblSetDateMsg.setText("Appointment date failed to be set." +
                                          "\nYou must fill in the text box correctly." + 
                                          "\nValid number: 1 - 999 represents the user index." +
                                          "\nValid date is from 1/1/2020.");
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        GridPane updateStatusPane = new GridPane();
        updateStatusPane.add(updateStatusTitle, 0, 0);
        updateStatusPane.add(new Label(), 0, 1);
        updateStatusPane.add(lblRecipientName, 0, 2);
        updateStatusPane.add(recipientName, 1, 2);
        updateStatusPane.add(lblRecipientPhone, 0, 3);
        updateStatusPane.add(recipientPhone, 1, 3);
        updateStatusPane.add(new Label(), 0, 4);
        updateStatusPane.add(updateStatusBtn, 0, 5);
        updateStatusPane.add(lblUpdateStatusMsg, 1, 5, 1, 2);
        updateStatusPane.add(backBtn3, 0, 6);
        updateStatusPane.setAlignment(Pos.CENTER);

        updateStatusPane.setStyle("-fx-background-color: #CCCCFF; -fx-font-size: 20;");
        updateStatusTitle.setStyle("-fx-text-fill: #52595D; -fx-font-size: 40; -fx-font-weight: bold; -fx-font-family: Verdana");
        updateStatusBtn.setMaxWidth(200);

        recipientName.setMaxSize(300, 50);
        recipientPhone.setMaxSize(300, 50);

        Scene updateStatusScene = new Scene(updateStatusPane, 1080, 720);
        btnThree.setOnAction(e -> mainStage.setScene(updateStatusScene));

        updateStatusBtn.setOnAction(e -> {
            if (!recipientName.getText().isEmpty() && !recipientPhone.getText().isEmpty()) {
                String name = recipientName.getText();
                String phone = recipientPhone.getText();

                int userIndex = verifyRecipient(name, phone);
                if (userIndex == -99) {
                    lblUpdateStatusMsg.setText("Accound not found, please try again.");
                }
                else {
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
                                lblUpdateStatusMsg.setText("**ACCOUNT FOUND**" + 
                                                           "\nRecipient's status failed to be updated as vaccination center is currently out of vaccine.");
                                return;
                            }
                            recipient.setFirstDoseBatch(vacCentre.usedVaccine());
                        }
                        else if (userStatus == 3 && recipient.getSecondApmtDate() != null &&
                                 recipient.getSecondApmtDate().compareTo(LocalDate.now()) <= 0)
                        {
                            if (vaccineQty == 0) {
                                lblUpdateStatusMsg.setText("**ACCOUNT FOUND**" + 
                                                           "\nRecipient's status failed to be updated as vaccination center is currently out of vaccine.");
                                return;
                            }
                            recipient.setSecondDoseBatch(vacCentre.usedVaccine());
                        }
                        else if (userStatus == 1 && recipient.getFirstApmtDate() == null ||
                                 userStatus == 3 && recipient.getSecondApmtDate() == null)
                        {
                            lblUpdateStatusMsg.setText("**ACCOUNT FOUND**" +
                                                       "\nRecipient's status failed to be updated as his/her appointment date hasn't been updated.");
                            return;
                        }
                        else if (userStatus == 1 && recipient.getFirstApmtDate().compareTo(LocalDate.now()) > 0 ||
                                 userStatus == 3 && recipient.getSecondApmtDate().compareTo(LocalDate.now()) > 0)
                        {
                            lblUpdateStatusMsg.setText("**ACCOUNT FOUND**" + 
                                                       "\nRecipient's status failed to be updated as his/her appointment date is beyond current date.");
                            return;
                        }
                        recipient.updateStatus();
                        try {
                            VCList.saveVacCentersToFile();
                            RecipientsList.saveRecipientsToFile();
                            lblUpdateStatusMsg.setText("**ACCOUNT FOUND**" + 
                                                       "\n**UPDATE SUCCESSFUL**" + 
                                                       "\nRecipient's status has been updated from " + userStatusString + " to " +
                                                       recipient.getStatus() + ".");
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    }
                    else {
                        lblUpdateStatusMsg.setText("**ACCOUNT FOUND**" + 
                                                   "\nRecipient status failed to be updated as the recipient is already fully vaccinated.");
                    }
                }
            }
            else {
                lblUpdateStatusMsg.setText("Update status failed, you must fill in the text box.");
            }
        });

        btnFour.setOnAction(e -> {
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

            StringBuilder statsOfVC = new StringBuilder();

            statsOfVC.append ("Capacity Per Day = " + vacCentre.getCapacityPerDay());
            statsOfVC.append ("\nVaccine Quantity = " + vacCentre.getVaccineQty());
            statsOfVC.append ("\n\nTotal Pending Recipients = " + totalPending);
            statsOfVC.append ("\nTotal First Dose Appointments = " + totalFirstDoseAppointments);
            statsOfVC.append ("\nTotal First Dose Completions = " + totalFirstDoseCompletions);
            statsOfVC.append ("\nTotal Second Dose Appointments = " + totalSecondDoseAppointments);
            statsOfVC.append ("\nTotal Second Dose Completions = " + totalSecondDoseCompletions);
            statsOfVC.append ("\nTotal Vaccinations = " + totalVaccination + "\n");
            for (LocalDate date = LocalDate.now(); date.compareTo(LocalDate.of(2020, 1, 1)) >= 0; date = date.minusDays(1)) {
                if (apmtsOnDays.containsKey(date))
                    statsOfVC.append ("\nTotal Vaccinations on " + dateTimeForm.format(date) + " = " + apmtsOnDays.get(date));
            }

            Label lblViewStatsTitle = new Label("Statistics of " + vacCentre.getName());
            Label lblStatistics = new Label(statsOfVC.toString());
            GridPane vcStatsPane = new GridPane();
            vcStatsPane.add(lblViewStatsTitle, 0, 0);
            vcStatsPane.add(new Label(), 0, 1);
            vcStatsPane.add(lblStatistics, 0, 2);
            vcStatsPane.setAlignment(Pos.CENTER);
            vcStatsPane.setStyle("-fx-background-color: #aaaaff; -fx-font-size: 20; -fx-font-family: Verdana;");

            lblViewStatsTitle.setStyle("-fx-text-fill: #52595D; -fx-font-size: 40; -fx-font-weight: bold;");
            Scene viewStatsScene = new Scene(vcStatsPane, 600, 750);

            Stage viewStatsStage = new Stage();
            viewStatsStage.setTitle("View VC Statistics");
            viewStatsStage.setScene(viewStatsScene);
            viewStatsStage.initModality(Modality.APPLICATION_MODAL);
            viewStatsStage.showAndWait();
        });
    }

    private static int verifyVCAccess(String password) {
        for (int i = 0; i < VCList.vacCenters.size();  i++)
            if (password.equals(VCList.vacCenters.get(i).getPassword()))
                return i;
        return -99;
    }

    private static boolean checkIndex(String index) {
        return index.matches("^[1-9]\\d?$|^[1-9]\\d\\d$");
    }

    private static String setAppointmentsDate(int firstUserIndex, int lastUserIndex, int year, int month, int day) {
        LocalDate appointmentDate = LocalDate.of (year, month, day);
        StringBuilder message = new StringBuilder();

        for(int i = firstUserIndex - 1; i < lastUserIndex; i++) {
            Recipient recipient = VCList.vacCenters.get(vcIndex).getRecipient(i);
            if (recipient.getStatusIndex() == 1) {
                recipient.setFirstApmtDate(year, month, day);
            }
            else if (recipient.getStatusIndex() == 2) {
                message.append (recipient.getName() + "'s appointment date failed to be updated as he/she has just completed 1st dose.\n");
            }
            else if (recipient.getStatusIndex() == 3) {
                recipient.setSecondApmtDate(year, month, day);
            }
            else if (recipient.getStatusIndex() == 4) {
                message.append (recipient.getName() + "'s appointment date failed to be updated as he/she is already fully vaccinated.\n");
            }
            else {
                message.append (recipient.getName() + "'s appointment date failed to be updated as his/her status are still currently pending.\n");
            }
        }
        message.append ("**UPDATE SUCCESSFUL**\n");
        message.append ("Except the Invalid Appointment Date Recipients\n"+
                        "All Recipients's appointment date has been successfully updated to " + dateTimeForm.format(appointmentDate) + ".");

        return message.toString();
    }

    private static int verifyRecipient(String name, String phone) {
        int userIndex = -99;
        VC vacCentre = VCList.vacCenters.get(vcIndex);

        for (int i = 0; i < vacCentre.getRecipients().size(); i++) {
            Recipient recipient = vacCentre.getRecipient(i);

            if (name.equals(recipient.getName()) && phone.equals(recipient.getPhone())) {
                userIndex = i;
                break;
            }
        }
        return userIndex;
    }

    public static void main(String[] args) {
        Application.launch(args);
    }
}
