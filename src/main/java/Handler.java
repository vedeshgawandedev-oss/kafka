import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;

public class Handler {
    public void execute(DataInputStream in, OutputStream out) {

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
