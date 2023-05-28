import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
 
public class VC_ViewRecipient {
    // Returns an observable list of Recipients
    public static ObservableList<Recipient> getRecipientList() 
    {
        return FXCollections.<Recipient>observableArrayList(VCList.vacCenters.get(VC_JavaFX.vcIndex).getRecipients());
    }
     
    // Returns Recipient Name TableColumn
    public static TableColumn<Recipient, String> getNameColumn() 
    {
        TableColumn<Recipient, String> nameCol = new TableColumn<>("Name");
        PropertyValueFactory<Recipient, String> nameCellValueFactory = new PropertyValueFactory<>("name");
        nameCol.setCellValueFactory(nameCellValueFactory);
        return nameCol;
    }
     
    // Returns Phone No. TableColumn
    public static TableColumn<Recipient, String> getPhoneColumn() 
    {
        TableColumn<Recipient, String> phoneCol = new TableColumn<>("Phone");
        PropertyValueFactory<Recipient, String> phoneCellValueFactory = new PropertyValueFactory<>("phone");
        phoneCol.setCellValueFactory(phoneCellValueFactory);
        return phoneCol;
    }
     
    // Returns Age TableColumn
    public static TableColumn<Recipient, Integer> getAgeColumn() 
    {
        TableColumn<Recipient, Integer> ageCol = new TableColumn<>("Age");
        PropertyValueFactory<Recipient, Integer> ageCellValueFactory = new PropertyValueFactory<>("age");
        ageCol.setCellValueFactory(ageCellValueFactory);
        return ageCol;
    }
 
    // Returns Status TableColumn 
    public static TableColumn<Recipient, String> getStatusColumn() 
    {
        TableColumn<Recipient, String> statusCol = new TableColumn<>("Vaccination Status");
        PropertyValueFactory<Recipient, String> statusCellValueFactory = new PropertyValueFactory<>("currentStatus");
        statusCol.setCellValueFactory(statusCellValueFactory);
        return statusCol;
    }   
 
    // Returns First Appointment Date TableColumn
    public static TableColumn<Recipient, String> getFirstApmtDateColumn() 
    {
        TableColumn<Recipient, String> apmtDateCol = new TableColumn<>("1st Appointment Date");
        PropertyValueFactory<Recipient, String> apmtDateCellValueFactory = new PropertyValueFactory<>("formattedFirstApmtDate");
        apmtDateCol.setCellValueFactory(apmtDateCellValueFactory);
        return apmtDateCol;
    }

    // Returns Second Appointment Date TableColumn
    public static TableColumn<Recipient, String> getSecondApmtDateColumn() 
    {
        TableColumn<Recipient, String> apmtDateCol = new TableColumn<>("2nd Appointment Date");
        PropertyValueFactory<Recipient, String> apmtDateCellValueFactory = new PropertyValueFactory<>("formattedSecondApmtDate");
        apmtDateCol.setCellValueFactory(apmtDateCellValueFactory);
        return apmtDateCol;
    }
}
