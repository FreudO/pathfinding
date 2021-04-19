package edu.wpi.teamname.views;

import edu.wpi.teamname.Algo.Node;
import edu.wpi.teamname.Authentication.AuthListener;
import edu.wpi.teamname.Authentication.AuthenticationManager;
import edu.wpi.teamname.simplify.Shutdown;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Controller for DefaultPage.fxml
 * @author Anthony LoPresti, Lauren Sowerbutts, Justin Luce
 */
public class DefaultPage implements AuthListener {

    @FXML
    private VBox popPop; // vbox to populate with different fxml such as Navigation/Requests/Login
    @FXML
    private VBox adminPop; // vbox to populate Map Editor button
    @FXML
    private VBox requestPop; // vbox to populate Submitted Requests button
    @FXML
    private Path tonysPath; // this is Tony's path
    @FXML
    private AnchorPane anchor; // the foundation of the image (needs to be replaced by a stack pane or vbox that has the proportions of the map)
    @FXML
    private ImageView hospitalMap; // the map itself

    String openWindow = ""; // used to check which window is currently open
    ArrayList<Node> currentPath = new ArrayList<>(); // the current list of nodes

    public void initialize() {
        AuthenticationManager.getInstance().addListener(this);

        tonysPath.getElements().clear();

        anchor.widthProperty().addListener((obs, oldVal, newVal) -> {
            if (currentPath.size() > 0) {
                drawPath(currentPath);
            }
        });

        anchor.heightProperty().addListener((obs, oldVal, newVal) -> {
            if (currentPath.size() > 0) {
                drawPath(currentPath);
            }
        });
    }

    public void loadWindowPopPop(String fileName, String windowName) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/teamname/views/" + fileName + ".fxml"));
            openWindowPopPop(windowName, root);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void loadWindowAdminPop(String fileName, String windowName) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/teamname/views/" + fileName + ".fxml"));
            openWindowAdminPop(windowName, root);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void loadWindowRequestPop(String fileName, String windowName) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/teamname/views/" + fileName + ".fxml"));
            openWindowRequestPop(windowName, root);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void openWindowPopPop(String windowName, Parent root) {
        popPop.getChildren().clear();
        if (!windowName.equals(openWindow)) {
            popPop.getChildren().add(root);
            openWindow = windowName;
            return;
        }
        openWindow = "";
    }

    public void openWindowAdminPop(String windowName, Parent root) {
        adminPop.getChildren().clear();
        if (!windowName.equals(openWindow)) {
            adminPop.getChildren().add(root);
            openWindow = windowName;
            return;
        }
        openWindow = "";
    }

    public void openWindowRequestPop(String windowName, Parent root) {
        requestPop.getChildren().clear();
        if (!windowName.equals(openWindow)) {
            requestPop.getChildren().add(root);
            openWindow = windowName;
            return;
        }
        openWindow = "";
    }

    public void toggleNav(ActionEvent actionEvent) {

        // load controller here
        Navigation navigation = new Navigation(this);

        navigation.loadNav();
    }

    public void openRequests(ActionEvent actionEvent) {
        loadWindowPopPop("Requests", "reqBar");
    }

    public void openLogin(ActionEvent actionEvent) {
        loadWindowPopPop("Login", "loginBar");
    }

    public void exitApplication(ActionEvent actionEvent) {
        Shutdown.getInstance().exit();
    }

    public void drawPath(ArrayList<Node> _listOfNodes) {
        if (_listOfNodes.size() < 1) {
            return;
        }
        currentPath = _listOfNodes;
        tonysPath.getElements().clear();
        double mapWidth = hospitalMap.boundsInParentProperty().get().getWidth();
        double mapHeight = hospitalMap.boundsInParentProperty().get().getHeight();
        double fileWidth = hospitalMap.getImage().getWidth();
        double fileHeight = hospitalMap.getImage().getHeight();
        double fileFxWidthRatio = mapWidth / fileWidth;
        double fileFxHeightRatio = mapHeight / fileHeight;
        Node firstNode = _listOfNodes.get(0);
        MoveTo start = new MoveTo(firstNode.getX() * fileFxWidthRatio, firstNode.getY() * fileFxHeightRatio);
        tonysPath.getElements().add(start);
        System.out.println(fileFxWidthRatio);
        _listOfNodes.forEach(n -> {
            tonysPath.getElements().add(new LineTo(n.getX() * fileFxWidthRatio, n.getY() * fileFxHeightRatio));
        });
    }

    @Override
    public void userLogin() {
        loadWindowAdminPop("MapEditorButton", "mapButton");
        loadWindowRequestPop("SubmittedRequests", "reqButton");
    }
}
