package com.mycompany.irr00_group_project.representation;

import com.mycompany.irr00_group_project.gui.data.Anchor;
import java.util.Objects;

/**
 * Represents a UML connection (e.g., Inheritance, Association, Aggregation, or Composition)
 * between two {@link ClassNode} instances.
 * Each connection may have a role name, multiplicity, and anchor on both source and target sides.
 * This class is used in the diagram representation to model relationships between classes.
 *
 * @author Long Pham
 * @author Aiham Al-Ashwal
 * @author Abdul Gadaborchev
 */
public class UMLConnection {

    private DiagramData parent;
    
    private final ClassNode sourceClass;
    private final ClassNode targetClass;
    private final Anchor sourceAnchor;
    private final Anchor targetAnchor;
    
    private ConnectionType type = ConnectionType.ASSOCIATION;
    private String sourceMultiplicity = "";
    private String targetMultiplicity = "";
    private String sourceRoleName = "";
    private String targetRoleName = "";
    private double sourceOffsetX;
    private double sourceOffsetY;
    private double targetOffsetX;
    private double targetOffsetY;
    private double midlineX;
    private double midlineY;

    /**
     * Constructs a UMLConnection between two class nodes.
     * 
     * @param sourceClass the originating class node
     * @param targetClass the target class node
     * @param sourceAnchor to which side of the source class is the connection anchored
     * @param targetAnchor to which side of the target class is the connection anchored
     * @throws NullPointerException     if either class node is null
     * @throws IllegalArgumentException if both class nodes are the same
     */
    public UMLConnection(
            ClassNode sourceClass,
            ClassNode targetClass,
            Anchor sourceAnchor,
            Anchor targetAnchor) {
        
        if (sourceClass == null || targetClass == null
                || sourceAnchor == null || targetAnchor == null) {
            
            throw new NullPointerException();
        }
        if (sourceClass == targetClass) {
            throw new IllegalArgumentException();
        }
        
        this.sourceClass = sourceClass;
        this.targetClass = targetClass;
        this.sourceAnchor = sourceAnchor;
        this.targetAnchor = targetAnchor;
    }
    
    /**
     * Gets the parent diagram the connection is attached to.
     * @return the parent diagram the connection is attached to. null if connection is not on
     *         any diagram.
     */
    DiagramData getParent() {
        return parent;
    }
    
    /**
     * Set the parent of the connection.
     * @param parent new parent diagram of the connection
     */
    void setParent(DiagramData parent) {
        this.parent = parent;
    }

    /**
     * Gets the source class of the connection.
     * 
     * @return the source {@link ClassNode}
     */
    public ClassNode getSourceClass() {
        return sourceClass;
    }

    /**
     * Gets the target class of the connection.
     * 
     * @return the target {@link ClassNode}
     */
    public ClassNode getTargetClass() {
        return targetClass;
    }

    /**
     * Gets the connection type.
     * 
     * @return the {@link ConnectionType}
     */
    public ConnectionType getType() {
        return type;
    }

    /**
     * Sets the connection type.
     * 
     * @param type the {@link ConnectionType}
     * @throws NullPointerException if type is null
     */
    public void setType(ConnectionType type) {
        if (type == null) {
            throw new NullPointerException();
        }
        
        this.type = type;
        
        if (parent != null) {
            parent.notifyObservers();
        }
    }

    /**
     * Returns the multiplicity at the source end of the connection.
     *
     * @return the source multiplicity string.
     */
    public String getSourceMultiplicity() {
        return sourceMultiplicity;
    }

    /**
     * Sets the multiplicity at the source end of the connection.
     *
     * @param sourceMultiplicity the multiplicity to set at the source.
     * @throws NullPointerException if sourceMultiplicity is null
     */
    public void setSourceMultiplicity(String sourceMultiplicity) {
        if (sourceMultiplicity == null) {
            throw new NullPointerException();
        }
        
        this.sourceMultiplicity = sourceMultiplicity;
        
        if (parent != null) {
            parent.notifyObservers();
        }
    }

    /**
     * Returns the multiplicity at the target end of the connection.
     *
     * @return the target multiplicity string.
     */
    public String getTargetMultiplicity() {
        return targetMultiplicity;
    }

    /**
     * Sets the multiplicity at the target end of the connection.
     *
     * @param targetMultiplicity the multiplicity to set at the target.
     * @throws NullPointerException if targetMultiplicity is null
     */
    public void setTargetMultiplicity(String targetMultiplicity) {
        if (targetMultiplicity == null) {
            throw new NullPointerException();
        }
        
        this.targetMultiplicity = targetMultiplicity;
        
        if (parent != null) {
            parent.notifyObservers();
        }
    }

    /**
     * Returns the role name of the source class in the connection.
     *
     * @return the source role name.
     */
    public String getSourceRoleName() {
        return sourceRoleName;
    }

