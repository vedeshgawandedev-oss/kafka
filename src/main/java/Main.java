import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;

public class Main {
  public static void main(String[] args){
    // You can use print statements as follows for debugging, they'll be visible when running tests.
    System.err.println("Logs from your program will appear here!");

    // Uncomment this block to pass the first stage

     ServerSocket serverSocket = null;
     Socket clientSocket = null;
     int port = 9092;
     try {
       serverSocket = new ServerSocket(port);
       // Since the tester restarts your program quite often, setting SO_REUSEADDR
       // ensures that we don't run into 'Address already in use' errors
       serverSocket.setReuseAddress(true);
       // Wait for connection from client.
       clientSocket = serverSocket.accept();
       DataInputStream in = new DataInputStream(clientSocket.getInputStream());
       OutputStream out = clientSocket.getOutputStream();

       while(true){
           int message_size = 0, corelation_id = 7;


           int reqSize = in.readInt();
           short request_api_key = in.readShort();
           short request_api_version = in.readShort();
           int request_correlation_id = in.readInt();
           int request_client_id_length = in.readShort();
           byte[] request_client_id = new byte[request_client_id_length];
           in.readFully(request_client_id);
           //            message_size = reqSize + 4;
           corelation_id = request_correlation_id;
           // read the rest of the request
           //            int toRead = reqSize - 2 - 2 - 4 - 2 - request_client_id_length;

//           System.out.println("Received request: api_key=" + request_api_key + ", api_version=" + request_api_version + ", correlation_id=" + request_correlation_id + ", client_id=" + new String(request_client_id));
           if(request_api_version<0 || request_api_version>4){
               System.out.println("Unsupported api_version: " + request_api_version);
               writeInt(out, 0);
               writeInt(out, corelation_id);
               writeShort(out, (short)35);
               out.flush();
           } else {
               out.write(ByteBuffer.allocate(4).putInt(19).array());
               writeInt(out, corelation_id);
               writeShort(out, (short) 0); // error code
               writeByte(out, (byte) 2); // array length
               writeShort(out, (short)18); // api key
               writeShort(out, (short)0); // min api version
               writeShort(out, (short)4); // max api version
               writeByte(out, (byte)0); // tag buffer
               writeInt(out, 0); // throttle time
               writeByte(out, (byte)0); // tag buffer

               out.flush();
           }
       }
     } catch (IOException e) {
       System.out.println("IOException: " + e.getMessage());
     } finally {
       try {
         if (clientSocket != null) {
           clientSocket.close();
         }
       } catch (IOException e) {
         System.out.println("IOException: " + e.getMessage());
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
