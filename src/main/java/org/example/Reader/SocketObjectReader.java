package org.example.Reader;

import org.example.ClientAction.ClientShutdown;

import java.io.IOException;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.sql.SQLException;

public class SocketObjectReader {
    private final ClientShutdown clientShutdown;

    public SocketObjectReader(ClientShutdown clientShutdown) {
        this.clientShutdown = clientShutdown;
    }

    public byte[] readSerializedObject(SocketChannel client) throws IOException, SQLException, ClassNotFoundException {
        ByteBuffer sizeBuffer = ByteBuffer.allocate(4);
        sizeBuffer.clear();
        int bytesRead = -1;
        try{
            bytesRead = client.read(sizeBuffer);

        }catch (SocketException e){
            e.printStackTrace();
        }
        if (bytesRead == -1 && !client.isOpen()) {
            clientShutdown.clientShutdown(client);
            return null;
        }
        if (bytesRead < 4) return null;

        sizeBuffer.flip();
        int objectSize = sizeBuffer.getInt();

        ByteBuffer objectBuffer = ByteBuffer.allocate(objectSize);
        objectBuffer.clear();
        bytesRead = client.read(objectBuffer);

        if (bytesRead < objectSize) return null;

        objectBuffer.flip();
        byte[] objectData = new byte[objectSize];
        objectBuffer.get(objectData);
        return objectData;
    }
}