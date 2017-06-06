package ua.pp.sanderzet.staffagancy.view;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import ua.pp.sanderzet.staffagancy.model.Job;
import ua.pp.sanderzet.staffagancy.util.DateUtil;

import javafx.scene.control.*;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by alzet on 13.05.17.
 */
public class JobEditDialogController implements Initializable {



    @FXML
    private TextField placeTextField;
    @FXML
    private TextField firmTextField;
    @FXML
    private TextField positionTextField;

//  Next two now don`t use. For future purpose.
//    @FXML
//    private TextField startTextField;
//    @FXML
//    private TextField endTextField;
    @FXML
    private TextField transitionTextField;
    @FXML
    private Button okButton;
    @FXML
    private Button cancelButton;
    @FXML
    private Label personNameLabel;

    private Job job;
    private boolean okClicked = false;
    private Stage jobEditStage;



    /**
     * The constructor.
     * The constructor is called before the initialize() method.
     */

    public JobEditDialogController(){

    }

    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */

    @FXML
    @Override
    public void initialize (URL url, ResourceBundle resourceBundle){

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

public void setPersonNameLabel (String personName) {
        personNameLabel.setText(personName);
}

    public void setJobEditStage(Stage stage) {
        jobEditStage = stage;
    }

    public boolean isOkClicked() {
        return okClicked;
    }

    public void setJob (Job job) {
        this.job = job;
        placeTextField.setText(job.getPlace());
        firmTextField.setText(job.getPlace());
        positionTextField.setText(job.getPosition());
        transitionTextField.setText(DateUtil.format(job.getTransitionJob()));
    transitionTextField.setPromptText("dd.mm.yyyy");
    }

@FXML
public void handleOk () {
        if (isInputValid()) {
            job.setPlace(placeTextField.getText());
            job.setFirm(firmTextField.getText());
            job.setPosition(positionTextField.getText());
            job.setTransitionJob(DateUtil.parse(transitionTextField.getText()));
            okClicked = true;
            jobEditStage.close();
        }

}
@FXML
public void handleCancel(){
jobEditStage.close();
}

public boolean isInputValid(){
    String errorMessage = "";
//    if (placeTextField.getText() == null || placeTextField.getLength() == 0)
//        errorMessage += "No valid place of work\n";
    if (placeTextField.getText() == null ) placeTextField.setPromptText("");
    if (firmTextField.getText() == null || firmTextField.getLength() == 0)
        errorMessage += "No valid firm\n";
    if (positionTextField.getText() == null || positionTextField.getLength() == 0)
        errorMessage += "No valid position of work\n";


    if (transitionTextField.getText() == null || transitionTextField.getLength() == 0)
transitionTextField.setText("");
    else {
        if (!DateUtil.validDate(transitionTextField.getText()))
            errorMessage += "No valid end time of working. Use the format dd.mm.yyyy !\n";
    }

    if (errorMessage.length() == 0) {
        return true;
            }
            else {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.initOwner(jobEditStage);
        alert.setTitle("Invalid fields");
        alert.setHeaderText("Please correct invalid fields");
        alert.setContentText(errorMessage);
//    Text in alert window can be no seen completely so we need to do some manipulation
alert.getDialogPane().getChildren().stream().filter(node -> node instanceof Label).forEach(node -> ((Label) node).setMinHeight(Region.USE_PREF_SIZE));
alert.showAndWait();
        return false;

    }

}


}
