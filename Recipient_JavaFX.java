import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Recipient_JavaFX extends Application {
    private static final DateTimeFormatter dateTimeForm = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    
    // labels
    private Label menuTitle = new Label("RECIPIENT MENU");
    private Label registerTitle = new Label("Register new account");
    private Label lblName = new Label("Please enter your full name: "); 
    private Label lblPhone = new Label("Please enter your phone number: ");
    private Label lblAge = new Label("Please enter your age: ");
    private Label signInTitle = new Label("Sign-in to check status");
    private Label lblSignInName = new Label("Please enter your name: ");
    private Label lblSignInPhone = new Label("Please enter your phone number: ");
    private Label lblRegistrationMsg = new Label();
    private Label lblSignInMsg = new Label();

    // buttons
    private Button btnOne = new Button("Register a new recipient account");
    private Button btnTwo = new Button("Sign-in to check status");
    private Button registerBtn = new Button("Register");
    private Button signInBtn = new Button("Sign-in");
    private Button backBtn1 = new Button("Back");
    private Button backBtn2 = new Button("Back");

    // textfields
    private final TextField recipientName = new TextField();
    private final TextField recipientPhone = new TextField();
    private final TextField recipientAge = new TextField();
    private final TextField signInName = new TextField();
    private final TextField signInPhone = new TextField();

    @Override
    public void start(Stage mainStage) throws IOException {
        GridPane gridPane = new GridPane();
        gridPane.add(menuTitle, 0, 0);
        gridPane.add(new Label(), 0, 1);
        gridPane.add(btnOne, 0, 2);
        gridPane.add(btnTwo, 0, 3);
        gridPane.setAlignment(Pos.CENTER);

        gridPane.setStyle("-fx-background-color: #CCCCFF; -fx-font-size: 20;");
        menuTitle.setStyle("-fx-text-fill: #52595D; -fx-font-size: 50; -fx-font-weight: bold; -fx-font-family: Verdana");
        lblRegistrationMsg.setStyle("-fx-font-size: 15;");
        lblSignInMsg.setStyle("-fx-font-size: 15;");

        btnOne.setMaxWidth(800);
        btnTwo.setMaxWidth(800);

        Scene recipientMenu = new Scene(gridPane, 1080, 720);

        mainStage.setTitle("Recipient");
        mainStage.setScene(recipientMenu);
        mainStage.show();

        VCList.readVacCentersFromFile();
        RecipientsList.readRecipientsFromFile();

        registerBtn.setStyle("-fx-font-size: 25;");
        signInBtn.setStyle("-fx-font-size: 25;");
        backBtn1.setOnAction(e -> mainStage.setScene(recipientMenu));
        backBtn2.setOnAction(e -> mainStage.setScene(recipientMenu));

        GridPane registrationPane = new GridPane();
        registrationPane.add(registerTitle, 0, 0);
        registrationPane.add(lblName, 0, 1);
        registrationPane.add(recipientName, 1, 1);
        registrationPane.add(lblPhone, 0, 2);
        registrationPane.add(recipientPhone, 1, 2);
        registrationPane.add(lblAge, 0, 3);
        registrationPane.add(recipientAge, 1, 3);
        registrationPane.add(new Label(), 0, 4);
        registrationPane.add(registerBtn, 0, 5);
        registrationPane.add(lblRegistrationMsg, 1, 5, 1, 2);
        registrationPane.add(backBtn1, 0, 6);
        registrationPane.setAlignment(Pos.CENTER);

        registrationPane.setStyle("-fx-background-color: #CCCCFF; -fx-font-size: 20;");
        registerTitle.setStyle("-fx-text-fill: #52595D; -fx-font-size: 40; -fx-font-weight: bold; -fx-font-family: Verdana");
        recipientName.setMaxSize(300, 50);
        recipientPhone.setMaxSize(300, 50);
        recipientAge.setMaxSize(300, 50);
        Scene registrationScene = new Scene(registrationPane, 1080, 720);
        btnOne.setOnAction(e -> mainStage.setScene(registrationScene));

        registerBtn.setOnAction(e -> {
            if (!recipientName.getText().isEmpty() && !recipientPhone.getText().isEmpty() && !recipientAge.getText().isEmpty()) {
                String name = recipientName.getText();
                String phone = recipientPhone.getText();
                String age = recipientAge.getText();

                boolean validName = checkName(name);
                boolean validPhone = checkPhone(phone);
                boolean validAge = checkAge(age);
                boolean phoneRegistered = checkPhoneRegistered(phone);

                if (validName && validPhone && validAge && !phoneRegistered) {
                    int ageChecked = Integer.parseInt(age);
                    try {
                        RecipientsList.recipients.add(new Recipient(name, phone, ageChecked));
                        RecipientsList.saveRecipientsToFile();
                        lblRegistrationMsg.setText("**REGISTRATION SUCCESSFUL**\nYour account has been created.");
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
                else if (phoneRegistered) {
                    lblRegistrationMsg.setText("Registration failed, phone has already been used.");
                }
                else if (!validName && !validPhone && !validAge) {
                    lblRegistrationMsg.setText("Registration failed, invalid name, phone and age.\n" +
                                                "Example of valid name: Steven Tan Chung Hong\n" +
                                                "Example of valid phone: 0123456789\n" +
                                                "Example of valid age : 0 - 122");
                }
                else if (!validName && !validPhone) {
                    lblRegistrationMsg.setText("Registration failed, invalid name and phone.\n" +
                                                "Example of valid name: Steven Tan Chung Hong\n" +
                                                "Example of valid phone: 0123456789");
                }
                else if (!validName && !validAge) {
                    lblRegistrationMsg.setText("\nRegistration failed, invalid name and age.\n" +
                                                "Example of valid name: Steven Tan Chung Hong\n" +
                                                "Example of valid age : 0 - 122");
                }
                else if (!validPhone && !validAge) {
                    lblRegistrationMsg.setText("\nRegistration failed, invalid phone and age.\n" +
                                                "Example of valid phone: 0123456789\n" +
                                                "Example of valid age : 0 - 122");
                }
                else if (!validName) {
                    lblRegistrationMsg.setText("Registration failed, invalid name.\n" +
                                                "Example of valid name: Steven Tan Chung Hong");
                }
                else if (!validPhone) {
                    lblRegistrationMsg.setText("Registration failed, invalid phone.\n" +
                                                "Example of valid phone: 0123456789");
                }
                else if (!validAge) {
                    lblRegistrationMsg.setText("Registration failed, invalid age.\n" +
                                                "Please enter an integer between 0 and 122.");
                }
            }
            else {
                lblRegistrationMsg.setText("Registration failed, you must fill in the text box.");
            }
        });

        GridPane loginPane = new GridPane();
        loginPane.add(signInTitle, 0, 0);
        loginPane.add(lblSignInName, 0, 1);
        loginPane.add(signInName, 1, 1);
        loginPane.add(lblSignInPhone, 0, 2);
        loginPane.add(signInPhone, 1, 2);
        loginPane.add(new Label(), 0, 3);
        loginPane.add(signInBtn, 0, 4);
        loginPane.add(lblSignInMsg, 1, 4, 1, 2);
        loginPane.add(backBtn2, 0, 5);
        loginPane.setAlignment(Pos.CENTER);

        loginPane.setStyle("-fx-background-color: #CCCCFF; -fx-font-size: 20;");
        signInTitle.setStyle("-fx-text-fill: #52595D; -fx-font-size: 40; -fx-font-weight: bold; -fx-font-family: Verdana");
        signInName.setMaxSize(300, 50);
        signInPhone.setMaxSize(300, 50);
        Scene signInScene = new Scene(loginPane, 1080, 720);
        btnTwo.setOnAction(e -> mainStage.setScene(signInScene));

        signInBtn.setOnAction(e -> {
            if (!signInName.getText().isEmpty() && !signInPhone.getText().isEmpty()) {
                String name = signInName.getText();
                String phone = signInPhone.getText();

                int userIndex = verifyAcc(name, phone);
                if (userIndex == -99) {
                    lblSignInMsg.setText("Account not found, please try again.");
                }
                else {
                    Recipient recipient = RecipientsList.recipients.get(userIndex);
                    StringBuilder userStatus = new StringBuilder();

                    if (recipient.getAssignedToVC() == 0) {
                        userStatus.append ("\nHello, " + recipient.getName() + ".\n" +
                                           "Your current status: " + recipient.getStatus());
                    }
                    else {
                        for (int i = 0; i < VCList.vacCenters.size(); i++) {
                            if (i == recipient.getAssignedToVC() - 1) {
                                userStatus.append ("\nHello, " + recipient.getName() + ".\n" +
                                                   "Your current status: " + recipient.getStatus() +
                                                   " at " + VCList.vacCenters.get(i).getName());
                                if (recipient.getStatusIndex() >= 1) {
                                    LocalDate appointmentDate = recipient.getFirstApmtDate();
                                    if (recipient.getStatusIndex() == 1)
                                        userStatus.append ("\n1st Dose Appointment - ");
                                    else
                                        userStatus.append ("\n1st Dose Completed - ");
                                    
                                    if (recipient.getFirstApmtDate() != null)
                                        userStatus.append (dateTimeForm.format(appointmentDate));
                                    else
                                        userStatus.append ("Date haven't been assigned");
                                }
                                if (recipient.getStatusIndex() >= 2)
                                    userStatus.append ("\nBatch: " + recipient.getFirstDoseBatch());
            
                                if (recipient.getStatusIndex() >= 3) {
                                    LocalDate appointmentDate = recipient.getSecondApmtDate();
                                    if (recipient.getSecondApmtDate() != null)
                                        userStatus.append ("\n" + recipient.getStatus() + " - " + dateTimeForm.format(appointmentDate));
                                    else
                                        userStatus.append ("\n" + recipient.getStatus() + " - Date haven't been assigned");
                                }
                                if (recipient.getStatusIndex() == 4) 
                                userStatus.append ("\nBatch: " + recipient.getSecondDoseBatch());
                            }
                        }
                    }

                    Label lblUserStatus = new Label(userStatus.toString());
                    BorderPane userStatusPane = new BorderPane();
                    userStatusPane.setCenter(lblUserStatus);
                    userStatusPane.setStyle("-fx-background-color: #aaaaff; -fx-font-size: 25; -fx-font-family: Verdana;");

                    Scene userStatusScene = new Scene(userStatusPane, 700, 400);

                    Stage userStatusStage = new Stage();
                    userStatusStage.setTitle("Recipient Status");
                    userStatusStage.setScene(userStatusScene);
                    userStatusStage.initModality(Modality.APPLICATION_MODAL);
                    userStatusStage.showAndWait();
                }
            }
            else {
                lblSignInMsg.setText("Login failed, you must fill in the text box");
            }
        });
    }

    private static boolean checkName(String name) {
        return name.matches("^[a-zA-Z ]+$");
    }

    private static boolean checkPhone(String phone) {
        return phone.matches("[0][1-9][0-9]{8,9}");
    }

    private static boolean checkAge(String age) {
        return age.matches("^12[0-2]$|^1[0-1]\\d$|^[1-9]?\\d$");
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

    private static int verifyAcc(String name, String phone) {
        int userIndex = -99;

        for (int i = 0; i < RecipientsList.recipients.size(); i++)
            if (name.equals(RecipientsList.recipients.get(i).getName()) && phone.equals(RecipientsList.recipients.get(i).getPhone())) {
                userIndex = i;
                break;
            }
        return userIndex;
    }

    public static void main(String[] args) {
        Application.launch(args);
    }
}