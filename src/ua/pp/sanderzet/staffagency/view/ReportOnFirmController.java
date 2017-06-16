package ua.pp.sanderzet.staffagency.view;

import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import ua.pp.sanderzet.staffagency.MainApp;
import ua.pp.sanderzet.staffagency.model.PersonJob;

import java.net.URL;
import java.util.ResourceBundle;


/**
 * Created by alzet on 14.06.17.
 */
public class ReportOnFirmController  {
    @FXML
    private TableView<PersonJob> reportOnFirmTable;

    @FXML
    private TableColumn<PersonJob, String> nameColumn;

    @FXML
    private TableColumn<PersonJob, String> phoneColumn;

    @FXML
    private TableColumn<PersonJob,String> firmColumn;

    @FXML
    private TableColumn<PersonJob,String> placeColumn;

    @FXML
    private TableColumn<PersonJob,String> positionColumn;

    @FXML
    private Label accountLabel;

    @FXML
    private Label accountNumberLabel;

    private MainApp mainApp;


    private ObservableList<PersonJob> personJobs;

    public ReportOnFirmController(ObservableList<PersonJob> personJobs) {
        this.personJobs = personJobs;
    }

    public void initialize() {

//        Sorting on name
        SortedList<PersonJob> personJobSortedList = personJobs.sorted((personJob, t1) -> {
            return personJob.getName().compareTo(t1.getName());
        });
        nameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        phoneColumn.setCellValueFactory(cellData -> cellData.getValue().phoneProperty());
        firmColumn.setCellValueFactory(cellData -> cellData.getValue().firmProperty());
        placeColumn.setCellValueFactory(cellData -> cellData.getValue().placeProperty());
        positionColumn.setCellValueFactory(cellData -> cellData.getValue().positionProperty());
        reportOnFirmTable.setItems(personJobSortedList);

        accountLabel.setText(Integer.toString(reportOnFirmTable.getItems().size()));
    }
}
