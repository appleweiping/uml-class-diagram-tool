package com.mycompany.irr00_group_project.gui;

import com.mycompany.irr00_group_project.gui.eventData.ResizeEvent;
import java.util.ArrayList;
import javafx.geometry.HPos;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import com.mycompany.irr00_group_project.listeners.EventListener;
import com.mycompany.irr00_group_project.utils.GeometryUtils;
import javafx.scene.Cursor;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;

/**
 * Resize pane can be used by rectangular nodes to handle user resize requests.
 * 
 * After instantiation, {@code attachToParent(parent)} must be called once to
 * bind the resize pane to the parent region.
 * 
 * The resize pane is initially disabled and hidden. It can be enabled by
 * calling {@code setEnabled(true)}.
 * 
 * Once the user finishes resizing the node, a resize event will be emitted, notifying
 * all listeners registered with {@code addResizeListener(listener)}.
 * 
 * @author Deniz Büyükgüral
 */
class ResizePane extends StackPane {
    
    private final StackPane resizeUL;
    private final StackPane resizeU;
    private final StackPane resizeUR;
    private final StackPane resizeBL;
    private final StackPane resizeB;
    private final StackPane resizeBR;
    private final StackPane resizeL;
    private final StackPane resizeR;
    
    private final StackPane resizePane;
    private final Pane resizeFrame;
    
    private final ArrayList<EventListener<? super ResizeEvent>> resizeListeners
            = new ArrayList<>();
    
    private double minWidth = 0;
    private double minHeight = 0;
    
    private boolean enabled = true;
    
    private Region parent = null;
    
    private Region referenceRegion = null;
    
    /**
     * Create the resize pane and attach it to a parent.
     * @param parent the parent of the resize pane
     * @param referenceRegion region which will be used for mouse calculations
     * @throws IllegalArgumentException if parent == null
     */
    public ResizePane(Region parent, Region referenceRegion) {
        super();
        
        if (parent == null) {
            throw new IllegalArgumentException("parent cannot be null");
        }
        
        this.parent = parent;
        this.referenceRegion = referenceRegion;
        
        // Init root pane
        setPickOnBounds(false);
        prefWidthProperty().bind(parent.widthProperty());
        prefHeightProperty().bind(parent.heightProperty());
        super.setMinWidth(USE_PREF_SIZE);
        super.setMinHeight(USE_PREF_SIZE);
        setMaxWidth(USE_PREF_SIZE);
        setMaxHeight(USE_PREF_SIZE);
        
        // Initialize the container for draggable handles
        resizePane = new StackPane();
        getChildren().add(resizePane);
        resizePane.setPickOnBounds(false);
        resizePane.prefWidthProperty().bind(widthProperty());
        resizePane.prefHeightProperty().bind(heightProperty());
        resizePane.setMinWidth(USE_PREF_SIZE);
        resizePane.setMinHeight(USE_PREF_SIZE);
        resizePane.setMaxWidth(USE_PREF_SIZE);
        resizePane.setMaxHeight(USE_PREF_SIZE);
        
        resizeUL = createDraggableHandle(Pos.TOP_LEFT);
        resizeU = createDraggableHandle(Pos.TOP_CENTER);
        resizeUR = createDraggableHandle(Pos.TOP_RIGHT);
        resizeL = createDraggableHandle(Pos.CENTER_LEFT);
        resizeR = createDraggableHandle(Pos.CENTER_RIGHT);
        resizeBL = createDraggableHandle(Pos.BOTTOM_LEFT);
        resizeB = createDraggableHandle(Pos.BOTTOM_CENTER);
        resizeBR = createDraggableHandle(Pos.BOTTOM_RIGHT);
        resizePane.getChildren().addAll(resizeUL, resizeU, resizeUR, resizeL, resizeR, resizeBL, resizeB, resizeBR);
        
        resizeUL.setCursor(Cursor.NW_RESIZE);
        resizeU.setCursor(Cursor.V_RESIZE);
        resizeUR.setCursor(Cursor.NE_RESIZE);
        resizeL.setCursor(Cursor.H_RESIZE);
        resizeR.setCursor(Cursor.H_RESIZE);
        resizeBL.setCursor(Cursor.SW_RESIZE);
        resizeB.setCursor(Cursor.V_RESIZE);
        resizeBR.setCursor(Cursor.SE_RESIZE);
        
        resizeFrame = new Pane();
        resizePane.getChildren().add(resizeFrame);
        resizeFrame.setMouseTransparent(true);
        resizeFrame.setVisible(false);
        resizeFrame.setMaxWidth(Double.MAX_VALUE);
        resizeFrame.setMaxHeight(Double.MAX_VALUE);
        resizeFrame.setStyle("-fx-border-style: dashed; -fx-border-color: black;");
        
        setDisable(true);
        setVisible(false);
    }
    
