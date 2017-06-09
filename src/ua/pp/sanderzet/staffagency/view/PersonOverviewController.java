package ua.pp.sanderzet.staffagency.view;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.util.Callback;
import ua.pp.sanderzet.staffagency.MainApp;
import ua.pp.sanderzet.staffagency.model.Job;
import ua.pp.sanderzet.staffagency.model.Person;
import ua.pp.sanderzet.staffagency.util.DateUtil;
import ua.pp.sanderzet.staffagency.util.dbSqlite;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Optional;
import java.util.ResourceBundle;
import java.net.URL;
import java.util.function.Predicate;
import java.util.prefs.Preferences;

/**
 * Created by sander on 24.04.17.
 */
public class PersonOverviewController implements Initializable {

    @FXML
    private TextField filterField;
    @FXML
    private TableView<Person> personTable;

    @FXML
    private TableColumn<Person,String> firstNameColumn;
    @FXML
    private TableColumn<Person,String> lastNameColumn;
    @FXML
    private TableColumn<Person,String> passportColumn;
    @FXML
    private TableColumn<Person,String> phoneColumn;
    @FXML
    private TableColumn<Person,LocalDate> dateOfContractColumn;
    @FXML
    private TableColumn<Person,String> sanBookColumn;
    @FXML
    private TableColumn<Person,LocalDate> endOfVisaColumn;
    @FXML
    private TableColumn<Person,String> fileNumberColumn;
    @FXML
    private TableColumn<Person,String> documentColumn;
@FXML
private TableColumn<Person,String> criticalNoteColumn;
@FXML
private TableColumn<Person,LocalDate> dateQuitColumn;

@FXML
private TextArea usualNoteTextArea ;



    @FXML
    private TableView<Job> jobTable;

    @FXML
    private TableColumn<Job,String> placeColumn;
    @FXML
    private TableColumn<Job,String> firmColumn;
    @FXML
    private TableColumn<Job,String> positionColumn;
    @FXML
    private TableColumn<Job,LocalDate> transitionColumn;


    @FXML
    private Label personLabel;
    @FXML
    private Label jobLabel;
    @FXML
    private Label numberOfPersonsLabel;
    @FXML
    private Button personAddButton;
    @FXML
    private Button personEditButton;
    @FXML
    private Button personDelButton;
    @FXML
    private Button jobAddButton;
    @FXML
    private Button jobEditButton;
    @FXML
    private Button jobDelButton;
    @FXML
    private CheckBox allPersonCheckBox;


private MainApp mainApp;
private ObservableList<Person> persons = FXCollections.observableArrayList();
private FilteredList<Person> filteredPersons;
private SortedList<Person> sortedPersons;
private String numberOfPersons;
private ResourceBundle bundle;
private HashMap<String,String> docHashMap = new HashMap<>();

private Predicate<Person> personPredicate;

