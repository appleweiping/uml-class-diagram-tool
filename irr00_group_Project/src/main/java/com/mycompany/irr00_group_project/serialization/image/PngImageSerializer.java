package com.mycompany.irr00_group_project.serialization.image;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javax.imageio.ImageIO;

/**
 * Implementation of image serializer which can serialize an image object into a png file.
 * 
 * @author Deniz Büyükgüral
 * @author Aiham Al-Ashwal
 * @author Long Pham
 */
public class PngImageSerializer extends ImageSerializer {

    @Override
    public void serializeImage(Image image, OutputStream stream) throws IOException {
        
        if (image == null || stream == null) {
            throw new NullPointerException();
        }
        
        BufferedImage bufferedImage = new BufferedImage(
                (int) image.getWidth(),
                (int) image.getHeight(),
                BufferedImage.TYPE_INT_ARGB
        );
        
        PixelReader pixelReader = image.getPixelReader();
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                bufferedImage.setRGB(x, y, pixelReader.getArgb(x, y));
            }
        }
        
        if (!ImageIO.write(bufferedImage, "png", stream)) {
            throw new IOException("ImageIO failed to find a writer for the PNG format.");
        }
    }
}
