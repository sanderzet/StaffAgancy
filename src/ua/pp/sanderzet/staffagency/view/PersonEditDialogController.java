package ua.pp.sanderzet.staffagency.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import jdk.nashorn.internal.codegen.CompilerConstants;
import ua.pp.sanderzet.staffagency.model.Person;
import ua.pp.sanderzet.staffagency.util.DateUtil;
import ua.pp.sanderzet.staffagency.util.ResourceBundleUtil;

import javax.security.auth.callback.Callback;
import java.net.URL;
import java.util.*;

/**
 * Created by alzet on 26.04.17.
 */
public class PersonEditDialogController implements Initializable  {

    @FXML
    private TextField firstNameField;
    @FXML
    private TextField lastNameField;
    @FXML
    private TextField passportField;
    @FXML
    private TextField phoneField;
    @FXML
    private TextField dateOfContractField;
    @FXML
    private TextField sanBookField;
    @FXML
    private TextField endOfVisaField;
    @FXML
    private TextField fileNumberField;
    @FXML
    private TextArea usualNoteTextArea;
    @FXML
    private TextField dateQuitField;
    @FXML
    private TextField criticalNoteField;

    @FXML
    private ComboBox<String> docComboBox;

    @FXML
    private Label dateOfContractLabel;
    @FXML
    private Label endOfVisaLabel;
    @FXML
    private Label dateQuitLabel;


    @FXML
    private Button okButton;
    @FXML
    private Button cancelButton;

    private ObservableList<Person> persons;
private Stage personAddStage;
private Person person;

//Person having passport with such number.
//Using for checking passport number unique.
private Person personWithSuchPassport;

private boolean okClicked = false;
private ResourceBundle bundle;
//    list of item for docChoisBox
  private   ObservableList<String> docChoiceBoxItemsList;
    private final String DOC_CHOICE_BOX_KEY = "choiceBoxDoc";
    private HashMap<String,String> docHashMap;

@Override
    public void initialize (URL url, ResourceBundle bundle) {
    this.bundle = bundle;
    docComboBox.setCellFactory(new javafx.util.Callback<ListView<String>, ListCell<String>>() {
                                   @Override
                                   public ListCell<String> call(ListView<String> param) {
                                       return new ListCell<String>(){
                                           @Override
                                           protected void updateItem(String item, boolean empty) {
                                               super.updateItem(item, empty);
                                               if (item == null || empty) {
                                                   setGraphic(null);
                                               } else {
                                                   setText(docHashMap.get(item));
                                               }
                                           }
                                       };
                                   }
                               });

docComboBox.setButtonCell(new ListCell<String>(){
    @Override
    protected void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);
        if (item == null || empty) {
            setGraphic(null);
        } else {
            setText(docHashMap.get(item));
        };
    }
});



//        If pressed Enter - button must fire (not only Space pressed)
            okButton.setOnKeyPressed(keyEvent -> {
                if (keyEvent.getCode() == KeyCode.ENTER) {
                    okButton.fire();
                }
            });
        cancelButton.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ENTER) {
                cancelButton.fire();
            }
        });

    }


    public void setPersonAddStage(Stage personAddStage) {
        this.personAddStage = personAddStage;
    }
public void setPersons(ObservableList<Person> persons) {this.persons = persons;}

    public void setPerson(Person person, HashMap<String, String> docHashMap){
    this.docHashMap = docHashMap;
        ObservableList docItems  = FXCollections.observableArrayList(docHashMap.keySet());
        docComboBox.setItems(docItems);
        this.person = person;
//        In those label  multi-row text must be converted to one-row
        dateOfContractLabel.setText(bundle.getString("table.dateOfContract").replaceAll("\n", " "));
        endOfVisaLabel.setText(bundle.getString("table.endOfVisa").replaceAll("\n"," "));
dateQuitLabel.setText(bundle.getString("table.endOfJob").replaceAll("\n"," "));

        firstNameField.setText(person.getFirstName());
        lastNameField.setText(person.getLastName());
        passportField.setText(person.getPassport());
        phoneField.setText(person.getPhone());
        dateOfContractField.setText(DateUtil.format(person.getDateOfContract()));
dateOfContractField.setPromptText("dd.mm.yyyy");
sanBookField.setText(person.getSanBook());

endOfVisaField.setText(DateUtil.format(person.getEndOfVisa()));
endOfVisaField.setPromptText("dd.mm.yyyy");

if(person.getDocument() != null && person.getDocument().length() != 0 ) {
    docComboBox.setValue(person.getDocument());
}

        fileNumberField.setText(person.getFileNumber());
        usualNoteTextArea.setText(person.getUsualNote());
        usualNoteTextArea.setWrapText(true);
criticalNoteField.setText(person.getCriticalNote());
dateQuitField.setText(DateUtil.format(person.getDateQuit()));
dateQuitField.setPromptText("dd.mm.yyyy");
    }


