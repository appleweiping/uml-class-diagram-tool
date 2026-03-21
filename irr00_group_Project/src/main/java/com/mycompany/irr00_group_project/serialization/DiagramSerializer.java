package com.mycompany.irr00_group_project.serialization;

import com.mycompany.irr00_group_project.representation.DiagramData;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Abstract definition of a diagram serializer.
 * 
 * @author Deniz Büyükgüral
 */
public abstract class DiagramSerializer {
    
    /**
     * Serialize the diagram and send the contents to the given stream.
     * @param diagram the diagram to be serialized
     * @param outputStream the stream to which the serialized diagram data will be sent
     * @pre {diagram != null && outputStream != null}
     * @throws NullPointerException if preconditions are violated
     * @throws IOException in case of stream exceptions
     */
    public abstract void serializeDiagram(DiagramData diagram, OutputStream outputStream)
            throws IOException;
    
    /**
     * Deserialize the diagram from the given stream and return the diagram instance.
     * @param inputStream the stream from which the diagram will be read
     * @return the deserialized diagram instance
     * @pre {inputStream != null}
     * @throws NullPointerException if preconditions are violated
     * @throws IOException in case of stream exceptions
     */
    public abstract DiagramData deserializeDiagram(InputStream inputStream)
            throws IOException;
}
