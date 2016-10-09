package net.alfredandersson.anyanglepathfinding.util;

import com.badlogic.gdx.graphics.glutils.ShaderProgram;

public final class GraphicsUtil {
  
  private GraphicsUtil() {}
  
  public static ShaderProgram loadShader(String vertexShader, String fragmentShader) {
    ShaderProgram shader = new ShaderProgram(vertexShader, fragmentShader);
    
    if (!shader.isCompiled()) {
      shader.begin();
      String log = shader.getLog();
      shader.end();
      throw new RuntimeException("Error compiling shader: " + log);
    }
    
    return shader;
  }
}
