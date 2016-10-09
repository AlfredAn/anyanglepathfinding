package net.alfredandersson.anyanglepathfinding.engine;

import de.matthiasmann.twl.utils.PNGDecoder;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

public final class Map {
  
  private final boolean[][] cellBlocked;
  
  private Map(boolean[][] cellBlocked) {
    this.cellBlocked = cellBlocked;
  }
  
  public int getWidth() {
    return cellBlocked.length;
  }
  
  public int getHeight() {
    return cellBlocked[0].length;
  }
  
  public boolean isBlocked(int x, int y) {
    if (!isValidCell(x, y)) {
      return true;
    } else {
      return cellBlocked[x][y];
    }
  }
  
  public boolean isValidCell(int x, int y) {
    return x >= 0 && x < getWidth() && y >= 0 && y < getHeight();
  }
  
  public static Map fromPNG(InputStream in) throws IOException {
    PNGDecoder dec = new PNGDecoder(in);

    ByteBuffer buf = ByteBuffer.allocate(4 * dec.getWidth() * dec.getHeight());
    dec.decode(buf, 4 * dec.getWidth(), PNGDecoder.Format.RGBA);
    buf.flip();
    
    boolean[][] cells = new boolean[dec.getWidth()][dec.getHeight()];
    
    for (int y = 0; y < dec.getHeight(); y++) {
      for (int x = 0; x < dec.getWidth(); x++) {
        int brightness = (buf.get() & 0xff) + (buf.get() & 0xff) + (buf.get() & 0xff);
        buf.get(); // skip the alpha byte
        cells[x][y] = brightness <= (255 * 3) / 2;
      }
    }
    
    return new Map(cells);
  }
}
