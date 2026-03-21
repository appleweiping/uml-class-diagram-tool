package com.mycompany.irr00_group_project.serialization.image;

import com.mycompany.irr00_group_project.utils.FxUtils;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import javafx.application.Platform;
import javafx.scene.image.Image;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeAll;

/**
 * Tests for PngImageSerializer.java.
 * 
 * @author Deniz Büyükgüral
 */
public class PngImageSerializerTest {
    
    /**
     * Starts JavaFX to use graphics calls.
     */
    @BeforeAll
    static void startToolkit() {
        try {
            Platform.startup(() -> {});
        } catch (IllegalStateException e) {
            // Startup called multiple times, ignore
        }
    }

    /**
     * Test to check if the image is serialized/deserialized correctly.
     */
    @Test
    public void testGoodWeather() {
        
        Image image = FxUtils.getImage("images/testImage.png");
        
        ImageSerializer serializer = new PngImageSerializer();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        
        try {
            serializer.serializeImage(image, outputStream);
        } catch (IOException e) {
            e.printStackTrace();
            throw new AssertionError();
        }
        
        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        Image converted = new Image(inputStream);
        
        assertNotNull(converted);
    }
    
    /**
     * Check whether the serializer checks for the preconditions.
     */
    @Test
    public void checkRobustness() {
        
        Image image = FxUtils.getImage("images/info.png");
        
        ImageSerializer serializer = new PngImageSerializer();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        
        assertThrows(NullPointerException.class,
                () -> serializer.serializeImage(image, null));
        
        assertThrows(NullPointerException.class,
                () -> serializer.serializeImage(null, outputStream));
    }
    
}
