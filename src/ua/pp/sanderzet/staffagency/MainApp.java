package ua.pp.sanderzet.staffagency;

import javafx.application.Application;
import javafx.beans.binding.Binding;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXMLLoader;
import javafx.print.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.transform.Scale;
import javafx.scene.transform.Translate;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import ua.pp.sanderzet.staffagency.model.Job;
import ua.pp.sanderzet.staffagency.model.Person;
import ua.pp.sanderzet.staffagency.model.PersonJob;
import ua.pp.sanderzet.staffagency.util.DateUtil;
import ua.pp.sanderzet.staffagency.util.ResourceBundleUtil;
import ua.pp.sanderzet.staffagency.util.dbSqlite;
import ua.pp.sanderzet.staffagency.view.*;

import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;

public class MainApp extends Application {

    private Stage primaryStage;
    private BorderPane rootLayout;
//    Directory for data


    private final String DIR_SA = "StaffAgencyData";//dir for StaffAgency where all data will be
private final String DIR_DB = "db"; // dir for db in DIR_SA
private final String NAME_DB = "sa1_2.db";
String fileSeparator = System.getProperty("file.separator");
private String fullPathToDb;
private String fileDB;
private final String PREF_ALL_PERSON_CHECK_BOX = "allPersonCheckBox";
private final String PREF_FULL_PATH_TO_DB = "fullPathToDB";
// Result of querying to db
    private ResultSet resultSet;
////Current person for selecting job list for this person
    private Person person;
    private ObservableList<Person> personData = FXCollections.observableArrayList();
    private ObservableList<Job> jobData = FXCollections.observableArrayList();
    private Locale locale = Locale.getDefault();
//    Bundle with default local setting
private ResourceBundle bundle = ResourceBundle.getBundle("bundles/bundle");
    private final String DOC_CHOICE_BOX_KEY = "choiceBoxDoc";
private HashMap<String,String> docHashMap;
private PersonOverviewController personOverviewController;


//    Constructor
        public MainApp () {

    }



    @Override
    public void start(Stage primaryStage) throws Exception{
//            All items in bundle with keys starting with DOC_CHOICE_BOX_KEY
//        will be list of items for documents (base of work) combobox,
// and for Document column in persons table
        docHashMap = ResourceBundleUtil.getHashMapDoc(bundle, DOC_CHOICE_BOX_KEY);
        this.primaryStage=primaryStage;
        primaryStage.setTitle("Staff Agency by SanderZet");
        primaryStage.getIcons().add(new Image("file:resources/images/StaffAgency.png"));
        primaryStage.setMaximized(true);
        initRootLayout();
        showPersonOverview();

    }


    public void initRootLayout (){
        try {
// Load root layout from fxml file
            FXMLLoader loader = new FXMLLoader();
             loader.setLocation(MainApp.class.getResource("view/RootLayout.fxml"));
             //             Bundle for internationalizing
             loader.setResources(bundle);
            rootLayout = (BorderPane) loader.load();
            //Show the scene containing root layout
            primaryStage.setScene(new Scene(rootLayout));
            RootLayoutController rootLayoutController = loader.getController();
            rootLayoutController.setMainApp(this);

            primaryStage.show();

        }
        catch (Exception e){

        }
    }
// List of persons
    public void showPersonOverview (){
        try {
            restoreDataFromDb();
            // Load person overview.
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(MainApp.class.getResource("view/PersonOverview.fxml"));
           //             Bundle for internationalizing
            fxmlLoader.setResources(bundle);


            AnchorPane anchorPane = (AnchorPane) fxmlLoader.load();
            rootLayout.setCenter(anchorPane);
            // Give the controller access to the main app.
            personOverviewController = fxmlLoader.getController();
            personOverviewController.setMainApp(this);
        }
        catch (Exception e){
            e.printStackTrace();

        }

    }

//Adding and editing list of persons
    public boolean showPersonEditDialog(Person person) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(MainApp.class.getResource("view/PersonEditDialog.fxml"));
            //             Bundle for internationalizing
            fxmlLoader.setResources(bundle);
            AnchorPane page = fxmlLoader.load();

