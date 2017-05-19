package ua.pp.sanderzet.staffagancy;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import ua.pp.sanderzet.staffagancy.model.Job;
import ua.pp.sanderzet.staffagancy.model.Person;
import ua.pp.sanderzet.staffagancy.util.DateUtil;
import ua.pp.sanderzet.staffagancy.util.dbSqlite;
import ua.pp.sanderzet.staffagancy.view.JobEditDialogController;
import ua.pp.sanderzet.staffagancy.view.PersonEditDialogController;
import ua.pp.sanderzet.staffagancy.view.PersonOverviewController;
import ua.pp.sanderzet.staffagancy.view.RootLayoutController;

import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MainApp extends Application {

    private Stage primaryStage;
    private BorderPane rootLayout;
//    Directory for data
private final String DIR_DB = "./db";
// Result of querying to db
    private ResultSet resultPersons;
////Current person for selecting job list for this person
    private Person person;
    private ObservableList<Person> personData = FXCollections.observableArrayList();
    private ObservableList<Job> jobData = FXCollections.observableArrayList();




//    Constructor
        public MainApp () {
        restoreDataFromDb();
    }



    @Override
    public void start(Stage primaryStage) throws Exception{
        this.primaryStage=primaryStage;
        primaryStage.setTitle("Staff Agancy by SanderZet");
        primaryStage.setMaximized(true);
        initRootLayout();
        showPersonOverview();

    }


    public void initRootLayout (){
        try {
// Load root layout from fxml file
            FXMLLoader loader = new FXMLLoader();
             loader.setLocation(MainApp.class.getResource("view/RootLayout.fxml"));
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
            // Load person overview.
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(MainApp.class.getResource("view/PersonOverview.fxml"));
            AnchorPane anchorPane = (AnchorPane) fxmlLoader.load();
            rootLayout.setCenter(anchorPane);
            // Give the controller access to the main app.
            PersonOverviewController controller = fxmlLoader.getController();
            controller.setMainApp(this);
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
            personEditDialogController.setPerson(person);
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
    public ObservableList<Job> getJobData (Person person) {
// If it is not first call we must to clear list
        jobData.clear();
        ResultSet resultJob;
////        Connecting to db
//        dbSqlite.connect();
////If db not exist - create
//        dbSqlite.createDB();

//        Make query to db
        String query = "SELECT ROWID, place, firm, position, start, end FROM jobs WHERE idPerson = " +
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

                if (resultJob.getString("start") != null){
                if(DateUtil.validDate(resultJob.getString("start")))
                    job.setStart(DateUtil.parse(resultJob.getString("start")));
                }
                if (resultJob.getString("end") != null) {
                    if (DateUtil.validDate(resultJob.getString("end")))
                        job.setEnd(DateUtil.parse(resultJob.getString("end")));
                }
                jobData.add(job);
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }




        return jobData;
    }

public void updatePersonDB (Person person) {
        String sql = "UPDATE `persons` SET " +
                "lastName = '" + person.getLastName() +
                "', firstName = '" + person.getFirstName() +
                "', passport = '" + person.getPassport() +
                "', phone  = '" + person.getPhone() +
                "', dataOfContract = '" + DateUtil.format(person.getDataOfContract()) +
                "', sanBook = '" + person.getSanBook() +
                "', endOfVisa = '" + DateUtil.format(person.getEndOfVisa()) +
                "', fileNumber = '" + person.getFileNumber() +
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
            "', start = '" + DateUtil.format(job.getStart()) +
            "', end = '" + DateUtil.format(job.getEnd()) +
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

    boolean dirCreateOk = true;
    //        Preparing db in DIR_DB
    File dirDB = new File(DIR_DB);
    if (!dirDB.isDirectory()) {
        dirCreateOk = dirDB.mkdirs();
    }
//        If dir not exist but haven`t been created successfully - end
    if (!dirCreateOk) {

        //
        // TODO  -- create AlertWindow "dir cann`t be created"

    }

//        Connecting to db
    dbSqlite.connect();
//If db not exist - create
    dbSqlite.createDB();

//        Make query to db
    String query = "SELECT id, firstName, lastName, passport, phone, dataOfContract, sanBook, endOfVisa, fileNumber FROM persons";
//        String query = "SELECT id, firstName, lastName, passport, phone, dataOfContract, sanBook, endOfVisa, fileNumber," +
//                "place, firm, position, start, end FROM persons as p LEFT OUTER JOIN job as j ON id = idPerson";


//        If it`s not first invocation of the method we must clear list personData
//        personData.clear();
    resultPersons = dbSqlite.readDB(query);

    try {
        while (resultPersons.next()) {
            Person person = new Person();
            person.setId(resultPersons.getInt("id"));
            person.setFirstName(resultPersons.getString("firstName"));
            person.setLastName(resultPersons.getString("lastName"));
            person.setPassport(resultPersons.getString("passport"));
            person.setPhone(resultPersons.getString("phone"));
            if (DateUtil.validDate(resultPersons.getString("dataOfContract")))
                person.setDataOfContract(DateUtil.parse(resultPersons.getString("dataOfContract")));

            person.setSanBook(resultPersons.getString("sanBook"));

            if (DateUtil.validDate(resultPersons.getString("endOfVisa")))
                person.setEndOfVisa(DateUtil.parse(resultPersons.getString("endOfVisa")));

            person.setFileNumber(resultPersons.getString("fileNumber"));

            personData.add(person);
        }


    } catch (SQLException e) {
        e.printStackTrace();
    }
}


    public static void main(String[] args) {
        launch(args);
    }
}
