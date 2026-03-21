package com.mycompany.irr00_group_project.gui;

/**
 * Static class for creating concrete GUI objects for testing purposes.
 * 
 * @author Deniz BÜyükgüral
 */
public class ConcreteGuiFactory {
    
    // Static class, no instance
    private ConcreteGuiFactory() {
    }
    
    /**
     * Creates a mock main scene object.
     * @return a mock main scene object
     */
    public static MainScene createMainScene() {
        return new MainScene(null);
    }
    
    /**
     * Creates a mock diagram canvas object.
     * @param mainScene the scene to create the canvas in
     * @return a mock diagram canvas object
     */
    public static DiagramCanvasPane createDiagramCanvas(MainScene mainScene) {
        return new ConcreteDiagramCanvasPane(mainScene);
    }
}
