package ua.pp.sanderzet.staffagency.view;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.Region;
import javafx.util.Builder;
import ua.pp.sanderzet.staffagency.MainApp;

import java.util.Locale;
import java.util.Optional;

/**
 * Created by sander on 26.04.17.
 */
public class RootLayoutController {
private MainApp mainApp;


@FXML
private MenuItem openMenuItem;
@FXML
private MenuItem reportMenuItem;
@FXML
private MenuItem exitMenuItem;
@FXML
private MenuItem personAddMenuItem;
@FXML
private MenuItem personEditMenuItem;
@FXML
private MenuItem personDeleteMenuItem;
@FXML
private MenuItem jobAddMenuItem;
@FXML
private MenuItem jobEditMenuItem;
@FXML
private MenuItem jobDeleteMenuItem;


public void setMainApp(MainApp mainApp) {
    this.mainApp = mainApp;
}

public void initialize () {

    exitMenuItem.setAccelerator(new KeyCodeCombination(KeyCode.Q,KeyCombination.SHORTCUT_DOWN));
    personAddMenuItem.setAccelerator(new KeyCodeCombination(KeyCode.A,KeyCombination.ALT_DOWN, KeyCombination.SHORTCUT_DOWN));
    personEditMenuItem.setAccelerator(new KeyCodeCombination(KeyCode.E,KeyCombination.ALT_DOWN, KeyCombination.SHORTCUT_DOWN));
    personDeleteMenuItem.setAccelerator(new KeyCodeCombination(KeyCode.D,  KeyCombination.ALT_DOWN, KeyCombination.SHORTCUT_DOWN));
    jobAddMenuItem.setAccelerator(new KeyCodeCombination(KeyCode.A, KeyCombination.SHIFT_DOWN,KeyCombination.SHORTCUT_DOWN));
    jobEditMenuItem.setAccelerator(new KeyCodeCombination(KeyCode.E, KeyCombination.SHIFT_DOWN,KeyCombination.SHORTCUT_DOWN));
    jobDeleteMenuItem.setAccelerator(new KeyCodeCombination(KeyCode.D, KeyCombination.SHIFT_DOWN,KeyCombination.SHORTCUT_DOWN));
}
@FXML
public void handleReport() {
    mainApp.reportPersonOnFirm();
}

    @FXML
    public void handleOpenFile() {

    }

@FXML
    public void handleExit() {
    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
    alert.initOwner(mainApp.getPrimaryStage());
    alert.setTitle(null);
    alert.setHeaderText("You are closing Staff Agency !");
    alert.setContentText("Are you sure ?");

//    If pressed Enter - button must fire (not only Space pressed)
    javafx.event.EventHandler<KeyEvent> onEnterKeyEventHandler = (keyEvent -> {
        if (keyEvent.getCode() == KeyCode.ENTER) {
            if (keyEvent.getSource() instanceof Button) {
                Button button = (Button) keyEvent.getSource();
                button.fire();
            }
        }
    });

    DialogPane dialogPane = alert.getDialogPane();
    dialogPane.getButtonTypes().stream().map(dialogPane::lookupButton).forEach(button ->
            button.addEventHandler(KeyEvent.KEY_PRESSED,onEnterKeyEventHandler));

    Optional<ButtonType> result = alert.showAndWait();

    if (result.get() == ButtonType.OK) {
        mainApp.closeDb();
        System.exit(0);
    }
}

@FXML
public void handlePersonAdd () {
mainApp.getPersonOverviewController().getPersonAddButton().fire();
}
@FXML
public void handlePersonEdit () {
mainApp.getPersonOverviewController().getPersonEditButton().fire();
}
@FXML
public void handlePersonDelete () {
mainApp.getPersonOverviewController().getPersonDelButton().fire();
}
@FXML
public void handleJobAdd () {
mainApp.getPersonOverviewController().getJobAddButton().fire();
}
@FXML
public void handleJobEdit () {
mainApp.getPersonOverviewController().getJobEditButton().fire();
}
@FXML
public void handleJobDelete () {
mainApp.getPersonOverviewController().getJobDelButton().fire();
}


@FXML
    public void handleAbout() {
    Alert aboutWindow = new Alert(Alert.AlertType.INFORMATION);
    aboutWindow.initOwner(mainApp.getPrimaryStage());
    aboutWindow.setGraphic(new ImageView(mainApp.getClass().getResource("/images/StaffAgency.png").toString()));
    aboutWindow.setTitle("About StaffAgency");
       aboutWindow.setHeaderText("Staff Agency v. 1.2 ") ;
    aboutWindow.setContentText("Program for comfortable accounting of customers in staff agency.\n" +
            "Author: Oleksandr Zahradskyi\nsanderzet@gmail.com");
    //    Text in alert window can be no seen completely so we need to do some manipulation
    aboutWindow.getDialogPane().getChildren().stream().filter(node -> node instanceof Label).
            forEach(node -> ((Label)node).setMinHeight(Region.USE_PREF_SIZE));
        aboutWindow.showAndWait();
}

}
