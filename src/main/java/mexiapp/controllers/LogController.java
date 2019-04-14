package mexiapp.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;

public class LogController {

    public ListView logList;

    @FXML
    public void initialize() {
        logList.getItems();
    }
}
