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

     int port = 9092;
     try {
       serverSocket = new ServerSocket(port);
       // Since the tester restarts your program quite often, setting SO_REUSEADDR
       // ensures that we don't run into 'Address already in use' errors
       serverSocket.setReuseAddress(true);
       while(true){
           Socket clientSocket = serverSocket.accept();
           Thread.ofVirtual().start(() -> {
               try{
                 new Handler().execute(clientSocket);
               }catch (Exception e) {
                 e.printStackTrace();
               }
           });
       }
     }catch (Exception e) {
         e.printStackTrace();
     }finally {
        if(serverSocket != null){
            try {
                serverSocket.close();
            }catch (IOException e){
                e.printStackTrace();
            }
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
