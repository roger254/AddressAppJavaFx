package roger.app.address;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import roger.app.address.model.Person;
import roger.app.address.view.PersonEditDialogController;
import roger.app.address.view.PersonOverViewController;

import java.io.IOException;
import java.net.URL;

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
    /*
    Returns the data as an observable lists of persons
    @return
     */

    public static void main(String[] args) {
        launch(args);
    }

    public ObservableList<Person> getPersonData() {
        return personData;
    }

    /*
    Initialize the root layout
     */

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("AddressApp");

        initRootLayout();

        showPersonalOverview();
    }

    /*
    Shows the person overview inside the root layout
     */

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
        } catch (IOException e) {
            e.printStackTrace();

        }
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
}