    /**
     * Sets the role name of the source class in the connection.
     *
     * @param sourceRoleName the source role name to set.
     * @throws NullPointerException if sourceRoleName is null
     */
    public void setSourceRoleName(String sourceRoleName) {
        if (sourceRoleName == null) {
            throw new NullPointerException();
        }
        
        this.sourceRoleName = sourceRoleName;
        
        if (parent != null) {
            parent.notifyObservers();
        }
    }

    /**
     * Returns the role name of the target class in the connection.
     *
     * @return the target role name.
     */
    public String getTargetRoleName() {
        return targetRoleName;
    }

    /**
     * Sets the role name of the target class in the connection.
     *
     * @param targetRoleName the target role name to set.
     * @throws NullPointerException if targetRoleName is null
     */
    public void setTargetRoleName(String targetRoleName) {
        if (targetRoleName == null) {
            throw new NullPointerException();
        }
        
        this.targetRoleName = targetRoleName;
        
        if (parent != null) {
            parent.notifyObservers();
        }
    }

    /**
     * Returns the anchor point used for the source class in the connection.
     *
     * @return the source anchor.
     */
    public Anchor getSourceAnchor() {
        return sourceAnchor;
    }
    
    /**
     * Returns the anchor point used for the target class in the connection.
     *
     * @return the target anchor.
     */
    public Anchor getTargetAnchor() {
        return targetAnchor;
    }
    
    /**
     * Returns the X offset of the source point.
     *
     * @return the source X offset.
     */
    public double getSourceOffsetX() { 
        return sourceOffsetX; 
    }

    /**
     * Sets the X offset of the source point.
     *
     * @param sourceOffsetX the new X offset.
     * @throws IllegalArgumentException if sourceOffsetX is NaN or negative
     */
    public void setSourceOffsetX(double sourceOffsetX) {
        if (Double.isNaN(sourceOffsetX) || sourceOffsetX < 0) {
            throw new IllegalArgumentException();
        }
        
        this.sourceOffsetX = sourceOffsetX; 
    }

    /**
     * Returns the Y offset of the source point.
     *
     * @return the source Y offset.
     */
    public double getSourceOffsetY() { 
        return sourceOffsetY; 
    }

    /**
     * Sets the Y offset of the source point.
     *
     * @param sourceOffsetY the new Y offset.
     * @throws IllegalArgumentException if sourceOffsetY is NaN or negative
     */
    public void setSourceOffsetY(double sourceOffsetY) { 
        if (Double.isNaN(sourceOffsetY) || sourceOffsetY < 0) {
            throw new IllegalArgumentException();
        }
        
        this.sourceOffsetY = sourceOffsetY; 
    }

    /**
     * Returns the X offset of the target point.
     *
     * @return the target X offset.
     */
    public double getTargetOffsetX() { 
        return targetOffsetX; 
    }

    /**
     * Sets the X offset of the target point.
     *
     * @param targetOffsetX the new X offset.
     * @throws IllegalArgumentException if targetOffsetX is NaN or negative
     */
    public void setTargetOffsetX(double targetOffsetX) { 
        if (Double.isNaN(targetOffsetX) || targetOffsetX < 0) {
            throw new IllegalArgumentException();
        }
        
        this.targetOffsetX = targetOffsetX; 
    }

    /**
     * Returns the Y offset of the target point.
     *
     * @return the target Y offset.
     */
    public double getTargetOffsetY() { 
        return targetOffsetY; 
    }

    /**
     * Sets the Y offset of the target point.
     *
     * @param targetOffsetY the new Y offset.
     * @throws IllegalArgumentException if targetOffsetY is NaN or negative
     */
    public void setTargetOffsetY(double targetOffsetY) {
        if (Double.isNaN(targetOffsetY) || targetOffsetY < 0) {
            throw new IllegalArgumentException();
        }
        
        this.targetOffsetY = targetOffsetY; 
    }

    /**
     * Returns the X coordinate of the midline for the connection.
     *
     * @return the midline X coordinate.
     */
    public double getMidlineX() { 
        return midlineX; 
    }

    /**
     * Sets the X coordinate of the midline for the connection.
     *
     * @param midlineX the new X coordinate of the midline.
     * @throws IllegalArgumentException if midlineX is NaN or negative
     */
    public void setMidlineX(double midlineX) {
        if (Double.isNaN(midlineX) || midlineX < 0) {
            throw new IllegalArgumentException();
        }
        
        this.midlineX = midlineX; 
    }

    /**
     * Returns the Y coordinate of the midline for the connection.
     *
     * @return the midline Y coordinate.
     */
    public double getMidlineY() { 
        return midlineY; 
    }

    /**
     * Sets the Y coordinate of the midline for the connection.
     *
     * @param midlineY the new Y coordinate of the midline.
     * @throws IllegalArgumentException if midlineY is NaN or negative
     */
    public void setMidlineY(double midlineY) {
        if (Double.isNaN(midlineY) || midlineY < 0) {
            throw new IllegalArgumentException();
        }
        
        this.midlineY = midlineY; 
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        UMLConnection other = (UMLConnection) obj;
        return sourceClass.equals(other.sourceClass)
            && targetClass.equals(other.targetClass)
            && type == other.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(sourceClass, targetClass, type);
    }
}
