package edu.wpi.teamname.views;

import edu.wpi.teamname.bridge.Bridge;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class SubmittedRegistrations extends LoadFXML{

    @FXML
    private DefaultPage defaultPage;

    public void openSubmittedRegistrations() {
        Bridge.getInstance().loadRegistration();
    }
}