package net.alfredandersson.anyanglepathfinding;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import java.io.IOException;
import java.io.InputStream;
import net.alfredandersson.anyanglepathfinding.engine.Map;

public final class Game {
  
  public final AnyAnglePathfinding main;
  
  private Map map;
  
  private Texture mapTex;
  
  public Game(AnyAnglePathfinding main) {
    this.main = main;
    
    try (InputStream in = Gdx.files.internal("maps/test2.png").read(8192)) {
      map = Map.fromPNG(in);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
  
  void update() {
    
  }
  
  void draw(Draw d) {
    double xScale = (double)map.width / Gdx.graphics.getBackBufferWidth();
    double yScale = (double)map.height / Gdx.graphics.getBackBufferHeight();
    double maxScale = Math.max(xScale, yScale);
    
    double xSize = map.width * maxScale / xScale;
    double ySize = map.height * maxScale / yScale;
    d.cam.setToOrtho(true, (float)xSize, (float)ySize);
    d.cam.translate((float)((map.width - xSize) * 0.5), (float)((map.height - ySize) * 0.5));
    d.cam.update();
    
    d.enableBlending();
    d.sprites.setProjectionMatrix(d.cam.combined);
    d.sprites.begin();
    d.sprites.draw(mapTex, 0, 0);
    d.sprites.end();
  }
  
  void create() {
    mapTex = map.createTexture();
  }
  
  void dispose() {
    mapTex.dispose();
    mapTex = null;
  }
}
