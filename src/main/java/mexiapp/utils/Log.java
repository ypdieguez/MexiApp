package mexiapp.utils;

import javafx.application.Platform;
import javafx.scene.control.Label;

public class Log {
    private static Label mLabel;

    public static void setLabel(Label label) {
       mLabel = label;
    }

    public static void error(String msg) {
        if(mLabel != null) {
            Platform.runLater(() -> mLabel.setText(msg));
        }
    }

    public static void exception(Exception e) {
        error(e.getMessage());
    }
}
