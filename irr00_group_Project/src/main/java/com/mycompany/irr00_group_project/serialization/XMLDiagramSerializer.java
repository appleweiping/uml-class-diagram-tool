package com.mycompany.irr00_group_project.serialization;

import com.mycompany.irr00_group_project.representation.DiagramData;
import java.beans.XMLEncoder;
import java.beans.XMLDecoder;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Handles the serialization and de-serialization of DiagramData objects.
 * Contains no UI logic.
 *
 * @author Aiham Al-Ashwal
 * @author Long Pham
 */
public class XMLDiagramSerializer extends DiagramSerializer {
    
    @Override
    public void serializeDiagram(DiagramData diagram, OutputStream outputStream)
            throws IOException {
        
        if (diagram == null || outputStream == null) {
            throw new NullPointerException();
        }
        
        DiagramDataFile flatObj = new DiagramDataFile(diagram);
        
        try (XMLEncoder encoder = new XMLEncoder(new BufferedOutputStream(outputStream))) {
            encoder.writeObject(flatObj);
        }
    }

    @Override
    public DiagramData deserializeDiagram(InputStream inputStream) throws IOException {
        
        if (inputStream == null) {
            throw new NullPointerException();
        }
        
        DiagramDataFile flatObj;
        
        try (XMLDecoder decoder = new XMLDecoder(new BufferedInputStream(inputStream))) {
            flatObj = (DiagramDataFile) decoder.readObject();
        }
        
        return flatObj.toDiagram();
    }
}
