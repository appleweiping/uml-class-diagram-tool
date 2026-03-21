package com.mycompany.irr00_group_project.serialization.image;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javax.imageio.ImageIO;

/**
 * Implementation of image serializer which can serialize an image object into a jpg file.
 * 
 * @author Deniz Büyükgüral
 * @author Aiham Al-Ashwal
 * @author Deniz Büyükgüral
 */
public class JpgImageSerializer extends ImageSerializer {

    @Override
    public void serializeImage(Image image, OutputStream stream) throws IOException {
        
        if (image == null || stream == null) {
            throw new NullPointerException();
        }
        
        int width = (int) image.getWidth();
        int height = (int) image.getHeight();

        // Create a new BufferedImage that does NOT have transparency (TYPE_INT_RGB).
        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        PixelReader pixelReader = image.getPixelReader();

        // Loop through every pixel of the source image.
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                // Get the color and opacity of the original pixel.
                javafx.scene.paint.Color fxColor = pixelReader.getColor(x, y);
                double alpha = fxColor.getOpacity();

                // Manually blend the original color with a white background.
                //  If the pixel is transparent, it will become more white.
                //  If it's opaque, it will stay its original color.
                double red   = (fxColor.getRed()   * alpha) + (1.0 * (1.0 - alpha));
                double green = (fxColor.getGreen() * alpha) + (1.0 * (1.0 - alpha));
                double blue  = (fxColor.getBlue()  * alpha) + (1.0 * (1.0 - alpha));

                // Create a standard Java AWT Color and set the pixel in our new image.
                java.awt.Color finalColor = new java.awt.Color(
                        (float) red,
                        (float) green,
                        (float) blue
                );
                
                bufferedImage.setRGB(x, y, finalColor.getRGB());
            }
        }
        
        // Write the new, opaque image to the file as a JPEG.
        if (!ImageIO.write(bufferedImage, "jpeg", stream)) {
            throw new IOException("ImageIO failed to find a writer for the JPEG format.");
        }
    }
}
