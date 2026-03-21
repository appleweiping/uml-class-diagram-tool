package com.mycompany.irr00_group_project.gui;

import com.mycompany.irr00_group_project.utils.FxUtils;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;
import javafx.scene.transform.Scale;

/**
 * Pane containing controls for the bar at the bottom of the scene.
 * 
 * @author Deniz Büyükgüral
 */
public class BottomBar extends HBox {
    
    /**
     * Pane which displays number of validation results
     */
    class ValidationCountBar extends HBox {
        
        public ValidationCountBar() {
            
            ValidationPane validationPane = mainScene.getValidationPane();
            
            // Init UI
            
            setAlignment(Pos.CENTER_LEFT);
            setSpacing(2.0);
            
            createImageLabel(
                    ConcreteValidationPane.INFO_IMAGE, validationPane.infoCountProperty());
            
            createImageLabel(
                    ConcreteValidationPane.WARNING_IMAGE, validationPane.warningCountProperty());
            
            createImageLabel(
                    ConcreteValidationPane.ERROR_IMAGE, validationPane.errorCountProperty());
        }
        
        /**
         * Create an icon and label and add them to the children of the HBox.
         * The label is set to the count's value.
         * Label and icon gets hidden when count is zero.
         * @param image image of the icon
         * @param count property to which label's text will be bound to
         */
        private void createImageLabel(Image image, ReadOnlyIntegerProperty count) {
            
            Pane iconPane = makeImage(image);
            getChildren().add(iconPane);
            Label countLabel = new Label("0");
            countLabel.textProperty().bind(count.asString());
            getChildren().add(countLabel);
            
            count.addListener((ov, oldVal, newVal) -> {
                if ((int) newVal == 0) {
                    iconPane.setVisible(false);
                    iconPane.setMinWidth(0.0);
                    iconPane.setPrefWidth(0.0);
                    countLabel.setVisible(false);
                    countLabel.setMinWidth(0.0);
                    countLabel.setPrefWidth(0.0);
                } else {
                    iconPane.setVisible(true);
                    iconPane.setMinWidth(USE_COMPUTED_SIZE);
                    iconPane.setPrefWidth(12.0);
                    countLabel.setVisible(true);
                    countLabel.setMinWidth(USE_COMPUTED_SIZE);
                    countLabel.setPrefWidth(USE_COMPUTED_SIZE);
                }
            });
            
            if (count.get() == 0) {
                iconPane.setVisible(false);
                iconPane.setMinWidth(0.0);
                iconPane.setPrefWidth(0.0);
                countLabel.setVisible(false);
                countLabel.setMinWidth(0.0);
                countLabel.setPrefWidth(0.0);
            } else {
                iconPane.setVisible(true);
                iconPane.setMinWidth(USE_COMPUTED_SIZE);
                iconPane.setPrefWidth(12.0);
                countLabel.setVisible(true);
                countLabel.setMinWidth(USE_COMPUTED_SIZE);
                countLabel.setPrefWidth(USE_COMPUTED_SIZE);
            }
        }
        
        /**
         * Create an empty pane with the given image on the background.
         * @param image image to put on the background
         * @return small pane with the given image on the background
         */
        private Pane makeImage(Image image) {
            Pane pane = new Pane();
            pane.setBackground(FxUtils.createImageBackground(image));
            pane.setPrefWidth(12.0);
            pane.setPrefHeight(12.0);
            
            return pane;
        }
    }
    
    public static final double MIN_ZOOM = 0.5;
    public static final double MAX_ZOOM = 4.0;
    
    private final MainScene mainScene;
    private final Scale canvasScale = new Scale();
    
    private final Label zoomLabel;
    private final Slider zoomSlider;
    
    public BottomBar(MainScene mainScene) {
        
        if (mainScene == null) {
            throw new IllegalArgumentException("mainScene cannot be null");
        }
        
        this.mainScene = mainScene;
        
        // Init UI
        
        setAlignment(Pos.CENTER_LEFT);
        setPrefHeight(20.0);
        setBackground(new Background(
                new BackgroundFill(Color.GHOSTWHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        
        ValidationCountBar validationCountBar = new ValidationCountBar();
        getChildren().add(validationCountBar);
        HBox.setHgrow(validationCountBar, Priority.SOMETIMES);
        setMargin(validationCountBar, new Insets(0.0, 0.0, 0.0, 5.0));
        
        Pane flexPane = new Pane();
        getChildren().add(flexPane);
        flexPane.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(flexPane, Priority.ALWAYS);
        
        zoomLabel = new Label("Zoom: 100,0%");
        getChildren().add(zoomLabel);
        HBox.setHgrow(zoomLabel, Priority.SOMETIMES);
        
        zoomSlider = new Slider(MIN_ZOOM, MAX_ZOOM, 1.0);
        getChildren().add(zoomSlider);
        zoomSlider.setFocusTraversable(false);
        
        attachZoomListener();
    }
    
    /**
     * Get the canvas zoom multiplier.
     * @return zoom multiplier of the canvas
     */
    private double getZoom() {
        return zoomSlider.getValue();
    }

    /**
     * Set the zoom multiplier of the canvas.
     * @param newZoom zoom multiplier of the canvas
     * @pre {newZoom != Double.NaN}
     * @throws IllegalArgumentExceptin if preconditions are violated
     */
    private void setZoom(double newZoom) {
        if (Double.isNaN(newZoom)) {
            throw new IllegalArgumentException("newZoom cannot be NaN");
        }
        
        newZoom = Math.clamp(newZoom, MIN_ZOOM, MAX_ZOOM);
        zoomSlider.setValue(newZoom);
    }
    
    /**
     * Adds relevant listeners and binds canvas scale to the zoom factor.
     */
    private void attachZoomListener() {
        ScrollPane rootPane = mainScene.getRootPane();
        
        // Listen for mouse scroll with CTRL key down
        rootPane.addEventFilter(ScrollEvent.ANY, (e) -> {
            if (!e.isControlDown()) {
                return;
            }
            
            e.consume();
            canvasScale.setPivotX(e.getX());
            canvasScale.setPivotY(e.getY());
            
            if (e.getDeltaY() < 0) {
                setZoom(getZoom() - 0.1);
            } else {
                setZoom(getZoom() + 0.1);
            }
        });
        
        // Listen for zoom slider value changes, set canvas scale accordingly
        zoomSlider.valueProperty().addListener((ov, oldZoom, newZoom) -> {
            zoomLabel.setText(String.format("Zoom: %.1f%%", (double) newZoom * 100.0));
            canvasScale.setX((double) newZoom);
            canvasScale.setY((double) newZoom);
        });
        
        DiagramCanvasPane canvas = mainScene.getCanvas();
        
        canvas.getRoot().getTransforms().add(canvasScale);
        
        // Resize canvas with the root pane. Account for scroll bars.
        canvas.getRoot().prefWidthProperty().bind(
                rootPane.widthProperty().subtract(10).divide(canvasScale.xProperty()));
        canvas.getRoot().prefHeightProperty().bind(
                rootPane.heightProperty().subtract(10).divide(canvasScale.yProperty()));
    }
}
