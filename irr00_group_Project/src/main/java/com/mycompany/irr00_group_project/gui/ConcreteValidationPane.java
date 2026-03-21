package com.mycompany.irr00_group_project.gui;

import com.mycompany.irr00_group_project.representation.DiagramData;
import com.mycompany.irr00_group_project.utils.FxUtils;
import com.mycompany.irr00_group_project.validation.DiagramValidator;
import com.mycompany.irr00_group_project.validation.Severity;
import com.mycompany.irr00_group_project.validation.ValidationResult;
import java.util.List;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.geometry.Pos;
import javafx.scene.control.Accordion;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TitledPane;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;

/**
 * Pane showing the validation results of the diagram on the screen.
 * 
 * @author Deniz Büyükgüral
 */
class ConcreteValidationPane extends Accordion implements ValidationPane {
    
    private static final String INFO_IMAGE_PATH = "images/info.png";
    private static final String WARNING_IMAGE_PATH = "images/warning.png";
    private static final String ERROR_IMAGE_PATH = "images/error.png";
    
    static final Image INFO_IMAGE;
    static final Image WARNING_IMAGE;
    static final Image ERROR_IMAGE;

    static {
        INFO_IMAGE = FxUtils.getImage(INFO_IMAGE_PATH);
        WARNING_IMAGE = FxUtils.getImage(WARNING_IMAGE_PATH);
        ERROR_IMAGE = FxUtils.getImage(ERROR_IMAGE_PATH);
    }
    
    /**
     * List element showing the validation result and severity
     */
    static class ValidationItem extends HBox {
        
        private final ValidationResult validationResult;
        
        /**
         * Create a new validation item.
         * @param validationResult reference validation result
         * @pre {validationResult != null && !validationResult.isValid()}
         * @throws IllegalArgumentException if preconditions are violated
         */
        public ValidationItem(ValidationResult validationResult) {
            
            if (validationResult == null) {
                throw new IllegalArgumentException("validationResult cannot be null");
            }
            
            if (validationResult.isValid()) {
                throw new IllegalArgumentException("validationResult must be invalid");
            }
            
            this.validationResult = validationResult;
            
            setAlignment(Pos.CENTER_LEFT);
            setSpacing(5.0);
            
            Pane image = new Pane();
            getChildren().add(image);
            HBox.setHgrow(image, Priority.NEVER);
            image.setPrefWidth(18.0);
            image.setPrefWidth(18.0);
            
            switch (validationResult.getSeverity()) {
                case INFORMATION -> image.setBackground(FxUtils.createImageBackground(INFO_IMAGE));
                case WARNING -> image.setBackground(FxUtils.createImageBackground(WARNING_IMAGE));
                case ERROR -> image.setBackground(FxUtils.createImageBackground(ERROR_IMAGE));
            }
            
            Label label = new Label(validationResult.getMessage());
            getChildren().add(label);
            HBox.setHgrow(label, Priority.ALWAYS);
        }
    
        /**
         * Get the validation result of the validation item.
         * @return the validation result of the validation item
         */
        public ValidationResult getValidationResult() {
            return validationResult;
        }
    }
    
    private final TitledPane rootPane;
    private final ListView<ValidationItem> validationList;
    
    private final IntegerProperty infoCount = new SimpleIntegerProperty(0);
    private final IntegerProperty warningCount = new SimpleIntegerProperty(0);
    private final IntegerProperty errorCount = new SimpleIntegerProperty(0);
    
    private final DiagramValidator validator = new DiagramValidator();
    
    public ConcreteValidationPane(MainScene mainScene) {
        
        if (mainScene == null) {
            throw new NullPointerException("mainScene cannot be null");
        }
        
        // Run after object initialization
        Platform.runLater(() -> {
            mainScene.getCanvas().getModel().registerObserver(this);
        });
        
        // Init UI
        
        rootPane = new TitledPane();
        getPanes().add(rootPane);
        rootPane.setText("Validation Results");
        
        ScrollPane scrollPane = new ScrollPane();
        rootPane.setContent(scrollPane);
        
        validationList = new ListView<>();
        scrollPane.setContent(validationList);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        
        // Bind properties
        
        infoCount.bind(Bindings.createIntegerBinding(() -> {
            int cnt = 0;
            for (ValidationItem item : validationList.getItems()) {
                if (item.getValidationResult().getSeverity() == Severity.INFORMATION) {
                    cnt += 1;
                }
            }
            
            return cnt;
        }, validationList.getItems()));
        
        warningCount.bind(Bindings.createIntegerBinding(() -> {
            int cnt = 0;
            for (ValidationItem item : validationList.getItems()) {
                if (item.getValidationResult().getSeverity() == Severity.WARNING) {
                    cnt += 1;
                }
            }
            
            return cnt;
        }, validationList.getItems()));
        
        errorCount.bind(Bindings.createIntegerBinding(() -> {
            int cnt = 0;
            for (ValidationItem item : validationList.getItems()) {
                if (item.getValidationResult().getSeverity() == Severity.ERROR) {
                    cnt += 1;
                }
            }
            
            return cnt;
        }, validationList.getItems()));
    }

    @Override
    public void update(DiagramData subject) {
        validationList.getItems().clear();
        
        List<ValidationResult> results = validator.validate(subject);
        
        for (ValidationResult validationResult : results) {
            System.out.println(validationResult);
            validationList.getItems().add(new ValidationItem(validationResult));
        }
    }
    
    @Override
    public int getInfoCount() {
        return infoCount.get();
    }
    
    @Override
    public ReadOnlyIntegerProperty infoCountProperty() {
        return infoCount;
    }

    @Override
    public int getWarningCount() {
        return warningCount.get();
    }
    
    @Override
    public ReadOnlyIntegerProperty warningCountProperty() {
        return warningCount;
    }

    @Override
    public int getErrorCount() {
        return errorCount.get();
    }
    
    @Override
    public ReadOnlyIntegerProperty errorCountProperty() {
        return errorCount;
    }
}