//public void setMainApp (MainApp mainApp) {
//        this.mainApp = mainApp;
//        jobTable.setItems(mainApp.getJobData());
//}

    public boolean isOkClicked(){
        return okClicked;
    }


@FXML
public void handleOk() {
        if (isInputValid()) {
            person.setLastName(lastNameField.getText());
            person.setFirstName(firstNameField.getText());
            person.setPassport(passportField.getText());
            person.setPhone(phoneField.getText());
            person.setDateOfContract(DateUtil.parse(dateOfContractField.getText()));
            person.setSanBook(sanBookField.getText());
            person.setEndOfVisa(DateUtil.parse(endOfVisaField.getText()));
            person.setFileNumber(fileNumberField.getText());
            person.setDateQuit(DateUtil.parse(dateQuitField.getText()));
            if(docComboBox.getValue() != null) person.setDocument(docComboBox.getValue());
                    else person.setDocument("");
            person.setUsualNote(usualNoteTextArea.getText());
            person.setCriticalNote(criticalNoteField.getText());
            okClicked = true;
            personAddStage.close();
        }
}


@FXML
public void handleCancel() {
        personAddStage.close();
}
// Passport must be unique
public boolean isPassportExist (){
          String passport = passportField.getText();
   for (Person person1 : persons) {
       if (!person.equals(person1) && person1.getPassport().equals(passport)) {
           personWithSuchPassport = person1;
           return true;
       }
   }
   return false;
}

//public Map.Entry<String,String> getEntryFromMap (HashMap<String,String> hashMap) {
//    Map.Entry<String,String> entry;
//    Set<Map.Entry<String,String>> set = hashMap.entrySet();
//    for (entry = set.i) {
//
//    }
//}


public boolean isInputValid () {
String errorMessage = "";

if (lastNameField.getText() == null || lastNameField.getLength() == 0)
    errorMessage += bundle.getString("message.noValidLastName")+"\n";
if (firstNameField.getText() == null || firstNameField.getLength() == 0)
        errorMessage += bundle.getString("message.noValidFirstName") + "\n";
if (passportField.getText() == null || passportField.getLength() == 0)
        errorMessage += bundle.getString("message.noValidPassport")+ "\n";
else{
    
    if (isPassportExist()){
        errorMessage += bundle.getString("message.passportNotUnique") + "\n" +
                personWithSuchPassport.getLastName() + " " +
                personWithSuchPassport.getFirstName() + " " +
                personWithSuchPassport.getPassport() + "\n";
    }
}

//If test == null - write "", or in db will be record "null".
if (sanBookField.getText() == null || sanBookField.getLength() == 0)
        sanBookField.setText("");
if (phoneField.getText() == null || phoneField.getLength() == 0)
phoneField.setText("");
if (fileNumberField.getText() == null || fileNumberField.getLength() == 0)
fileNumberField.setText("");
if (usualNoteTextArea.getText() == null) usualNoteTextArea.setText("");
if (criticalNoteField.getText() == null) criticalNoteField.setText("");
if (dateOfContractField.getText() == null || dateOfContractField.getLength() == 0)
    dateOfContractField.setText("");
else {
    if (!DateUtil.validDate(dateOfContractField.getText()))
        errorMessage += bundle.getString("message.noValidContractDate")+ "\n";
}

if (endOfVisaField.getText() == null || endOfVisaField.getLength() == 0)
endOfVisaField.setText("");
else {
    if (!DateUtil.validDate(endOfVisaField.getText()))
        errorMessage += bundle.getString("message.noValidVisaExpDate") + "\n";
}
if (dateQuitField.getText() == null || dateQuitField.getLength() == 0)
        dateQuitField.setText("");
    else {
        if (!DateUtil.validDate(dateQuitField.getText()))
            errorMessage += bundle.getString("message.noValidQuitDate") + "\n";
    }

if (errorMessage.length() == 0)
    return true;

else {
    Alert alert = new Alert(Alert.AlertType.ERROR);
    alert.initOwner(personAddStage);
    alert.setTitle("Invalid fields");
    alert.setHeaderText("Please correct invalid fields");
    alert.setContentText(errorMessage);
//    Text in alert window can be no seen completely so we need to do some manipulation
    alert.getDialogPane().getChildren().stream().filter(node -> node instanceof Label).forEach(node -> ((Label)node).setMinHeight(Region.USE_PREF_SIZE));
    alert.showAndWait();
    return false;
}

    }


}



