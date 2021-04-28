package edu.wpi.teamname.views;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import edu.wpi.teamname.Algo.Edge;
import edu.wpi.teamname.Algo.Node;
import edu.wpi.teamname.App;
import edu.wpi.teamname.Authentication.AuthListener;
import edu.wpi.teamname.Authentication.AuthenticationManager;
import edu.wpi.teamname.Database.LocalStorage;
import edu.wpi.teamname.simplify.Shutdown;
import edu.wpi.teamname.views.manager.LevelManager;
import edu.wpi.teamname.views.manager.SceneManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;

/**
 * Controller for DefaultPage.fxml
 *
 * @author Anthony LoPresti, Lauren Sowerbutts, Justin Luce
 */
public class DefaultPage extends MapDisplay implements AuthListener {

    // used to save the current list of nodes after AStar

    @FXML
    private JFXButton floor3Bttn, floor2Bttn, floor1Bttn, GBttn, L1Bttn, L2Bttn;
    @FXML
    private VBox popPop, adminPop, requestPop, registrationPop; // vbox to populate with different fxml such as Navigation/Requests/Login
    @FXML
    private Path tonysPath; // the path displayed on the map
    @FXML
    private ImageView hospitalMap; // the map
    @FXML
    private StackPane stackPane; // the pane the map is housed in
    @FXML
    private JFXButton adminButton; // button that allows you to sign in
    @FXML
    private AnchorPane topElements; // anchor pane where displayed nodes reside
    @FXML
    private JFXComboBox<String> languageBox; //selects language you want
    @FXML
    private JFXButton Navigation;
    @FXML
    private JFXButton CheckIn;
    @FXML
    private JFXButton Requests;


    public String languageHelper(String getText){ //simplifies the language in helper 2, gets the value for given language
        return Translator.getInstance().languageHashmap.get(Translator.getInstance().getCurrentLanguage()).get(getText);
    }

    public void languageHelper2(){ //can call this for each language
        Navigation.setText(languageHelper("Navigation"));
        CheckIn.setText(languageHelper("CheckIn"));
        Requests.setText(languageHelper("Requests"));
    }


    public void languageSwitch() { //picks a language and checks current language
        if(languageBox.getValue().equals("English")){
            Translator.getInstance().setCurrentLanguage("language_english");
            languageHelper2();
        }
        if(languageBox.getValue().equals("Spanish")){
            Translator.getInstance().setCurrentLanguage("language_spanish");
            languageHelper2();
        }
        if(languageBox.getValue().equals("Chinese - Simplified")){
            Translator.getInstance().setCurrentLanguage("language_chineseSimplified");
            languageHelper2();
        }
    }
    /**
     * run on startup
     */
    public void initialize() {
        languageBox.setVisible(false);
        hideAddNodePopup();
        SceneManager.getInstance().setDefaultPage(this);
        LevelManager.getInstance().setFloor(3);
        AuthenticationManager.getInstance().addListener(this);
        languageBox.getItems().add("English");
        languageBox.getItems().add("Spanish");

        if (AuthenticationManager.getInstance().isAuthenticated()) {
            displayAuthPages();
        }

        tonysPath.getElements().clear(); // clear the path
        LoadFXML.setCurrentWindow(""); // set the open window to nothing

        anchor.heightProperty().addListener((obs, oldVal, newVal) -> { // adjust the path and the map to the window as it changes
            if (currentPath.size() > 0 && LoadFXML.getCurrentWindow().equals("navBar")) {
                drawPath(currentPath);
            }
            if(!LoadFXML.getCurrentWindow().equals("navBar")){
                currentPath= new ArrayList();
            }
            topElements.getChildren().clear();
            resizingInfo();
            zooM.zoomAndPan();
        });

        anchor.widthProperty().addListener((obs, oldVal, newVal) -> { // adjust the path and the map to the window as it changes
            if (currentPath.size() > 0 && LoadFXML.getCurrentWindow().equals("navBar")) {
                drawPath(currentPath);
            }
            if(!LoadFXML.getCurrentWindow().equals("navBar")){
                currentPath= new ArrayList();
            }

            topElements.getChildren().clear();
            resizingInfo();
            zooM.zoomAndPan();
        });

        refreshData();
        zooM.zoomAndPan();
    }

