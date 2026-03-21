package com.mycompany.irr00_group_project.serialization;

import com.mycompany.irr00_group_project.representation.ClassNode;
import com.mycompany.irr00_group_project.representation.DiagramData;
import com.mycompany.irr00_group_project.representation.UMLConnection;
import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * A serializable representation of a UML diagram, intended for file storage.
 * It contains simplified versions of class nodes and connections suitable for serialization.
 * 
 * <p>This class is typically used for saving and loading diagrams without the full runtime behavior
 * of the actual diagram components.</p>
 * 
 * @author Aiham Al-Ashwal
 */
public class DiagramDataFile implements Serializable {

    private static final long serialVersionUID = 1L;

    private List<ClassNodeFile> nodes;
    private List<ConnectionFile> connections;
    private double canvasWidth;
    private double canvasHeight;

    /**
     * Constructs an empty {@code DiagramDataFile} instance.
     */
    public DiagramDataFile() {
        this.nodes = new ArrayList<>();
        this.connections = new ArrayList<>();
    }
    
    /**
     * Construct diagram data file from a diagram instance.
     * @param diagram the diagram instance
     * @throws NullPointerException if diagram == null
     */
    public DiagramDataFile(DiagramData diagram) {
        if (diagram == null) {
            throw new NullPointerException();
        }
        
        this.nodes = new ArrayList<>();
        this.connections = new ArrayList<>();
        
        Map<ClassNode, Integer> nodeToIdMap = new HashMap<>();
        this.canvasWidth = diagram.getCanvasWidth();
        this.canvasHeight = diagram.getCanvasHeight();
        int currentId = 0;

        for (ClassNode liveNode : diagram.getClassNodes()) {
            nodeToIdMap.put(liveNode, currentId);

            ClassNodeFile nodeFile = new ClassNodeFile(liveNode);
            nodeFile.setId(currentId);

            nodes.add(nodeFile);
            currentId++;
        }

        for (UMLConnection liveConnection : diagram.getConnections()) {
            ConnectionFile connFile = new ConnectionFile(liveConnection);
            connFile.setSourceNodeId(nodeToIdMap.get(liveConnection.getSourceClass()));
            connFile.setTargetNodeId(nodeToIdMap.get(liveConnection.getTargetClass()));

            connections.add(connFile);
        }
    }

    /**
     * Instantiate a diagram from the diagram file.
     * @return the diagram object instantiated from the data
     */
    public DiagramData toDiagram() {
        DiagramData liveModel = new DiagramData();
        Map<Integer, ClassNode> idToNodeMap = new HashMap<>();
        liveModel.setCanvasWidth(canvasWidth);
        liveModel.setCanvasHeight(canvasHeight);

        for (ClassNodeFile nodeFile : nodes) {
            ClassNode liveNode = new ClassNode();
            liveNode.setClassName(nodeFile.getClassName());
            liveNode.setAttributes(nodeFile.getAttributes());
            liveNode.setOperations(nodeFile.getOperations());
            liveNode.setLayoutX(nodeFile.getLayoutX());
            liveNode.setLayoutY(nodeFile.getLayoutY());
            liveNode.setWidth(nodeFile.getWidth());
            liveNode.setHeight(nodeFile.getHeight());
            liveNode.setClassType(nodeFile.getClassType());
            
            if (!liveModel.addClassNode(liveNode)) {
                throw new RuntimeException("Could not add class to the diagram");
            }
            idToNodeMap.put(nodeFile.getId(), liveNode);
        }

        for (ConnectionFile connFile : connections) {

            ClassNode sourceNode = idToNodeMap.get(connFile.getSourceNodeId());
            ClassNode targetNode = idToNodeMap.get(connFile.getTargetNodeId());
            UMLConnection liveConnection = new UMLConnection(
                    sourceNode,
                    targetNode,
                    connFile.getSourceAnchor(),
                    connFile.getTargetAnchor());
            liveConnection.setType(connFile.getType());

            liveConnection.setSourceOffsetX(connFile.getSourceOffsetX());
            liveConnection.setSourceOffsetY(connFile.getSourceOffsetY());
            liveConnection.setTargetOffsetX(connFile.getTargetOffsetX());
            liveConnection.setTargetOffsetY(connFile.getTargetOffsetY());
            liveConnection.setMidlineX(connFile.getMidlineX());
            liveConnection.setMidlineY(connFile.getMidlineY());
            liveConnection.setSourceRoleName(connFile.getSourceRoleName());
            liveConnection.setTargetRoleName(connFile.getTargetRoleName());
            liveConnection.setSourceMultiplicity(connFile.getSourceMultiplicity());
            liveConnection.setTargetMultiplicity(connFile.getTargetMultiplicity());

            if (!liveModel.addConnection(liveConnection)) {
                throw new RuntimeException("Could not add connection");
            }
        }
        
        return liveModel;
    }
    
    /**
     * Gets the list of class node files.
     * 
     * @return list of {@link ClassNodeFile} instances
     */
    public List<ClassNodeFile> getNodes() { 
        return nodes; 
    }

    /**
     * Sets the list of class node files.
     * 
     * @param nodes the new list of class node files
     */
    public void setNodes(List<ClassNodeFile> nodes) { 
        this.nodes.clear();
        this.nodes.addAll(nodes);
    }

    /**
     * Gets the list of connection files.
     * 
     * @return list of {@link ConnectionFile} instances
     */
    public List<ConnectionFile> getConnections() { 
        return connections; 
    }

    /**
     * Sets the list of connection files.
     * 
     * @param connections the new list of connection files
     */
    public void setConnections(List<ConnectionFile> connections) { 
        this.connections.clear();
        this.connections.addAll(connections);
    }
    /**
     * Gets the width of the canvas.
     *
     * @return the canvas width
     */

    public double getCanvasWidth() {
        return canvasWidth;
    }

    /**
     * Sets the width of the canvas.
     *
     * @param canvasWidth the width to set
     */
    public void setCanvasWidth(double canvasWidth) {
        this.canvasWidth = canvasWidth;
    }

    /**
     * Gets the height of the canvas.
     *
     * @return the canvas height
     */
    public double getCanvasHeight() {
        return canvasHeight;
    }

    /**
     * Sets the height of the canvas.
     *
     * @param canvasHeight the height to set
     */
    public void setCanvasHeight(double canvasHeight) {
        this.canvasHeight = canvasHeight;
    }
}
