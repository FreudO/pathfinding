package edu.wpi.teamname.views.help;

import edu.wpi.teamname.views.LoadFXML;
import edu.wpi.teamname.views.manager.SceneManager;

public class mapEditorBar {
    public void closeWindow() {
        SceneManager.getInstance().getDefaultPage().getPopPop().getChildren().clear();
        LoadFXML.setCurrentHelp("");
    }
}
