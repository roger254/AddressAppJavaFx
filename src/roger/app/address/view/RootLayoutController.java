package roger.app.address.view;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.stage.FileChooser;
import roger.app.address.Main;

import java.io.File;

public class RootLayoutController {

    /*
    The controller for the root layout. The root layout provides the basic application layout
    containing a menu bar and space where the other JavaFX elements are placed
     */

    private Main main;

    /*
    Is called by the main application to give a reference back to itself.

    @param main
     */
    public void setMain(Main main) {
        this.main = main;
    }

    //creates an empty address book
    @FXML
    private void handleNew() {
        main.getPersonData().clear();
        main.setPersonFilePath(null);
    }


    //opens a fileChooser to let the use select an address book to load
    @FXML
    private void handleOpen() {
        FileChooser fileChooser = new FileChooser();

        //Set extension filter
        FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter("XML files (*.xml)", "*.xml");
        fileChooser.getExtensionFilters().add(extensionFilter);

        // Show open file dialog
        File file = fileChooser.showOpenDialog(main.getPrimaryStage());

        if (file != null)
            main.loadPersonDataFromFile(file);
    }

    /*Saves the file to the person file that is currently open
     If there is no open file, the  "save as" dialog is shown
     */
    @FXML
    private void handleSave() {
        File personFile = main.getPersonFilePath();
        if (personFile != null)
            main.savePersonDataToFile(personFile);
        else
            handleSaveAs();
    }

    //opens a fileChooser to let the user select to save to.
    @FXML
    private void handleSaveAs() {
        FileChooser fileChooser = new FileChooser();

        //Set extension filter
        FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter("XML files (*.xml)", "*.xml");
        fileChooser.getExtensionFilters().add(extensionFilter);

        //show save file dialog
        File file = fileChooser.showSaveDialog(main.getPrimaryStage());

        if (file != null) {
            //Make sure it has the correct extension
            if (!file.getPath().endsWith(".xml")) {
                file = new File(file.getPath() + ".xml");
            }
            main.savePersonDataToFile(file);
        }
    }

    //Opens an about dialog
    @FXML
    private void handleAbout() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("AddressApp");
        alert.setHeaderText("About");
        alert.setContentText("Made by Roger Brian");

        alert.showAndWait();
    }


    //opens the birthday statistics
    @FXML
    private void handleShowBirthdayStatistics() {
        main.showBirthdayStatistics();
    }

    //Closes the application
    @FXML
    private void handleExit() {
        System.exit(0);
    }
}
