package ua.pp.sanderzet.staffagancy.view;

import javafx.fxml.FXML;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import ua.pp.sanderzet.staffagancy.model.Job;
import ua.pp.sanderzet.staffagancy.util.DateUtil;

import javafx.scene.control.*;

/**
 * Created by alzet on 13.05.17.
 */
public class JobEditDialogController {

    @FXML
    private TextField placeTextField;
    @FXML
    private TextField firmTextField;
    @FXML
    private TextField positionTextField;
    @FXML
    private TextField startTextField;
    @FXML
    private TextField endTextField;
    @FXML
    private Button okButton;
    @FXML
    private Button cancelButton;

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
    public void initialize (){

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
        startTextField.setText(DateUtil.format(job.getStart()));
        endTextField.setText(DateUtil.format(job.getEnd()));
        startTextField.setPromptText("dd.mm.yyyy");
    endTextField.setPromptText("dd.mm.yyyy");
    }

@FXML
public void handleOk () {
        if (isInputValid()) {
            job.setPlace(placeTextField.getText());
            job.setFirm(firmTextField.getText());
            job.setPosition(positionTextField.getText());
            job.setStart(DateUtil.parse(startTextField.getText()));
            job.setEnd(DateUtil.parse(endTextField.getText()));
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
    if (firmTextField.getText() == null || firmTextField.getLength() == 0)
        errorMessage += "No valid firm\n";
    if (positionTextField.getText() == null || positionTextField.getLength() == 0)
        errorMessage += "No valid position of work\n";
    if (startTextField.getText() == null || startTextField.getLength() == 0)
        errorMessage += "No valid start time of working\n";
    else {
        if (!DateUtil.validDate(startTextField.getText()))
            errorMessage += "No valid start time of working. Use the format dd.mm.yyyy !\n";
    }

    if (endTextField.getText() == null || endTextField.getLength() == 0)
endTextField.setText("");
    else {
        if (!DateUtil.validDate(endTextField.getText()))
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
