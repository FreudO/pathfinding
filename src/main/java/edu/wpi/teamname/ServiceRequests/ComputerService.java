package edu.wpi.teamname.ServiceRequests;

import com.jfoenix.controls.*;
import edu.wpi.teamname.Algo.Node;
import edu.wpi.teamname.Database.MasterServiceRequestStorage;
import edu.wpi.teamname.Database.LocalStorage;
import edu.wpi.teamname.Database.Submit;
import edu.wpi.teamname.Entities.ServiceRequests.ServiceRequest;
import edu.wpi.teamname.views.LoadFXML;
import edu.wpi.teamname.views.Requests;
import edu.wpi.teamname.views.Success;
import edu.wpi.teamname.views.manager.ButtonManager;
import edu.wpi.teamname.views.manager.SceneManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class ComputerService {

    /**
     * Label indicating if a name has been filled in incorrectly
     */
    @FXML
    private Label failedName;

    /**
     * Text field to input name
     */
    @FXML
    private JFXTextField nameInput;

    /**
     * Label indicating that a description wasn't entered
     */
    @FXML
    private Label failedServiceDescription;

    @FXML
    private JFXTextArea descriptionInput;

    @FXML
    private Label failedUrgency;

    /**
     * Checkbox for selecting a low urgency request
     */
    @FXML
    private JFXCheckBox lowUrgency;

    /**
     * Checkbox for selecting a medium urgency request
     */
    @FXML
    private JFXCheckBox mediumUrgency;

    /**
     * Checkbox for selecting a high urgency request
     */
    @FXML
    private JFXCheckBox highUrgency;

    /**
     * Label indicating the phone number text field has been filled in incorrectly
     */
    @FXML
    private Label failedPhoneNumber;

    /**
     * Text Field to enter phone number
     */
    @FXML
    private JFXTextField phoneInput;

    /**
     * Label indicating a location hasn't been selected
     */
    @FXML
    private Label failedLocationEntry;

    /**
     * Combo Box selecting what location the delivery should be sent to
     */
    @FXML
    private JFXComboBox<String> requestLocation;

    /**
     * Success pop up page
     */
    @FXML
    private VBox successPop;
    @FXML
    private Label title;
    @FXML
    private Text desc;
    @FXML
    private Label askFullName;
    @FXML
    private Label askDesc;
    @FXML
    private Label askPriority;
    @FXML
    private Label askPhone;
    @FXML
    private Label askLocation;
    @FXML
    private JFXButton submitButton;


    /**
     * Instance of Requests class used to create a popup window
     */
    @FXML
    private edu.wpi.teamname.views.Requests request;

    /**
     * List of Service Requests
     */
    private List<ServiceRequest> requests;

    /**
     * Constructor used to create a pop up window for GiftDelivery Request
     * @param request an instance of Requests.java
     */
    public ComputerService(Requests request) {
        this.request = request;
    }

    public void initialize() {
        ArrayList<String> listOfNodeNames = new ArrayList<>();
        HashMap<String, Node> nodesMap = new HashMap<>();
        for (Node node : LocalStorage.getInstance().getNodes()) {
            nodesMap.put(node.getNodeID(), node); // put the nodes in the hashmap
            listOfNodeNames.add(node.getLongName());
            Collections.sort(listOfNodeNames);
        }  listOfNodeNames.forEach(n -> {
            requestLocation.getItems().add(n); // make the nodes appear in the combobox
        });

    }

    /**
     * Retrieves sucess pop up page
     *
     * @return Success pop up page
     */
    public VBox getSuccessPop() {
        return successPop;
    }

    /**
     * Checks if first name and last name have been filled in correctly
     *
     * @return true if there's a space between the first and last name, and false otherwise
     */
    public boolean nameInputValid() {
        return nameInput.getText().contains(" ");
    }

    /**
     * Checks if the description has been filled in
     *
     * @return true if there's a space between the first and last name, and false otherwise
     */
    public boolean descriptionValid() {
        return !descriptionInput.getText().isEmpty();
    }

    /**
     * Checks if a checkbox has been selected
     *
     * @return true if any checkbox has been selected, and false otherwise
     */
    public boolean checkBoxSelected() {
        return lowUrgency.isSelected() || mediumUrgency.isSelected() || highUrgency.isSelected();
    }

    public boolean oneUrgencySelected() {
        return (lowUrgency.isSelected() && !mediumUrgency.isSelected() && !highUrgency.isSelected()) || (!lowUrgency.isSelected() && mediumUrgency.isSelected() && !highUrgency.isSelected()) || (!lowUrgency.isSelected() && !mediumUrgency.isSelected() && highUrgency.isSelected());
    }

    /**
     * Checks if the phone number text field has been filled in correctly
     *
     * @return true if the number matches the "XXX-XXX-XXXX" format
     */
    public boolean phoneNumberValid() {
        return phoneInput.getText().matches("\\d{3}-\\d{3}-\\d{4}");
    }

    /**
     * Checks if a location has been selected correctly
     *
     * @return true if a location has been selected, and false otherwise
     */
    public boolean locationValid() {
        return requestLocation.getValue() != null;
    }

    public void addRequest(ServiceRequest request) {
        requests.add(request);
    }

    /**
     * Performs operations when the submit button is clicked
     * <ul>
     *     <li>Checks if the inputs are valid</li>
     *     <li>Creates a new gift request object</li>
     *     <li>Adds the gift request to the service request database</li>
     * </ul>
     *
     * @param event event triggering submission
     */
    public void submitRequest(ActionEvent event) {
        if (phoneInput.getText().length() == 10 && !phoneInput.getText().contains("-")) {
            phoneInput.setText(phoneInput.getText().substring(0, 3) + "-" + phoneInput.getText().substring(3, 6) + "-" + phoneInput.getText().substring(6));
        }

        //Checks if all the inputs are valid
        if (!nameInputValid())
            failedName.setText("Invalid Name Entry");
        else
            failedName.setText("");

        if (!descriptionValid())
            failedServiceDescription.setText("Invalid Description Entry");
        else
            failedServiceDescription.setText("");

        if (!checkBoxSelected())
            failedUrgency.setText("Select an Urgency Level");
        else if (!oneUrgencySelected())
            failedUrgency.setText("Select Only One Urgency Level");
        else
            failedUrgency.setText("");

        if (!phoneNumberValid())
            failedPhoneNumber.setText("Invalid Phone Number");
        else
            failedPhoneNumber.setText("");

        if (!locationValid())
            failedLocationEntry.setText("Invalid Location Selection");

        if (requests == null) {
            requests = new ArrayList<ServiceRequest>();
        }

        if (nameInputValid() && checkBoxSelected() && phoneNumberValid() && descriptionValid() && oneUrgencySelected() && locationValid()) {
            //Adds all the selected gifts to an arraylist
            ArrayList<String> selected = new ArrayList<>();
            if (lowUrgency.isSelected())
                selected.add("Low Urgency");
            if (mediumUrgency.isSelected())
                selected.add("Medium Urgency");
            if (highUrgency.isSelected())
                selected.add("High Urgency");

            LoadFXML.setCurrentWindow("");

            //Add this request to our list of requests
            //requests.add(new GiftRequest(phoneInput.getText(), requestLocation.getValue(), nameInput.getText()));
            MasterServiceRequestStorage request = new MasterServiceRequestStorage("Computer Services", requestLocation.getValue(), selected, descriptionInput.getText(), nameInput.getText(), phoneInput.getText(), "", false);
            Submit.getInstance().submitGiftDelivery(request);

            // load Success page in successPop VBox
            successPop.setPrefWidth(657.0);
            Success success = new Success(this);
            success.loadSuccess("You have successfully submitted the form. Your request will be fulfilled shortly.", successPop);

            ButtonManager.remove_class();
        }
    }

    /**
     * Load Request form when the button is pressed/make it disappear
     */
    public void loadRequest() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/edu/wpi/teamname/views/ServiceRequestComponents/ComputerService.fxml"));
        try {
            loader.setControllerFactory(type -> {
                if (type == ComputerService.class)
                    return this;
                else
                    try {
                        return type.newInstance();
                    } catch (RuntimeException e) {
                        throw e;
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
            });
            Parent root = loader.load();
            LoadFXML.getInstance().openWindow("computerServicesBar", root, SceneManager.getInstance().getDefaultPage().getPopPop()); //open/close request form
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void backToRequests(ActionEvent actionEvent) {
        LoadFXML.getInstance().loadWindow("Requests", "reqBar", SceneManager.getInstance().getDefaultPage().getPopPop());
    }
}
