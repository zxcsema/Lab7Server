package org.example.Send;
import org.example.Response.Message;
import org.w3c.dom.ls.LSOutput;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class Sender {
    public   <T> void sendSerializedObject(SocketChannel client, T message) throws IOException {
        try {
            byte[] data = Serializer.serialize(message);

            ByteBuffer buffer = ByteBuffer.allocate(4 + data.length);
            buffer.putInt(data.length);
            buffer.put(data);
            buffer.flip();
            client.write(buffer);

        } catch (IOException e) {
            System.err.println("Ошибка при отправке данных клиенту: " + e.getMessage());
        }

    }

}
