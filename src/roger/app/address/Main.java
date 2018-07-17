package roger.app.address;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import roger.app.address.model.Person;
import roger.app.address.model.PersonListWrapper;
import roger.app.address.view.PersonEditDialogController;
import roger.app.address.view.PersonOverViewController;
import roger.app.address.view.RootLayoutController;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.prefs.Preferences;

public class Main extends Application {

    private Stage primaryStage;
    private BorderPane rootLayout;

    //The data as an observable list of Persons
    private ObservableList<Person> personData = FXCollections.observableArrayList();

    /*
    Constructor
     */

    public Main() {
        //Add Some sample data
        personData.add(new Person("Hans", "Muster"));
        personData.add(new Person("Ruth", "Mueller"));
        personData.add(new Person("Heinz", "Kurz"));
        personData.add(new Person("Cornelia", "Meier"));
        personData.add(new Person("Werner", "Meyer"));
        personData.add(new Person("Lydia", "Kunz"));
        personData.add(new Person("Anna", "Best"));
        personData.add(new Person("Stefan", "Meier"));
        personData.add(new Person("Martin", "Mueller"));
    }

    public static void main(String[] args) {
        launch(args);
    }

    /*
    Initialize the root layout
     */

    /*
    Returns the data as an observable lists of persons
    @return
     */
    public ObservableList<Person> getPersonData() {
        return personData;
    }

    /*
    Shows the person overview inside the root layout
     */

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("AddressApp");

        initRootLayout();

        showPersonalOverview();
    }

    private void initRootLayout() {
        try {
            //load root layout from xml file
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation((new URL("file:///home/roger/IdeaProjects/AddressAppJavaFx/src/roger/app/address/view/RootLayout.fxml")));
            rootLayout = loader.load();

            //Show the scene containing the root layout
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.show();

            //Give the controller access to the main ap
            RootLayoutController controller = loader.getController();
            controller.setMain(this);

            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();

        }

        //Try to load last opened person file
        File file = getPersonFilePath();
        if (file != null)
            loadPersonDataFromFile(file);
    }

    private void showPersonalOverview() {
        try {
            //Load person overview
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(new URL("file:///home/roger/IdeaProjects/AddressAppJavaFx/src/roger/app/address/view/PersonOverView.fxml"));
            AnchorPane personOverview = loader.load();

            //set person overview into the center of root layout
            rootLayout.setCenter(personOverview);

            //Give the controller access to the main app
            PersonOverViewController controller = loader.getController();
            controller.setMain(this);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
    Returns the main stage.
    @return
     */
    public Stage getPrimaryStage() {
        return primaryStage;
    }

    /*
    Opens a dialog to edit details for the specific person. If the user
    clicks OK, the changes are saved into the provided person object and true
    is returned

    @param person the person object to be edited
    @return true if user clicked OK, false otherwise.
     */
    public boolean showPersonEditDialog(Person person) {
        try {
            //load the fxml file  and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(new URL("file:///home/roger/IdeaProjects/AddressAppJavaFx/src/roger/app/address/view/PersonEditDialog.fxml"));
            AnchorPane page = loader.load();

            //Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Edit Person");
            dialogStage.initModality(Modality.WINDOW_MODAL); //TODO: check what modality is
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            //set the person into the controller
            PersonEditDialogController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setPerson(person);

            //show the dialog and wait until the user closes it
            dialogStage.showAndWait();

            return controller.isOkClicked();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /*
    Return the person file preference, i.e the file tha was last opened.
    The preference is read from the OS specific registry. If no such
    preference can be found, null is returned.

    @return
     */
    public File getPersonFilePath() {
        Preferences preferences = Preferences.userNodeForPackage(Main.class);
        String filePath = preferences.get("filePath", null);
        if (filePath != null)
            return new File(filePath);
        else
            return null;
    }

    /*
    SSets the file path of the currently loaded file. The path is persisted in the
    OS specific registry

    @param file the file or null to remove the path
     */
    public void setPersonFilePath(File file) {
        Preferences preferences = Preferences.userNodeForPackage(Main.class);
        if (file != null) {
            preferences.put("filePath", file.getPath());
            //update the stage title
            primaryStage.setTitle("AddressApp - " + file.getName());
        } else {
            preferences.remove("filePath");
            //update the stage title
            primaryStage.setTitle("AddressApp");
        }
    }

    /*
    Loads person data from the specified file
    The current person data will be replaced

    @param file
     */
    public void loadPersonDataFromFile(File file) {
        try {
            JAXBContext context = JAXBContext.newInstance(PersonListWrapper.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();

            //Reading XML from the file and unmarshalling.
            PersonListWrapper wrapper = (PersonListWrapper) unmarshaller.unmarshal(file);

            personData.clear();
            personData.addAll(wrapper.getPersons());
        } catch (Exception e) { //catch any exception
            failToLoadAlert(file);
        }
    }

    /*
    Saves the current person data to the specified file

    @param file
     */
    public void savePersonDataToFile(File file) {
        try {
            JAXBContext context = JAXBContext.newInstance(PersonListWrapper.class);
            Marshaller m = context.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            //wrapping our person data
            PersonListWrapper wrapper = new PersonListWrapper();
            wrapper.setPersons(personData);

            //Marshalling and saving XML to the file
            m.marshal(wrapper, file);

            //save the file path to the registry
            setPersonFilePath(file);
        } catch (Exception e) {
            failToLoadAlert(file);
        }
    }

    private void failToLoadAlert(File file) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Could not load data");
        alert.setContentText("Could not load data from file: \n" + file.getPath());

        alert.showAndWait();
    }
}
