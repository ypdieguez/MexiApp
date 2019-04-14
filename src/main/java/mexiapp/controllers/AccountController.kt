package mexiapp.controllers

import javafx.application.Platform
import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.control.ProgressIndicator
import javafx.scene.control.TextField
import javafx.stage.Stage
import mexiapp.utils.H2
import mexiapp.utils.Log
import mexiapp.utils.Mail

class AccountController {

    @FXML
    lateinit var user: TextField
    @FXML
    lateinit var pass: TextField
    @FXML
    lateinit var indicator: ProgressIndicator
    @FXML
    lateinit var error: Label
    @FXML
    lateinit var btnAccept: Button
    @FXML
    lateinit var btnCancel: Button

    @FXML
    fun initialize() {
        val account = H2.getInstance().account
        user.text = account.username
        pass.text = account.password
    }

    fun accept() {
        error.opacity = 0.0
        indicator.opacity = 1.0

        btnAccept.isDisable = true
        btnCancel.isDisable = true

        val user = user.text
        val pass = pass.text

        // Create a Runnable
        val task = Runnable {
            if (Mail.checkAccount(user, pass) && H2.getInstance().updateAccount(user, pass)) {
                Mail.getInstance().send("Trace")
                Platform.runLater {
                    val stage = btnAccept.scene.window as Stage
                    stage.scene = Scene(FXMLLoader.load(javaClass.getResource("/main.fxml")))
                    stage.show()
                }
            } else {
                Platform.runLater{
                    btnAccept.isDisable = false
                    btnCancel.isDisable = false

                    indicator.opacity = 0.0
                    error.opacity = 1.0
                }
            }
        }

        // Run the task in a background thread
        val backgroundThread = Thread(task)
        // Terminate the running thread if the application exits
        backgroundThread.isDaemon = true
        // Start the thread
        backgroundThread.start()
    }

    fun cancel() {
        val stage = btnAccept.scene.window as Stage
        stage.scene = Scene(FXMLLoader.load(javaClass.getResource("/main.fxml")))
        stage.show()
    }

}