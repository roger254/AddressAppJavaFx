package roger.app.address.view;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import roger.app.address.Main;
import roger.app.address.model.Person;

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
    public PersonOverViewController(){
    }

    /*
    Initializes the controller class. This method is automatically called
    after the fxml file has been loaded
     */
    @FXML
    private void initialize(){
        //Initialize the person table with the two columns
        firstNameColumn.setCellValueFactory(cellData -> cellData.getValue().firstNameProperty());
        lastNameColumn.setCellValueFactory(cellData -> cellData.getValue().lastNameProperty());

        /*
        for integer and doubles
        myIntegerColumn.setCellValueFactory(cellData ->
      cellData.getValue().myIntegerProperty().asObject());
         */
    }

    /*
    This is called by the main application to give a reference back to itself
    @param main
     */
    public void setMain(Main main){
        this.main = main;

        //add observable list data to the table
        personTable.setItems(main.getPersonData());
    }

    /*
    Fills all text fields to show details about the person.
    if the Specified person is null, all text fields are cleared.
    @param person the person or null
     */

    private void showPersonDetails(Person person){
        if (person != null){
            //fill the labels with info from the person object.
            firstNameLabel.setText(person.getFirstName());
            lastNameLabel.setText(person.getLastName());
            streetLabel.setText(person.getStreet());
            postalCodeLabel.setText(Integer.toString(person.getPostalCode()));
            cityLabel.setText(person.getCity());

            //TODO: need a way to convert the birthday into a string
            //birthdayLabel.setText(...);
        }else {
            //person is null, remove all the text
            firstNameLabel.setText("");
            lastNameLabel.setText("");
            streetLabel.setText("");
            postalCodeLabel.setText("");
            cityLabel.setText("");
            birthdayLabel.setText("");
        }
    }
}