    private StackPane createDraggableHandle(Pos direction) {
        StackPane handle = new StackPane();
        StackPane.setAlignment(handle, direction);
        
        handle.setPrefWidth(15.0);
        handle.setPrefHeight(15.0);
        handle.setMinWidth(USE_PREF_SIZE);
        handle.setMinHeight(USE_PREF_SIZE);
        handle.setMaxWidth(USE_PREF_SIZE);
        handle.setMaxHeight(USE_PREF_SIZE);
        
        if (direction.getHpos() == HPos.LEFT) {
            handle.setTranslateX(-15.0);
        } else if (direction.getHpos() == HPos.RIGHT) {
            handle.setTranslateX(15.0);
        }
        
        if (direction.getVpos() == VPos.TOP) {
            handle.setTranslateY(-15.0);
        } else if (direction.getVpos() == VPos.BOTTOM) {
            handle.setTranslateY(15.0);
        }
        
        Rectangle graphic = new Rectangle();
        handle.getChildren().add(graphic);
        StackPane.setAlignment(graphic, GeometryUtils.negatePos(direction));
        graphic.setMouseTransparent(true);
        graphic.setFill(Color.WHITE);
        graphic.setStroke(Color.BLACK);
        graphic.setStrokeType(StrokeType.INSIDE);
        graphic.setWidth(5.0);
        graphic.setHeight(5.0);
        
        handle.setOnMousePressed((e) -> {
            resizeDirection = direction;
            StackPane.setAlignment(resizePane, GeometryUtils.negatePos(direction));
            onResizePanePress(e);
        });
        
        handle.setOnMouseDragged((e) -> onResizePaneDrag(e));
        
        handle.setOnMouseReleased((e) -> onResizePaneRelease(e));
        
        return handle;
    }
    
    /**
     * Create the resize pane and attach it to a parent.
     * @param parent the parent of the resize pane
     * @throws IllegalArgumentException if parent == null
     */
    public ResizePane(Region parent) {
        this(parent, null);
    }
    
    /**
     * Sets the region which will be used to calculate mouse position. Useful when
     * the parent is scaled.
     * @param referenceRegion region which will be used to calculate the change in mouse position
     */
    public void setReferenceRegion(Region referenceRegion) {
        this.referenceRegion = referenceRegion;
    }
    
    /**
     * Get the minimum width the container can be resized into.
     * @return minimum width container can be resized into
     */
    public double getMinResizeWidth() {
        return minWidth;
    }
    
    /**
     * Set the minimum width the container can be resized into.
     * @param minWidth new minimum width
     * @pre minWidth != Double.NaN && minWidth >= 0
     * @throws IllegalArgumentException if precondition is violated
     */
    public void setMinResizeWidth(double minWidth) {
        if (Double.isNaN(minWidth) || minWidth < 0) {
            throw new IllegalArgumentException("minWidth must be non negative");
        }
        
        this.minWidth = minWidth;
    }
    
    /**
     * Get the minimum height the container can be resized into.
     * @return minimum height container can be resized into
     */
    public double getMinResizeHeight() {
        return minHeight;
    }
    
    /**
     * Set the minimum height the container can be resized into.
     * @param minHeight new minimum height
     * @pre minHeight != Double.NaN && minHeight >= 0
     * @throws IllegalArgumentException if precondition is violated
     */
    public void setMinResizeHeight(double minHeight) {
        if (Double.isNaN(minHeight) || minHeight < 0) {
            throw new IllegalArgumentException("minHeight must be non negative");
        }
        
        this.minHeight = minHeight;
    }
    
    /**
     * Get whether the resize pane is enabled and visible.
     * @return true if the resize pane is enabled and visible
     */
    public boolean getEnabled() {
        return enabled;
    }
    