    /**
     * The constructor.
     * The constructor is called before the initialize() method.
     */
    public PersonOverviewController()  {
    }

    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
@Override
public void initialize (URL url, ResourceBundle bundle) {
    this.bundle = bundle;
    usualNoteTextArea.setWrapText(true);
//    Initialize personPredicate
    personPredicate = person -> true;


//    If pressed Enter - button must fire (not only Space pressed)
    javafx.event.EventHandler<KeyEvent> onEnterKeyEventHandler = (keyEvent -> {
        if (keyEvent.getCode() == KeyCode.ENTER) {
            if (keyEvent.getSource() instanceof Button) {
                Button button = (Button) keyEvent.getSource();
                button.fire();
            }
        }
    });


// This the same as above only without lambda

//    javafx.event.EventHandler<KeyEvent> onEnterKeyEventHandler = new javafx.event.EventHandler<KeyEvent>() {
//        @Override
//        public void handle(KeyEvent keyEvent) {
//
//            if(keyEvent.getCode() == KeyCode.ENTER) {
//                Button button = (Button)  keyEvent.getSource();
//                button.fire();
//            }
//        }
//    };

    personAddButton.setOnKeyPressed(onEnterKeyEventHandler);

    personEditButton.setOnKeyPressed(onEnterKeyEventHandler);

    personAddButton.setOnKeyPressed(onEnterKeyEventHandler);

    jobAddButton.setOnKeyPressed(onEnterKeyEventHandler);

    jobEditButton.setOnKeyPressed(onEnterKeyEventHandler);

    jobDelButton.setOnKeyPressed(onEnterKeyEventHandler);


    firstNameColumn.setCellValueFactory(cellData -> cellData.getValue().firstNameProperty());
    lastNameColumn.setCellValueFactory(cellData -> cellData.getValue().lastNameProperty());
    passportColumn.setCellValueFactory(cellData -> cellData.getValue().passportProperty());
    phoneColumn.setCellValueFactory(cellData -> cellData.getValue().phoneProperty());
    dateOfContractColumn.setCellValueFactory(cellData -> cellData.getValue().dateOfContractProperty());
    // For LocalData correct formatting need our CellFactory
    dateOfContractColumn.setCellFactory(new Callback<TableColumn<Person, LocalDate>, TableCell<Person, LocalDate>>() {
        @Override
        public TableCell<Person, LocalDate> call(TableColumn<Person, LocalDate> jobLocalDateTableColumn) {
            TextFieldTableCell<Person, LocalDate> cell = new TextFieldTableCell<Person, LocalDate>(DateUtil.localDateStringConverter) {
                @Override
                public void updateItem(LocalDate item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item == null || empty) {
                        setText(null);
                        setStyle("");
                    } else {
//                Format cell
//                    this.setTextFill(Color.RED);

                    }
                }
            };
            return cell;
        }
    });

    dateQuitColumn.setCellValueFactory(cellData -> cellData.getValue().dateQuitProperty());

    dateQuitColumn.setCellFactory(new Callback<TableColumn<Person, LocalDate>, TableCell<Person, LocalDate>>() {
        @Override
        public TableCell<Person, LocalDate> call(TableColumn<Person, LocalDate> personLocalDateTableColumn) {
            TextFieldTableCell<Person, LocalDate> cell = new TextFieldTableCell<Person, LocalDate>(DateUtil.localDateStringConverter) {
                @Override
                public void updateItem(LocalDate item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item == null || empty) {
                        setText(null);
                        setStyle("");
                    } else this.setTextFill(Color.BLUEVIOLET);
                }
            };
            return cell;
        }
    });

    sanBookColumn.setCellValueFactory(cellData -> cellData.getValue().sanBookProperty());
    endOfVisaColumn.setCellValueFactory(cellData -> cellData.getValue().endOfVisaProperty());

    endOfVisaColumn.setCellFactory(new Callback<TableColumn<Person, LocalDate>, TableCell<Person, LocalDate>>() {
        @Override
        public TableCell<Person, LocalDate> call(TableColumn<Person, LocalDate> jobLocalDateTableColumn) {
            TextFieldTableCell<Person, LocalDate> cell = new TextFieldTableCell<Person, LocalDate>(DateUtil.localDateStringConverter) {
                @Override
                public void updateItem(LocalDate item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item == null || empty) {
                        setText("");
                        setStyle("");
                    } else {
//    Format cell
//      If expiration date of visa less then after 45 days - text is marked by red color
                        if (item.isBefore(LocalDate.now().plusDays(45)))
                            this.setTextFill(Color.RED);
                    }
                }
            };
            return cell;
        }
    });


    fileNumberColumn.setCellValueFactory(cellData -> cellData.getValue().fileNumberProperty());
    documentColumn.setCellValueFactory(cellDate -> cellDate.getValue().documentProperty());
    documentColumn.setCellFactory(new Callback<TableColumn<Person, String>, TableCell<Person, String>>() {
        @Override
        public TableCell<Person, String> call(TableColumn<Person, String> param) {
            return new TextFieldTableCell<Person, String>() {
                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item == null || empty)
                        setText("");
                    else this.setText(docHashMap.get(item));
                }
            };
        }
    });
    criticalNoteColumn.setCellValueFactory(cellDate -> cellDate.getValue().criticalNoteProperty());

    criticalNoteColumn.setCellFactory(new Callback<TableColumn<Person, String>, TableCell<Person, String>>() {
        @Override
        public TableCell<Person, String> call(TableColumn<Person, String> param) {
            return new TextFieldTableCell<Person, String>() {
                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item == null || empty) setText("");
                    else setTextFill(Color.RED);

                }
            };
        }
    });

