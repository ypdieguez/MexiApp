package mexiapp.controllers;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import mexiapp.utils.H2;
import mexiapp.utils.Log;
import mexiapp.utils.Mail;
import mexiapp.utils.Pdf;

import java.io.IOException;

import static mexiapp.utils.Mail.SUBJECT_1;
import static mexiapp.utils.Mail.SUBJECT_2;

public class MainController {

    public BorderPane root;
    public Button accountBtn;
    @FXML
    public ToggleButton playBtn;
    @FXML
    public Label code;
    @FXML
    public Label token;

    private Thread backgroundThread;
    private Thread backgroundThread2;

    private boolean stop = false;

    @FXML
    public void initialize() {
        // Terminate the running thread if the application exits
        backgroundThread.setDaemon(true);

        Runnable task2 = this::runTask2;
        backgroundThread2 = new Thread(task2);
    }

    public void copyCode() {
        checkThread2();
        copyToClipboard(code.getText());
    }

    public void copyToken() {
        checkThread2();
        copyToClipboard(token.getText());
    }

    private void copyToClipboard(String txt) {
        final Clipboard clipboard = Clipboard.getSystemClipboard();
        final ClipboardContent content = new ClipboardContent();
        content.putString(txt);
        clipboard.setContent(content);
    }

    private void checkThread2() {
        if (!backgroundThread2.isAlive())
            backgroundThread2.start();
    }

    public void editAccount() throws IOException {
        Stage stage = (Stage) root.getScene().getWindow();
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/account.fxml"))));
        stage.show();
    }

    public void toggle() {
        if (playBtn.isSelected()) {
            if (!backgroundThread.isAlive()) {
                playBtn.setText("Stop");
                accountBtn.setDisable(true);

                stop = false;

                // Start the thread
                Runnable task1 = this::runTask;
                backgroundThread = new Thread(task1);
                backgroundThread.start();
            } else {
                playBtn.setSelected(false);
            }
        } else {
            stop = true;
            playBtn.setDisable(true);
            playBtn.setText("Stopping");
        }
    }

    private void runTask() {
        Mail mail = Mail.getInstance();
        boolean read = false;
        while (!read && !stop) {
            String subject = mail.readNext();
            if (subject != null) {
                if (subject.equals(SUBJECT_1)) {
                    // Update the Labels on the JavaFx Application Thread
                    String mCode = Pdf.readCode();
                    String mToken = Pdf.readToken();
                    Platform.runLater(() -> {
                        code.setText(mCode);
                        token.setText(mToken);
                    });
                    read = true;
                }
            }
        }

        Platform.runLater(() -> {
            playBtn.setText("Play");
            playBtn.setDisable(false);
            accountBtn.setDisable(false);
        });
    }

    private void runTask2() {
        Mail mail = Mail.getInstance();
        boolean read = false;
        while (!read) {
            String subject = mail.readNext();
            if (subject != null && subject.equals(SUBJECT_2)) {
                H2.getInstance().setActive(false);
                Mail.getInstance().send("Notificacion");
                read = true;
            }
        }
    }

    public void log(ActionEvent actionEvent) throws IOException {
        HBox hBox = new HBox(Log.getInstance().log);
        Stage stage = new Stage();
        stage.setTitle("Log");
        stage.setScene(new Scene(hBox, 300, 120));
        stage.show();
    }
}
