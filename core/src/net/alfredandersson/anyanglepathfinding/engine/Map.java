package net.alfredandersson.anyanglepathfinding.engine;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import de.matthiasmann.twl.utils.PNGDecoder;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

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
  
  public Texture createTexture() {
    // write the image as BMP
    ByteBuffer buf = ByteBuffer.allocate(4 * width * height + 14 + 40);
    buf.order(ByteOrder.LITTLE_ENDIAN);
    
    // file header (14 bytes)
    buf.put((byte)'B');
    buf.put((byte)'M');
    buf.putInt(4 * width * height + 14 + 40);
    buf.putInt(0);
    buf.putInt(14 + 40);
    
    // BITMAPINFO header (40 bytes)
    buf.putInt(40);
    buf.putInt(width);
    buf.putInt(height);
    buf.putShort((short)1);
    buf.putShort((short)32); // bits per pixel
    buf.putInt(0);
    buf.putInt(4 * width * height);
    buf.putInt(3600);
    buf.putInt(3600);
    buf.putInt(0);
    buf.putInt(0);
    
    for (int i = 0; i < cellBlocked.length; i++) {
      if (cellBlocked[i]) {
        buf.putInt(0xff000000); // black
      } else {
        buf.putInt(0xffc0c0c0); // light gray
      }
    }
    
    Pixmap pixmap = new Pixmap(buf.array(), 0, buf.capacity());
    Texture tex = new Texture(pixmap, Format.RGBA8888, false);
    pixmap.dispose();
    
    tex.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
    tex.setWrap(Texture.TextureWrap.ClampToEdge, Texture.TextureWrap.ClampToEdge);
    
    return tex;
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
