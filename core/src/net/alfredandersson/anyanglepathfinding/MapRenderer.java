package net.alfredandersson.anyanglepathfinding;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.Disposable;
import gnu.trove.list.TFloatList;
import gnu.trove.list.array.TFloatArrayList;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import net.alfredandersson.anyanglepathfinding.engine.GridConnections;
import net.alfredandersson.anyanglepathfinding.engine.Map;
import net.alfredandersson.anyanglepathfinding.util.GraphicsUtil;

public final class MapRenderer implements Disposable {
  
  private static final String edgeVertexShader =
            "attribute vec4 a_position;\n"
          + "uniform mat4 u_projectionViewMatrix;\n"
          + "void main() {\n"
          + "  gl_Position = u_projectionViewMatrix * a_position;\n"
          + "}";
  private static final String edgeFragmentShader =
            "void main() {\n"
          + "  gl_FragColor = vec4(1, 0, 0, 0.125);\n"
          + "}";
  
  private ShaderProgram edgeShader;
  
  public final Map map;
  private final GridConnections con;
  
  private Texture mapTex;
  private Mesh edgeMesh;
  
  public MapRenderer(Map map, GridConnections con) {
    this.map = map;
    this.con = con;
    
    mapTex = createTexture();
    
    if (con != null) {
      edgeMesh = createEdgeMesh();
      edgeShader = GraphicsUtil.loadShader(edgeVertexShader, edgeFragmentShader);
    }
  }
  
  @Override
  public void dispose() {
    if (mapTex != null) {
      mapTex.dispose();
      mapTex = null;
    }
    
    if (edgeMesh != null) {
      edgeMesh.dispose();
      edgeMesh = null;
    }
    
    if (edgeShader != null) {
      edgeShader.dispose();
      edgeShader = null;
    }
  }
  
  public void draw(Draw d) {
    d.enableBlending();
    d.sprites.setProjectionMatrix(d.cam.combined);
    d.sprites.begin();
    d.sprites.draw(mapTex, 0, 0);
    d.sprites.end();
    
    if (edgeMesh != null) {
      edgeShader.begin();
      d.enableBlending();
      edgeShader.setUniformMatrix("u_projectionViewMatrix", d.cam.combined);
      edgeMesh.render(edgeShader, GL20.GL_LINES);
      edgeShader.end();
    }
  }
  
  public Texture getMapTexture() {
    return mapTex;
  }
  
  private Texture createTexture() {
    // write the image as BMP
    ByteBuffer buf = ByteBuffer.allocate(4 * map.getWidth() * map.getHeight() + 14 + 40);
    buf.order(ByteOrder.LITTLE_ENDIAN);
    
    // file header (14 bytes)
    buf.put((byte)'B');
    buf.put((byte)'M');
    buf.putInt(4 * map.getWidth() * map.getHeight() + 14 + 40);
    buf.putInt(0);
    buf.putInt(14 + 40);
    
    // BITMAPINFO header (40 bytes)
    buf.putInt(40);
    buf.putInt(map.getWidth());
    buf.putInt(map.getHeight());
    buf.putShort((short)1);
    buf.putShort((short)32); // bits per pixel
    buf.putInt(0);
    buf.putInt(4 * map.getWidth() * map.getHeight());
    buf.putInt(3600);
    buf.putInt(3600);
    buf.putInt(0);
    buf.putInt(0);
    
    for (int y = 0; y < map.getHeight(); y++) {
      for (int x = 0; x < map.getWidth(); x++) {
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
  
  /**
   * Could be optimized for large grids using an index buffer,
   * using unsigned bytes instead of floats,
   * and splitting it up into 256x256 parts
   */
  private Mesh createEdgeMesh() {
    TFloatList verts = new TFloatArrayList();
    
    int[] buf = new int[con.maxNeighbors() * 2];
    float[] costBuf = new float[con.maxNeighbors()];
    
    for (int fromY = 0; fromY <= map.getHeight(); fromY++) {
      for (int fromX = 0; fromX <= map.getWidth(); fromX++) {
        int numNeighbors = con.getNeighbors(fromX, fromY, buf, costBuf);
        
        for (int i = 0; i < numNeighbors; i++) {
          int toX = buf[i * 2];
          int toY = buf[i * 2 + 1];
          
          verts.add(fromX);
          verts.add(fromY);
          verts.add(toX);
          verts.add(toY);
        }
      }
    }
    
    Mesh mesh = new Mesh(true, verts.size() / 2, 0,
      new VertexAttribute(Usage.Position, 2, ShaderProgram.POSITION_ATTRIBUTE));
    
    mesh.setVertices(verts.toArray());
    return mesh;
  }
}