//persons.addListener((ListChangeListener<? super Person>) change -> {
//    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
//    alert.initOwner(mainApp.getPrimaryStage());
//    alert.setTitle("HOHOH");
//    alert.showAndWait();
//});
    personTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
        if (newValue != null) {
            personDelButton.setDisable(false);
            personEditButton.setDisable(false);
            jobAddButton.setDisable(false);
            usualNoteTextArea.setText(newValue.getUsualNote());
            showJob(newValue);

        }
    });
// Set listener on focus of personTable. If it have focus we activate buttons for person
    personTable.focusedProperty().addListener(new ChangeListener<Boolean>() {
        @Override
        public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean t1) {
//        Check if any item selected. If no, disable buttons that do something with selected item.
            boolean isItemSelected = !personTable.getSelectionModel().isEmpty();


            if (t1) {

//            If no item selected (if we navigate by TAB) - attempt to get first item selected
                if (!isItemSelected && personTable.getItems().size() > 0) {

                    personTable.getSelectionModel().selectFirst();
                    isItemSelected = !personTable.getSelectionModel().isEmpty();
                }


                if (isItemSelected) {
                    personDelButton.setDisable(false);
                    personEditButton.setDisable(false);
                    jobAddButton.setDisable(false);
                } else {
                    personDelButton.setDisable(true);
                    personEditButton.setDisable(true);
                    jobAddButton.setDisable(true);
                }
            }
//else {
////IF person table loss focus - disable buttons for editing and deleting
//            personDelButton.setDisable(true);
//            personEditButton.setDisable(true);
//        }

        }
    });

//Job table

    placeColumn.setCellFactory(TextFieldTableCell.forTableColumn());
    firmColumn.setCellFactory(TextFieldTableCell.forTableColumn());
    positionColumn.setCellFactory(TextFieldTableCell.forTableColumn());


    transitionColumn.setCellFactory(new Callback<TableColumn<Job, LocalDate>, TableCell<Job, LocalDate>>() {
        @Override
        public TableCell<Job, LocalDate> call(TableColumn<Job, LocalDate> jobLocalDateTableColumn) {
            TextFieldTableCell<Job, LocalDate> cell = new TextFieldTableCell<Job, LocalDate>(DateUtil.localDateStringConverter) {
                @Override
                public void updateItem(LocalDate item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item == null || empty) {
                        setText("");
                        setStyle("");
                    } else {
//                Format cell
//                        this.setTextFill(Color.RED);

                    }
                }
            };
            return cell;
        }
    });


    placeColumn.setCellValueFactory(cellData -> cellData.getValue().placeProperty());
    firmColumn.setCellValueFactory(cellData -> cellData.getValue().firmProperty());
    positionColumn.setCellValueFactory(cellData -> cellData.getValue().positionProperty());
    transitionColumn.setCellValueFactory(cellData -> cellData.getValue().transitionJobProperty());

    jobTable.focusedProperty().addListener(new ChangeListener<Boolean>() {
        @Override
        public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean t1) {
//        Check if any item selected. If no, disable buttons that do something with selected item.
            boolean isItemSelected = !jobTable.getSelectionModel().isEmpty();

            if (t1) {


//            If no item selected (if we navigate by TAB) - attempt to get first item selected
                if (!isItemSelected && jobTable.getItems().size() > 0) {

                    jobTable.getSelectionModel().selectFirst();
                    isItemSelected = !jobTable.getSelectionModel().isEmpty();
                }


                if (isItemSelected) {
                    jobDelButton.setDisable(false);
                    jobEditButton.setDisable(false);
                } else {
                    personDelButton.setDisable(true);
                    personEditButton.setDisable(true);
                }
            }

//else {
////IF job table loss focus - disable buttons for editing and deleting
//                jobEditButton.setDisable(true);
//                jobDelButton.setDisable(true);
//            }


        }
    });

