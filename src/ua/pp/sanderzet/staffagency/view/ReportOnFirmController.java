package ua.pp.sanderzet.staffagency.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.text.Font;
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



    public ReportOnFirmController() {
    }

    public void initialize() {

//personJobs = FXCollections.observableArrayList();
        nameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        phoneColumn.setCellValueFactory(cellData -> cellData.getValue().phoneProperty());
        firmColumn.setCellValueFactory(cellData -> cellData.getValue().firmProperty());
        placeColumn.setCellValueFactory(cellData -> cellData.getValue().placeProperty());
        positionColumn.setCellValueFactory(cellData -> cellData.getValue().positionProperty());

    }

    public void setMainApp (ObservableList<PersonJob> personJobs) {

       reportOnFirmTable.setItems(personJobs);
       reportOnFirmTable.getSortOrder().addAll(firmColumn, nameColumn);
       int rowCount = reportOnFirmTable.getItems().size();
       accountNumberLabel.setText(Integer.toString(rowCount));
       reportOnFirmTable.setPrefHeight(rowCount*18+18);

    }

    public TableView getReportTable() {
        return reportOnFirmTable;
    }
}

