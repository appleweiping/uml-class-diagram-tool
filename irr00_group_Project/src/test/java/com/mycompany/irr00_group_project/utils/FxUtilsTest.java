package com.mycompany.irr00_group_project.utils;

import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for FxUtils utility class.
 * Tests image loading and background creation methods.
 *
 * @author Weiping Yan
 * @author Long Pham
 */
public class FxUtilsTest {

    @Test
    void testGetImage_ValidPath_ReturnsImage() {
        Image image = FxUtils.getImage("images/info.png");
        assertNotNull(image);
        assertTrue(image.getWidth() > 0);
    }

    @Test
    void testGetImage_InvalidPath_ReturnsNull() {
        Image image = FxUtils.getImage("/non/existing/image.png");
        assertNull(image);
    }

    @Test
    void testCreateImageBackground_FromValidPath() {
        Background bg = FxUtils.createImageBackground(
                "images/warning.png");
        assertNotNull(bg);
    }

    @Test
    void testCreateImageBackground_FromInvalidPath() {
        Background bg = FxUtils.createImageBackground("/non/existing/image.png");
        assertNotNull(bg); // should still return a Background even with null Image
    }

    @Test
    void testCreateImageBackground_FromImageObject() {
        Image image = FxUtils.getImage("images/EnumType.png");
        Background bg = FxUtils.createImageBackground(image);
        assertNotNull(bg);
    }

    @Test
    void testCreateImageBackground_FromNullImage() {
        assertThrows(NullPointerException.class, () -> FxUtils.createImageBackground((Image) null));
    }
    
    /**
     * Test case: null images get passed to getImage.
     * 
     * Expectation: method should throw NullPointerException as null
     * input is not allowed.
     */
    @Test
    void testGetImage_NullPath_ThrowException() {
        assertThrows(NullPointerException.class, () -> FxUtils.getImage(null));
    }
    
    /**
     * Test case: null image path passed to createImageBackground(String).
     * 
     * Expectation: method should throw NullPointerException as null
     * input is not allowed.
     */
    @Test
    void testCreateImageBackground_NullPath_ThrowException() {
        assertThrows(NullPointerException.class, () -> FxUtils.createImageBackground(
                (String) null));
    }
}
