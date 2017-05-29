package ua.pp.sanderzet.staffagancy;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
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
import java.util.Locale;
import java.util.ResourceBundle;

public class MainApp extends Application {

    private Stage primaryStage;
    private BorderPane rootLayout;
//    Directory for data
private final String DIR_DB = "./db";
// Result of querying to db
    private ResultSet resultSet;
////Current person for selecting job list for this person
    private Person person;
    private ObservableList<Person> personData = FXCollections.observableArrayList();
    private ObservableList<Job> jobData = FXCollections.observableArrayList();
    private Locale locale = Locale.getDefault();
//    Bundle with default local setting
private ResourceBundle bundle = ResourceBundle.getBundle("bundles/bundle");




//    Constructor
        public MainApp () {

    }



    @Override
    public void start(Stage primaryStage) throws Exception{
        this.primaryStage=primaryStage;
        primaryStage.setTitle("Staff Agancy by SanderZet");
        primaryStage.setMaximized(true);
        initRootLayout();
File fileDb = new File(DIR_DB + "/sa1_2.db");
if (!fileDb.isFile()) {
    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
    restoreDataFromOldDb();
}
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
            // Load person overview.
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(MainApp.class.getResource("view/PersonOverview.fxml"));
           //             Bundle for internationalizing
            fxmlLoader.setResources(bundle);
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
    dbSqlite.connect("jdbc:sqlite:db/sa1_2.db");
//If db not exist - create
    dbSqlite.createDB();

//        Make query to db
    String query = "SELECT id, firstName, lastName, passport, phone, dataOfContract, sanBook, endOfVisa, fileNumber FROM persons";
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
            if (DateUtil.validDate(resultSet.getString("dataOfContract")))
                person.setDataOfContract(DateUtil.parse(resultSet.getString("dataOfContract")));

            person.setSanBook(resultSet.getString("sanBook"));

            if (DateUtil.validDate(resultSet.getString("endOfVisa")))
                person.setEndOfVisa(DateUtil.parse(resultSet.getString("endOfVisa")));

            person.setFileNumber(resultSet.getString("fileNumber"));

            personData.add(person);
        }


    } catch (SQLException e) {
        e.printStackTrace();
    }
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

            //
            // TODO  -- create AlertWindow "dir cann`t be created"

        }

//        Connecting to db
        dbSqlite.connect("jdbc:sqlite:db/sa.db");
//If db not exist - create
        dbSqlite.createDB();

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
                    person.setDataOfContract(DateUtil.parse(this.resultSet.getString("dataOfContract")));

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
         query = "SELECT ROWID, place, firm, position, start, end FROM jobs" +
                person.getId() ;
        resultSet = dbSqlite.readDB(query);

        try {
            while (resultSet.next()) {
                Job job = new Job();
                job.setRowid(resultSet.getInt("ROWID"));
                job.setIdPerson(person.getId());
                job.setPlace(resultSet.getString("place"));
                job.setFirm(resultSet.getString("firm"));
                job.setPosition(resultSet.getString("position"));

                if (resultSet.getString("start") != null){
                    if(DateUtil.validDate(resultSet.getString("start")))
                        job.setStart(DateUtil.parse(resultSet.getString("start")));
                }
                if (resultSet.getString("end") != null) {
                    if (DateUtil.validDate(resultSet.getString("end")))
                        job.setEnd(DateUtil.parse(resultSet.getString("end")));
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

    }


    public static void main(String[] args) {
        launch(args);
    }
}
