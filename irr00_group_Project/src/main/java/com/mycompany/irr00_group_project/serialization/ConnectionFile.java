package com.mycompany.irr00_group_project.serialization;

import com.mycompany.irr00_group_project.gui.data.Anchor;
import com.mycompany.irr00_group_project.representation.ConnectionType;
import com.mycompany.irr00_group_project.representation.UMLConnection;
import java.io.Serializable;

/**
 * Serializable representation of a UML connection between two class nodes,
 * storing IDs and metadata for export/import (e.g., to file).
 * <p>
 * Includes connection type, multiplicities, role names, and anchors.
 * </p>
 *
 * @author Aiham Al-Ashwal
 * @author Abdul
 */
public class ConnectionFile implements Serializable {

    private int sourceNodeId;
    private int targetNodeId;
    private ConnectionType type;

    private String sourceMultiplicity;
    private String targetMultiplicity;
    private String sourceRoleName;
    private String targetRoleName;

    private Anchor sourceAnchor;
    private Anchor targetAnchor;
    private double sourceOffsetX;
    private double sourceOffsetY;
    private double targetOffsetX;
    private double targetOffsetY;
    private double midlineX;
    private double midlineY;

    /**
     * No-argument constructor for serialization.
     */
    public ConnectionFile() {
        type = ConnectionType.ASSOCIATION;
        sourceMultiplicity = "";
        targetMultiplicity = "";
        sourceRoleName = "";
        targetRoleName = "";
    }
    
    /**
     * Create a new connection file from connection.
     * @param conn the connection to clone
     * @throws NullPointerException if conn is null
     */
    public ConnectionFile(UMLConnection conn) {
        
        if (conn == null) {
            throw new NullPointerException();
        }
        
        setType(conn.getType());
        setSourceAnchor(conn.getSourceAnchor());
        setTargetAnchor(conn.getTargetAnchor());

        setSourceOffsetX(conn.getSourceOffsetX());
        setSourceOffsetY(conn.getSourceOffsetY());
        setTargetOffsetX(conn.getTargetOffsetX());
        setTargetOffsetY(conn.getTargetOffsetY());
        setMidlineX(conn.getMidlineX());
        setMidlineY(conn.getMidlineY());
        setSourceRoleName(conn.getSourceRoleName());
        setTargetRoleName(conn.getTargetRoleName());
        setSourceMultiplicity(conn.getSourceMultiplicity());
        setTargetMultiplicity(conn.getTargetMultiplicity());
    }

    /**
     * Gets the ID of the source node.
     * @return source node ID
     */
    public int getSourceNodeId() {
        return sourceNodeId;
    }

    /**
     * Sets the ID of the source node.
     * @param sourceNodeId the source node ID
     */
    public void setSourceNodeId(int sourceNodeId) {
        this.sourceNodeId = sourceNodeId;
    }

    /**
     * Gets the ID of the target node.
     * @return target node ID
     */
    public int getTargetNodeId() {
        return targetNodeId;
    }

    /**
     * Sets the ID of the target node.
     * @param targetNodeId the target node ID
     */
    public void setTargetNodeId(int targetNodeId) {
        this.targetNodeId = targetNodeId;
    }

    /**
     * Gets the type of UML connection.
     * @return connection type
     */
    public ConnectionType getType() {
        return type;
    }

    /**
     * Sets the type of UML connection.
     * @param type connection type
     */
    public void setType(ConnectionType type) {
        this.type = type;
    }

    /**
     * Gets the source anchor.
     * @return source anchor
     */
    public Anchor getSourceAnchor() {
        return sourceAnchor;
    }

    /**
     * Sets the source anchor.
     * @param sourceAnchor anchor from the source node
     */
    public void setSourceAnchor(Anchor sourceAnchor) {
        this.sourceAnchor = sourceAnchor;
    }

    /**
     * Gets the target anchor.
     * @return target anchor
     */
    public Anchor getTargetAnchor() {
        return targetAnchor;
    }

    /**
     * Sets the target anchor.
     * @param targetAnchor anchor to the target node
     */
    public void setTargetAnchor(Anchor targetAnchor) {
        this.targetAnchor = targetAnchor;
    }

