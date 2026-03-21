package com.mycompany.irr00_group_project.validation.DiagramData;

import com.mycompany.irr00_group_project.gui.data.Anchor;
import com.mycompany.irr00_group_project.representation.ClassNode;
import com.mycompany.irr00_group_project.representation.ClassType;
import com.mycompany.irr00_group_project.representation.ConnectionType;
import com.mycompany.irr00_group_project.representation.DiagramData;
import com.mycompany.irr00_group_project.representation.UMLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * A factory capable of incrementally creating a diagram data.
 * 
 * @author Deniz Büyükgüral
 */
public class DiagramDataFactory {
    
    private final DiagramData diagram = new DiagramData();
    private final List<ClassNode> nodes = new ArrayList();
    
    /**
     * Creates nodes from the given adjacency matrix.
     * @param matrix 2D edge adjacency matrix where {@code matrix[s][t]} is the
     *               connection between nodes s and t (null if there is no connection between nodes)
     * @param types types of the nodes to be added
     * @pre matrix != null
     */
    public void addNodesFromAdjacencyMatric(ConnectionType[][] matrix, ClassType[] types) {
        
        int len = matrix.length;
        
        ClassNode[] nodes = new ClassNode[len];
        
        for (int i = 0; i < len; i++) {
            nodes[i] = new ClassNode();
            nodes[i].setClassName(Integer.toString(i));
            nodes[i].setClassType(types[i]);
            assertTrue(diagram.addClassNode(nodes[i]));
        }
        
        for (int sourceIndex = 0; sourceIndex < len; sourceIndex++) {
            for (int targetIndex = 0; targetIndex < len; targetIndex++) {
                if (matrix[sourceIndex][targetIndex] == null) {
                    continue;
                }
                
                UMLConnection connection 
                    = new UMLConnection(
                            nodes[sourceIndex],
                            nodes[targetIndex],
                            Anchor.TOP,
                            Anchor.TOP);
                
                connection.setType(matrix[sourceIndex][targetIndex]);
                
                assertTrue(diagram.addConnection(connection));
            }
        }
        
        this.nodes.addAll(Arrays.asList(nodes));
    }

    /**
     * Creates nodes from the given adjacency matrix. All nodes will have type NONE.
     * @param matrix 2D edge adjacency matrix where {@code matrix[s][t]} is the
     *               connection between nodes s and t (null if there is no connection between nodes)
     * @pre matrix != null
     */
    public void addNodesFromAdjacencyMatric(ConnectionType[][] matrix) {
        ClassType[] types = new ClassType[matrix.length];
        Arrays.fill(types, ClassType.NONE);
        
        addNodesFromAdjacencyMatric(matrix, types);
    }
    
    /**
     * Returns the resulting diagram data.
     * @return created diagram
     */
    public DiagramData getDiagram() {
        return diagram;
    }
    
    /**
     * Get the index'th created node.
     * @param index index of the node. 0 is the first created node.
     * @return the index'th created node
     * @throws IndexOutOfBoundsException if index is out of bounds
     */
    public ClassNode getNode(int index) {
        return nodes.get(index);
    }
}
