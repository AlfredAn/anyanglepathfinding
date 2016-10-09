package net.alfredandersson.anyanglepathfinding.engine;

import de.matthiasmann.twl.utils.PNGDecoder;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

public final class Map {
  
  private final boolean[] cellBlocked;
  
  public final int width, height;
  
  private Map(boolean[] cellBlocked, int width) {
    this.cellBlocked = cellBlocked;
    this.width = width;
    height = cellBlocked.length / width;
    
    if (cellBlocked.length % width != 0) {
      throw new AssertionError();
    }
  }
  
  public boolean isBlocked(int x, int y) {
    return cellBlocked[x + y * width];
  }
  
  public static Map fromPNG(InputStream in) throws IOException {
    PNGDecoder dec = new PNGDecoder(in);

    ByteBuffer buf = ByteBuffer.allocate(4 * dec.getWidth() * dec.getHeight());
    dec.decode(buf, 4 * dec.getWidth(), PNGDecoder.Format.RGBA);
    buf.flip();
    
    byte[] data = buf.array();
    boolean[] cells = new boolean[dec.getWidth() * dec.getHeight()];
    
    for (int i = 0; i < data.length; i += 4) {
      int brightness = (data[i] & 0xff) + (data[i + 1] & 0xff) + (data[i + 2] & 0xff);
      cells[i / 4] = brightness <= (255 * 3) / 2;
    }
    
    return new Map(cells, dec.getWidth());
  }
}
