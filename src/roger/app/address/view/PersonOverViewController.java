package roger.app.address.view;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import roger.app.address.Main;
import roger.app.address.model.Person;
import roger.app.address.util.DateUtil;

public class PersonOverViewController {

    @FXML
    private TableView<Person> personTable;
    @FXML
    private TableColumn<Person, String> firstNameColumn;
    @FXML
    private TableColumn<Person, String> lastNameColumn;

    @FXML
    private Label firstNameLabel;
    @FXML
    private Label lastNameLabel;
    @FXML
    private Label streetLabel;
    @FXML
    private Label postalCodeLabel;
    @FXML
    private Label cityLabel;
    @FXML
    private Label birthdayLabel;

    //main application object
    private Main main;

    //The constructor
    public PersonOverViewController() {
    }

    /*
    Initializes the controller class. This method is automatically called
    after the fxml file has been loaded
     */
    @FXML
    private void initialize() {
        //Initialize the person table with the two columns
        firstNameColumn.setCellValueFactory(cellData -> cellData.getValue().firstNameProperty());
        lastNameColumn.setCellValueFactory(cellData -> cellData.getValue().lastNameProperty());

        /*
        for integer and doubles
        myIntegerColumn.setCellValueFactory(cellData ->
      cellData.getValue().myIntegerProperty().asObject());
         */

        //clear person details
        showPersonDetails(null);

        //listen for selection changes and show the person details when changed
        personTable.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> showPersonDetails(newValue)));
    }

    /*
    This is called by the main application to give a reference back to itself
    @param main
     */
    public void setMain(Main main) {
        this.main = main;

        //add observable list data to the table
        personTable.setItems(main.getPersonData());
    }

    /*
    Fills all text fields to show details about the person.
    if the Specified person is null, all text fields are cleared.
    @param person the person or null
     */

    private void showPersonDetails(Person person) {
        if (person != null) {
            //fill the labels with info from the person object.
            firstNameLabel.setText(person.getFirstName());
            lastNameLabel.setText(person.getLastName());
            streetLabel.setText(person.getStreet());
            postalCodeLabel.setText(Integer.toString(person.getPostalCode()));
            cityLabel.setText(person.getCity());

            //TODO: need a way to convert the birthday into a string ->Done
            birthdayLabel.setText(DateUtil.format(person.getBirthday()));
        } else {
            //person is null, remove all the text
            firstNameLabel.setText("");
            lastNameLabel.setText("");
            streetLabel.setText("");
            postalCodeLabel.setText("");
            cityLabel.setText("");
            birthdayLabel.setText("");
        }
    }

    /*
    called when the user clicks on the delete button..
     */
    @FXML
    private void handleDeletePerson() {
        int selectedIndex = personTable.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0)
            personTable.getItems().remove(selectedIndex);
        else
            warningAlert();
    }

    /*
    Called when the user clicks the new button.
     */
    @FXML
    private void handleNewPerson() {
        Person tempPerson = new Person();
        boolean okClicked = main.showPersonEditDialog(tempPerson);
        if (okClicked)
            main.getPersonData().add(tempPerson);
    }

    /*
    Called when the user clicks the edit button.
    Opens a dialog to edit details for the selected person.
     */
    @FXML
    private void handleEditPerson() {
        Person selectedPerson = personTable.getSelectionModel().getSelectedItem();
        if (selectedPerson != null) {
            boolean okClicked = main.showPersonEditDialog(selectedPerson);
            if (okClicked)
                showPersonDetails(selectedPerson);
        } else {
            //Nothing is selected
            warningAlert();
        }
    }

    private void warningAlert() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.initOwner(main.getPrimaryStage());
        alert.setTitle("No Selection");
        alert.setHeaderText("No Person Selected");
        alert.setContentText("Please select a person in the table");

        alert.showAndWait();
    }
}
