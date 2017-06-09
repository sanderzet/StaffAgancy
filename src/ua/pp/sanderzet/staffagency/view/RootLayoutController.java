package ua.pp.sanderzet.staffagency.view;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Region;
import javafx.util.Builder;
import ua.pp.sanderzet.staffagency.MainApp;

import java.util.Locale;

/**
 * Created by sander on 26.04.17.
 */
public class RootLayoutController {
private MainApp mainApp;

public void setMainApp(MainApp mainApp) {
    this.mainApp = mainApp;
}

@FXML
    public void handleExit() {
    mainApp.closeDb();
    System.exit(0);
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
