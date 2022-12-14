package edu.wpi.teamname.views;

import edu.wpi.teamname.views.manager.SceneManager;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

/**
 * Controller for COVIDMessage.fxml
 *
 * @author Lauren Sowerbutts, Frank McShan
 */
public class COVIDMessage {

    @FXML private Label title;
    @FXML private Label successText;

    public static boolean covid = false;

    public void openPathToEmergency() {
        covid = true;
        SceneManager.getInstance().getDefaultPage().toggleNav();
    }
}
