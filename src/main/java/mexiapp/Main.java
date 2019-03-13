package mexiapp;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import mexiapp.utils.H2;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        System.setProperty("sun.java2d.cmm", "sun.java2d.cmm.kcms.KcmsServiceProvider");

        if (H2.getInstance().isActive()) {
            // Load root layout from fxml file
            Parent root = FXMLLoader.load(getClass().getResource("/main.fxml"));

            primaryStage.setTitle("MexiApp");
            primaryStage.setScene(new Scene(root));
            primaryStage.setResizable(false);
            primaryStage.setAlwaysOnTop(true);
            // Disable maximize and minimize
            primaryStage.initStyle(StageStyle.UTILITY);

            Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
            primaryStage.setX(primaryScreenBounds.getMinX() + primaryScreenBounds.getWidth() - 675.0);
            primaryStage.setY(primaryScreenBounds.getMinY() + primaryScreenBounds.getHeight() - 95.0);

            primaryStage.setOnHiding(event -> Platform.runLater(() -> H2.getInstance().close()));
            primaryStage.show();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Mexiapp");
            alert.setHeaderText(null);
            alert.setContentText("Contacte con el proovedor...");
            alert.showAndWait();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
