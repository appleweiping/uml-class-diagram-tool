package com.mycompany.irr00_group_project.utils;

import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the GeometryUtils class.
 * 
 * Tests negation methods for JavaFX positional enums.
 * 
 * @author Weiping Yan
 * @author Long Pham
 */
public class GeometryUtilsTest {

    @Test
    void testNegateHPos() {
        assertEquals(HPos.CENTER, GeometryUtils.negateHPos(HPos.CENTER));
        assertEquals(HPos.LEFT, GeometryUtils.negateHPos(HPos.RIGHT));
        assertEquals(HPos.RIGHT, GeometryUtils.negateHPos(HPos.LEFT));
        assertThrows(NullPointerException.class, () -> GeometryUtils.negateHPos(null));
    }

    @Test
    void testNegateVPos() {
        assertEquals(VPos.CENTER, GeometryUtils.negateVPos(VPos.CENTER));
        assertEquals(VPos.BASELINE, GeometryUtils.negateVPos(VPos.BASELINE));
        assertEquals(VPos.TOP, GeometryUtils.negateVPos(VPos.BOTTOM));
        assertEquals(VPos.BOTTOM, GeometryUtils.negateVPos(VPos.TOP));
        assertThrows(NullPointerException.class, () -> GeometryUtils.negateVPos(null));
    }

    @Test
    void testNegatePos() {
        assertEquals(Pos.CENTER, GeometryUtils.negatePos(Pos.CENTER));
        assertEquals(Pos.CENTER_LEFT, GeometryUtils.negatePos(Pos.CENTER_RIGHT));
        assertEquals(Pos.CENTER_RIGHT, GeometryUtils.negatePos(Pos.CENTER_LEFT));
        assertEquals(Pos.TOP_LEFT, GeometryUtils.negatePos(Pos.BOTTOM_RIGHT));
        assertEquals(Pos.TOP_CENTER, GeometryUtils.negatePos(Pos.BOTTOM_CENTER));
        assertEquals(Pos.TOP_RIGHT, GeometryUtils.negatePos(Pos.BOTTOM_LEFT));
        assertEquals(Pos.BOTTOM_LEFT, GeometryUtils.negatePos(Pos.TOP_RIGHT));
        assertEquals(Pos.BOTTOM_CENTER, GeometryUtils.negatePos(Pos.TOP_CENTER));
        assertEquals(Pos.BOTTOM_RIGHT, GeometryUtils.negatePos(Pos.TOP_LEFT));
        assertEquals(Pos.BASELINE_LEFT, GeometryUtils.negatePos(Pos.BASELINE_RIGHT));
        assertEquals(Pos.BASELINE_RIGHT, GeometryUtils.negatePos(Pos.BASELINE_LEFT));
        assertEquals(Pos.BASELINE_CENTER, GeometryUtils.negatePos(Pos.BASELINE_CENTER));
        assertThrows(NullPointerException.class, () -> GeometryUtils.negatePos(null));
    }
    
    /**
     * Verifies that negateHPos is symmetric: negating a HPos twice
     * returns the original value.
     */
    @Test
    void testNegateHPosIsSymmetric() {
        for (HPos pos : HPos.values()) {
            assertEquals(pos, GeometryUtils.negateHPos(GeometryUtils.negateHPos(pos)));
        }
    }
    
    /**
     * Verifies that negateVPos is symmetric: negating a VPos twice
     * returns the original value.
     */
    @Test
    void testNegateVPosIsSymmetric() {
        for (VPos pos : VPos.values()) {
            assertEquals(pos, GeometryUtils.negateVPos(GeometryUtils.negateVPos(pos)));
        }
    }
    
    /**
     * Verifies that negatePos is symmetric: 
     * negating an alignment twice returns the original value.
     */
    @Test
    void testNegatePosIsSymmetric() {
        for (Pos pos : Pos.values()) {
            assertEquals(pos, GeometryUtils.negatePos(GeometryUtils.negatePos(pos)));
        }
    }
}