    /**
     * Set whether the resize pane is enabled and visible.
     * @param enabled whether the resize pane should be visible or hidden
     */
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        
        super.setVisible(enabled);
        super.setDisable(!enabled);
        
        if (!enabled) {
            resizeFrame.setVisible(false);
            resizePane.prefWidthProperty().bind(super.widthProperty());
            resizePane.prefHeightProperty().bind(super.heightProperty());
        }
    }
    
    /**
     * Add a listener which is notified when a resize event is emitted.
     * @param listener listener to be added
     * @return true if listener was registered. false if listener is null or
     * listener was already added.
     */
    public boolean addResizeListener(EventListener<? super ResizeEvent> listener) {
        if (listener == null || resizeListeners.contains(listener)) {
            return false;
        }
        
        resizeListeners.add(listener);
        return true;
    }
    
    /**
     * Remove a resize listener which was previously added
     * via {@code addResizeListener(listener)}.
     * @param listener listener to be removed.
     * @return true if the listener was previously registered. false if listener
     *         is null or was not registered before.
     */
    public boolean removeResizeListener(EventListener<? super ResizeEvent> listener) {
        if (listener == null) {
            return false;
        }
        
        return resizeListeners.remove(listener);
    }
    
    // Direction of the resize
    private Pos resizeDirection;
    // Initial canvas width and height
    private double resizeInitialWidth;
    private double resizeInitialHeight;
    // Initial mouse x and y prior to resize, relative to scene
    private double resizeInitialX;
    private double resizeInitialY;
    
    private void onResizePanePress(MouseEvent e) {
        if (!enabled) {
            return;
        }
        
        // Temporarily unbind to modify the pane size
        resizePane.prefWidthProperty().unbind();
        resizePane.prefHeightProperty().unbind();
        
        resizeInitialWidth = parent.getPrefWidth();
        resizeInitialHeight = parent.getPrefHeight();
        
        if (referenceRegion == null) {
            resizeInitialX = e.getSceneX();
            resizeInitialY = e.getSceneY();
        } else {
            Point2D point = referenceRegion.sceneToLocal(e.getSceneX(), e.getSceneY());
            resizeInitialX = point.getX();
            resizeInitialY = point.getY();
        }
        
        resizeFrame.setVisible(true);
        
        e.consume();
    }
    
    private void onResizePaneDrag(MouseEvent e) {
        if (!enabled) {
            return;
        }
        
        boolean horizontalResize = resizeDirection.getHpos() != HPos.CENTER;
        boolean verticalResize = resizeDirection.getVpos() != VPos.CENTER;
        
        double x;
        double y;
        
        if (referenceRegion == null) {
            x = e.getSceneX();
            y = e.getSceneY();
        } else {
            Point2D point = referenceRegion.sceneToLocal(e.getSceneX(), e.getSceneY());
            x = point.getX();
            y = point.getY();
        }
        
        if (horizontalResize) {
            double deltaX = x - resizeInitialX;
            boolean negativeDir = resizeDirection.getHpos() == HPos.LEFT;
            
            if (negativeDir) {
                deltaX *= -1;
            }
            
            double newWidth = Math.max(resizeInitialWidth + deltaX, minWidth);
            resizePane.setPrefWidth(newWidth);
        }
        
        if (verticalResize) {
            double deltaY = y - resizeInitialY;
            boolean negativeDrag = resizeDirection.getVpos() == VPos.TOP;
            
            if (negativeDrag) {
                deltaY *= -1;
            }
            
            double newHeight = Math.max(resizeInitialHeight + deltaY, minHeight);
            resizePane.setPrefHeight(newHeight);
        }
        
        e.consume();
    }
    
    private void onResizePaneRelease(MouseEvent e) {
        if (!enabled) {
            return;
        }
        
        resizeFrame.setVisible(false);
        
        double width = resizePane.getPrefWidth();
        double height = resizePane.getPrefHeight();
        
        ResizeEvent data = new ResizeEvent(width, height, resizeDirection);
        
        for (EventListener<? super ResizeEvent> listener : resizeListeners) {
            listener.handle(data);
        }
        
        // Bind the properties again to fit resize pane to the parent
        resizePane.prefWidthProperty().bind(super.widthProperty());
        resizePane.prefHeightProperty().bind(super.heightProperty());
        
        e.consume();
    }
}
