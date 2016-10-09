package net.alfredandersson.anyanglepathfinding;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Disposable;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import net.alfredandersson.anyanglepathfinding.engine.Map;

public final class MapRenderer implements Disposable {
  
  public final Map map;
  
  private Texture mapTex;
  
  public MapRenderer(Map map) {
    this.map = map;
    
    mapTex = createTexture();
  }
  
  @Override
  public void dispose() {
    mapTex.dispose();
    mapTex = null;
  }
  
  public void draw(Draw d) {
    d.enableBlending();
    d.sprites.setProjectionMatrix(d.cam.combined);
    d.sprites.begin();
    d.sprites.draw(mapTex, 0, 0);
    d.sprites.end();
  }
  
  public Texture getMapTexture() {
    return mapTex;
  }
  
  private Texture createTexture() {
    // write the image as BMP
    ByteBuffer buf = ByteBuffer.allocate(4 * map.width * map.height + 14 + 40);
    buf.order(ByteOrder.LITTLE_ENDIAN);
    
    // file header (14 bytes)
    buf.put((byte)'B');
    buf.put((byte)'M');
    buf.putInt(4 * map.width * map.height + 14 + 40);
    buf.putInt(0);
    buf.putInt(14 + 40);
    
    // BITMAPINFO header (40 bytes)
    buf.putInt(40);
    buf.putInt(map.width);
    buf.putInt(map.height);
    buf.putShort((short)1);
    buf.putShort((short)32); // bits per pixel
    buf.putInt(0);
    buf.putInt(4 * map.width * map.height);
    buf.putInt(3600);
    buf.putInt(3600);
    buf.putInt(0);
    buf.putInt(0);
    
    for (int y = 0; y < map.height; y++) {
      for (int x = 0; x < map.width; x++) {
        if (map.isBlocked(x, y)) {
          buf.putInt(0xff000000); // black
        } else {
          buf.putInt(0xffc0c0c0); // light gray
        }
      }
    }
    
    buf.flip();
    
    Pixmap pixmap = new Pixmap(buf.array(), 0, buf.limit());
    Texture tex = new Texture(pixmap, Pixmap.Format.RGBA8888, false);
    pixmap.dispose();
    
    tex.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
    tex.setWrap(Texture.TextureWrap.ClampToEdge, Texture.TextureWrap.ClampToEdge);
    
    return tex;
  }
}
