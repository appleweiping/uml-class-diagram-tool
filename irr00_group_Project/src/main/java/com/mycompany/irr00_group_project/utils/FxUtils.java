package com.mycompany.irr00_group_project.utils;

import com.mycompany.irr00_group_project.App;
import java.net.URL;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;

/**
 * JavaFX related utility methods.
 * 
 * @author Deniz Büyükgüral
 */
public class FxUtils {

    /**
     * Get image from the given URL.
     * @param imageUrl path to the image relative to App package
     * @return image at the given path. null if no resource is found at the given path.
     * @throws NullPointerException if {@code imageUrl == null}
     */
    public static Image getImage(String imageUrl) {
        if (imageUrl == null) {
            throw new NullPointerException();
        }
        
        URL imageUrlObj = App.class.getResource(imageUrl);
        return (imageUrlObj == null) ? null : new Image(imageUrlObj.toString());
    }
    
    /**
     * Create background using the given image.
     * @param imageUrl path to the image relative to App package
     * @return instantiated background if the image is loaded, empty background otherwise
     * @throws NullPointerException if {@code imageUrl == null}
     */
    public static Background createImageBackground(String imageUrl) {
        if (imageUrl == null) {
            throw new NullPointerException();
        }
        
        URL imageUrlObj = App.class.getResource(imageUrl);
        Image bgImage = (imageUrlObj == null) ? null : new Image(imageUrlObj.toString());
        return bgImage == null ? Background.EMPTY : new Background(new BackgroundImage(
            bgImage, BackgroundRepeat.NO_REPEAT, 
            BackgroundRepeat.NO_REPEAT, 
            BackgroundPosition.CENTER, new BackgroundSize(50.0, 50.0, false, false, true, false)));
    }
    
    /**
     * Create background using the given image.
     * @param bgImage background image to use for the background
     * @return instantiated background
     * @throws NullPointerException if {@code bgImage == null}
     */
    public static Background createImageBackground(Image bgImage) {
        if (bgImage == null) {
            throw new NullPointerException();
        }
        
        return new Background(new BackgroundImage(
            bgImage, BackgroundRepeat.NO_REPEAT, 
            BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, 
            new BackgroundSize(50.0, 50.0, false, false, true, false)));
    }
}
