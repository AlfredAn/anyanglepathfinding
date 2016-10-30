package net.alfredandersson.anyanglepathfinding.core;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.Disposable;
import gnu.trove.list.TFloatList;
import gnu.trove.list.array.TFloatArrayList;
import net.alfredandersson.anyanglepathfinding.core.util.GraphicsUtil;

public final class PathRenderer implements Disposable {
  
  private static final String pathVertexShader =
            "attribute vec4 a_position;\n"
          + "uniform mat4 u_projectionViewMatrix;\n"
          + "void main() {\n"
          + "  gl_Position = u_projectionViewMatrix * a_position;\n"
          + "}";
  private static final String pathFragmentShader =
            "uniform vec4 u_color;\n"
          + "void main() {\n"
          + "  gl_FragColor = u_color;\n"
          + "}";
  
  private ShaderProgram shader;
  
  private Mesh mesh;
  
  public PathRenderer() {
    shader = GraphicsUtil.loadShader(pathVertexShader, pathFragmentShader);
  }
  
  public void setPath(int[] path) {
    if (mesh != null) {
      mesh.dispose();
      mesh = null;
    }
    
    TFloatList verts = new TFloatArrayList(path.length);
    
    for (int i = 0; i < path.length / 2; i++) {
      verts.add(path[i * 2]);
      verts.add(path[i * 2 + 1]);
    }
    
    mesh = new Mesh(true, verts.size() / 2, 0,
      new VertexAttribute(VertexAttributes.Usage.Position, 2, ShaderProgram.POSITION_ATTRIBUTE));
    
    mesh.setVertices(verts.toArray());
  }
  
  private final float[] buf = new float[4];
  public void draw(Draw d, float r, float g, float b, float a) {
    if (mesh == null) {
      return;
    }
    
    buf[0] = r;
    buf[1] = g;
    buf[2] = b;
    buf[3] = a;
    
    shader.begin();
    shader.setUniform4fv("u_color", buf, 0, 4);
    d.enableBlending();
    shader.setUniformMatrix("u_projectionViewMatrix", d.cam.combined);
    mesh.render(shader, GL20.GL_LINE_STRIP);
    shader.end();
  }
  
  @Override
  public void dispose() {
    if (mesh != null) {
      mesh.dispose();
      mesh = null;
    }
    
    if (shader != null) {
      shader.dispose();
      shader = null;
    }
  }
}
