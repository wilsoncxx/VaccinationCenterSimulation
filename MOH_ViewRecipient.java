import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;

public class MOH_ViewRecipient {
    public static ObservableList<Recipient> getRecipientList() {
        return FXCollections.<Recipient>observableArrayList(RecipientsList.recipients);
    }

    public static TableColumn<Recipient, String> getNameColumn() {
        TableColumn<Recipient, String> nameColumn = new TableColumn<>("Name");
        PropertyValueFactory<Recipient, String> nameCellValueFactory = new PropertyValueFactory<>("name");
        nameColumn.setCellValueFactory(nameCellValueFactory);
        return nameColumn;
    }

    public static TableColumn<Recipient, String> getPhoneColumn() {
        TableColumn<Recipient, String> phoneColumn = new TableColumn<>("Phone");
        PropertyValueFactory<Recipient, String> phoneCellValueFactory = new PropertyValueFactory<>("phone");
        phoneColumn.setCellValueFactory(phoneCellValueFactory);
        return phoneColumn;
    }

    public static TableColumn<Recipient, String> getAgeColumn() {
        TableColumn<Recipient, String> ageColumn = new TableColumn<>("Age");
        PropertyValueFactory<Recipient, String> ageCellValueFactory = new PropertyValueFactory<>("age");
        ageColumn.setCellValueFactory(ageCellValueFactory);
        return ageColumn;
    }

    public static TableColumn<Recipient, String> getStatusColumn() {
        TableColumn<Recipient, String> statusColumn = new TableColumn<>("Vaccination Status");
        PropertyValueFactory<Recipient, String> statusCellValueFactory = new PropertyValueFactory<>("currentStatus");
        statusColumn.setCellValueFactory(statusCellValueFactory);
        return statusColumn;
    }

    public static TableColumn<Recipient, String> getFirstAppointmentDateColumn() 
    {
        TableColumn<Recipient, String> apmtDateCol = new TableColumn<>("1st Appointment Date");
        PropertyValueFactory<Recipient, String> apmtDateCellValueFactory = new PropertyValueFactory<>("formattedFirstApmtDate");
        apmtDateCol.setCellValueFactory(apmtDateCellValueFactory);
        return apmtDateCol;
    }

    public static TableColumn<Recipient, String> getSecondAppointmentDateColumn() 
    {
        TableColumn<Recipient, String> apmtDateCol = new TableColumn<>("2nd Appointment Date");
        PropertyValueFactory<Recipient, String> apmtDateCellValueFactory = new PropertyValueFactory<>("formattedSecondApmtDate");
        apmtDateCol.setCellValueFactory(apmtDateCellValueFactory);
        return apmtDateCol;
    }

    public static TableColumn<Recipient, String> getAssignedToVCColumn()
    {
        TableColumn<Recipient, String> assignedToVCCol = new TableColumn<>("Vaccination Center");
        PropertyValueFactory<Recipient, String> assignedToVCCellValueFactory = new PropertyValueFactory<>("assignedToVCString");
        assignedToVCCol.setCellValueFactory(assignedToVCCellValueFactory);
        return assignedToVCCol;
    }
}
