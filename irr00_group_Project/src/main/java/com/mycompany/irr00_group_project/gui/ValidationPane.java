package com.mycompany.irr00_group_project.gui;

import com.mycompany.irr00_group_project.listeners.Observer;
import com.mycompany.irr00_group_project.representation.DiagramData;
import javafx.beans.property.ReadOnlyIntegerProperty;

/**
 * Interface which declares exposed methods of the validation pane.
 * 
 * @author Deniz Büyükgüral
 */
interface ValidationPane extends Observer<DiagramData> {
    
    /**
     * Get number of validation results with information severity.
     * @return number of validation results with information severity
     */
    int getInfoCount();
    
    /**
     * Property bound to the number of validation results with information severity.
     * @return Property bound to the number of validation results with information severity
     */
    ReadOnlyIntegerProperty infoCountProperty();
    
    /**
     * Get number of validation results with warning severity.
     * @return number of validation results with warning severity
     */
    int getWarningCount();
    
    /**
     * Property bound to the number of validation results with warning severity.
     * @return Property bound to the number of validation results with warning severity
     */
    ReadOnlyIntegerProperty warningCountProperty();
    
    /**
     * Get number of validation results with error severity.
     * @return number of validation results with error severity
     */
    int getErrorCount();
    
    /**
     * Property bound to the number of validation results with error severity.
     * @return Property bound to the number of validation results with error severity
     */
    ReadOnlyIntegerProperty errorCountProperty();
}
