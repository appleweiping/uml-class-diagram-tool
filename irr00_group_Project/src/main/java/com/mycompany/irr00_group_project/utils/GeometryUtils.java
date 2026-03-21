package com.mycompany.irr00_group_project.utils;

import java.util.EnumMap;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;

/**
 * Extension methods for JavaFX geometry objects.
 * 
 * @author Deniz Büyükgüral
 * @author Long Pham
 */
public class GeometryUtils {
    private GeometryUtils() {
    }
    
    /**
     * Returns the horizontal position opposite to the given one.
     * 
     * @param pos the original horizontal position
     * @return the opposite HPos, or CENTER if pos is CENTER
     * @throws NullPointerException if pos is null
     */
    public static HPos negateHPos(HPos pos) {
        if (pos == null) {
            throw new NullPointerException();
        }
        
        return switch (pos) {
            case HPos.CENTER -> HPos.CENTER;
            case HPos.LEFT -> HPos.RIGHT;
            case HPos.RIGHT -> HPos.LEFT;
                
            default -> throw new IllegalStateException("impossible state");
        };
    }
    
    /**
     * Returns the vertical position opposite to the given one.
     * 
     * @param pos the original vertical position
     * @return the opposite VPos, or CENTER/BASELINE if unchanged
     * @throws NullPointerException if pos is null
     */
    public static VPos negateVPos(VPos pos) {
        if (pos == null) {
            throw new NullPointerException();
        }
        
        return switch (pos) {
            case VPos.CENTER -> VPos.CENTER;
            case VPos.BASELINE -> VPos.BASELINE;
            case VPos.TOP -> VPos.BOTTOM;
            case VPos.BOTTOM -> VPos.TOP;
            default -> throw new IllegalStateException("impossible state");
        };
    }
    
    private static final EnumMap<Pos, Pos> POS_NEGATION_MAP
            = new EnumMap<>(Pos.class);
    
    static {
        POS_NEGATION_MAP.put(Pos.BASELINE_CENTER, Pos.BASELINE_CENTER);
        POS_NEGATION_MAP.put(Pos.BASELINE_LEFT, Pos.BASELINE_RIGHT);
        POS_NEGATION_MAP.put(Pos.BASELINE_RIGHT, Pos.BASELINE_LEFT);
        POS_NEGATION_MAP.put(Pos.BOTTOM_CENTER, Pos.TOP_CENTER);
        POS_NEGATION_MAP.put(Pos.BOTTOM_LEFT, Pos.TOP_RIGHT);
        POS_NEGATION_MAP.put(Pos.BOTTOM_RIGHT, Pos.TOP_LEFT);
        POS_NEGATION_MAP.put(Pos.CENTER, Pos.CENTER);
        POS_NEGATION_MAP.put(Pos.CENTER_LEFT, Pos.CENTER_RIGHT);
        POS_NEGATION_MAP.put(Pos.CENTER_RIGHT, Pos.CENTER_LEFT);
        POS_NEGATION_MAP.put(Pos.TOP_CENTER, Pos.BOTTOM_CENTER);
        POS_NEGATION_MAP.put(Pos.TOP_LEFT, Pos.BOTTOM_RIGHT);
        POS_NEGATION_MAP.put(Pos.TOP_RIGHT, Pos.BOTTOM_LEFT);
    }
    
    /**
     * Returns the opposite alignment for the given Pos.
     * 
     * @param pos the original alignment
     * @return the opposite Pos, or CENTER if unchanged
     * @throws NullPointerException if pos is null
     */
    public static Pos negatePos(Pos pos) {
        if (pos == null) {
            throw new NullPointerException();
        }
        
        return POS_NEGATION_MAP.get(pos);
    }
}
