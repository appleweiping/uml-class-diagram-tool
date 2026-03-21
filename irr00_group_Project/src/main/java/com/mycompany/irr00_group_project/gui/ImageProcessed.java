package com.mycompany.irr00_group_project.gui;

/**
 * Interface for nodes which must be processed before exporting the diagram canvas as an image file.
 * 
 * @author Deniz Büyükgüral
 */
interface ImageProcessed {
    
    /**
     * Called before exporting the diagram as an image. Can be used to hide the editor fields.
     */
    void onPreImageExport();
    
    /**
     * Called after exporting the diagram as an image. Can be used to recover the editor fields.
     */
    void onPostImageExport();
}