    private void displayAuthPages() {
        LoadFXML.getInstance().loadWindow("MapEditorButton", "mapButton", adminPop);
        LoadFXML.getInstance().loadWindow("SubmittedRequestsButton", "reqButton", requestPop);
        LoadFXML.getInstance().loadWindow("SubmittedRegistrationsButton", "regButton", registrationPop);
        LoadFXML.getInstance().loadWindow("EmployeeTableButton", "employeeButton", employeePop);
        MaterialDesignIconView signOut = new MaterialDesignIconView(MaterialDesignIcon.EXIT_TO_APP);
        signOut.setFill(Paint.valueOf("#c3c3c3"));
        signOut.setGlyphSize(52);
        adminButton.setGraphic(signOut);
    }

    /**
     * for displaying the new buttons after user logins
     */
    @Override
    public void userLogin() {
        displayAuthPages();
    }

    /**
     * getting rid of the buttons after user sign outs
     */
    @Override
    public void userLogout() {
        adminPop.getChildren().clear();
        requestPop.getChildren().clear();
        registrationPop.getChildren().clear();
        employeePop.getChildren().clear();
        MaterialDesignIconView signOut = new MaterialDesignIconView(MaterialDesignIcon.ACCOUNT_BOX_OUTLINE);
        signOut.setFill(Paint.valueOf("#c3c3c3"));
        signOut.setGlyphSize(52);
        adminButton.setGraphic(signOut);
        popPop.getChildren().clear();
    }

    /**
     * toggle the map editor window
     */
    public void toggleMapEditor() {
        scaledX = 0;
        scaledY = 0;
        scaledWidth = 5000;
        scaledHeight = 3400.0;
        clearMap();
        popPop.getChildren().clear();
        popPop2.getChildren().clear();
        zooM.zoomAndPan();
        if (LoadFXML.getCurrentWindow().equals("mapEditorBar")) {
            topElements.getChildren().clear();
            LoadFXML.setCurrentWindow("");
            zooM.zoomAndPan();
            return;
        }

        initMapEditor();

        LoadFXML.setCurrentWindow("mapEditorBar");
    }

    /**
     * toggle the admin registration window
     */
    public void toggleRegistration() {
        clearMap();
        popPop.setPrefWidth(1000);
        LoadFXML.getInstance().loadWindow("RegistrationAdminViewNew", "registrationBar", popPop);
    }

    /**
     * toggle the admin request window
     */
    public void toggleRequest() {
        clearMap();
        popPop.setPrefWidth(1000);
        LoadFXML.getInstance().loadWindow("RequestAdminNew", "reqAdminBar", popPop);
    }

    /**
     * toggle the admin request window
     */
    public void toggleEmployee() {
        clearMap();
        popPop.setPrefWidth(1000);
        LoadFXML.getInstance().loadWindow("EmployeeTable", "employeeBar", popPop);
    }

    public void toggleGoogleMaps() {
        clearMap();
        popPop.setPrefWidth(400);
        popPop.getChildren().clear();
        LoadFXML.getInstance().loadWindow("GoogleMapForm", "googleMapBar", popPop);
    }
    public void toggleGoogleMapsHome() {
        clearMap();
        popPop.setPrefWidth(400);
        popPop.getChildren().clear();
        LoadFXML.getInstance().loadWindow("GoogleMapHome", "googleMapHomeBar", popPop);
    }
    @FXML
    private void openHelp() {
        popPop.setPrefWidth(340);
        if (LoadFXML.getCurrentWindow().equals("")) {
            LoadFXML.getInstance().loadHelp("defaultBar", "help_defaultBar", popPop);
            return;
        }
        if (LoadFXML.getCurrentWindow().equals("mapEditorBar")) {
            LoadFXML.getInstance().loadHelp("mapEditorBar", "help_mapBar", popPop);
            return;
        }
        LoadFXML.getInstance().loadHelp(LoadFXML.getCurrentWindow(), "help_" + LoadFXML.getCurrentWindow(), popPop2);
    }
    
    void toggleButtons(ArrayList<String> floors){
        if (floors.contains("L2"))
            L2Bttn.setDisable(true);
        else
            L2Bttn.setDisable(false);
        if (floors.contains("L1"))
            L1Bttn.setDisable(true);
        else
            L1Bttn.setDisable(false);
        if (floors.contains("G"))
            GBttn.setDisable(true);
        else
            GBttn.setDisable(false);
        if (floors.contains("1"))
            floor1Bttn.setDisable(true);
        else
            floor1Bttn.setDisable(false);
        if (floors.contains("2"))
            floor2Bttn.setDisable(true);
        else
            floor2Bttn.setDisable(false);
        if (floors.contains("3"))
            floor3Bttn.setDisable(true);
        else
            floor3Bttn.setDisable(false);
    }

    public void closeWindows() {
        popPop.getChildren().clear();
    }

    public void setHospitalMap(Image _image) {
        hospitalMap.setImage(_image);
    }

}