    /**
     * Gets the source multiplicity string.
     * @return source multiplicity
     */
    public String getSourceMultiplicity() {
        return sourceMultiplicity;
    }

    /**
     * Sets the source multiplicity.
     * @param sourceMultiplicity the multiplicity on the source side
     */
    public void setSourceMultiplicity(String sourceMultiplicity) {
        this.sourceMultiplicity = sourceMultiplicity;
    }

    /**
     * Gets the target multiplicity string.
     * @return target multiplicity
     */
    public String getTargetMultiplicity() {
        return targetMultiplicity;
    }

    /**
     * Sets the target multiplicity.
     * @param targetMultiplicity the multiplicity on the target side
     */
    public void setTargetMultiplicity(String targetMultiplicity) {
        this.targetMultiplicity = targetMultiplicity;
    }

    /**
     * Gets the source role name.
     * @return source role name
     */
    public String getSourceRoleName() {
        return sourceRoleName;
    }

    /**
     * Sets the source role name.
     * @param sourceRoleName name of the role on the source side
     */
    public void setSourceRoleName(String sourceRoleName) {
        this.sourceRoleName = sourceRoleName;
    }

    /**
     * Gets the target role name.
     * @return target role name
     */
    public String getTargetRoleName() {
        return targetRoleName;
    }

    /**
     * Sets the target role name.
     * @param targetRoleName name of the role on the target side
     */
    public void setTargetRoleName(String targetRoleName) {
        this.targetRoleName = targetRoleName;
    }

    /**
     * Gets the horizontal offset of the source point.
     *
     * @return the X offset of the source.
     */
    public double getSourceOffsetX() {
        return sourceOffsetX;
    }

    /**
     * Sets the horizontal offset of the source point.
     *
     * @param sourceOffsetX the new X offset for the source.
     */
    public void setSourceOffsetX(double sourceOffsetX) {
        this.sourceOffsetX = sourceOffsetX;
    }

    /**
     * Gets the vertical offset of the source point.
     *
     * @return the Y offset of the source.
     */
    public double getSourceOffsetY() {
        return sourceOffsetY;
    }

    /**
     * Sets the vertical offset of the source point.
     *
     * @param sourceOffsetY the new Y offset for the source.
     */
    public void setSourceOffsetY(double sourceOffsetY) {
        this.sourceOffsetY = sourceOffsetY;
    }

    /**
     * Gets the horizontal offset of the target point.
     *
     * @return the X offset of the target.
     */
    public double getTargetOffsetX() {
        return targetOffsetX;
    }

    /**
     * Sets the horizontal offset of the target point.
     *
     * @param targetOffsetX the new X offset for the target.
     */
    public void setTargetOffsetX(double targetOffsetX) {
        this.targetOffsetX = targetOffsetX;
    }

    /**
     * Gets the vertical offset of the target point.
     *
     * @return the Y offset of the target.
     */
    public double getTargetOffsetY() {
        return targetOffsetY;
    }

    /**
     * Sets the vertical offset of the target point.
     *
     * @param targetOffsetY the new Y offset for the target.
     */
    public void setTargetOffsetY(double targetOffsetY) {
        this.targetOffsetY = targetOffsetY;
    }

    /**
     * Gets the X coordinate of the midline between source and target.
     *
     * @return the X position of the midline.
     */
    public double getMidlineX() {
        return midlineX;
    }

    /**
     * Sets the X coordinate of the midline between source and target.
     *
     * @param midlineX the new X position of the midline.
     */
    public void setMidlineX(double midlineX) {
        this.midlineX = midlineX;
    }

    /**
     * Gets the Y coordinate of the midline between source and target.
     *
     * @return the Y position of the midline.
     */
    public double getMidlineY() {
        return midlineY;
    }

    /**
     * Sets the Y coordinate of the midline between source and target.
     *
     * @param midlineY the new Y position of the midline.
     */
    public void setMidlineY(double midlineY) {
        this.midlineY = midlineY;
    }

}