//    Set the filter Predicate whenever the filter change
    filterField.textProperty().addListener((observableValue, oldValue, newValue) -> {
                Predicate<Person> filterFieldPredicate = person -> {
                    if (newValue == null || newValue.isEmpty())
                        return true;
                    String lowerCaseFilter = newValue.toLowerCase();
//            Filter on lastName or on Passport
                    if (person.getLastName().toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                    } else if (person.getPassport().toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                    }
//            else if (person.getDataOfContract().contains(lowerCaseFilter)) {
//                return true;
//            }
                    return false;
                };
                filteredPersons.setPredicate(personPredicate.and(filterFieldPredicate));
            }
        );



    allPersonCheckBox.selectedProperty().addListener(new ChangeListener<Boolean>() {

        @Override
        public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean t1) {
    filterField.setText("");
            personPredicate = person -> {
                if ( !t1) {
                    if (person.getDateQuit() != null && person.getDateQuit().isBefore(LocalDate.now())) {

                        return false;
                    }
                }
                return true;
            };
//Remember in preferences selected value of allPersonCheckBox
            Preferences preferences = Preferences.userNodeForPackage(MainApp.class);

            preferences.putBoolean(mainApp.getPREF_ALL_PERSON_CHECK_BOX(),t1);

            filteredPersons.setPredicate(personPredicate);
            }

    });
}



//    allPersonCheckBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
//        @Override
//        public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean t1) {
//            filteredPersons.setPredicate(person -> {
//                if (t1 != null && !t1)
//                    if (person.getDateQuit() != null && person.getDateQuit().isBefore(LocalDate.now())) {
//
//                        return false;
//                    }
//                return true;
//            });
//        }
//    });
//
//}



    /**
     * Is called by the main application to give a reference back to itself.
     *
     * @param mainApp
     *
     *
     *
     *   */
public void setMainApp(MainApp mainApp){

    this.mainApp = mainApp;
    persons = mainApp.getPersonData();
    docHashMap = mainApp.getDocHashMap();
    Preferences preferences = Preferences.userNodeForPackage(MainApp.class);



    filteredPersons = new FilteredList<Person>(persons, person -> true);
    sortedPersons = new SortedList<Person>(filteredPersons);
    //    Bind the sorted list comparator to the TableView comparator
    sortedPersons.comparatorProperty().bind(personTable.comparatorProperty());

    //    Set opposite value and then switch so we can initialize listener
//    allPersonCheckBox.fire();
    allPersonCheckBox.setSelected(!preferences.getBoolean(mainApp.getPREF_ALL_PERSON_CHECK_BOX(),false));
    allPersonCheckBox.fire();

    // Listener - if changing number of persons.
    sortedPersons.addListener((ListChangeListener<? super Person>) change -> {

        numberOfPersonsLabel.setText(bundle.getString("customers") + " : " + Integer.toString(sortedPersons.size()) +
        " / " + bundle.getString("all")+ " : " + persons.size());
    });

//    Initial value
    numberOfPersonsLabel.setText(bundle.getString("customers")+ " : " + Integer.toString(sortedPersons.size()) +
            " / " + bundle.getString("all")+ " : " + persons.size());


    personTable.setItems(sortedPersons);

}




@FXML
private void onAddPerson(){


    Person tempPerson = new Person();
//    It is a editing of new person,
//    so we need to INSERT in db

    boolean okClicked = mainApp.showPersonEditDialog(tempPerson);
if (okClicked) {
//    Select added person
    int resId = dbSqlite.insertPersonDb(tempPerson);
    if ( resId > 0)
    {
        tempPerson.setId(resId);
        mainApp.getPersonData().add(tempPerson);
        personTable.getSelectionModel().select(tempPerson);
    }
    else {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.initOwner(mainApp.getPrimaryStage());
        alert.setHeaderText("Error during adding entry to Persons db");
        alert.showAndWait();
    }
}

}