            Stage PersonEditStage = new Stage ();
            PersonEditStage.setTitle("Persons");
            PersonEditStage.initModality(Modality.WINDOW_MODAL);
            PersonEditStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            PersonEditStage.setScene(scene);
            // Give the controller access to the main app.
            PersonEditDialogController personEditDialogController = fxmlLoader.getController();
//            Give person to the controller
            personEditDialogController.setPerson(person, docHashMap);
//            Give stage to the controller
            personEditDialogController.setPersonAddStage(PersonEditStage);
            personEditDialogController.setPersons(personData);
//            Show and wait until user close it
            PersonEditStage.showAndWait();
            return personEditDialogController.isOkClicked();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

// Adding and editing list of jobs
    public boolean showJobEditDialog(Job job, String personName) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/JobEditDialog.fxml"));
            //             Bundle for internationalizing
            loader.setResources(bundle);

            AnchorPane pane = loader.load();

            Stage jobEditStage = new Stage();
            jobEditStage.setTitle("Jobs");
            jobEditStage.initModality(Modality.WINDOW_MODAL);
            jobEditStage.initOwner(primaryStage);
            Scene scene = new Scene(pane);
            jobEditStage.setScene(scene);
            JobEditDialogController controller = loader.getController();
            controller.setJobEditStage(jobEditStage);
            controller.setJob(job);
            controller.setPersonNameLabel(personName);
            jobEditStage.showAndWait();

            return controller.isOkClicked();
        } catch (IOException e) {
            e.printStackTrace();
            return false;

        }
    }





    public ObservableList<Person> getPersonData () {
        return personData;
    }





