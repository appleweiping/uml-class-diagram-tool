package com.mycompany.irr00_group_project.gui;

import com.mycompany.irr00_group_project.representation.*;
import com.mycompany.irr00_group_project.serialization.DiagramSerializer;
import com.mycompany.irr00_group_project.serialization.XMLDiagramSerializer;
import com.mycompany.irr00_group_project.serialization.image.ImageSerializer;
import com.mycompany.irr00_group_project.serialization.image.JpgImageSerializer;
import com.mycompany.irr00_group_project.serialization.image.PngImageSerializer;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Optional;

/**
 * The Controller in the MVC pattern. Handles file operations and
 * coordinates between the View (MainScene) and the Model (DiagramData).
 *
 * @author Aiham Al-Ashwal
 */
public class AppController {

    private final Stage primaryStage;
    private final MainScene mainScene;
    
    private final DiagramSerializer serializer = new XMLDiagramSerializer();

    public AppController(Stage primaryStage, MainScene mainScene) {
        this.primaryStage = primaryStage;
        this.mainScene = mainScene;
    }

    public void handleSaveAs() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Diagram As...");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("UML Node Chart (*.unc)", "*.unc"));
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("All Files (*.*)", "*.*"));
        File file = fileChooser.showSaveDialog(primaryStage);

        if (file != null) {
            if (!file.getName().toLowerCase().endsWith(".unc")) {
                file = new File(file.getAbsolutePath() + ".unc");
            }
            try {
                serializer.serializeDiagram(
                        mainScene.getCanvas().getModel(),
                        new FileOutputStream(file));
                
                mainScene.setHasUnsavedChanges(false);
                primaryStage.setTitle(file.getName() + " - UML Class Diagram Editor");
            } catch (IOException e) {
                showError("Save Error", "Could not save the diagram.", e);
            }
        }
    }

    public void handleOpen() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Diagram File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("UML Node Chart (*.unc)", "*.unc"));
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("All Files (*.*)", "*.*"));
        File file = fileChooser.showOpenDialog(primaryStage);

        if (file != null) {
            try {
                DiagramData liveModel = serializer.deserializeDiagram(new FileInputStream(file));
                
                mainScene.displayDiagram(liveModel);
                primaryStage.setTitle(file.getName() + " - UML Class Diagram Editor");
            } catch (IOException e) {
                showError("Error Opening File", "Could not load the diagram.", e);
            }
        }
    }


    private void showError(String title, String header, Exception e) {
        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
        errorAlert.setTitle(title);
        errorAlert.setHeaderText(header);
        errorAlert.setContentText("Error: " + e.getMessage());
        errorAlert.showAndWait();
        e.printStackTrace();
    }
    
    public void handleExportAsImage() {
        // Get the image from the canvas view, resolution multiplier of 2.0 for higher quality
        Image image = mainScene.getCanvas().toImage(2.0);

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Export Diagram as Image...");
        FileChooser.ExtensionFilter pngFilter = new FileChooser.ExtensionFilter("PNG Image (*.png)", "*.png");
        FileChooser.ExtensionFilter jpgFilter = new FileChooser.ExtensionFilter("JPEG Image (*.jpg, *.jpeg)", "*.jpg", "*.jpeg");
        fileChooser.getExtensionFilters().addAll(pngFilter, jpgFilter);
        File file = fileChooser.showSaveDialog(primaryStage);

        // If the user chose a file, tell the FileManager to save it.
        if (file != null) {
            String format;
            FileChooser.ExtensionFilter selectedFilter = fileChooser.getSelectedExtensionFilter();
            if (selectedFilter == jpgFilter) {
                format = "jpeg";
                if (!file.getName().toLowerCase().endsWith(".jpg") && !file.getName().toLowerCase().endsWith(".jpeg")) {
                    file = new File(file.getAbsolutePath() + ".jpeg");
                }
            } else {
                format = "png";
                if (!file.getName().toLowerCase().endsWith(".png")) {
                    file = new File(file.getAbsolutePath() + ".png");
                }
            }

            try {
                
                // Select the appropriate serializer based on file format
                ImageSerializer imageSerializer;
                if (format.equals("jpeg")) {
                    imageSerializer = new JpgImageSerializer();
                } else {
                    imageSerializer = new PngImageSerializer();
                }
                
                imageSerializer.serializeImage(image, new FileOutputStream(file));

                // Show success message
                Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                successAlert.setTitle("Export Successful");
                successAlert.setHeaderText(null);
                successAlert.setContentText("Diagram saved as an image to:\n" + file.getAbsolutePath());
                successAlert.showAndWait();

            } catch (IOException e) {
                showError("Export Error", "Could not export the diagram as an image.", e);
            }

        }
    }
    
    public void handleClose() {
        if (mainScene.hasUnsavedChanges()) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Unsaved Changes");
            alert.setHeaderText("You have unsaved changes.");
            alert.setContentText("Do you want to save your work before closing?");

            ButtonType saveButton = new ButtonType("Save");
            ButtonType dontSaveButton = new ButtonType("Don't Save");
            ButtonType cancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

            alert.getButtonTypes().setAll(saveButton, dontSaveButton, cancelButton);

            Optional<ButtonType> result = alert.showAndWait();

            if (result.isPresent()) {
                if (result.get() == saveButton) {
                    handleSaveAs();
                    // If save was not canceled, the flag will be false.
                    if (!mainScene.hasUnsavedChanges()) {
                        primaryStage.close();
                    }
                } else if (result.get() == dontSaveButton) {
                    primaryStage.close();
                }
            }
            // If "Cancel" is clicked, the dialog closes and we do nothing.
        } else {
            // No unsaved changes, so close immediately.
            primaryStage.close();
        }
    }
}