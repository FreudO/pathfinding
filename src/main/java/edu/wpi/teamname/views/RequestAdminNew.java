package edu.wpi.teamname.views;

import edu.wpi.teamname.Database.LocalStorage;
import edu.wpi.teamname.Database.MasterServiceRequestStorage;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class RequestAdminNew {

    @FXML
    private VBox cellHolder;
    @FXML
    private VBox giftCellHolder;
    @FXML
    private VBox foodCellHolder;
    @FXML
    private VBox computerCellHolder;
    @FXML
    private VBox facilitiesCellHolder;
    @FXML
    private VBox laundryCellHolder;
    @FXML
    private VBox medicineCellHolder;
    @FXML
    private VBox transportCellHolder;
    @FXML
    private VBox sanitationCellHolder;

    public void initialize() {
        LocalStorage.getInstance().getMasterStorages().forEach(g -> {
            for (int i = 0; i < 2; i++) {
                try {
                    String requestType = g.getRequestType().replace(" ", "");
                    System.out.println(requestType);
                    Node node = loadWindow(requestType);
                    if (i == 0) { cellHolder.getChildren().add(node); }
                    HBox hbox = (HBox) node;
                    switch (g.getRequestType()) {
                        case "Gift Delivery":
                            if (i == 1) { giftCellHolder.getChildren().add(node); }
                            hbox.getChildren().forEach(h -> {
                                if (h instanceof Label) {
                                    Label label = (Label) h;
                                    switch (label.getId()) {
                                        case "nameCell":
                                            label.setText(g.getRequestedBy());
                                            break;
                                        case "giftCell":
                                            String youAreStringNow = String.join(", ", g.getRequestedItems());
                                            label.setText(youAreStringNow.replace("\"", ""));
                                            break;
                                        case "phoneCell":
                                            label.setText(g.getContact());
                                            break;
                                        case "locationCell":
                                            label.setText(g.getLocation());
                                            break;
                                        default:
                                            label.setText("PANIK");
                                    }
                                }
                            });
                            break;
                        case "Food Delivery":
                            if (i == 1) { foodCellHolder.getChildren().add(node); }
                            hbox.getChildren().forEach(h -> {
                                if (h instanceof Label) {
                                    Label label = (Label) h;
                                    switch (label.getId()) {
                                        case "nameCell":
                                            label.setText(g.getRequestedBy());
                                            break;
                                        case "foodCell":
                                            String youAreStringNow = String.join(", ", g.getRequestedItems());
                                            label.setText(youAreStringNow.replace("\"", ""));
                                            break;
                                        case "phoneCell":
                                            label.setText(g.getContact());
                                            break;
                                        case "locationCell":
                                            label.setText(g.getLocation());
                                            break;
                                        default:
                                            label.setText("PANIK");
                                    }
                                }
                            });
                            break;
                        case "Computer Services":
                            if (i == 1) { computerCellHolder.getChildren().add(node); }
                            hbox.getChildren().forEach(h -> {
                                if (h instanceof Label) {
                                    Label label = (Label) h;
                                    switch (label.getId()) {
                                        case "nameCell":
                                            label.setText(g.getRequestedBy());
                                            break;
                                        case "descriptionCell":
                                            label.setText(g.getDescription());
                                            break;
                                        case "priorityCell":
                                            label.setText(g.getRequestedItems().get(0));
                                            break;
                                        case "phoneCell":
                                            label.setText(g.getContact());
                                            break;
                                        case "locationCell":
                                            label.setText(g.getLocation());
                                            break;
                                        default:
                                            label.setText("PANIK");
                                    }
                                }
                            });
                            break;
                        case "Facilities Request":
                            if (i == 1) { facilitiesCellHolder.getChildren().add(node); }
                            hbox.getChildren().forEach(h -> {
                                if (h instanceof Label) {
                                    Label label = (Label) h;
                                    switch (label.getId()) {
                                        case "nameCell":
                                            label.setText(g.getRequestedBy());
                                            break;
                                        case "descriptionCell":
                                            label.setText(g.getDescription());
                                            break;
                                        case "urgencyCell":
                                            label.setText(g.getRequestedItems().get(0));
                                            break;
                                        case "phoneCell":
                                            label.setText(g.getContact());
                                            break;
                                        case "locationCell":
                                            label.setText(g.getLocation());
                                            break;
                                        default:
                                            label.setText("PANIK");
                                    }
                                }
                            });
                            break;
                        case "Laundry Service":
                            if (i == 1) { laundryCellHolder.getChildren().add(node); }
                            hbox.getChildren().forEach(h -> {
                                if (h instanceof Label) {
                                    Label label = (Label) h;
                                    switch (label.getId()) {
                                        case "nameCell":
                                            label.setText(g.getRequestedBy());
                                            break;
                                        case "loadCell":
                                            label.setText(g.getRequestedItems().get(0));
                                            break;
                                        case "washCell":
                                            label.setText(g.getDescription());
                                            break;
                                        case "phoneCell":
                                            label.setText(g.getContact());
                                            break;
                                        case "locationCell":
                                            label.setText(g.getLocation());
                                            break;
                                        default:
                                            label.setText("PANIK");
                                    }
                                }
                            });
                            break;
                        case "Medicine Delivery":
                            if (i == 1) { medicineCellHolder.getChildren().add(node); }
                            hbox.getChildren().forEach(h -> {
                                if (h instanceof Label) {
                                    Label label = (Label) h;
                                    switch (label.getId()) {
                                        case "nameCell":
                                            label.setText(g.getRequestedBy());
                                            break;
                                        case "medicationCell":
                                            label.setText(g.getRequestedItems().get(0));
                                            break;
                                        case "dosageCell":
                                            label.setText(g.getDescription());
                                            break;
                                        case "locationCell":
                                            label.setText(g.getLocation());
                                            break;
                                        default:
                                            label.setText("PANIK");
                                    }
                                }
                            });
                            break;
                        case "Patient Transportation":
                            if (i == 1) { transportCellHolder.getChildren().add(node); }
                            hbox.getChildren().forEach(h -> {
                                if (h instanceof Label) {
                                    Label label = (Label) h;
                                    switch (label.getId()) {
                                        case "nameCell":
                                            label.setText(g.getRequestedBy());
                                            break;
                                        case "currentCell":
                                            label.setText(g.getLocation());
                                            break;
                                        case "destCell":
                                            label.setText(g.getDescription());
                                            break;
                                        case "reasonCell":
                                            label.setText(g.getRequestedItems().get(0));
                                            break;
                                        default:
                                            label.setText("PANIK");
                                    }
                                }
                            });
                            break;
                        case "Sanitation Service":
                            if (i == 1) { sanitationCellHolder.getChildren().add(node); }
                            hbox.getChildren().forEach(h -> {
                                if (h instanceof Label) {
                                    Label label = (Label) h;
                                    switch (label.getId()) {
                                        case "nameCell":
                                            label.setText(g.getRequestedBy());
                                            break;
                                        case "urgencyCell":
                                            label.setText(g.getRequestedItems().get(0));
                                            break;
                                        case "reasonCell":
                                            label.setText(g.getDescription());
                                            break;
                                        case "locationCell":
                                            label.setText(g.getLocation());
                                            break;
                                        default:
                                            label.setText("PANIK");
                                    }
                                }
                            });
                            break;
                        default:
                            System.out.println("PANIK");
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    public Node loadWindow(String fileName) {
        try {
            Node node = FXMLLoader.load(getClass().getResource("/edu/wpi/teamname/views/ServiceRequestCells/" + fileName + "Cells.fxml"));
            return node;
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return null;
    }
}
