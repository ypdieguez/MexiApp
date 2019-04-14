package mexiapp.utils;

import javafx.scene.control.ListView;

public class Log {

    public ListView<String> log;
    private static Log instance = null;

    private Log() {
        log = new ListView<>();
    }

    public static Log getInstance() {
        if (instance == null) {
            instance = new Log();
        }
        return instance;
    }

//    public static void setLabel(Label label) {
//        mLabel = label;
//    }
//
//    public static void error(String msg) {
//        if (mLabel != null) {
//            Platform.runLater(() -> mLabel.setText(msg));
//        }
//    }


    public void error(String s) {
        log.getItems().add(s);
    }

    public void exception(Exception e) {
        error(e.getMessage());
    }
}
