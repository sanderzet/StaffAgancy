package ua.pp.sanderzet.staffagancy.view;

import javafx.fxml.FXML;
import ua.pp.sanderzet.staffagancy.MainApp;

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


}
