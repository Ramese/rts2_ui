package VO;

import java.io.DataInputStream;
import java.io.IOException;

/**
 *
 * @author Radek
 */
public class TelescopeImageVO {
    
    public TelescopeImageVO() {
        
    }
    
    public int[] imageData;
    
    public short data_type;
    public short naxes;
    public int width;
    public int height;
    public int a3, a4, a5;
    public short bv, bh, b3, b4, b5, shutter, filt, x, y, chan;
    
    public static TelescopeImageVO Fill(DataInputStream dis) throws IOException {
        TelescopeImageVO result = new TelescopeImageVO();
        
        result.data_type = dis.readShort();
        result.naxes = dis.readShort();
        result.width = dis.readInt();
        result.height = dis.readInt();
        result.a3 = dis.readInt();
        result.a4 = dis.readInt();
        result.a5 = dis.readInt();
        result.bv = dis.readShort();
        result.bh = dis.readShort();
        result.b3 = dis.readShort();
        result.b4 = dis.readShort();
        result.b5 = dis.readShort();
        result.shutter = dis.readShort();
        result.filt = dis.readShort();
        result.x = dis.readShort();
        result.y = dis.readShort();
        result.chan = dis.readShort();
        
        int sizeOfImage = result.width * result.height;
        
        result.imageData = new int[sizeOfImage];
        
        for (int i = 0; i < sizeOfImage; i++) {
            result.imageData[i] = dis.readUnsignedShort();
        }
        
        return result;
    }
}
