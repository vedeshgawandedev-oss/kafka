import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;

public class Handler {
    public void execute(Socket clientSocket) throws IOException {
        DataInputStream in = new DataInputStream(clientSocket.getInputStream());
        OutputStream out = clientSocket.getOutputStream();
        while(true){
            try{
                if(in.available() > 0){
                    int message_size = in.readInt();
                    short request_api_key = in.readShort();
                    short request_api_version = in.readShort();
                    int corelation_id = in.readInt();
                    byte[] remaining_bytes = new byte[message_size - 8];
                    in.readFully(remaining_bytes);

                    System.out.println("Received request: api_key=" + request_api_key + ", api_version=" + request_api_version + ", correlation_id=" + corelation_id);
                    if(request_api_version<0 || request_api_version>4){
                        // System.out.println("Unsupported api_version: " + request_api_version);
                        writeInt(out, 0);
                        writeInt(out, corelation_id);
                        writeShort(out, (short)35);
                        out.flush();
                    } else if(request_api_key == 18){
                        out.write(ByteBuffer.allocate(4).putInt(19).array());
                        writeInt(out, corelation_id); // correlation id
                        writeShort(out, (short) 0); // error code
                        writeByte(out, (byte) 3); // array length
                        writeShort(out, (short)18); // api key
                        writeShort(out, (short)0); // min api version
                        writeShort(out, (short)16); // max api version
                        writeByte(out, (byte)0); // tag buffer
                        writeInt(out, 0); // throttle time
                        writeByte(out, (byte)0); // tag buffer
                        out.flush();
                    }else if(request_api_key == 1){
                        out.write(ByteBuffer.allocate(4).putInt(10).array());
                        writeInt(out, corelation_id);
                        writeShort(out, (short)0); // error code
                        writeByte(out, (byte) 2); // array length
                        writeShort(out, request_api_key); // api key
                        writeShort(out, (short)0); // min api version
                        writeShort(out, (short)4); // max api version
                        writeByte(out, (byte)0); // tag buffer
                        writeInt(out, 0); // throttle time
                        writeByte(out, (byte)0); // tag buffer
                        out.flush();
                    }else{
                        // System.out.println("Unknown api_key: " + request_api_key);
                        writeInt(out, 0);
                        writeInt(out, corelation_id);
                        writeShort(out, (short)3);
                        out.flush();
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public static void writeInt(OutputStream out, int value) throws IOException {
        // Network byte order = big-endian
        out.write((value >>> 24) & 0xFF);
        out.write((value >>> 16) & 0xFF);
        out.write((value >>> 8) & 0xFF);
        out.write(value & 0xFF);
    }
    public static void writeShort(OutputStream out, short value) throws IOException{
        out.write((value >>> 8) & 0xFF);
        out.write(value & 0xFF);
    }

    public static void writeByte(OutputStream out, byte value) throws IOException{
        out.write(value & 0xFF);
    }
}
