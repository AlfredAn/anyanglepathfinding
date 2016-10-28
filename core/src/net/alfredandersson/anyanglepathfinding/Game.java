package net.alfredandersson.anyanglepathfinding;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;
import net.alfredandersson.anyanglepathfinding.engine.EightConnectedGrid;
import net.alfredandersson.anyanglepathfinding.engine.GridConnections;
import net.alfredandersson.anyanglepathfinding.engine.Map;
import net.alfredandersson.anyanglepathfinding.engine.Pathfinder;
import net.alfredandersson.anyanglepathfinding.engine.algorithms.DijkstraSearch;

public final class Game {
  
  public final AnyAnglePathfinding main;
  
  private Map map;
  private GridConnections con = new EightConnectedGrid();
  private Pathfinder pathfinder;
  
  private MapRenderer mapRenderer;
  
  private PathRenderer pathRenderer;
  
  private final Random rand = new Random();
  
  public Game(AnyAnglePathfinding main) {
    this.main = main;
    
    try (InputStream in = Gdx.files.internal("maps/test2.png").read(8192)) {
      map = Map.fromPNG(in);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    
    pathfinder = new DijkstraSearch(map, con);
    
    create();
    displayRandomPath();
  }
  
  private void displayRandomPath() {
    while (true) {
      int startX = rand.nextInt(map.getWidth() + 1);
      int startY = rand.nextInt(map.getHeight() + 1);
      
      int endX = rand.nextInt(map.getWidth() + 1);
      int endY = rand.nextInt(map.getHeight() + 1);
      
      long time = System.nanoTime();
      int[] path = pathfinder.findPath(startX, startY, endX, endY);
      long delta = System.nanoTime() - time;
      System.out.println(pathfinder.getClass().getSimpleName() + " took " + ((double)delta * 0.000001) + " ms.");
      
      if (path != null) {
        pathRenderer.setPath(path);
        break;
      }
    }
  }
  
  void update() {
    if (Gdx.input.isKeyJustPressed(Keys.SPACE)) {
      displayRandomPath();
    }
  }
  
  void draw(Draw d) {
    double xScale = (double)map.getWidth() / Gdx.graphics.getBackBufferWidth();
    double yScale = (double)map.getHeight() / Gdx.graphics.getBackBufferHeight();
    double maxScale = Math.max(xScale, yScale);
    
    double xSize = map.getWidth() * maxScale / xScale;
    double ySize = map.getHeight() * maxScale / yScale;
    d.cam.setToOrtho(true, (float)xSize, (float)ySize);
    d.cam.translate((float)((map.getWidth() - xSize) * 0.5), (float)((map.getHeight() - ySize) * 0.5));
    d.cam.update();
    
    mapRenderer.draw(d);
    pathRenderer.draw(d);
  }
  
  void create() {
    if (mapRenderer == null) {
      mapRenderer = new MapRenderer(map, con);
      pathRenderer = new PathRenderer();
    }
  }
  
  void dispose() {
    if (mapRenderer != null) {
      mapRenderer.dispose();
      mapRenderer = null;

      pathRenderer.dispose();
      pathRenderer = null;
    }
  }
}