@FXML
private void onEditPerson() {
    Person selectedPerson = personTable.getSelectionModel().getSelectedItem();
    boolean okClicked =  mainApp.showPersonEditDialog(selectedPerson);
if (okClicked)
    mainApp.updatePersonDB(selectedPerson);
usualNoteTextArea.setText(selectedPerson.getUsualNote());
}

@FXML
private void onDelPerson() {
Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
alert.initOwner(mainApp.getPrimaryStage());
alert.setTitle("Are you sure ?");
alert.setHeaderText("Next entry will be deleted: ");
alert.setContentText(personTable.getSelectionModel().getSelectedItem().getLastName().toString() + " "
        + personTable.getSelectionModel().getSelectedItem().getFirstName().toString());
Optional<ButtonType> result = alert.showAndWait();
if (result.get() == ButtonType.OK) {

//Receive index in table
    int selectedIndex = personTable.getSelectionModel().getSelectedIndex();
// Receive   id in db
    int id = (personTable.getSelectionModel().getSelectedItem()).getId();
//    Remove from tableview and person list
    mainApp.getPersonData().remove(selectedIndex);
//        Remove from db
    mainApp.delPersonDb(id);
}
}

@FXML
private void onAddJob(){
Job jobTemp = new Job();
Person selectedPerson = personTable.getSelectionModel().getSelectedItem();
int personId = selectedPerson.getId();
String personName = selectedPerson.getLastName() + " " + selectedPerson.getFirstName();
jobTemp.setIdPerson(personId);
boolean okClicked = mainApp.showJobEditDialog(jobTemp, personName);

if(okClicked){ int resId = dbSqlite.insertJobDb(jobTemp);
    if ( resId > 0)
    {
        jobTemp.setRowid(resId);
        mainApp.getJobData(selectedPerson);
        jobTable.getSelectionModel().select(jobTemp);
    }
    else {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.initOwner(mainApp.getPrimaryStage());
        alert.setHeaderText("Error during adding entry to Jobs db");
        alert.showAndWait();
    }
}

}

@FXML
private void onEditJob(){
    Person selectedPerson = personTable.getSelectionModel().getSelectedItem();
    String personName = selectedPerson.getLastName() + " " + selectedPerson.getFirstName();

    Job job = jobTable.getSelectionModel().getSelectedItem();
boolean okClicked = mainApp.showJobEditDialog(job, personName);
    if (okClicked) {
        mainApp.updateJobDb(job);
    }
}


@FXML
private void onDelJob(){
    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
    alert.initOwner(mainApp.getPrimaryStage());
    alert.setTitle("Are you sure ?");
    alert.setHeaderText("For " + personTable.getSelectionModel().getSelectedItem().getLastName().toString() + " " +
            personTable.getSelectionModel().getSelectedItem().getFirstName().toString() +
            "\nnext job entry will be deleted: ");
    alert.setContentText(jobTable.getSelectionModel().getSelectedItem().getFirm().toString() + "\n"
            + jobTable.getSelectionModel().getSelectedItem().getPosition().toString());

    Optional<ButtonType> result = alert.showAndWait();
    if (result.get() == ButtonType.OK) {
//Receive index in table
        int selectedIndex = jobTable.getSelectionModel().getSelectedIndex();
// Receive   id in db
        int id = (jobTable.getSelectionModel().getSelectedItem()).getRowid();
//    Remove from tableview and person list
        jobTable.getItems().remove(selectedIndex);
//        Remove from db
        mainApp.delJobDb(id);
    }
}


private void showJob(Person person){
    jobTable.setItems(mainApp.getJobData(person));
}

//
//    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
//    alert.initOwner(mainApp.getPrimaryStage());
//    alert.setTitle("OK");
//    alert.setHeaderText(firstName);
//    alert.setContentText(lastName);
//    alert.show();


}
