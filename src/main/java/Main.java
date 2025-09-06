import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

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
       int message_size = 0, corelation_id = 7;

         DataInputStream in = new DataInputStream(clientSocket.getInputStream());
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




       OutputStream out = clientSocket.getOutputStream();
       writeInt(out, message_size);
         writeInt(out, corelation_id);
         out.flush();
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
}