//Get jobs list for the person
    public ObservableList<Job> getJobData (Person person)  {
// If it is not first call we must to clear list
        jobData.clear();
        ResultSet resultJob;
////        Connecting to db
//        dbSqlite.connect();
////If db not exist - create
//        dbSqlite.createDB();

//        Make query to db
        String query = "SELECT ROWID, place, firm, position, transitionJob FROM jobs WHERE idPerson = " +
                person.getId() ;
        resultJob = dbSqlite.readDB(query);

        try {
            while (resultJob.next()) {
                Job job = new Job();
                job.setRowid(resultJob.getInt("ROWID"));
                job.setIdPerson(person.getId());
                job.setPlace(resultJob.getString("place"));
                job.setFirm(resultJob.getString("firm"));
                job.setPosition(resultJob.getString("position"));

                if (resultJob.getString("transitionJob") != null){
                if(DateUtil.validDate(resultJob.getString("transitionJob")))
                    job.setStartJob(DateUtil.parse(resultJob.getString("transitionJob")));
                }

                jobData.add(job);
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }

finally {
            if (resultJob != null) try {
                resultJob.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }


        return jobData;
    }

public void updatePersonDB (Person person) {
        String sql = "UPDATE `persons` SET " +
                "lastName = '" + person.getLastName() +
                "', firstName = '" + person.getFirstName() +
                "', passport = '" + person.getPassport() +
                "', phone  = '" + person.getPhone() +
                "', dateOfContract = '" + DateUtil.format(person.getDateOfContract()) +
                "', sanBook = '" + person.getSanBook() +
                "', endOfVisa = '" + DateUtil.format(person.getEndOfVisa()) +
                "', fileNumber = '" + person.getFileNumber() +
                "', dateQuit = '" + DateUtil.format(person.getDateQuit()) +
                "', Document = '" + person.getDocument() +
                "', usualNote = '" + person.getUsualNote() +
                "', criticalNote = '" + person.getCriticalNote() +
                "' WHERE  id = " +
                person.getId();
dbSqlite.upgradeDb(sql);
//dbSqlite.closeDB();
    }





public void delPersonDb (int id) {
String sql = "delete from persons WHERE id = " +id;
dbSqlite.delRowDb(sql);
sql = "delete from jobs WHERE idPerson = " + id;
dbSqlite.delRowDb(sql);
}

public void updateJobDb (Job job){
    String sql = "UPDATE `jobs` SET " +
            "place = '" + job.getPlace() +
            "', firm = '" + job.getFirm() +
            "', position = '" + job.getPosition() +
            "', transitionJob = '" + DateUtil.format(job.getTransitionJob()) +
            "' WHERE  ROWID = " +
            job.getRowid();
    dbSqlite.upgradeDb(sql);
}



public void delJobDb(int id){
    String sql = "delete from jobs WHERE ROWID = " + id;
    dbSqlite.delRowDb(sql);
}

public void closeDb() {
    dbSqlite.closeDB();
}

    /**
     * Returns the main stage.
     * @return
     */
    public Stage getPrimaryStage() {
        return primaryStage;
    }


// Restore Data from Db
private void restoreDataFromDb() {

        personData.clear();
        Preferences preferences = Preferences.userNodeForPackage(this.getClass());
        fullPathToDb = preferences.get(PREF_FULL_PATH_TO_DB,"");
        File file  = new File(fullPathToDb+fileSeparator+NAME_DB);
    if (fullPathToDb.length() == 0 || !file.isFile()) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(null);
        alert.setHeaderText("No database file !");
        alert.setContentText("Please, select :");
        ButtonType buttonTypeCreateNewDB = new ButtonType("New");
        ButtonType buttonTypeSelectDb = new ButtonType("Select db");
        ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(buttonTypeCreateNewDB,buttonTypeSelectDb, buttonTypeCancel);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == buttonTypeCreateNewDB) {
if(!createFileDB()) System.exit(100);
        } else if (result.get() == buttonTypeSelectDb) {
            if(!selectFileDB()) System.exit(100);
        }
        else if (result.get() == buttonTypeCancel) System.exit(100);



    }
//String homeDir = System.getProperty("user.home");
//String fileSeparator = System.getProperty("file.separator");
//    boolean dirCreateOk = true;
//    //        Preparing db in DIR_DB
//    File dirDB = new File(homeDir +fileSeparator+ DIR_SA + fileSeparator + DIR_DB);
//    if (!dirDB.isDirectory()) {
//        dirCreateOk = dirDB.mkdirs();
//    }
////        If dir not exist but haven`t been created successfully - end
//    if (!dirCreateOk) {
//        System.exit(100);
//
//    }

//        Connecting to db
    dbSqlite.connect("jdbc:sqlite:" + fullPathToDb + fileSeparator+NAME_DB);

//        Make query to db
    String query = "SELECT id, firstName, lastName, passport, phone, dateOfContract, sanBook, endOfVisa, fileNumber," +
            " document, dateQuit, usualNote, criticalNote FROM persons";
//        String query = "SELECT id, firstName, lastName, passport, phone, dataOfContract, sanBook, endOfVisa, fileNumber," +
//                "place, firm, position, start, end FROM persons as p LEFT OUTER JOIN job as j ON id = idPerson";


//        If it`s not first invocation of the method we must clear list personData
//        personData.clear();
    resultSet = dbSqlite.readDB(query);

    try {
        while (resultSet.next()) {
            Person person = new Person();
            person.setId(resultSet.getInt("id"));
            person.setFirstName(resultSet.getString("firstName"));
            person.setLastName(resultSet.getString("lastName"));
            person.setPassport(resultSet.getString("passport"));
            person.setPhone(resultSet.getString("phone"));
            if (DateUtil.validDate(resultSet.getString("dateOfContract")))
                person.setDateOfContract(DateUtil.parse(resultSet.getString("dateOfContract")));

            person.setSanBook(resultSet.getString("sanBook"));

            if (DateUtil.validDate(resultSet.getString("endOfVisa")))
                person.setEndOfVisa(DateUtil.parse(resultSet.getString("endOfVisa")));
            if (DateUtil.validDate(resultSet.getString("dateQuit")))
                person.setDateQuit(DateUtil.parse(resultSet.getString("dateQuit")));
            else person.setDateQuit(null);
            person.setFileNumber(resultSet.getString("fileNumber"));
person.setDocument(resultSet.getString("Document" ));
person.setUsualNote(resultSet.getString("usualNote"));
person.setCriticalNote(resultSet.getString("criticalNote"));
            personData.add(person);
        }


    } catch (SQLException e) {
        e.printStackTrace();
    }
}

    public boolean selectFileDB() {
        boolean result = true;
        File initialDir = new File(System.getProperty("user.home"));
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(initialDir);
        FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter("Staff Agency db file", NAME_DB);
        fileChooser.getExtensionFilters().add(extensionFilter);
        fileChooser.setTitle("Choose file.");
        File fileDB = fileChooser.showOpenDialog(primaryStage);
        if (fileDB == null || !fileDB.isFile()) {
            result = false;
        } else {
            Preferences preferences = Preferences.userNodeForPackage(MainApp.class);
            preferences.put(PREF_FULL_PATH_TO_DB,fileDB.getParent());
            fullPathToDb = fileDB.getParent();
        }
        return result;
    }



    private void restoreDataFromOldDb() {
ResultSet resultSet;
        boolean dirCreateOk = true;
        //        Preparing db in DIR_DB
        File dirDB = new File(DIR_DB);
        if (!dirDB.isDirectory()) {
            dirCreateOk = dirDB.mkdirs();
        }
//        If dir not exist but haven`t been created successfully - end
        if (!dirCreateOk) {
        }
    File newFile = new File("db/sa.db");

//        Connecting to db
        dbSqlite.connect("jdbc:sqlite:db/sa.db");

//        Make query to db
     String query = "SELECT id, firstName, lastName, passport, phone, dataOfContract, sanBook, endOfVisa, fileNumber FROM persons";
//        String query = "SELECT id, firstName, lastName, passport, phone, dataOfContract, sanBook, endOfVisa, fileNumber," +
//                "place, firm, position, start, end FROM persons as p LEFT OUTER JOIN job as j ON id = idPerson";


//        If it`s not first invocation of the method we must clear list personData
//        personData.clear();
        this.resultSet = dbSqlite.readDB(query);

        try {
            while (this.resultSet.next()) {
                Person person = new Person();
                person.setId(this.resultSet.getInt("id"));
                person.setFirstName(this.resultSet.getString("firstName"));
                person.setLastName(this.resultSet.getString("lastName"));
                person.setPassport(this.resultSet.getString("passport"));
                person.setPhone(this.resultSet.getString("phone"));
                if (DateUtil.validDate(this.resultSet.getString("dataOfContract")))
                    person.setDateOfContract(DateUtil.parse(this.resultSet.getString("dataOfContract")));

                person.setSanBook(this.resultSet.getString("sanBook"));

                if (DateUtil.validDate(this.resultSet.getString("endOfVisa")))
                    person.setEndOfVisa(DateUtil.parse(this.resultSet.getString("endOfVisa")));

                person.setFileNumber(this.resultSet.getString("fileNumber"));

                personData.add(person);
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }

//     Getting job data
//        jobData.clear();
////        Connecting to db
//        dbSqlite.connect();
////If db not exist - create
//        dbSqlite.createDB();

//        Make query to db
         query = "SELECT ROWID, place, firm, position, start, end, idPerson FROM jobs";
        resultSet = dbSqlite.readDB(query);

        try {
            while (resultSet.next()) {
                Job job = new Job();
                job.setRowid(resultSet.getInt("ROWID"));
                job.setIdPerson(resultSet.getInt("idPerson"));
                job.setPlace(resultSet.getString("place"));
                job.setFirm(resultSet.getString("firm"));
                job.setPosition(resultSet.getString("position"));

                if (resultSet.getString("start") != null){
                    if(DateUtil.validDate(resultSet.getString("start")))
                        job.setStartJob(DateUtil.parse(resultSet.getString("start")));
                }
                if (resultSet.getString("end") != null) {
                    if (DateUtil.validDate(resultSet.getString("end")))
                        job.setEndJob(DateUtil.parse(resultSet.getString("end")));
                }
                jobData.add(job);
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }

        finally {
            if (resultSet != null) try {
                resultSet.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }

        dbSqlite.closeDB();

//        Insert in new db
    dbSqlite.connect("jdbc:sqlite:db/sa1_2.db");
    dbSqlite.createDB();
for (Person person : personData){
    dbSqlite.insertPersonInNewDb(person);
}

for (Job job : jobData) {
    job.setTransitionJob(job.getStartJob());
    job.setStartJob(null);
    dbSqlite.insertJobDb(job);
}

}

public void reportPersonOnFirm() {
ObservableList<PersonJob> personJobs = FXCollections.observableArrayList();
ObservableList<Job> jobData  = FXCollections.observableArrayList();
    FXMLLoader reportPersonOnFirmFxmlLoader = new FXMLLoader();
    reportPersonOnFirmFxmlLoader.setLocation(MainApp.class.getResource("view/ReportOnFirm.fxml"));
    reportPersonOnFirmFxmlLoader.setResources(bundle);
    try {
//create node
        AnchorPane node = (AnchorPane) reportPersonOnFirmFxmlLoader.load();
    FilteredList<Person> filteredPerson = new FilteredList<Person>(personData, person -> {
        if (person.getDateQuit() != null && person.getDateQuit().isBefore(LocalDate.now())) {

            return false;
        }
        return true;
    });
    SortedList<Person> sortedList = new SortedList<>(filteredPerson);

//sortedList.comparatorProperty().bind()

for (Person person:sortedList){
    PersonJob personJob = new PersonJob();
    personJob.setName(person.getLastName()+" "+person.getFirstName());
    personJob.setPhone(person.getPhone());
    jobData =  getJobData(person);
//    Seek for job where no data of transition.
//    Person on default may have only one job so last it founded will be taken.
    for (Job job:jobData){
        if (job.getTransitionJob() == null || DateUtil.format(job.getTransitionJob()).length() == 0 ){
            personJob.setFirm(job.getFirm());
            personJob.setPlace(job.getPlace());
            personJob.setPosition(job.getPosition());
                    }
    }
personJobs.add(personJob);
}

ReportOnFirmController reportOnFirmController = reportPersonOnFirmFxmlLoader.getController();
        node.setStyle("-fx-font-size: 10"); // Font set to fixed size for computing TableView length
reportOnFirmController.setMainApp(personJobs);
TableView<PersonJob> reportPersonJobTableView = reportOnFirmController.getReportTable();
reportPersonJobTableView.prefHeightProperty().bind(Bindings.size(reportPersonJobTableView.getItems()).multiply(25).add(25));




//        double scaleX = pageLayout.getPrintableWidth() / node.getBoundsInParent().getWidth();
//        double scaleY = pageLayout.getPrintableHeight() / node.getBoundsInParent().getHeight();
//                node.getTransforms().add(new Scale(scaleX, scaleY));
        PrinterJob job = PrinterJob.createPrinterJob();
            if (job != null) {
                boolean proceed = job.showPrintDialog(primaryStage);
                if (proceed) {
                    PageLayout pageLayout = job.getJobSettings().getPageLayout();
                    double scaleX =  pageLayout.getPrintableWidth() / node.getPrefWidth();
                    double scaleY = scaleX;
                    double numberOfPages = (reportPersonJobTableView.getPrefHeight()+25) * scaleY / pageLayout.getPrintableHeight();

                    node.getTransforms().add(new Scale(scaleX, scaleY));
                    node.getTransforms().add(new Translate(0,0));
                    Translate gridTransform = new Translate();
                    node.getTransforms().add(gridTransform);
                    //now we loop though the image that needs to be printed and we only print a subimage of the full image.
                    //for example: In the first loop we only pint the printable image from the top down to the height of a standard piece of paper. Then we print starting from were the last printed page ended down to the height of the next page. This happens until all of the pages are printed.
                    // first page prints from 0 height to -11 inches height, Second page prints from -11 inches height to -22 inches height, etc.
                    for(int i = 0; i < numberOfPages; i++)
                    {
                        gridTransform.setY(-i * (pageLayout.getPrintableHeight() / scaleX));
                        job.printPage(pageLayout, node);
                    }

                    job.endJob();//finally end the printing job.

                }
            }

    } catch (IOException e) {
        e.printStackTrace();
    }

}

public boolean createFileDB() {
        boolean result = true;
    String fileSeparator = System.getProperty("file.separator");
        DirectoryChooser directoryChooser = new DirectoryChooser();
        boolean dirCreateOK = true;
    File pathToDb = new File(System.getProperty("user.home"));
    directoryChooser.setInitialDirectory(pathToDb);
    pathToDb = directoryChooser.showDialog(primaryStage);
    File pathToSaDb = new File(pathToDb.getAbsolutePath()+fileSeparator+DIR_SA);
    if (!pathToSaDb.isDirectory()){
        dirCreateOK = pathToSaDb.mkdir();
    }
    if (!dirCreateOK) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.initOwner(primaryStage);
        alert.setContentText("Can`t create dir:\n" + pathToSaDb.getAbsolutePath());
        alert.showAndWait();
        result = false;
    } else {
        dbSqlite.connect("jdbc:sqlite:" + pathToSaDb + fileSeparator+NAME_DB);
       dbSqlite.createDB();
            Preferences preferences = Preferences.userNodeForPackage(this.getClass());
            preferences.put(PREF_FULL_PATH_TO_DB, pathToSaDb.getAbsolutePath());
            fullPathToDb = pathToSaDb.getAbsolutePath();
            dbSqlite.closeDB();

    }
return result;

}

public String getPREF_ALL_PERSON_CHECK_BOX() {
        return PREF_ALL_PERSON_CHECK_BOX;
    }
    public String getPREF_FULL_PATH_TO_DB () {
        return PREF_FULL_PATH_TO_DB;
    }
public String getNAME_DB() {
        return NAME_DB;
}
    public PersonOverviewController getPersonOverviewController() {
        return personOverviewController;
    }


    public HashMap<String, String> getDocHashMap () {
        return docHashMap;
    }
    public static void main(String[] args) {
        launch(args);
    }
}
