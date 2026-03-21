package com.mycompany.irr00_group_project.serialization.image;

import java.io.IOException;
import java.io.OutputStream;
import javafx.scene.image.Image;

/**
 * Abstract definition of an image serializer. Can take in an image object
 * and send the contents to an output stream.
 * 
 * @author Deniz Büyükgüral
 */
public abstract class ImageSerializer {
    
    /**
     * Serializes the given image and sends the data to the given stream.
     * @param image the image to serialize
     * @param stream the stream to send the serialized data into
     * @pre {image != null && stream != null}
     * @throws NullPointerException if preconditions are violated
     * @throws IOException if an exception in the stream occurs
     */
    public abstract void serializeImage(Image image, OutputStream stream)
            throws IOException;
}
