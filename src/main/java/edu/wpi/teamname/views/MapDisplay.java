package edu.wpi.teamname.views;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import edu.wpi.teamname.Algo.Edge;
import edu.wpi.teamname.Algo.Node;
import edu.wpi.teamname.Authentication.AuthenticationManager;
import edu.wpi.teamname.Database.LocalStorage;
import edu.wpi.teamname.Database.Submit;
import edu.wpi.teamname.simplify.Shutdown;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.LineBuilder;
import javafx.scene.shape.Path;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class MapDisplay {

    static double scaledWidth = 5000;
    static double scaledHeight = 3400.0;
    static double scaledX = 0;
    static double scaledY = 0;
    double mapWidth; //= 1000.0;
    double mapHeight;// = 680.0;
    double fileWidth; //= 5000.0;
    double fileHeight;// = 3400.0;
    double fileFxWidthRatio = mapWidth / fileWidth;
    double fileFxHeightRatio = mapHeight / fileHeight;
    ArrayList<Node> listOfNodes;
    ArrayList<Node> nodes;
    ArrayList<Node> localNodes = new ArrayList<>(); // Nodes within current parameters (IE: floor)
    ArrayList<Edge> edges;
    ArrayList<Edge> localEdges = new ArrayList<>(); // Edges within current parameters (IE: floor)
    HashMap<String, Node> nodesMap = new HashMap<>();
    HashMap<String, Edge> edgesMap = new HashMap<>();
    HashMap<Circle, Node> renderedNodeMap = new HashMap<>();
    HashMap<Line, Edge> renderedEdgeMap = new HashMap<>();
    Circle renderedAddNode;
    int addNodeX;
    int addNodeY;

    Circle dragStart;
    Circle dragEnd;
    Line renderedEdgePreview;

    Node addEdgeStart;
    Node addEdgeEnd;

    Node selectedNode;
    Edge selectedEdge;

    @FXML
    VBox popPop, adminPop, requestPop, registrationPop; // vbox to populate with different fxml such as Navigation/Requests/Login
    @FXML
    Path tonysPath; // the path displayed on the map
    @FXML
    JFXButton adminButton; // button that allows you to sign in
    @FXML
    ImageView hospitalMap;
    @FXML
    AnchorPane topElements;
    @FXML
    AnchorPane onTopOfTopElements;
    @FXML
    StackPane stackPane; // the pane the map is housed in
    @FXML
    VBox addNodeField;
    @FXML
    VBox addEdgeField;
    @FXML
    AnchorPane pathAnchor;
    @FXML
    AnchorPane anchor;
    @FXML
    JFXTextField edgeIdPreview;
    @FXML
    private JFXTextField nodeId;
    @FXML
    private JFXTextField nodeBuilding;
    @FXML
    private JFXTextField nodeType;
    @FXML
    private JFXTextField nodeShortName;
    @FXML
    private JFXTextField nodeLongName;
    @FXML
    private Label addEdgeWarning;

    @FXML
    private VBox editNode; // Edit node menu
    @FXML
    private JFXTextField editNodeBuilding;
    @FXML
    private JFXTextField editNodeType;
    @FXML
    private JFXTextField editNodeShortName;
    @FXML
    private JFXTextField editNodeLongName;
    @FXML
    private VBox deleteEdge;
    @FXML
    private JFXTextField deleteEdgeId;


    /**
     * getter for popPop Vbox
     *
     * @return popPop
     */
    public VBox getPopPop() {
        return popPop;
    }

    public abstract void initialize();

    /**
     * Display localNodes on the map
     *
     * @param _opacity Node opacity
     */
    public void displayNodes(double _opacity) {
        resizingInfo(); // Set resizing info

        localNodes.forEach(n -> { // For each node in localNodes
            Circle circle = new Circle(n.getX() * fileFxWidthRatio, n.getY() * fileFxHeightRatio, 8); // New node/cicle
            circle.setStrokeWidth(4); // Set the stroke with to 4
            /* Set the stroke color to transparent.
            This allows us to have an invisible border
            around the node where it's still selectable. */
            circle.setStroke(Color.TRANSPARENT);
            circle.setFill(Color.OLIVE); // Set node color to olive
            circle.setOpacity(_opacity); // Set node opacity (input param)
            renderedNodeMap.put(circle, n); // Link the rendered circle to the node in renderedNodeMap
            onTopOfTopElements.getChildren().add(circle); // Render the node

            circle.setOnMouseEntered(e -> { // Show a hover effect
                circle.setRadius(12); // Increase radius
                circle.setOpacity(0.6); // Decrease opacity
            });

            circle.setOnMouseExited(e -> { // Hide hover effect
                circle.setRadius(8); // Reset/set radius
                circle.setOpacity(0.8); // Reset/set opacity
            });
        });
    }

    /**
     * Display localEdges on the map
     *
     * @param _opacity Edge opacity
     */
    public void displayEdges(double _opacity) {
        resizingInfo(); // Set sizing info
        localEdges.forEach(e -> { // For edge in localEdges
            if (nodesMap.containsKey(e.getStartNode()) && nodesMap.containsKey(e.getEndNode())) { // If nodes exist
                // Create edge
                LineBuilder<?> edgeLocation = LineBuilder.create().startX(nodesMap.get(e.getStartNode()).getX() * fileFxWidthRatio).startY(nodesMap.get(e.getStartNode()).getY() * fileFxHeightRatio).endX(nodesMap.get(e.getEndNode()).getX() * fileFxWidthRatio).endY(nodesMap.get(e.getEndNode()).getY() * fileFxHeightRatio);
                Line edge = edgeLocation.stroke(Color.BLUE).strokeWidth(3).opacity(_opacity).build(); // Style edge
                renderedEdgeMap.put(edge, e);
                onTopOfTopElements.getChildren().add(edge); // Render edge

                edge.setOnMouseEntered(t -> { // Show a hover effect
                    edge.setStrokeWidth(6); // Increase width
                    edge.setOpacity(1); // Increase opacity
                });

                edge.setOnMouseExited(t -> { // Hide hover effect
                    edge.setOpacity(_opacity); // Reset/set opacity
                    edge.setStrokeWidth(3); // Reset/set stroke width
                });
            }
        });
    }

    /**
     * Initialize the map editor/display
     */
    public void initMapEditor() {
        popPop.setPickOnBounds(false); // Set popPop to disregard clicks
        displayEdges(.6); // Render edges at 0.6 opacity
        displayNodes(.8); // Render nodes at 0.8 opacity

        onTopOfTopElements.addEventHandler(MouseEvent.MOUSE_CLICKED, this::processClick); // Process click events
        onTopOfTopElements.addEventHandler(MouseEvent.MOUSE_MOVED, this::processMovement); // Process mouse movement events

        addNodeField.addEventHandler(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() { // Exit add node popup on "Esc" key
            @Override
            public void handle(KeyEvent t) {
                if (t.getCode() == KeyCode.ESCAPE) {
                    hideAddNodePopup();
                }
            }
        });

        addEdgeField.addEventHandler(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() { // Exit add edge popup on "Esc" key
            @Override
            public void handle(KeyEvent t) {
                if (t.getCode() == KeyCode.ESCAPE) {
                    hideAddEdgePopup();
                }
            }
        });

        editNode.addEventHandler(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() { // Exit add edge popup on "Esc" key
            @Override
            public void handle(KeyEvent t) {
                if (t.getCode() == KeyCode.ESCAPE) {
                    hideEditNodePopup();
                }
            }
        });

        deleteEdge.addEventHandler(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() { // Exit add edge popup on "Esc" key
            @Override
            public void handle(KeyEvent t) {
                if (t.getCode() == KeyCode.ESCAPE) {
                    hideDeleteEdgePopup();
                }
            }
        });

        anchor.addEventHandler(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() { // Exit popups on "Esc" key
            @Override
            public void handle(KeyEvent t) {
                if (t.getCode() == KeyCode.ESCAPE) {
                    hideAddNodePopup();
                    hideAddEdgePopup();
                    hideEditNodePopup();
                    hideDeleteEdgePopup();
                }
            }
        });
    }

    /**
     * Process mouse movement (IE: display edge preview)
     *
     * @param t Mouse event
     */
    private void processMovement(MouseEvent t) {
        if (dragStart != null && dragEnd != null) {
            return;
        }
        if (dragStart != null) {
            if (renderedEdgePreview != null) {
                topElements.getChildren().remove(renderedEdgePreview);
            }
            LineBuilder<?> edgeLocation = LineBuilder.create().startX(dragStart.getCenterX()).startY(dragStart.getCenterY()).endX(t.getX()).endY(t.getY());
            this.renderedEdgePreview = edgeLocation.stroke(Color.TOMATO).strokeWidth(3).opacity(1).build();
            this.renderedEdgePreview.setPickOnBounds(false);
            topElements.getChildren().add(renderedEdgePreview);
        }
    }

    /**
     * stores needed resizing info for scaling the displayed nodes on the map as the window changes
     */
    public void resizingInfo() {
        mapWidth = hospitalMap.boundsInParentProperty().get().getWidth();
        mapHeight = hospitalMap.boundsInParentProperty().get().getHeight();
        fileWidth = hospitalMap.getImage().getWidth();
        fileHeight = hospitalMap.getImage().getHeight();
        fileFxWidthRatio = mapWidth / fileWidth;
        fileFxHeightRatio = mapHeight / fileHeight;
    }

    public double xCoordOnTopElement(int x) {
        double fileWidth = 5000.0;
        double fileHeight = 3400.0;

        double widthScale = scaledWidth / fileWidth;
        double heightScale = scaledHeight / fileHeight;

        double windowWidth = hospitalMap.boundsInParentProperty().get().getWidth() / fileWidth;
        double windowHeight = hospitalMap.boundsInParentProperty().get().getHeight() / fileHeight;
        double windowSmallestScale = Math.max(Math.min(windowHeight, windowWidth), 0);
        double viewportSmallestScale = Math.max(Math.min(heightScale, widthScale), 0);
        return ((x - scaledX) / viewportSmallestScale) * windowSmallestScale;
    }

    /**
     * for the scaling the displayed nodes on the map
     *
     * @param y the y coordinate of the anchor pane, top element
     * @return the scaled y coordinate
     */
    public double yCoordOnTopElement(int y) {
        double fileWidth = 5000.0;

        double fileHeight = 3400.0;
        double widthScale = scaledWidth / fileWidth;
        double heightScale = scaledHeight / fileHeight;

        double windowWidth = hospitalMap.boundsInParentProperty().get().getWidth() / fileWidth;
        double windowHeight = hospitalMap.boundsInParentProperty().get().getHeight() / fileHeight;
        double windowSmallestScale = Math.max(Math.min(windowHeight, windowWidth), 0);
        double viewportSmallestScale = Math.max(Math.min(heightScale, widthScale), 0);
        return ((y - scaledY) / viewportSmallestScale) * windowSmallestScale;
    }

    /**
     * Pull from LocalStorage and refresh data
     */
    void refreshData() {
        nodes = LocalStorage.getInstance().getNodes(); // Get nodes from LocalStorage
        localNodes = new ArrayList<>(); // Reset localNodes
        nodes.forEach(n -> { // For node in nodes
            if (nodeWithinSpec(n)) { // If node is within spec (IE: building/floor)
                localNodes.add(n); // Add to local nodes
            }
        });

        nodesMap.clear(); // CLear the node map
        localNodes.forEach(n -> { // For node in localNodes
            nodesMap.put(n.getNodeID(), n); // Add node to nodesMap
        });

        edges = LocalStorage.getInstance().getEdges(); // Get edges from LocalStorage
        localEdges = new ArrayList<>(); // Reset localEdges
        edges.forEach(e -> { // For edge in edges
            if (nodesMap.containsKey(e.getStartNode()) && nodesMap.containsKey(e.getEndNode())) { // If nodes exist in nodes map
                /* We don't have to check if the start and end nodes
                are within spec as all nodes in nodesMap are in spec. */
                localEdges.add(e); // Add edge to local edges
            }
        });

        edgesMap.clear(); // Clear edge map
        localEdges.forEach(e -> { // For edge in localEdges
            edgesMap.put(e.getEdgeID(), e); // Add edge to edgesMap
        });
    }

    /**
     * Refresh/display map
     */
    private void renderMap() {
        clearMap(); // Clear map
        displayEdges(.6); // Display edges at 0.6 opacity
        displayNodes(.8); // Display nodes at 0.8 opacity
        dragStart = null; // Reset dragStart (IE: user clicks away)
        dragEnd = null; // Reset dragEnd (IE: user clicks away)
    }

    /**
     * Process click events. Handles showing/hiding the add node
     * and add edge popups, as well as add edge tracking vars.
     *
     * @param t Mouse Event
     */
    private void processClick(MouseEvent t) {
        if (t.getButton() == MouseButton.SECONDARY) {
            processRightClick(t);
            return;
        } else if (t.getButton() != MouseButton.PRIMARY) {
            return;
        }
        if (!LoadFXML.getCurrentWindow().equals("mapEditorBar")) {
            return; // Don't process clicks outside of the map editor.
        }

        if (t.getTarget() instanceof Circle) { // If a circle object is clicked
            if (dragStart == null) { // If dragStart isn't null (IE: If the user has started to create an edge)
                hideAddEdgePopup(); // Hide the edge popup
                hideAddNodePopup(); // Hide the node popup
                hideEditNodePopup(); // Hide the edit node popup
                hideDeleteEdgePopup(); // Hide the remove edge popup
                dragStart = (Circle) t.getTarget(); // Set selected circle as dragStart (new edge start)
            } else if (dragEnd == null) { // Else if dragEnd isn't null (IE: If the user is partway through creating an edge)
                dragEnd = (Circle) t.getTarget(); // Set selected circle as dragEnd (new edge end)

                // Build line between dragStart and dragEnd (potential new edge)
                LineBuilder<?> edgeLocation = LineBuilder.create().startX(dragStart.getCenterX()).startY(dragStart.getCenterY()).endX(dragEnd.getCenterX()).endY(dragEnd.getCenterY());
                topElements.getChildren().remove(renderedEdgePreview); // Remove previously displayed edge preview
                this.renderedEdgePreview = edgeLocation.stroke(Color.RED).strokeWidth(3).opacity(1).build(); // Set potential edge styling
                topElements.getChildren().add(renderedEdgePreview); // Display potential edge

                // Edge popup
                addEdgeStart = renderedNodeMap.get(dragStart); // Get potential edge start node
                addEdgeEnd = renderedNodeMap.get(dragEnd); // Get potential edge end node
                showEdgePopup(renderedNodeMap.get(dragStart), renderedNodeMap.get(dragEnd), dragEnd); // Show edge confirmation window

                dragStart = null; // Reset dragStart
                dragEnd = null; // Reset dragEnd
            }
            return;
        }

        hideAddEdgePopup(); // Hide edge popup on click (IE: The user click away from the add edge popup)
        hideEditNodePopup(); // Hide the edit node popup
        hideDeleteEdgePopup(); // Hide the remove edge popup

        dragStart = null; // Reset dragStart (IE: user clicks away)
        dragEnd = null; // Reset dragEnd (IE: user clicks away)

        addNodeX = (int) (t.getX() / fileFxWidthRatio); // Set the potential new node X coords
        addNodeY = (int) (t.getY() / fileFxHeightRatio); // Set the potential new node Y coords

        addNodeField.setPickOnBounds(true); // Enable clicking on the add node popup
        addNodeField.setVisible(true); // Show the add node popup

        // Relative to mouse
        if (t.getY() < onTopOfTopElements.getHeight() / 2) { // If mouse is in bottom half of screen
            addNodeField.setTranslateY(t.getY() + 20); // Show above
        } else { // Else (if mouse is in top half of screen)
            addNodeField.setTranslateY(t.getY() - addNodeField.getHeight() - 20); // Show below
        }

        // Relative to mouse
        if (onTopOfTopElements.getWidth() * 0.2 > t.getX()) { // If mouse is in the left 1/5th of screen
            addNodeField.setTranslateX(t.getX()); // Show popup to the right
        } else if (onTopOfTopElements.getWidth() * 0.8 > t.getX()) { // Else if it's in the middle
            addNodeField.setTranslateX(t.getX() - (0.5 * addNodeField.getWidth())); // Show popup in the center
        } else { // Else (if mouse is in the right 1/5th)
            addNodeField.setTranslateX(t.getX() - addNodeField.getWidth()); // Show popup to the left
        }

        if (renderedAddNode != null) { // If an old node is rendered
            onTopOfTopElements.getChildren().remove(renderedAddNode); // Hide old node
        }
        renderedAddNode = new Circle(t.getX(), t.getY(), 8); // Build potential new node
        renderedAddNode.setFill(Color.TOMATO); // Set color
        renderedAddNode.setOpacity(0.9); // Set opacity
        onTopOfTopElements.getChildren().add(renderedAddNode); // Display potential new node
    }

    /**
     * Process right clicks
     * @param t Mouse Event
     */
    private void processRightClick(MouseEvent t) {
        if (t.getTarget() instanceof Circle) { // If the user clicks a circle/node
            Node toEdit = renderedNodeMap.get((Circle) t.getTarget()); // Get the node
            selectedNode = toEdit; // Update selectedNode
            showEditNodePopup(toEdit, t); // Show edit node popup
        } else if (t.getTarget() instanceof Line) { // Else if the user clicks a line/edge
            Edge toDelete = renderedEdgeMap.get((Line) t.getTarget()); // Get the edge
            selectedEdge = toDelete; // Updated selected edge
            showRemoveEdgePopup(toDelete, t); // Show the remove edge popup
        }
    }

    /**
     * Hide the edit node popup
     */
    private void hideEditNodePopup() {
        editNode.setVisible(false); // Hide the popup
        editNode.setPickOnBounds(false); // Set clickable to false

        editNodeBuilding.setText(""); // Reset the node building field
        editNodeType.setText(""); // Reset the node type field
        editNodeShortName.setText(""); // Reset the node short name field
        editNodeLongName.setText(""); // Reset the node long name field

        selectedNode = null; // Reset the selected node
    }

    /**
     * Show the edit node popup
     * @param _toEdit The node to edit
     * @param t Mouse Event
     */
    private void showEditNodePopup(Node _toEdit, MouseEvent t) {
        hideAddNodePopup(); // Hide the add edge popup
        hideAddEdgePopup(); // Hide the add edge popup
        hideDeleteEdgePopup(); // Hide the delete edge popup

        editNodeBuilding.setText(_toEdit.getBuilding()); // Set the building field
        editNodeType.setText(_toEdit.getNodeType()); // Set the type field
        editNodeShortName.setText(_toEdit.getShortName()); // Set the short name field
        editNodeLongName.setText(_toEdit.getLongName()); // Set the long name field

        editNode.setVisible(true); // Set visible to true
        editNode.setPickOnBounds(true); // Set clickable to true
        // Relative to mouse
        if (t.getY() < onTopOfTopElements.getHeight() / 2) { // If mouse is in bottom half of screen
            editNode.setTranslateY(t.getY() + 20); // Show above
        } else { // Else (if mouse is in top half of screen)
            editNode.setTranslateY(t.getY() - editNode.getHeight() - 20); // Show below
        }

        // Relative to mouse
        if (onTopOfTopElements.getWidth() * 0.2 > t.getX()) { // If mouse is in the left 1/5th of screen
            editNode.setTranslateX(t.getX()); // Show popup to the right
        } else if (onTopOfTopElements.getWidth() * 0.8 > t.getX()) { // Else if it's in the middle
            editNode.setTranslateX(t.getX() - (0.5 * editNode.getWidth())); // Show popup in the center
        } else { // Else (if mouse is in the right 1/5th)
            editNode.setTranslateX(t.getX() - editNode.getWidth()); // Show popup to the left
        }
    }

    /**
     * Show the remove edge popup
     * @param _toDelete Edge to delete
     * @param t Mouse Event
     */
    private void showRemoveEdgePopup(Edge _toDelete, MouseEvent t) {
        hideAddNodePopup(); // Hide the add node popup
        hideAddEdgePopup(); // Hide the add ege popup
        hideEditNodePopup(); // Hide the edit node popup

        deleteEdgeId.setText(_toDelete.getEdgeID()); // Set the edgeId field/preview

        deleteEdge.setVisible(true); // Show the remove edge popup
        deleteEdge.setPickOnBounds(true); // Set clickable to true
        // Relative to mouse
        if (t.getY() < onTopOfTopElements.getHeight() / 2) { // If mouse is in bottom half of screen
            deleteEdge.setTranslateY(t.getY() + 20); // Show above
        } else { // Else (if mouse is in top half of screen)
            deleteEdge.setTranslateY(t.getY() - deleteEdge.getHeight() - 20); // Show below
        }

        // Relative to mouse
        if (onTopOfTopElements.getWidth() * 0.2 > t.getX()) { // If mouse is in the left 1/5th of screen
            deleteEdge.setTranslateX(t.getX()); // Show popup to the right
        } else if (onTopOfTopElements.getWidth() * 0.8 > t.getX()) { // Else if it's in the middle
            deleteEdge.setTranslateX(t.getX() - (0.5 * deleteEdge.getWidth())); // Show popup in the center
        } else { // Else (if mouse is in the right 1/5th)
            deleteEdge.setTranslateX(t.getX() - deleteEdge.getWidth()); // Show popup to the left
        }
    }

    /**
     * Hide the delete edge popup
     */
    private void hideDeleteEdgePopup() {
        deleteEdge.setVisible(false); // Hide the popup
        deleteEdge.setPickOnBounds(false); // Set clickable to false

        deleteEdgeId.setText(""); // Reset the edge Id field
        selectedEdge = null; // Reset the selected edge
    }

    /**
     * Save the edited node
     * @param e Action Event
     */
    @FXML
    private void editNodeSave(ActionEvent e) {
        Node newNode = new Node( // Create new node object
                selectedNode.getNodeID(), // Same NodeID
                selectedNode.getX(),
                selectedNode.getY(),
                "1", // TODO: Node floor from level selector
                editNodeBuilding.getText(),
                editNodeType.getText(),
                editNodeLongName.getText(),
                editNodeShortName.getText()
        );
        Submit.getInstance().editNode(newNode); // Update LocalStorage/the database
        hideEditNodePopup(); // Hide the edit node popup
        refreshData(); // Refresh the data from LocalStorage
        renderMap(); // Render/refresh the map (with the updated data)
    }

    /**
     * Delete the currently selected edge
     * @param e Action Event
     */
    @FXML
    private void confirmDeleteEdge(ActionEvent e) {
        Submit.getInstance().removeEdge(selectedEdge); // Remove the selected edge
        hideDeleteEdgePopup(); // Hide the edit node popup
        refreshData(); // Refresh the node and edge data from LocalStorage
        renderMap(); // Render/display the map (with the updated information)
    }

    /**
     * Delete the currently selected node
     * @param e Action Event
     */
    @FXML
    private void deleteNode(ActionEvent e) {
        Submit.getInstance().removeNode(selectedNode); // Remove the selected node
        hideEditNodePopup(); // Hide the edit node popup
        refreshData(); // Refresh the node and edge data from LocalStorage
        renderMap(); // Render/display the map (with the updated information)
    }

    /**
     * Show the edge confirmation popup.
     *
     * @param startNode Edge start node
     * @param endNode   Edge end node
     * @param _dragEnd  Edge end circle
     */
    private void showEdgePopup(Node startNode, Node endNode, Circle _dragEnd) {
        int x = (int) _dragEnd.getCenterX(); // Get end node X position (for UI use)
        int y = (int) _dragEnd.getCenterY(); // Get end node Y position (for UI use)
        addEdgeField.setPickOnBounds(true); // Set popup as clickable
        addEdgeField.setVisible(true); // Show popup
        edgeIdPreview.setText(startNode.getNodeID() + "_" + endNode.getNodeID()); // Prefill edgeId (not editable)

        // Relative to mouse
        if (y < onTopOfTopElements.getHeight() / 2) { // If mouse is in the bottom half of screen
            addEdgeField.setTranslateY(y + 20); // Show popup above
        } else { // Else (if mouse is in top half of screen)
            addEdgeField.setTranslateY(y - addEdgeField.getHeight() - 20); // Show popup below
        }

        if (onTopOfTopElements.getWidth() * 0.2 > x) { // If mouse is in the left 1/5th of screen
            addEdgeField.setTranslateX(x); // Show popup to the right
        } else if (onTopOfTopElements.getWidth() * 0.8 > x) { // Else if it's in the middle
            addEdgeField.setTranslateX(x - (0.5 * addEdgeField.getWidth())); // Show popup in the center
        } else { // Else if it's in the right 1/5th of screen
            addEdgeField.setTranslateX(x - addEdgeField.getWidth()); // Show popup to the left
        }
    }

    /**
     * Hide add edge confirmation popup
     */
    private void hideAddEdgePopup() {
        addEdgeField.setPickOnBounds(false); // Set clickable to false
        addEdgeField.setVisible(false); // Hide popup
        dragStart = null; // Reset dragStart
        dragEnd = null; // Reset dragEnd
        addEdgeWarning.setVisible(false); // Hide warning message
        edgeIdPreview.setText(""); // Clear edgeId
        if (renderedEdgePreview != null) { // If edge preview rendered
            topElements.getChildren().remove(renderedEdgePreview); // Hide edge preview
        }
    }

    /**
     * Hide add node popup
     */
    public void hideAddNodePopup() {
        addNodeField.setPickOnBounds(false); // Set clickable to false
        addNodeField.setVisible(false); // Hide popup
        nodeId.setText(""); // Reset nodeId field
        nodeBuilding.setText(""); // Reset node building field
        nodeType.setText(""); // Reset node type field
        nodeShortName.setText(""); // Reset node short name field
        nodeLongName.setText(""); // Reset node long name field
        if (renderedAddNode != null) { // If node preview rendered
            onTopOfTopElements.getChildren().remove(renderedAddNode); // Hide node preview
        }
    }

    /**
     * Display list of nodes and associated edges
     *
     * @param _listOfNodes Arraylist of nodes to render
     */
    public abstract void drawPath(ArrayList<Node> _listOfNodes);

    /**
     * toggles the navigation window
     */
    public void toggleNav() {
        clearMap(); // clear the map
        popPop.setPickOnBounds(true); // Set popPop clickable to true
        popPop.setPrefWidth(350.0); // Set preferable width to 350
        Navigation navigation = new Navigation(this); // Load controller
        navigation.loadNav(); // Load nav controller
        listOfNodes = navigation.getListOfNodes(); // Get list of nodes from navigation
        stackPane.heightProperty().addListener((obs, oldVal, newVal) -> { // Add resize listener
            resizingInfo(); // Set resize listener
            onTopOfTopElements.getChildren().clear(); // Clear children
            displayNodes(1); // Display nodes at 1 (100%) opacity
            hospitalMap.fitHeightProperty().bind(stackPane.heightProperty()); // Bind map width to pane width
        });
        if (!LoadFXML.getCurrentWindow().equals("navBar")) { // If navbar selected
            onTopOfTopElements.getChildren().clear(); // Clear children
            return;
        }
        displayNodes(1); // Display nodes at 1 (100%) opacity
    }

    /**
     * Clear the map
     */
    public void clearMap() {
        onTopOfTopElements.getChildren().clear(); // Clear on top of top elements
        topElements.getChildren().clear(); // Clear top elements
        tonysPath.getElements().clear(); // Clear Tony's path
        hideAddNodePopup(); // Hide add node popup
        hideAddEdgePopup(); // Hide add edge popup
    }

    /**
     * toggle the requests window
     */
    public void openRequests() {
        popPop.setPickOnBounds(true); // Set clickable to true
        clearMap(); // Clear map
        popPop.setPrefWidth(350.0); // Set preferable width to 350
        LoadFXML.getInstance().loadWindow("Requests", "reqBar", popPop); // Load requests window
    }


    /**
     * toggle the login window
     */
    public void openLogin() {
        popPop.setPickOnBounds(true); // Set clickable to true
        clearMap(); // Clear map
        popPop.setPrefWidth(350.0); // Set preferable width to 350
        if (!AuthenticationManager.getInstance().isAuthenticated()) { // If user isn't authenticated
            LoadFXML.getInstance().loadWindow("Login", "loginBar", popPop); // Display login button
        } else { // Else (if user is authenticated)
            AuthenticationManager.getInstance().signOut(); // Display sign out button
        }
    }

    /**
     * toggle the check in window
     */
    public void openCheckIn() {
        popPop.setPickOnBounds(true); // Set clickable to true
        clearMap(); // Clear map
        popPop.setPrefWidth(657.0); // Set preferable width to 657
        LoadFXML.getInstance().loadWindow("UserRegistration", "registrationButton", popPop); // Load registration window
    }

    /**
     * Triggered by Add Node button
     *
     * @param event Action Event
     */
    public void addNode(ActionEvent event) {
        addNodeInternal( // Call add node internal with relevant information
                addNodeX,
                addNodeY,
                "1", // TODO: Node floor from level selector
                nodeId.getText(),
                nodeBuilding.getText(),
                nodeType.getText(),
                nodeShortName.getText(),
                nodeLongName.getText()
        );
        hideAddNodePopup(); // Hide add node popup
    }

    /**
     * Triggered by Add Edge button
     *
     * @param event Action event
     */
    public void addEdge(ActionEvent event) {
        String edgeId = addEdgeStart.getNodeID() + "_" + addEdgeEnd.getNodeID(); // Create edgeId
        // If the edge (or the reverse edge) already exists
        if (edgesMap.containsKey(edgeId) || edgesMap.containsKey(addEdgeEnd.getNodeID() + "_" + addEdgeStart.getNodeID()) || addEdgeStart.getNodeID().equals(addEdgeEnd.getNodeID())) {
            addEdgeWarning.setVisible(true); // Display the "Edge already exists" warning
            return; // Don't add edge
        }
        Edge edge = new Edge(edgeId, addEdgeStart.getNodeID(), addEdgeEnd.getNodeID()); // Create new edge
        Submit.getInstance().addEdge(edge); // Add the edge
        hideAddEdgePopup(); // Hide the edge popup
        refreshData(); // Refresh the node and edge data from LocalStorage
        renderMap(); // Render/display the map (with the updated information)
    }

    /**
     * exit the application
     */
    public void exitApplication() {
        Shutdown.getInstance().exit(); // Graceful shutdown
    }


    /**
     * Checks if the specified node is within the current specifications (IE: the correct building/s)
     *
     * @param n Node
     * @return true if the node is within current specifications
     */
    public boolean nodeWithinSpec(Node n) {
        return ((n.getFloor().equals("1") || n.getFloor().equals("G") || n.getFloor().equals("")) && (n.getBuilding().equals("Tower") || n.getBuilding().equals("45 Francis") || n.getBuilding().equals("15 Francis") || n.getBuilding().equals("Parking") || n.getBuilding().equals("")));
    }


    /**
     * Add a node
     *
     * @param x             Node x coordinate
     * @param y             Node y coordinate
     * @param nodeFloor     Node floor/level
     * @param nodeId        Node ID
     * @param nodeBuilding  Node building
     * @param nodeType      Node type
     * @param nodeShortName Node short name
     * @param nodeLongName  Node long name
     */
    private void addNodeInternal(int x, int y, String nodeFloor, String nodeId, String nodeBuilding, String nodeType, String nodeShortName, String nodeLongName) {
        Node node = new Node(nodeId, x, y, nodeFloor, nodeBuilding, nodeType, nodeLongName, nodeShortName); // Create a node
        Submit.getInstance().addNode(node); // Add the node
        refreshData(); // Refresh the node and edge data from LocalStorage
        renderMap(); // Render/display the map (with the updated information)
        dragStart = null; // Reset dragStart
        dragEnd = null; // Reset dragEnd
    }

    @FXML
    private void openHelp(ActionEvent e) {
        System.out.println("HALP");
    }
}